package tn.esprit.bibliothque;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Event event = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, parent, false);
        }
        TextView eventName = convertView.findViewById(android.R.id.text1);
        TextView eventDetails = convertView.findViewById(android.R.id.text2);

        eventName.setText(event.getName());
        eventDetails.setText("Date: " + event.getDate() + ", Lieu: " + event.getLocation());

        return convertView;
    }
}
