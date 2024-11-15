package tn.esprit.bibliothque;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteEventActivity extends AppCompatActivity {
    private EditText deleteEventNameEditText;
    private Button deleteEventButton;
    private EventPreferences eventPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_event);


        deleteEventNameEditText = findViewById(R.id.deleteEventNameEditText);
        deleteEventButton = findViewById(R.id.deleteEventButton);
        eventPreferences = new EventPreferences(this);


        Intent intent = getIntent();
        String eventName = intent.getStringExtra("EVENT_NAME");
        if (eventName != null) {
            deleteEventNameEditText.setText(eventName);
        }


        deleteEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEvent();
            }
        });
    }


    private void deleteEvent() {
        String eventName = deleteEventNameEditText.getText().toString().trim();


        if (eventName.isEmpty()) {

            deleteEventNameEditText.setError("Veuillez entrer le nom de l'événement");
            return;
        } else if (eventName.length() < 3) {

            deleteEventNameEditText.setError("Le nom de l'événement doit comporter au moins 3 caractères");
            return;
        }


        boolean isDeleted = eventPreferences.deleteEventByName(eventName);
        if (isDeleted) {
            Toast.makeText(this, "Événement supprimé avec succès", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Échec de la suppression de l'événement", Toast.LENGTH_SHORT).show();
        }
    }
}
