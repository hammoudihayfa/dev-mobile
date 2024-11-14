package tn.esprit.bibliothque;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ParticipateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participate);
        String eventId = getIntent().getStringExtra("eventId");
        Toast.makeText(this, "Participating in event ID: " + eventId, Toast.LENGTH_SHORT).show();
    }
}