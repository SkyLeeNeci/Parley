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

    private EditText passwordNew;
    private Button changePassBtn;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        passwordNew = findViewById(R.id.newPassBox);
        changePassBtn = findViewById(R.id.changePassBtn);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        changePassBtn.setOnClickListener(v -> {

            String newPass;
            newPass = passwordNew.getText().toString();
            user.updatePassword(newPass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ChangePassActivity.this, "Password updated!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ChangePassActivity.this, LoginActivity.class));
                        } else {
                            Toast.makeText(ChangePassActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}