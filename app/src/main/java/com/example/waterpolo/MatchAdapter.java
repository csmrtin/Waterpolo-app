package com.example.waterpolo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.MatchViewHolder> {
    private List<Match> matches;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Match match);
        void onDeleteClick(String matchId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public MatchAdapter(List<Match> matches) {
        this.matches = matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match_item, parent, false);
        return new MatchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchViewHolder holder, int position) {
        Match currentMatch = matches.get(position);
        holder.textViewMatchTeams.setText(currentMatch.getTeamA() + " vs " + currentMatch.getTeamB());
        holder.textViewMatchDate.setText(currentMatch.getDate());
        holder.textViewMatchScore.setText(currentMatch.getScoreA() + " - " + currentMatch.getScoreB());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(currentMatch);
            }
        });

    }

    @Override
    public int getItemCount() {
        return matches.size();
    }

    class MatchViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewMatchTeams;
        private TextView textViewMatchDate;
        private TextView textViewMatchScore;

        public MatchViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMatchTeams = itemView.findViewById(R.id.textViewMatchTeams);
            textViewMatchDate = itemView.findViewById(R.id.textViewMatchDate);
            textViewMatchScore = itemView.findViewById(R.id.textViewMatchScore);
        }
    }
} 