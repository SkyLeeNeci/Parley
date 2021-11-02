package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class SettingsActivity extends AppCompatActivity {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch micro, cam;
    TextView passChange, deleteAcc;
    BottomNavigationItemView home,settings,history,logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        micro = findViewById(R.id.microSetting);
        cam = findViewById(R.id.camSetting);
        passChange = findViewById(R.id.changePassBasic);
        deleteAcc = findViewById(R.id.deleteAccBtn);
        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        history = findViewById(R.id.historyBtn);
        logOut = findViewById(R.id.logOutBtn);

        micro.setOnClickListener(v ->{
            if(micro.isChecked()){
                    JitsiMeetConferenceOptions jitsiMeetConferenceOptions = new JitsiMeetConferenceOptions.Builder().setAudioMuted(true).build();
                    JitsiMeet.setDefaultConferenceOptions(jitsiMeetConferenceOptions);

            }else{
                JitsiMeetConferenceOptions jitsiMeetConferenceOptions = new JitsiMeetConferenceOptions.Builder().setAudioMuted(false).build();
                JitsiMeet.setDefaultConferenceOptions(jitsiMeetConferenceOptions);
            }
        });

        home.setOnClickListener(v ->{
            startActivity(new Intent(SettingsActivity.this,DashboardActivity.class));
        });


    }
}