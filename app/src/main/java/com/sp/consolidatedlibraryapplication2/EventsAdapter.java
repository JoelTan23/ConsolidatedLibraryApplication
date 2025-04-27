package com.sp.consolidatedlibraryapplication2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<EventModel> events;
    private Context context;

    public EventsAdapter(List<EventModel> eventList, Context context) {
        this.events = eventList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EventModel event = events.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventType.setText(event.getEventType());
        holder.eventDescription.setText(event.getEventDescription());
        holder.eventDate.setText((event.getEventDate()));
        holder.eventTime.setText(event.getEventTime());

        // Click Event to Open Calendar Fragment
        holder.itemView.setOnClickListener(v -> {
            fragment_event_calendar fragment = fragment_event_calendar.newInstance(
                    event.getEventName(),
                    event.getEventType(),
                    event.getEventDescription(),
                    event.getEventDate(),
                    event.getEventTime()
            );

            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            fragment.show(fragmentManager, "EventCalendarFragment");
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventType, eventDescription,eventDate, eventTime;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_name);
            eventType = itemView.findViewById(R.id.event_type);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDate = itemView.findViewById(R.id.event_date);
            eventTime = itemView.findViewById(R.id.event_time);
        }
    }
}
