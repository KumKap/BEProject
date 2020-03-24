package com.example.beproject;
import android.os.Build;
import android.util.Log;
import androidx.annotation.RequiresApi;
import java.time.Duration;
import java.time.Instant;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Argon {
    private static String password;
    Argon(String password){
        this.password = password;
    }
    public static String run() {
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(4, 1024 * 1024, 8, password);
        return  hash;
    }
}
