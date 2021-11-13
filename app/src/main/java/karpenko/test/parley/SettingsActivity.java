package karpenko.test.parley;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {


    private TextView passChange, deleteAcc;
    private BottomNavigationItemView home,settings,history,logOut;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        passChange = findViewById(R.id.changePassBasic);
        deleteAcc = findViewById(R.id.deleteAccBtn);
        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        history = findViewById(R.id.historyBtn);
        logOut = findViewById(R.id.logOutBtn);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        home.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this,DashboardActivity.class)));

        logOut.setOnClickListener(v ->{
            SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("remember", "false");
            editor.apply();
            startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
            FirebaseAuth.getInstance().signOut();
            finishAffinity();
        });

        history.setOnClickListener(v -> Toast.makeText(SettingsActivity.this, "The history of the rooms is not yet available.", Toast.LENGTH_SHORT).show());

        passChange.setOnClickListener(v -> startActivity(new Intent(SettingsActivity.this,ChangePassActivity.class)));

        deleteAcc.setOnClickListener(v ->{
            AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
            dialog.setTitle("Are you sure?");
            dialog.setMessage("Remove your account from the system?");
            dialog.setPositiveButton("Yes", (dialog1, which) -> user.delete().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    Toast.makeText(SettingsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SettingsActivity.this,LoginActivity.class));
                    finishAffinity();
                }else {
                    Toast.makeText(SettingsActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }));
            dialog.setNegativeButton("No", (dialog12, which) -> dialog12.dismiss());
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();
        });

    }
}