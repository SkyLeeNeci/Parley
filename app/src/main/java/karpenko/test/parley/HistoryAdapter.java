package karpenko.test.parley;

import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    Context context;
    ArrayList<UserHistory> userHistoryArrayList;

    public HistoryAdapter(Context context, ArrayList<UserHistory> userHistoryArrayList) {
        this.context = context;
        this.userHistoryArrayList = userHistoryArrayList;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_history_single_item,parent,false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.HistoryViewHolder holder, int position) {

        UserHistory userHistory = userHistoryArrayList.get(position);

        holder.roomName.setText(userHistory.history.getText());
        holder.roomName.setText(userHistory.history.getText());
        holder.roomName.setText(userHistory.history.getText());
    }

    @Override
    public int getItemCount() {
        return userHistoryArrayList.size();
    }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView roomName,roomCode,roomDate;


        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.tvRoomName);
            roomCode = itemView.findViewById(R.id.tvRoomCode);
            roomDate = itemView.findViewById(R.id.tvRoomVisited);

        }
    }

}
