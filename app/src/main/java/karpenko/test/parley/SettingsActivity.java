package karpenko.test.parley;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

import spencerstudios.com.bungeelib.Bungee;

public class SettingsActivity extends AppCompatActivity {


    private TextView passChange, deleteAcc, langChange;
    private BottomNavigationItemView home, settings, history, logOut;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        loadLocate();

        passChange = findViewById(R.id.changePassBasic);
        deleteAcc = findViewById(R.id.deleteAccBtn);
        langChange = findViewById(R.id.changeLang);
        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        history = findViewById(R.id.historyBtn);
        logOut = findViewById(R.id.logOutBtn);
        auth = FirebaseAuth.getInstance();
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
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Remove your account from the system?");
            dialog.setPositiveButton("Yes", (dialog1, which) -> user.delete().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                    Bungee.slideRight(SettingsActivity.this);
                    finishAffinity();
                } else {
                    Toast.makeText(SettingsActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }));
            dialog.setNegativeButton("No", (dialog12, which) -> dialog12.dismiss());
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        });

        langChange.setOnClickListener(v -> {
            showChangeLanguageDialog();
        });

    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"English", "Ukrainian"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chose Language");
        builder.setSingleChoiceItems(listItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    setLocate("en");
                    recreate();
                } else if (which == 1) {
                    setLocate("uk");
                    recreate();
                }

                dialog.dismiss();

            }
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