package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import spencerstudios.com.bungeelib.Bungee;

public class DashboardActivity extends AppCompatActivity {

    private EditText secretCode;
    private Button connectBtn, shareBtn;
    private BottomNavigationItemView settings, history, logOut;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String userID, roomCode;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private Date date = new Date();
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        secretCode = findViewById(R.id.secreatCodeBox);
        connectBtn = findViewById(R.id.connectBtn);
        shareBtn = findViewById(R.id.shareLinkBtn);
        settings = findViewById(R.id.settingsBtn);
        history = findViewById(R.id.historyBtn);
        logOut = findViewById(R.id.logOutBtn);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        jitsiConferenceOptions();

        connectBtn.setOnClickListener(v -> connectUserToRoomAndSaveRoomId());

        settings.setOnClickListener(v ->{
            startActivity(new Intent(DashboardActivity.this, SettingsActivity.class));
            Bungee.slideLeft(DashboardActivity.this);
        });

        history.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, UserHistory.class));
            Bungee.slideLeft(DashboardActivity.this);
        });

        logOut.setOnClickListener(v -> logOutUser());

        shareBtn.setOnClickListener(v -> shareSecretCode());
    }

    private void logOutUser() {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        Bungee.slideRight(DashboardActivity.this);
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }

    private void shareSecretCode() {
        String code;
        code = secretCode.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Hey! Join to my video chat by this code : " + code + " . Enter it in app : https://play.google.com/ ");
        startActivity(Intent.createChooser(intent, "Share to your friends"));
    }

    private void connectUserToRoomAndSaveRoomId() {

        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setRoom(secretCode.getText().toString())
                .setWelcomePageEnabled(false).build();

        JitsiMeetActivity.launch(DashboardActivity.this, options);

        userID = auth.getCurrentUser().getUid();
        roomCode = secretCode.getText().toString();

        DocumentReference documentReference = firestore.collection("userVisitedRoom").document();
        Map<String, Object> userHistory = new HashMap<>();
        userHistory.put("userID", userID);
        userHistory.put("roomName", "Default Name");
        userHistory.put("roomCode", roomCode);
        userHistory.put("date", formatter.format(date));
        documentReference.set(userHistory).addOnSuccessListener(unused -> Toast.makeText(DashboardActivity.this, getString(R.string.code_saved), Toast.LENGTH_SHORT).show());

    }

    private void jitsiConferenceOptions() {

        URL url;
        try {
            url = new URL("https://meet.jit.si");

            JitsiMeetConferenceOptions jitsiMeetConferenceOptions = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(url).setWelcomePageEnabled(false).setAudioMuted(true).setVideoMuted(true).build();
            JitsiMeet.setDefaultConferenceOptions(jitsiMeetConferenceOptions);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
