package com.example.waterpolo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    private List<Player> players;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Player player);
        void onDeleteClick(String playerId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public PlayerAdapter(List<Player> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);
        return new PlayerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player currentPlayer = players.get(position);
        holder.textViewName.setText(currentPlayer.getName());
        holder.textViewTeam.setText(currentPlayer.getTeam());
        holder.textViewYear.setText(String.valueOf(currentPlayer.getYear()));

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(players.get(adapterPosition));
            }
        });

        // Törlés gomb kezelése, feltételezve, hogy van egy törlés gomb a player_item layoutban
        // Ha nincs, ezt ki kell hagyni, vagy hozzá kell adni a layout-hoz
        // Példa:
        holder.buttonDelete.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(players.get(adapterPosition).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewTeam;
        private TextView textViewYear;
        private Button buttonDelete;

        public PlayerViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewPlayerName);
            textViewTeam = itemView.findViewById(R.id.textViewPlayerTeam);
            textViewYear = itemView.findViewById(R.id.textViewPlayerYear);
            buttonDelete = itemView.findViewById(R.id.buttonDeletePlayer);
        }
    }
} 