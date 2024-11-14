package tn.esprit.bibliothque;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ViewEventsActivity extends AppCompatActivity {

    private EventPreferences eventPreferences;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);

        eventPreferences = new EventPreferences(this);
        listView = findViewById(R.id.listView);

        displayEvents();
    }

    private void displayEvents() {
        List<Event> events = eventPreferences.getEventList();
        EventsAdapterFront adapter = new EventsAdapterFront(this, events);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            Event selectedEvent = events.get(position);
            Intent resultIntent = new Intent();
            resultIntent.putExtra("SELECTED_EVENT", selectedEvent);
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}