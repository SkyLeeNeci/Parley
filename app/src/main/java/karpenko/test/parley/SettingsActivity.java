package karpenko.test.parley;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import spencerstudios.com.bungeelib.Bungee;

public class SettingsActivity extends AppCompatActivity {


    private TextView passChange, deleteAcc, langChange;
    private BottomNavigationItemView home, history, logOut;
    private FirebaseUser user;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loadLocate();
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        passChange = findViewById(R.id.changePassBasic);
        deleteAcc = findViewById(R.id.deleteAccBtn);
        langChange = findViewById(R.id.changeLang);
        home = findViewById(R.id.homeBtn);
        history = findViewById(R.id.historyBtn);
        logOut = findViewById(R.id.logOutBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();

        home.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, DashboardActivity.class));
            Bungee.slideRight(SettingsActivity.this);
        });

        logOut.setOnClickListener(v -> logOutUser());

        history.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, UserHistory.class));
            Bungee.slideRight(SettingsActivity.this);
        });

        passChange.setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, ChangePassActivity.class));
            Bungee.slideLeft(SettingsActivity.this);
        });

        deleteAcc.setOnClickListener(v -> {
            AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
            dialog.setTitle(getString(R.string.are_u_sure));
            dialog.setMessage(getString(R.string.remove_acc_from_system));
            dialog.setPositiveButton(R.string.yes, (dialog1, which) -> user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                    Bungee.slideRight(SettingsActivity.this);
                    finishAffinity();
                } else {
                    Toast.makeText(SettingsActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }));
            dialog.setNegativeButton(R.string.no, (dialog12, which) -> dialog12.dismiss());
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        });

        langChange.setOnClickListener(v -> {
            showChangeLanguageDialog();
        });
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"English", "????????????????????"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.chose_lang);
        builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
            if (which == 0) {
                setLocate("en");
                recreate();
            } else if (which == 1) {
                setLocate("uk");
                recreate();
            }
            dialog.dismiss();

        });
        AlertDialog dialog = builder.create();
        dialog.show();
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

    private void loadLocate() {
        SharedPreferences preferences = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_lang", "");
        setLocate(language);

    }


    private void logOutUser() {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
        Bungee.slideRight(SettingsActivity.this);
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }

}