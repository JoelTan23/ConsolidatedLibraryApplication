package com.sp.consolidatedlibraryapplication2;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/*
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_event_calendar#newInstance} factory method to
 * create an instance of this fragment.
 */

public class fragment_event_calendar extends DialogFragment {

    private String eventName, eventType, eventDescription, eventDate, eventTime;

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_event_calendar.
     */

    public static fragment_event_calendar newInstance(String name, String type, String description, String date, String time) {
        fragment_event_calendar fragment = new fragment_event_calendar();
        Bundle args = new Bundle();
        args.putString("event_name", name);
        args.putString("event_type", type);
        args.putString("event_description", description);
        args.putString("event_date", date);
        args.putString("event_time", time);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_calendar, container, false);

        if (getArguments() != null) {
            eventName = getArguments().getString("event_name");
            eventType = getArguments().getString("event_type");
            eventDescription = getArguments().getString("event_description");
            eventDate = getArguments().getString("event_date");
            eventTime = getArguments().getString("event_time");
        }

        TextView nameTextView = view.findViewById(R.id.event_name);
        TextView typeTextView = view.findViewById(R.id.event_type);
        TextView descriptionTextView = view.findViewById(R.id.event_description);
        Button addToCalendarButton = view.findViewById(R.id.add_to_calendar_button);

        nameTextView.setText(eventName);
        typeTextView.setText(eventType);
        descriptionTextView.setText(eventDescription);

        addToCalendarButton.setOnClickListener(v -> addToPhoneCalendar());

        return view;
    }


    // This opens the actual phone calendar
    private void addToPhoneCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());  // We need to get the time date format
        Calendar calendar = Calendar.getInstance();

        try {
            calendar.setTime(sdf.parse(eventDate + " " + eventTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI);
        intent.putExtra(CalendarContract.Events.TITLE, eventName);
        intent.putExtra(CalendarContract.Events.DESCRIPTION, eventDescription);
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "Library");
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.getTimeInMillis() + 3600000); // 1 Hour Duration
        startActivity(intent);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }
}
