package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText emailBox, passwordBox;
    TextView changePass;
    Button loginBtn, createAccountBtn;
    FirebaseAuth auth;
    CheckBox rememberMe;

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
        rememberMe = findViewById(R.id.rememderMe);
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkBox =  preferences.getString("remember", "");
        if(checkBox.equals("true")){
            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
        }else if(checkBox.equals("false")){
            Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
        }

        loginBtn.setOnClickListener(v -> {

            String email, password;
            email = emailBox.getText().toString();
            password = passwordBox.getText().toString();

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    if(auth.getCurrentUser().isEmailVerified()){
                        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                        Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(LoginActivity.this, "Verify your email address", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        createAccountBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));


        changePass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ChangePassByEmail.class)));

        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){

                SharedPreferences preferences1 = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.putString("remember", "true");
                editor.apply();
                Toast.makeText(LoginActivity.this, "Remembered!", Toast.LENGTH_SHORT).show();

            }else if(!buttonView.isChecked()){

                SharedPreferences preferences1 = getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences1.edit();
                editor.putString("remember", "false");
                editor.apply();
                Toast.makeText(LoginActivity.this, "Forgot!", Toast.LENGTH_SHORT).show();

            }
        });

    }
}