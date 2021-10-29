package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText emailBox, passwordBox;
    Button loginBtn, createAccountBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailBox = findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        loginBtn = findViewById(R.id.loginbtn);
        createAccountBtn = findViewById(R.id.createAccoutbtn);

        createAccountBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

    }
}