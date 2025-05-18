package com.example.waterpolo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.TeamViewHolder> {
    private List<Team> teams;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Team team);
        void onDeleteClick(String teamId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TeamAdapter(List<Team> teams) {
        this.teams = teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_item, parent, false);
        return new TeamViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, int position) {
        Team currentTeam = teams.get(position);
        holder.textViewTeamName.setText(currentTeam.getName());
        holder.textViewTeamCity.setText(currentTeam.getCity());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(currentTeam);
            }
        });

        // Handle delete icon click (if uncommented in team_item.xml)
        /*
        holder.imageViewDeleteTeam.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(currentTeam.getId());
            }
        });
         */
    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    class TeamViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTeamName;
        private TextView textViewTeamCity;
        // private ImageView imageViewDeleteTeam; // Uncomment if delete icon is used

        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTeamName = itemView.findViewById(R.id.textViewTeamName);
            textViewTeamCity = itemView.findViewById(R.id.textViewTeamCity);
            // imageViewDeleteTeam = itemView.findViewById(R.id.imageViewDeleteTeam); // Uncomment if delete icon is used
        }
    }
} 