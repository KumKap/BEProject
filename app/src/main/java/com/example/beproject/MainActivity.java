package com.example.beproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "SignInActivity";
    int RC_SIGN_IN = 0;
    private int PERMISSION_CODE = 1;
    String result2;
    SignInButton signIn;
    GoogleSignInClient mGoogleSignInClient;
    TextView register;
    String address2;
    String address;
    String Name;
    String Email;
    Button button;
    EditText email;
    EditText password;
    String _email;
    String _password;
    String result3;
    int flag = 0;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_main);
        register = findViewById(R.id.register);
        button = findViewById(R.id.button);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        address2 = getString(R.string.URL_signIn);
        Context context = MainActivity.this;
        sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED ||
        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED||
        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_DENIED ){
            requestPermission();
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          //      address2 = addr.concat(address2);
                _email = email.getText().toString();
                //Toast.makeText(getApplicationContext(),_email,Toast.LENGTH_SHORT).show();
                String regx2 = "^(.+)@(.+)$";
                Pattern pattern2 = Pattern.compile(regx2, Pattern.CASE_INSENSITIVE);
                Matcher matcher2 = pattern2.matcher(_email);
                if (matcher2.find() == true) {
                    flag = flag + 1;
                } else {
                    if (flag == 0) {
                        Toast.makeText(getApplicationContext(), "Enter valid mail", Toast.LENGTH_SHORT).show();
                        email.requestFocus();
                        String replacedWith = "<font color='red'>" + _email + "</font>";
                        email.setText(Html.fromHtml(replacedWith));
                        flag = 0;
                        return;
                    }
                }
                _password = password.getText().toString();
                String regx4 = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
                Pattern pattern4 = Pattern.compile(regx4, Pattern.CASE_INSENSITIVE);
                Matcher matcher4 = pattern4.matcher(_password);
                if (matcher4.find() == true) {
                    flag = flag + 1;
                } else {
                    if (flag == 1) {
                        Toast.makeText(getApplicationContext(), "Enter password with 1 UpperCase, 1 LowerCase, 1 Digit and 1 Special Character", Toast.LENGTH_LONG).show();
                        password.requestFocus();
                        String replacedWith = "<font color='red'>" + _password + "</font>";
                        password.setText(Html.fromHtml(replacedWith));
                        flag = 0;
                        return;
                    }
                }
                if (flag == 2 ) {
                    flag = 0;
                    HashMap<String, String> getData = new HashMap<String, String>();
                    getData.put("email", _email);
                    getData.put("password", _password);
                    //Log.d("ss","s1");
                    PostResponseAsyncTask task = new PostResponseAsyncTask(MainActivity.this, getData, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {
                            if (!(s.isEmpty())) {
                                if(s.equals("Password Incorrect")){
                                    Toast.makeText(getApplicationContext(), "Password Incorrect", Toast.LENGTH_LONG).show();
                                    password.requestFocus();
                                    String replacedWith = "<font color='red'>" + _password + "</font>";
                                    password.setText(Html.fromHtml(replacedWith));
                                    flag = 0;
                                    return;
                                }
                                else if(s.equals("Email Incorrect")){
                                    Toast.makeText(getApplicationContext(), "Email Incorrect", Toast.LENGTH_SHORT).show();
                                    email.requestFocus();
                                    String replacedWith = "<font color='red'>" + _email + "</font>";
                                    email.setText(Html.fromHtml(replacedWith));
                                    flag = 0;
                                    return;
                                }
                                else {

                                    editor.putString(getString(R.string.email), _email);
                                    editor.commit();
                                    startActivity(new Intent(MainActivity.this, Home.class));


                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                    try {
                        result3 = task.execute(address2).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
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
        });

        // Views
        // mStatusTextView = findViewById(R.id.status);

        // Button listeners
        signIn = findViewById(R.id.sign_in_button);
        //findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        //SignInButton signInButton = findViewById(R.id.sign_in_button);
        //signInButton.setSize(SignInButton.SIZE_STANDARD);
        //signInButton.setColorScheme(SignInButton.COLOR_LIGHT);
        // [END customize_button]
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {


        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Email = account.getEmail();
            Name = account.getGivenName();

//            Log.d("Hash3",Name);
//            Log.d("Hash4",Email);
//            Log.d("Hash5",address);

            updateUser(Email, Name);
            //startActivity(new Intent(MainActivity.this, Home.class));
        }
        //Intent intent = new Intent(MainActivity.this,Page1.class);
        //startActivity(intent);
        // [END on_start_sign_in]
        super.onStart();
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Email = account.getEmail();
            Name = account.getGivenName();
            updateUser(Email, Name);
            editor.putString(getString(R.string.email), Email);
            editor.commit();
            startActivity(new Intent(MainActivity.this, Home.class));

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(MainActivity.this, "Log In Failed!", Toast.LENGTH_SHORT).show();
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END revokeAccess]

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));

            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            Email = account.getEmail();
            Name = account.getGivenName();
            updateUser(Email, Name);
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);

            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            //mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
    void updateUser(String Email, String Name){
        //address = addr.concat(address);
        HashMap<String, String> getData = new HashMap<String, String>();
        getData.put("email", Email);
        getData.put("fullname", Name);
        address = getString(R.string.URL_userUpdate);

        PostResponseAsyncTask task2 = new PostResponseAsyncTask(MainActivity.this, getData, new AsyncResponse() {
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
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.INTERNET,
                Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_CALENDAR, Manifest.permission.ACCESS_NETWORK_STATE}, PERMISSION_CODE);
     }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED && grantResults[3] == PackageManager.PERMISSION_GRANTED
                    && grantResults[4] == PackageManager.PERMISSION_GRANTED && grantResults[5] == PackageManager.PERMISSION_GRANTED
            && grantResults[6] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission was GRANTED!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Permission was DENIED!",Toast.LENGTH_SHORT).show();
            }
        }
    }
}


