package karpenko.test.parley;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

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
        holder.roomDate.setText(history.roomDate);

    }

    @Override
    public int getItemCount() {
        return HistoryArrayList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {

        TextView roomName, roomCode, roomDate;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.tvRoomName);
            roomCode = itemView.findViewById(R.id.tvRoomCode);
            roomDate = itemView.findViewById(R.id.tvRoomVisited);

        }
    }

}
