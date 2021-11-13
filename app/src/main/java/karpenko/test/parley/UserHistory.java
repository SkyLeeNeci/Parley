package karpenko.test.parley;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import timber.log.Timber;

public class UserHistory extends AppCompatActivity {

    /*private static final String TAG = "Read from db";*/
    RecyclerView recyclerView;
    ArrayList<History> histories;
    HistoryAdapter historyAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView history;
    FirebaseAuth auth;
    FirebaseUser user;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Search...");
        progressDialog.show();

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

         /*db.collection("userVisitedRoom").whereEqualTo("userID", user.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String date = document.get("date", String.class);
                                        String name = document.get("roomCode", String.class);
                                        history.setText(name + " " + date);
                                    }
                                } else {
                                    Log.w(TAG, "Error getting documents.", task.getException());
                                }
                            }
                        });*/

    }

    private void EventChangeListener() {

        db.collection("userVisitedRoom").whereEqualTo("userID", user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error!=null){

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }

                            Timber.e(error.getLocalizedMessage());
                        }

                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if(documentChange.getType() == DocumentChange.Type.ADDED){
                                histories.add(documentChange.getDocument().toObject(History.class));
                            }

                            historyAdapter.notifyDataSetChanged();

                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }


                        }

                    }
                });

    }
}