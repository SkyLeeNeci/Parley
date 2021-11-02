package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class DashboardActivity extends AppCompatActivity {

    EditText secretCode;
    Button connectBtn, shareBtn;
    BottomNavigationItemView home,settings,history,logOut;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        secretCode = findViewById(R.id.secreatCodeBox);
        connectBtn = findViewById(R.id.connectBtn);
        shareBtn = findViewById(R.id.shareLinkBtn);
        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        history = findViewById(R.id.historyBtn);
        logOut = findViewById(R.id.logOutBtn);

        URL url;
        try {
            url = new URL("https://meet.jit.si");

            JitsiMeetConferenceOptions jitsiMeetConferenceOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(url).setWelcomePageEnabled(false).build();
            JitsiMeet.setDefaultConferenceOptions(jitsiMeetConferenceOptions);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        connectBtn.setOnClickListener(v ->{
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setRoom(secretCode.getText().toString())
                    .setWelcomePageEnabled(false).build();

            JitsiMeetActivity.launch(DashboardActivity.this,options);
        });

        settings.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this,SettingsActivity.class));
        });

        history.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this,VisitHistoryActivity.class));
        });

        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
            finish();
        });
        shareBtn.setOnClickListener(v ->{

            String code;
            code = secretCode.getText().toString();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Hey! Join to my video chat by this code : " + code + " . Enter it in app : https://play.google.com/ ");
            startActivity(Intent.createChooser(intent,"Share to your friends"));
        });
    }
}