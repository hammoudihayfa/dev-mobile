package tn.esprit.bibliothque;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateEventActivity extends Activity {
    private EditText eventNameEditText;
    private EditText eventDateEditText;
    private EditText eventLocationEditText;
    private Button updateEventButton;

    private EventPreferences eventPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_event_activity);

        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDateEditText = findViewById(R.id.eventDateEditText);
        eventLocationEditText = findViewById(R.id.eventLocationEditText);
        updateEventButton = findViewById(R.id.updateEventButton);

        eventPreferences = new EventPreferences(this);

        // Récupérer le nom de l'événement passé dans l'intent
        String eventName = getIntent().getStringExtra("eventName");
        if (eventName != null) {
            eventNameEditText.setText(eventName);
        }

        updateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameEditText.getText().toString();
                String newDate = eventDateEditText.getText().toString();
                String newLocation = eventLocationEditText.getText().toString();

                Log.d("EventUpdate", "Trying to update event with name: " + eventName);

                boolean isModified = eventPreferences.modifyEventByName(eventName, newDate, newLocation);

                if (isModified) {
                    Toast.makeText(UpdateEventActivity.this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UpdateEventActivity.this, "Event not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }}