package com.example.beproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static android.text.TextUtils.concat;

public class Home extends AppCompatActivity {
    ImageView qrcode;
    String Email;
    String Name;
    String result2;
    GoogleSignInClient mGoogleSignInClient;
    String secretKey;
    String privateKey;
    String address;
    ImageView upload;
    private static final String TAG = "MainActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_home);
        qrcode = findViewById(R.id.QRButton);
        upload = findViewById(R.id.UploadButton);
        final Activity activity = this;
        String encryptedbinID;
        String encryptedEmail;
         address = getString(R.string.URL_session);
        String secretKey = getString(R.string.key_dec);
        String privateKey = getString(R.string.key_enc);
//        encryptedbinID= AES.encrypt("Mumbai411110021", privateKey);
  //      encryptedEmail= AES.encrypt("Kumel001@gmail.com", privateKey);
      //  Log.d("Hash1",encryptedbinID);
    //    Log.d("Hash2",encryptedEmail);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Home.this);
        if (acct != null) {
            Email = acct.getEmail();
            Name = acct.getGivenName();
        }
        upload .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServicesOK()){
                    Intent intent = new Intent(Home.this, MapsActivity.class);
                    startActivity(intent);
                }
            }
        });

        qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String temp = "";
        //secretKey = "PassedLockedDown";
        String decryptedbinID;
        String encryptedbinID;
        String encryptedEmail;

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " +  result.getContents(), Toast.LENGTH_LONG).show();

                temp = result.getContents();
                decryptedbinID = AES.decrypt(temp, secretKey) ;
                encryptedbinID= AES.encrypt(decryptedbinID, privateKey);
                encryptedEmail= AES.encrypt(Email, privateKey);

                HashMap<String, String> getData = new HashMap<String, String>();
                getData.put("email", encryptedEmail);
                getData.put("binID", encryptedbinID);

                PostResponseAsyncTask task2 = new PostResponseAsyncTask(Home.this, getData, new AsyncResponse() {
                    @Override
                    public void processFinish(String s) {
                        if (!(s.isEmpty())) {
                            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong! Try later!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                try {
                    result2 = task2.execute(address).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                task2.setEachExceptionsHandler(new EachExceptionsHandler() {
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
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Home.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Home.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}
