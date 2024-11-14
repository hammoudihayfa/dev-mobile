package tn.esprit.bibliothque;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

public class ManageEventsActivity extends AppCompatActivity {
    private EditText eventNameEditText;
    private EditText eventDateEditText;
    private EditText eventLocationEditText;
    private Button addEventButton;
    private Button deleteEventButton;
    private Button editEventButton;
    private Button viewEventsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_events);

        eventNameEditText = findViewById(R.id.eventNameEditText);
        eventDateEditText = findViewById(R.id.eventDateEditText);
        eventLocationEditText = findViewById(R.id.eventLocationEditText);
        addEventButton = findViewById(R.id.addEventButton);
        deleteEventButton = findViewById(R.id.deleteEventButton);
        editEventButton = findViewById(R.id.editEventButton);
        viewEventsButton = findViewById(R.id.viewEventsButton);

        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameEditText.getText().toString().trim();
                String eventDate = eventDateEditText.getText().toString().trim();
                String eventLocation = eventLocationEditText.getText().toString().trim();

                if (eventName.isEmpty()) {
                    eventNameEditText.setError("Le nom de l'événement est requis");
                    return;
                }
                if (eventDate.isEmpty()) {
                    eventDateEditText.setError("La date de l'événement est requise");
                    return;
                }
                if (eventLocation.isEmpty()) {
                    eventLocationEditText.setError("Le lieu de l'événement est requis");
                    return;
                }

                if (!isValidDate(eventDate)) {
                    eventDateEditText.setError("La date doit être au format YYYY-MM-DD et être valide");
                    return;
                }

                if (isDateInPast(eventDate)) {
                    eventDateEditText.setError("La date ne peut pas être dans le passé");
                    return;
                }

                if (eventName.length() < 3) {
                    eventNameEditText.setError("Le nom de l'événement doit comporter au moins 3 caractères");
                    return;
                }

                if (eventLocation.length() < 3) {
                    eventLocationEditText.setError("Le lieu de l'événement doit comporter au moins 3 caractères");
                    return;
                }

                Event event = new Event(String.valueOf(System.currentTimeMillis()), eventName, eventDate, eventLocation);
                EventPreferences eventPreferences = new EventPreferences(ManageEventsActivity.this);
                eventPreferences.saveEvent(event);
                Toast.makeText(ManageEventsActivity.this, "Événement ajouté avec succès", Toast.LENGTH_SHORT).show();
                eventNameEditText.setText("");
                eventDateEditText.setText("");
                eventLocationEditText.setText("");
            }
        });

        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageEventsActivity.this, DeleteEventActivity.class);
                startActivity(intent);
            }
        });

        editEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameEditText.getText().toString();
                Intent intent = new Intent(ManageEventsActivity.this, UpdateEventActivity.class);
                intent.putExtra("eventName", eventName);
                startActivity(intent);
            }
        });

        viewEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageEventsActivity.this, EventListActivity.class);
                startActivity(intent);
            }
        });
    }


    public boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }


    public boolean isDateInPast(String eventDateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date eventDate = sdf.parse(eventDateStr);
            Date currentDate = new Date();
            return eventDate.before(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }
}
