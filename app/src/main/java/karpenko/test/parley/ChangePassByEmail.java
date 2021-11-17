package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import spencerstudios.com.bungeelib.Bungee;

public class ChangePassByEmail extends AppCompatActivity {

    EditText userEmail;
    Button sentOnEmail;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_by_email);

        userEmail = findViewById(R.id.userEmail);
        sentOnEmail = findViewById(R.id.changePassBtn);
        auth = FirebaseAuth.getInstance();

        sentOnEmail.setOnClickListener(v -> {

            String email;

            email = userEmail.getText().toString();

            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassByEmail.this, "Check email!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangePassByEmail.this,LoginActivity.class));
                            Bungee.slideRight(ChangePassByEmail.this);
                            finish();
                        }
                    });

        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ChangePassByEmail.this,LoginActivity.class));
        Bungee.slideRight(ChangePassByEmail.this);
        finish();
    }
}