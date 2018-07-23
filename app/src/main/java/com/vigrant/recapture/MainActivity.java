package com.vigrant.recapture;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

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

        findViewById(R.id.main_button_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMethods.overlay().stop();
            }
        });

        findViewById(R.id.main_button_rot_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMethods.overlay().rotateLeft();
            }
        });

        findViewById(R.id.main_button_rot_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMethods.overlay().rotateRight();
            }
        });

        findViewById(R.id.main_button_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMethods.overlay().left();
            }
        });

        findViewById(R.id.main_button_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticMethods.overlay().right();
            }
        });
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

            findViewById(R.id.main_layout_buttons).setVisibility(View.VISIBLE);
        }

    }


}
