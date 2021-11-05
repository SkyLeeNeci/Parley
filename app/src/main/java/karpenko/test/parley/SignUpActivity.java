package karpenko.test.parley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {

    EditText userName,userEmail, userPassword;
    Button createAccountBtn, logInBtn;

    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;

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

        logInBtn.setOnClickListener(v -> startActivity( new Intent(SignUpActivity.this,LoginActivity.class)));



        createAccountBtn.setOnClickListener(v -> {
            String email, name , password;
            name = userName.getText().toString();
            email = userEmail.getText().toString();
            password = userPassword.getText().toString();

            User user = new User();

            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);

            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                        if(task1.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Check email and verify account", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(SignUpActivity.this, task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    firebaseFirestore.collection("users")
                            .document()
                            .set(user).addOnSuccessListener(unused ->
                            startActivity(new Intent(SignUpActivity.this,LoginActivity.class)));
                    Toast.makeText(SignUpActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

            });

        });

    }
}