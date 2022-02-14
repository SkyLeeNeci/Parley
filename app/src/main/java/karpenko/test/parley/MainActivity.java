package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });

       /* new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        }, 2500);*/
    }

    private void setLocate(String language) {

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,getBaseContext().getResources().getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_lang", language);
        editor.apply();

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences editor = getSharedPreferences("Settings", MODE_PRIVATE);
        String lang = editor.getString("My_lang","");
        setLocate(lang);

    }

}