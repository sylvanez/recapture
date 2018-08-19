package com.vigrant.recapture;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public final static int REQUEST_CAN_DRAW = 9001;
    public final static int REQUEST_LOAD_IMAGE = 9002;

    private Bitmap mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StaticMethods.setMainActivity(this);

        findViewById(R.id.button_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaticMethods.pickImage();
            }
        });

        checkDrawOverlayPermission();
    }

    void addButtons(LinearLayout layoutButtons){

        addButton("stop", new Runnable() {
            @Override
            public void run() {
                StaticMethods.overlay().stop();
            }
        }, layoutButtons);

//        addButton("a++", new Runnable() {
//            @Override
//            public void run() {
//                mImageZoom.alphaInc();
//            }
//        }, layoutButtons);
//
//        addButton("a--", new Runnable() {
//            @Override
//            public void run() {
//                mImageZoom.alphaDec();
//            }
//        }, layoutButtons);

        addButton("<<", new Runnable() {
            @Override
            public void run() {
                StaticMethods.overlay().left();
            }
        }, layoutButtons);

        addButton(">>", new Runnable() {
            @Override
            public void run() {
                StaticMethods.overlay().right();
            }
        }, layoutButtons);

        addButton("up", new Runnable() {
            @Override
            public void run() {
                StaticMethods.overlay().up();
            }
        }, layoutButtons);

        addButton("dn", new Runnable() {
            @Override
            public void run() {
                StaticMethods.overlay().down();
            }
        }, layoutButtons);

        addButton("in", new Runnable() {
            @Override
            public void run() {
                StaticMethods.overlay().zoomIn();
            }
        }, layoutButtons);

        addButton("out", new Runnable() {
            @Override
            public void run() {
                StaticMethods.overlay().zoomOut();
            }
        }, layoutButtons);

    }


    void checkDrawOverlayPermission() {
        /** check if we already  have permission to draw over other apps */
        if (!Settings.canDrawOverlays(this)) {
            /** if not construct intent to request permission */
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            /** request permission via start activity for result */
            startActivityForResult(intent, REQUEST_CAN_DRAW);
        }
    }

    void pickImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(i, REQUEST_LOAD_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if(resultCode != RESULT_OK) {
            System.out.println("request " + requestCode + " failed with result code " + resultCode);
            return;
        }
        /** check if received result code
         is equal our requested code for draw permission  */
        if (requestCode == REQUEST_CAN_DRAW) {
            if (Settings.canDrawOverlays(this)) {
                // continue here - permission was granted
            }
        } else if (requestCode == REQUEST_LOAD_IMAGE){
            if(null != data){
                Uri photoUri = data.getData();
                // Do something with the photo based on Uri
                try{
                    startService(new Intent(MainActivity.this, Overlay.class));

                    mImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                    addButtons((LinearLayout)findViewById(R.id.main_layout_buttons));
                    //moveTaskToBack(true);
                } catch(Exception e){
                    e.printStackTrace();
                }
            } else {
                System.out.println("request to load image, no data provided");
            }
        }
    }


    void onOverlayStarted(){
        if(null != mImage){
            StaticMethods.displayImage(mImage);

            //findViewById(R.id.main_layout_buttons).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)){
            StaticMethods.overlay().mImageZoom.alphaDec();
        } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){
            StaticMethods.overlay().mImageZoom.alphaInc();
        }
        return true;
    }

    private void addButton(String text, final Runnable onClick, LinearLayout layout){
        TextView button = new TextView(this);
        //button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        button.setPadding(30, 10, 30, 10);
        //button.setHeight(getPixelsForDP(28));
        //button.setPadding(getPixelsForDP(8), getPixelsForDP(4), getPixelsForDP(8), getPixelsForDP(4));
        button.setText(text);
        button.setBackgroundColor(Color.rgb(120, 120, 120));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.run();
            }
        });

        layout.addView(button);

        Space sp = new Space(this);
        sp.setMinimumWidth(12);
        sp.setMinimumHeight(12);
        layout.addView(sp);
    }


//    int getPixelsForDP(int dp){
//        return (int) (mScaleDP * (float)(dp) + 0.5f);
//    }

}
