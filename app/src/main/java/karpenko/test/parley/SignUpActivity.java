package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import spencerstudios.com.bungeelib.Bungee;


public class SignUpActivity extends AppCompatActivity {

    private EditText userName, userEmail, userPassword;
    private Button createAccountBtn, logInBtn;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        userName = findViewById(R.id.userNameBox);
        userEmail = findViewById(R.id.userEmailBox);
        userPassword = findViewById(R.id.passwordBox);
        createAccountBtn = findViewById(R.id.createAccBtn);
        logInBtn = findViewById(R.id.loginBtn);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        logInBtn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            Bungee.slideRight(SignUpActivity.this);
        });

        createAccountBtn.setOnClickListener(v -> createAccountWithEmailAndPassword());

    }


    private void createAccountWithEmailAndPassword() {
        String email, name, password;
        name = userName.getText().toString();
        email = userEmail.getText().toString();
        password = userPassword.getText().toString();

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, R.string.verify_email, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                firebaseFirestore.collection("users")
                        .document()
                        .set(user).addOnSuccessListener(unused -> {
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    Bungee.slideRight(SignUpActivity.this);
                });
                Toast.makeText(SignUpActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        Bungee.slideRight(SignUpActivity.this);
        finish();
    }
}
