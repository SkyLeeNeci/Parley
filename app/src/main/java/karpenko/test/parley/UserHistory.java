package karpenko.test.parley;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import spencerstudios.com.bungeelib.Bungee;
import timber.log.Timber;

public class UserHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<History> histories;
    private HistoryAdapter historyAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ProgressDialog progressDialog;
    private BottomNavigationItemView home, settings, logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Search...");
        progressDialog.show();

        home = findViewById(R.id.homeBtn);
        settings = findViewById(R.id.settingsBtn);
        logOut = findViewById(R.id.logOutBtn);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recycler_user_history);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        histories = new ArrayList<History>();
        historyAdapter = new HistoryAdapter(UserHistory.this, histories);
        recyclerView.setAdapter(historyAdapter);

        EventChangeListener();

        settings.setOnClickListener(v -> {
            startActivity(new Intent(UserHistory.this, SettingsActivity.class));
            Bungee.slideLeft(UserHistory.this);
        });

        home.setOnClickListener(v -> {
            startActivity(new Intent(UserHistory.this, DashboardActivity.class));
            Bungee.slideRight(UserHistory.this);
        });

        logOut.setOnClickListener(v -> logOutUser());

    }

    private void EventChangeListener() {

        db.collection("userVisitedRoom").whereEqualTo("userID", user.getUid())
                .addSnapshotListener((value, error) -> {

                    if (error != null) {

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        Timber.e(error.getLocalizedMessage());
                    }

                    for (DocumentChange documentChange : value.getDocumentChanges()) {
                        if (documentChange.getType() == DocumentChange.Type.ADDED) {
                            histories.add(documentChange.getDocument().toObject(History.class));
                        }

                        historyAdapter.notifyDataSetChanged();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void logOutUser() {
        SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remember", "false");
        editor.apply();
        startActivity(new Intent(UserHistory.this, LoginActivity.class));
        Bungee.slideRight(UserHistory.this);
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
    }

}