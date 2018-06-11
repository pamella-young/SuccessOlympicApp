package com.example.android.sootgi;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class RegistrationDetails extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getSimpleName();
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView mImageView;
    Button btn_takePhoto, btn_cancel, btn_register;
    private String mTempPhotoPath;

    private Bitmap mResultsBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_details);

        mImageView = (ImageView) findViewById(R.id.iv_profile_picture);
        btn_takePhoto = (Button) findViewById(R.id.btn_takePhoto);
        btn_takePhoto.setOnClickListener(this);

        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }

    public void onClick(View view){
        if(view == btn_takePhoto){
            dispatchTakePictureIntent();
        }else if(view == btn_cancel){
            launchNewActivity(this, MainActivity.class);
        }else if(view == btn_register){
            launchNewActivity(this, RegistrationSuccess.class);
        }
    }

    public void launchNewActivity(Context context, Class destination){
        Intent intent = new Intent(context,destination);
        startActivity(intent);
    }

    private void dispatchTakePictureIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = BitmapUtils.createTempImageFile(this);
            }catch (IOException ex){
                ex.printStackTrace();
            }

            if(photoFile!=null){
                mTempPhotoPath = photoFile.getAbsolutePath();

                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            }
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            mResultsBitmap = BitmapUtils.resamplePic(this, mTempPhotoPath);
            mImageView.setImageBitmap(mResultsBitmap);
        }else{
        }
    }
}
