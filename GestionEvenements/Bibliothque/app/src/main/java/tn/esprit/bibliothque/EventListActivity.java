package tn.esprit.bibliothque;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class EventListActivity extends AppCompatActivity {
    private EventPreferences eventPreferences;
    private ListView eventsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        eventPreferences = new EventPreferences(this);
        eventsListView = findViewById(R.id.eventsListView);


        displayEvents();
    }

    private void displayEvents() {

        List<Event> events = eventPreferences.getEventList();


        ArrayAdapter<Event> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, events);
        eventsListView.setAdapter(adapter);


        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Event selectedEvent = events.get(position);


                Intent intent = new Intent(EventListActivity.this, UpdateEventActivity.class);
                intent.putExtra("SELECTED_EVENT", selectedEvent);
                startActivity(intent);
            }
        });
    }
}
