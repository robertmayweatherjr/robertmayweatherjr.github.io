package com.example.eventtracking_rob;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private ArrayList<EventModel> events;
    private DBHelper db;

    public EventAdapter(Context context, ArrayList<EventModel> events, DBHelper db) {
        this.context = context;
        this.events = events;
        this.db = db;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventModel event = events.get(position);

        // Use getters (fields are private now)
        holder.name.setText(event.getName());
        holder.date.setText(event.getDate());

        // Delete event
        holder.deleteButton.setOnClickListener(v -> {
            db.deleteEvent(event.getId());
            events.remove(position);
            notifyItemRemoved(position);
        });

        // Update event
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof DataGridActivity) {
                ((DataGridActivity) context).showUpdateDialog(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView name, date;
        Button deleteButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.eventName);
            date = itemView.findViewById(R.id.eventDate);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}

