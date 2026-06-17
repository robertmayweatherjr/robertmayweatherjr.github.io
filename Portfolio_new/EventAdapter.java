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
            int pos = holder.getAbsoluteAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                db.deleteEvent(events.get(pos).getId());
                events.remove(pos);
                notifyItemRemoved(pos);
                notifyItemRangeChanged(pos, events.size());
            }
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

    // Sort alphabetically by name
    public void sortByName() {
        events.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        notifyDataSetChanged();
    }

    // Sort by (MM/dd/yyyy)
    public void sortByDate() {
        events.sort((a, b) -> {
            String[] pa = a.getDate().split("/");
            String[] pb = b.getDate().split("/");

            String da = pa[2] + pa[0] +pa[1]; //YYYYMMDD
            String db = pb[2] + pb[0] + pb[1];

            return da.compareTo(db);
        });

        notifyDataSetChanged();
    }

    // Filter upcoming events
    public void filterUpcoming() {
        String today = java.time.LocalDate.now().toString();

        ArrayList<EventModel> filtered = new ArrayList<>();
        for (EventModel e: events) {
            // Convert MM/dd/yyyy -> yyyy-MM-dd for comparision
            String[] parts = e.getDate().split("/");
            String formatted = parts[2] + "-" + parts[0] + "-" + parts[1];

            if (formatted.compareTo(today) >= 0) {
                filtered.add(e);
            }
        }

        events.clear();
        events.addAll(filtered);
        notifyDataSetChanged();
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

