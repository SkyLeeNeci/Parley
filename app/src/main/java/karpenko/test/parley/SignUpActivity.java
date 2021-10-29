package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity {

    EditText userName,userEmail, userPassword;
    Button createAccountBtn, logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.userNameBox);
        userEmail = findViewById(R.id.emailBox);
        userPassword = findViewById(R.id.passwordBox);
        createAccountBtn = findViewById(R.id.createAccoutbtn);
        logInBtn = findViewById(R.id.loginbtn);

        logInBtn.setOnClickListener(v -> startActivity( new Intent(SignUpActivity.this,LoginActivity.class)));

    }
}