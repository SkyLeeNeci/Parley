package karpenko.test.parley;

import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    Context context;
    ArrayList<History> HistoryArrayList;

    public HistoryAdapter(Context context, ArrayList<History> HistoryArrayList) {
        this.context = context;
        this.HistoryArrayList = HistoryArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_history_single_item, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {

        History history = HistoryArrayList.get(position);

        holder.roomName.setText(history.roomName);
        holder.roomCode.setText(history.roomCode);
        holder.roomDate.setText(String.valueOf(history.date));

        holder.itemView.setOnClickListener(v -> {
            jitsiConferenceOptions();
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setRoom(holder.roomCode.getText().toString())
                    .setWelcomePageEnabled(false).build();

            JitsiMeetActivity.launch(context, options);
        });

        holder.options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.options);
            popupMenu.inflate(R.menu.history_recyclerview_menu);

            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_remove) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Are you sure?");
                    dialog.setMessage(" Remove information about room from the system? ");
                    dialog.setPositiveButton("Yes", (dialog13, which) -> FirebaseFirestore.getInstance()
                            .collection("userVisitedRoom")
                            .whereEqualTo("roomCode", history.roomCode).get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                WriteBatch b = FirebaseFirestore.getInstance().batch();
                                List<DocumentSnapshot> s = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot snapshot : s) {
                                    b.delete(snapshot.getReference());
                                }
                                b.commit().addOnSuccessListener(unused -> Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show());
                            }));
                    dialog.setNegativeButton("Close", (dialog12, which) -> dialog12.dismiss());
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();
                } else if (item.getItemId() == R.id.menu_edit) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Set new room name : ");
                    EditText editText = new EditText(context);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    dialog.setView(editText);

                    dialog.setPositiveButton("Ok", (dialog1, which) -> {
                        String newRoomName = editText.getText().toString();
                        String roomCode = history.roomCode;

                        UpdateData(roomCode, newRoomName);
                    });
                    dialog.setNegativeButton("Close", (dialog12, which) -> dialog12.dismiss());
                    AlertDialog alertDialog = dialog.create();
                    alertDialog.show();

                }
                return true;

            });

            popupMenu.show();
        });
    }

    private void UpdateData(String roomCode, String newRoomName) {

        Map<String, Object> newName = new HashMap<>();
        newName.put("roomName", newRoomName);

        FirebaseFirestore.getInstance().collection("userVisitedRoom").whereEqualTo("roomCode", roomCode)
                .get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {

                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                String documentID = documentSnapshot.getId();
                FirebaseFirestore.getInstance().collection("userVisitedRoom")
                        .document(documentID).update(newName)
                        .addOnSuccessListener(unused -> Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(context, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show());

            } else {
                Toast.makeText(context, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return HistoryArrayList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView roomName, roomCode, roomDate, options;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.tvRoomName);
            roomCode = itemView.findViewById(R.id.tvRoomCode);
            roomDate = itemView.findViewById(R.id.tvRoomVisited);
            options = itemView.findViewById(R.id.tvOptions);

        }
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
