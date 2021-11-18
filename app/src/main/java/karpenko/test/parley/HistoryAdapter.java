package karpenko.test.parley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.roomDate.setText(String.valueOf(history.date));

        holder.options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context,holder.options);
            popupMenu.inflate(R.menu.history_recyclerview_menu);
          /*  popupMenu.setOnMenuItemClickListener(item -> {

                switch (item.getItemId()){
                    case R.id.menu_edit:
                        Intent intent = new Intent(context,DashboardActivity.class);

                        break;
                }

            });*/
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

}
