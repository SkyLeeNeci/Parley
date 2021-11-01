package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailBox, passwordBox;
    TextView changePass;
    Button loginBtn, createAccountBtn;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailBox = findViewById(R.id.userEmailBox);
        passwordBox = findViewById(R.id.passwordBox);
        loginBtn = findViewById(R.id.loginBtn);
        changePass = findViewById(R.id.changePass);
        createAccountBtn = findViewById(R.id.createAccBtn);
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(v -> {

            String email, password;
            email = emailBox.getText().toString();
            password = passwordBox.getText().toString();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            emailBox.getText().clear();
            passwordBox.getText().clear();
        });

        createAccountBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        changePass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this,ChangePassByEmail.class)));

    }
}