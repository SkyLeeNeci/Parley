package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class DashboardActivity extends AppCompatActivity {

    EditText secretCode;
    Button connectBtn, shareBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        secretCode = findViewById(R.id.secreatCodeBox);
        connectBtn = findViewById(R.id.connectBtn);
        shareBtn = findViewById(R.id.shareLinkBtn);

        URL url;
        try {
            url = new URL("https://meet.jit.si");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        JitsiMeetConferenceOptions jitsiMeetConferenceOptions;

        connectBtn.setOnClickListener(v ->{

        });
    }
}