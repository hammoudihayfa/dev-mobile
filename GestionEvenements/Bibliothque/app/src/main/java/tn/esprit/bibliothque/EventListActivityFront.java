package tn.esprit.bibliothque;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class EventListActivityFront extends AppCompatActivity {
    private ListView eventsListView;
    private EventPreferences eventPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        eventsListView = findViewById(R.id.eventsListView);
        eventPreferences = new EventPreferences(this);
        displayEvents();
    }

    private void displayEvents() {
        List<Event> events = eventPreferences.getEventList();
        ArrayAdapter<Event> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        eventsListView.setAdapter(adapter);
        eventsListView.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = events.get(position);
            Toast.makeText(this, "Vous avez choisi de participer à : " + selectedEvent.getName(), Toast.LENGTH_SHORT).show();
        });
    }
}