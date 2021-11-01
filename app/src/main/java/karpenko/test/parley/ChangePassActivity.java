package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {

    EditText passwordOld , passwordNew;
    Button changePassBtn, changePassByEmail;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        passwordNew = findViewById(R.id.passwordBox);
        passwordOld = findViewById(R.id.newPassBox);
        changePassBtn = findViewById(R.id.changePassBtn);
        changePassByEmail = findViewById(R.id.loginBtn);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        changePassBtn.setOnClickListener(v ->{

            String newPass;

            newPass = passwordNew.getText().toString();

            user.updatePassword(newPass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassActivity.this, "Password updated!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangePassActivity.this,LoginActivity.class));
                        }
                    });
        });

    }
}