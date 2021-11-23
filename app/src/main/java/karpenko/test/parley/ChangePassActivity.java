package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import spencerstudios.com.bungeelib.Bungee;

public class ChangePassActivity extends AppCompatActivity {

    private EditText passwordNew;
    private Button changePassBtn;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        passwordNew = findViewById(R.id.newPassBox);
        changePassBtn = findViewById(R.id.changePassBtn);
        user = FirebaseAuth.getInstance().getCurrentUser();

        changePassBtn.setOnClickListener(v -> {

            String newPass;
            newPass = passwordNew.getText().toString();
            user.updatePassword(newPass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(ChangePassActivity.this, LoginActivity.class));
                            Bungee.slideRight(ChangePassActivity.this);
                            finishAffinity();
                        } else {
                            Toast.makeText(ChangePassActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChangePassActivity.this,SettingsActivity.class));
        Bungee.slideRight(ChangePassActivity.this);
    }

}