package tn.esprit.bibliothque;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import java.util.List;

public class EventsAdapterFront extends ArrayAdapter<Event> {
    private Context context;
    private List<Event> events;

    public EventsAdapterFront(Context context, List<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.event_item, parent, false);
        }

        Event event = events.get(position);

        TextView eventNameTextView = convertView.findViewById(R.id.eventNameTextView);
        TextView eventDateTextView = convertView.findViewById(R.id.eventDateTextView);
        TextView eventLocationTextView = convertView.findViewById(R.id.eventLocationTextView);
        Button participateButton = convertView.findViewById(R.id.participateButton);

        eventNameTextView.setText(event.getName());
        eventDateTextView.setText(event.getDate());
        eventLocationTextView.setText(event.getLocation());

        participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ParticipateActivity.class);
                intent.putExtra("eventId", event.getId());
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
