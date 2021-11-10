package karpenko.test.parley;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private EditText emailBox, passwordBox;
    private TextView changePass ;
    private Button loginBtn, createAccountBtn;
    private FirebaseAuth auth;
    private CheckBox rememberMe;
    private ImageView googleSignIn;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private LoginButton facebookSignIn;

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
        googleSignIn = findViewById(R.id.signInWithGoogle);
        mAuth = FirebaseAuth.getInstance();
        facebookSignIn = findViewById(R.id.signInWithFacebook);
        callbackManager = CallbackManager.Factory.create();



        rememberOrNot();

        facebookSignIn.setOnClickListener(v -> authWithFacebook());

        loginBtn.setOnClickListener(v -> authWithMailAndPass());

        createAccountBtn.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignUpActivity.class)));

        changePass.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ChangePassByEmail.class)));

        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(buttonView.isChecked()){

                rememberUser();

            }else if(!buttonView.isChecked()){

                forgotUser();

            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("141783523039-iersgh9r01kraj1qajnl063ji01essvq.apps.googleusercontent.com")
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        // Configure Google Sign In
        googleSignIn.setOnClickListener(v -> resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent())));
    }

    private void handleFacebookToken(AccessToken accessToken) {
        //Toast.makeText(LoginActivity.this, "Get Token", Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        auth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this, task -> {
            if(task.isSuccessful()){
                //Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUI(user);
            }else{
                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {

        if(result.getResultCode() == Activity.RESULT_OK){
            Intent intent = result.getData();
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);

                assert account != null;
                firebaseAuthWithGoogle(account.getIdToken());
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    });

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                        finish();
                        Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void authWithFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void authWithMailAndPass(){
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
    }

    private void rememberOrNot(){
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkBox =  preferences.getString("remember", "");
        if(checkBox.equals("true")){
            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
        }else if(checkBox.equals("false")){
            //Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
        }
    }

    private void rememberUser(){
        SharedPreferences preferences1 = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.putString("remember", "true");
        editor.apply();
        Toast.makeText(LoginActivity.this, "Remembered!", Toast.LENGTH_SHORT).show();
    }

    private void forgotUser(){
        SharedPreferences preferences1 = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences1.edit();
        editor.putString("remember", "false");
        editor.apply();
        Toast.makeText(LoginActivity.this, "Forgot!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkBox =  preferences.getString("remember", "");
        if(checkBox.equals("true") && user!=null){
            startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
        }else if(checkBox.equals("false")){
            //Toast.makeText(LoginActivity.this, "Login to continue", Toast.LENGTH_SHORT).show();
        }
    }
}
