package com.example.beproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Upload extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    ImageView mimageView;
    String time;
    String Email;
    String UploadUrl;
    ImageView uploadButton;
    TextView textView;
    String encodedImage = null;
    CameraPhoto cameraPhoto;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    //final int CAMERA_REQUEST = 13323;
    Uri selectedImage;
    Bitmap b = null;
    private final int PICK_IMAGE_CAMERA = 1;
    public int counter;
    CountDownTimer countDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            textView.setText(String.valueOf(counter));
            counter--;
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(Upload.this, Upload.class);
            //intent.putExtra("Name",Name);
            //intent.putExtra("Email",Email);
            //intent.putExtra("photo",photo.toString());
            //intent.putExtra("id",ID);
            //intent.putExtra("GivenName",GivenName);
            startActivity(intent);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        StrictMode.VmPolicy.Builder builder1 = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder1.build());
        mimageView = findViewById(R.id.imageviewer);
        UploadUrl = getString(R.string.URL_upload);
        Context context = this;
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();


        //Button button = (Button) this.findViewById(R.id.takePicture);
        textView = findViewById(R.id.counter);
        uploadButton = findViewById(R.id.uploadPicture);
        uploadButton.setVisibility(View.INVISIBLE);
        counter = 60;
        cameraPhoto = new CameraPhoto(getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Upload.this);
        if (acct != null) {
            Email = acct.getEmail();
            Toast.makeText(getApplicationContext(), Email, Toast.LENGTH_SHORT).show();
        }


    }

    public void takeImageFromCamera(View view) {
        //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //startActivityForResult(cameraIntent, CAMERA_REQUEST);
        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Kumel/";
        File newdir = new File(dir);
        newdir.mkdirs();

        String file = dir + DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString() + ".jpg";

        File newfile = new File(file);
        try {
            newfile.createNewFile();
        } catch (IOException ignored) {
        }

        selectedImage = Uri.fromFile(newfile);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage);
        startActivityForResult(cameraIntent, PICK_IMAGE_CAMERA);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //set Clicked picture on ImageView
        //if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
        //  Bitmap mphoto = (Bitmap) data.getExtras().get("data");
        // mimageView.setImageBitmap(mphoto);

        //Upload image on server by converting to baseX64
        //BitmapDrawable drawable = (BitmapDrawable) mimageView.getDrawable();
        //Bitmap bitmap = drawable.getBitmap();
        //ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        //byte[] byteArray = byteArrayOutputStream .toByteArray();
        //String image_byte=String.valueOf(byteArray);

        //if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
        //  encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //}
        //Image can now be uploaded

        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (selectedImage != null) {
                if (selectedImage.toString().startsWith("file:")) {

                    File DKNY = new File(selectedImage.getPath());

                    InputStream in = null;
                    try {
                        final int IMAGE_MAX_SIZE = 1200000;
                        in = getContentResolver().openInputStream(selectedImage);
                        // Decode image size
                        BitmapFactory.Options o = new BitmapFactory.Options();
                        o.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(in, null, o);
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        int scale = 1;
                        while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
                                IMAGE_MAX_SIZE) {
                            scale++;
                        }

                        in = getContentResolver().openInputStream(selectedImage);
                        if (scale > 1) {
                            scale--;
                            // scale to max possible inSampleSize that still yields an image
                            // larger than target
                            o = new BitmapFactory.Options();
                            o.inSampleSize = scale;
                            b = BitmapFactory.decodeStream(in, null, o);

                            // resize to desired dimensions
                            int height = b.getHeight();
                            int width = b.getWidth();

                            double y = Math.sqrt(IMAGE_MAX_SIZE
                                    / (((double) width) / height));
                            double x = (y / height) * width;

                            Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
                                    (int) y, true);
                            b.recycle();
                            b = scaledBitmap;

                            System.gc();
                        } else {
                            b = BitmapFactory.decodeStream(in);
                        }
                        in.close();
                        mimageView.setImageBitmap(b);
                    } catch (Exception ignored) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}

        uploadButton.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        countDownTimer.start();

    }

    public void uploadImage(View view) {
        countDownTimer.cancel();
        //initRxTrueTime();
        //if (time == "NO"){
        //  return;
        //}

        textView.setVisibility(View.INVISIBLE);
        Toast.makeText(getApplicationContext(), "Upload Success!", Toast.LENGTH_SHORT).show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        //String image_byte=String.valueOf(byteArray);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
            encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        String location =  sharedPref.getString(getString(R.string.longi),"Unknown");
        location.concat(sharedPref.getString(getString(R.string.lat),"Unknown"));
        HashMap<String, String> postData = new HashMap<String, String>();
        postData.put("image", encodedImage);
        postData.put("email", Email);
        postData.put("location", location);

        PostResponseAsyncTask task = new PostResponseAsyncTask(Upload.this, postData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (s.contains("Uploaded_Success")) {

                    Toast.makeText(getApplicationContext(), "Upload Success!", Toast.LENGTH_SHORT).show();
                    try {
                        TimeUnit.SECONDS.sleep(8);
                        Intent intent2 = new Intent(Upload.this,Home.class);
                        startActivity(intent2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Upload Failed!", Toast.LENGTH_SHORT).show();
                    countDownTimer.start();
                }

            }
        });
        task.execute(UploadUrl);
        //"http://192.168.43.170/mcc/Upload.php"
        task.setEachExceptionsHandler(new EachExceptionsHandler() {
            @Override
            public void handleIOException(IOException e) {
                Toast.makeText(getApplicationContext(), "Cannot connect to server!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleMalformedURLException(MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleProtocolException(ProtocolException e) {
                Toast.makeText(getApplicationContext(), "Protocol Error!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                Toast.makeText(getApplicationContext(), "Encoding Error!", Toast.LENGTH_SHORT).show();

            }
        });




    }




}
