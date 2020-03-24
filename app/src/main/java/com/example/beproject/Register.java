  package com.example.beproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
public class Register extends AppCompatActivity {
    Button button;
    EditText username;
    EditText fullname;
    EditText email;
    EditText password;
    EditText repass;
    String _username;
    String ret;
    String _fullname;
    String _email;
    String _password;
    String _repass;
    String result2;
    String address2;
    int flag = 0;
    int emailVisited = 0;
    int unique_user = 0;
    int return_val = 0;
    String[] keys;
    String[] values;


    public Handler mhandler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_register);
        button = findViewById(R.id.button);
        fullname = findViewById(R.id.name);
        email = findViewById(R.id.Email);
        address2 = getString(R.string.URL_register);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        disbaleView(button, true);
        email.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                emailVisited = 1;
                return false;
            }
        });
        fullname.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                String s = email.getText().toString();
                if (emailVisited == 1 && (!s.isEmpty())) {
                    emailVisited = 2;
                    CheckEmail asyncTask = new CheckEmail(s);
                    new Thread(asyncTask).start();
                }
                return false;
            }
        });
        password.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                String s = email.getText().toString();
                if (emailVisited == 1 && (!s.isEmpty())) {
                    emailVisited = 2;
                    CheckEmail asyncTask = new CheckEmail(s);
                    new Thread(asyncTask).start();
                }
                return false;
            }
        });
        repass.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent arg1) {
                String s = email.getText().toString();
                if (emailVisited == 1 && (!s.isEmpty())) {
                    emailVisited = 2;
                    CheckEmail asyncTask = new CheckEmail(s);
                    new Thread(asyncTask).start();
                }
                return false;
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _fullname = fullname.getText().toString();
                String regx1 = "^[\\p{L} .'-]+$";
                Pattern pattern1 = Pattern.compile(regx1, Pattern.CASE_INSENSITIVE);
                Matcher matcher1 = pattern1.matcher(_fullname);
                if (matcher1.find() == true) {
                    flag = flag + 1;
                } else {
                    Toast.makeText(getApplicationContext(), "Enter valid name", Toast.LENGTH_SHORT).show();
                    fullname.requestFocus();
                    String replacedWith = "<font color='red'>" + _fullname + "</font>";
                    fullname.setText(Html.fromHtml(replacedWith));
                    flag = 0;
                    return;
                }
                _email = email.getText().toString();
                String regx2 = "^(.+)@(.+)$";
                Pattern pattern2 = Pattern.compile(regx2, Pattern.CASE_INSENSITIVE);
                Matcher matcher2 = pattern2.matcher(_email);
                if (matcher2.find() == true) {
                    flag = flag + 1;
                } else {
                    if (flag == 1) {
                        Toast.makeText(getApplicationContext(), "Enter valid mail", Toast.LENGTH_SHORT).show();
                        email.requestFocus();
                        String replacedWith = "<font color='red'>" + _email + "</font>";
                        email.setText(Html.fromHtml(replacedWith));
                        flag = 0;
                        return;
                    }
                }

//                _username = username.getText().toString();
//                String regx3 = "^[a-zA-Z0-9._-]{3,}$";
//                Pattern pattern3 = Pattern.compile(regx3,Pattern.CASE_INSENSITIVE);
//                Matcher matcher3 = pattern3.matcher(_username);
//                if(matcher3.find() == true){
//                    flag = flag + 1;
//
//                }
//                else {
//                    if (flag == 2) {
//                        Toast.makeText(getApplicationContext(), "Enter valid username with min. length of 3", Toast.LENGTH_SHORT).show();
//                        username.requestFocus();
//                        String replacedWith = "<font color='red'>" + _username + "</font>";
//                        username.setText(Html.fromHtml(replacedWith));
//                        flag = 0;
//                        return;
//
//                    }
//                }
                _password = password.getText().toString();
                String regx4 = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,20})";
                Pattern pattern4 = Pattern.compile(regx4, Pattern.CASE_INSENSITIVE);
                Matcher matcher4 = pattern4.matcher(_password);
                if (matcher4.find() == true) {
                    flag = flag + 1;
                } else {
                    if (flag == 2) {
                        Toast.makeText(getApplicationContext(), "Enter password with 1 UpperCase, 1 LowerCase, 1 Digit and 1 Special Character", Toast.LENGTH_LONG).show();
                        password.requestFocus();
                        String replacedWith = "<font color='red'>" + _password + "</font>";
                        password.setText(Html.fromHtml(replacedWith));
                        flag = 0;
                        return;
                    }
                }
                _repass = repass.getText().toString();
                if (_repass.equals(_password)) {
                    flag = flag + 1;
                } else {
                    if (flag == 3) {
                        Toast.makeText(getApplicationContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                        repass.requestFocus();
                        String replacedWith = "<font color='red'>" + _repass + "</font>";
                        repass.setText(Html.fromHtml(replacedWith));
                        flag = 0;
                        return;
                    }
                }
                if (flag == 4 && unique_user == 1) {
                    keys[0] = "email";
                    keys[1] = "fullname";
                    keys[2] = "password";
                    values[0] = _email;
                    values[1] = _fullname;
                    values[2] = _password;
                    serverConn(3, keys,values,address2);
                    if(!(ret.isEmpty())){
                        if(ret.equals("Successfully Registered")) {
                            startActivity(new Intent(Register.this, MainActivity.class));
                        }
                        else{
                            Toast.makeText(getApplication(),"Email does not exist", Toast.LENGTH_SHORT);
                        }
                    }

            }
            }
        });

    }

    void disbaleView(View view, Boolean isDisabled) {
        if (isDisabled) {
            view.setAlpha(0.5f);
            view.setEnabled(false);
        } else {
            view.setAlpha(1);
            view.setEnabled(true);
        }
    }

    class CheckEmail implements Runnable {
        String param;
        CheckEmail(String param) {
            this.param = param;
        }
        @Override
        public void run() {
                mhandler.post(new Runnable() {
                    @Override
                    public void run() {
                        String regx2 = "^(.+)@(.+)$";
                        Pattern pattern2 = Pattern.compile(regx2, Pattern.CASE_INSENSITIVE);
                        Matcher matcher2 = pattern2.matcher(param);
                        if (matcher2.find() == false) {
                            Toast.makeText(getApplicationContext(), "Enter valid mail!", Toast.LENGTH_SHORT).show();
                            email.requestFocus();
                            String replacedWith = "<font color='red'>" + param.toString() + "</font>";
                            email.setText(Html.fromHtml(replacedWith));
                        } else {
                            HashMap<String, String> getData = new HashMap<String, String>();
                            final String address3 = getString(R.string.URL_checkUser);
                            keys[0] = "email";
                            values[0] = param;
                            serverConn(1, keys, values, address3);
                            if (!(ret.isEmpty())) {
                                if (ret.equals("success")) {
                                    unique_user = 1;
                                    Toast.makeText(getApplicationContext(), "Email approved!", Toast.LENGTH_SHORT).show();
                                    disbaleView(button, false);
                                } else {
                                    unique_user = 0;
                                    Toast.makeText(getApplicationContext(), "Email already exists!", Toast.LENGTH_SHORT).show();
                                    email.requestFocus();
                                    String replacedWith = "<font color='red'>" + param + "</font>";
                                    email.setText(Html.fromHtml(replacedWith));
                                }
                            }
                        }
                    }
                });
            }
        }

        void serverConn(int n, String[] key, String[] value, String addr){

            HashMap<String, String> getData = new HashMap<String, String>();
            for(int i=0;i<n;i++){
                getData.put(key[i], value[i]);
            }
            PostResponseAsyncTask task2 = new PostResponseAsyncTask(Register.this, getData, new AsyncResponse() {
                @Override
                public void processFinish(String s) {
                    if (!(s.isEmpty())) {
                        ret = s;
                     } else {
                        Toast.makeText(getApplicationContext(), "Something went wrong!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            try {
                result2 = task2.execute(addr).get();
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
    }

