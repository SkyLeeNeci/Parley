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
    CardView cardViewHome, cardViewSettings, cardViewHistory;



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
        cardViewHistory = findViewById(R.id.cardViewHistory);
        cardViewHome = findViewById(R.id.cardViewHome);
        cardViewSettings = findViewById(R.id.cardViewSettings);

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

            cardViewHome.setVisibility(View.INVISIBLE);
            cardViewHistory.setVisibility(View.INVISIBLE);
            cardViewSettings.setVisibility(View.VISIBLE);
        });

        home.setOnClickListener(v -> {

            cardViewHome.setVisibility(View.VISIBLE);
            cardViewHistory.setVisibility(View.INVISIBLE);
            cardViewSettings.setVisibility(View.INVISIBLE);
        });

        history.setOnClickListener(v -> {

            cardViewHome.setVisibility(View.INVISIBLE);
            cardViewHistory.setVisibility(View.VISIBLE);
            cardViewSettings.setVisibility(View.INVISIBLE);
        });

        logOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
            finish();
        });
    }
}