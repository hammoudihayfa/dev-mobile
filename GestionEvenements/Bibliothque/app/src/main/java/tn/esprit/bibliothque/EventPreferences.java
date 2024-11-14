package tn.esprit.bibliothque;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EventPreferences  {
    private static final String PREFS_NAME = "event_prefs";
    private static final String EVENT_LIST_KEY = "event_list";
    private SharedPreferences sharedPreferences;
    private Context context; 

    public EventPreferences(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveEvent(Event event) {
        List<Event> eventList = getEventList();
        eventList.add(event);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EVENT_LIST_KEY, serializeEventList(eventList));
        editor.apply();
    }

    public List<Event> getEventList() {

        String serializedEvents = sharedPreferences.getString(EVENT_LIST_KEY, "");
        return deserializeEventList(serializedEvents);
    }

    public boolean modifyEventByName(String eventName, String newDate, String newLocation) {
        List<Event> eventList = getEventList();

        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            if (event.getName().equalsIgnoreCase(eventName.trim())) {

                event.setDate(newDate);
                event.setLocation(newLocation);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(EVENT_LIST_KEY, serializeEventList(eventList));
                editor.apply();

                Log.d("EventUpdate", "Event updated: " + event.getName());
                return true;
            }
        }
        Log.d("EventUpdate", "Event not found: " + eventName);
        return false;
    }


    public boolean deleteEventByName(String eventName) {
        List<Event> eventList = getEventList();
        boolean isDeleted = false;

        for (int i = 0; i < eventList.size(); i++) {
            Event event = eventList.get(i);
            if (event.getName().equals(eventName)) {
                eventList.remove(i);
                isDeleted = true;
                break;
            }
        }

        if (isDeleted) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(EVENT_LIST_KEY, serializeEventList(eventList));
            editor.apply();
        }

        return isDeleted;
    }

    private String serializeEventList(List<Event> eventList) {
        StringBuilder sb = new StringBuilder();
        for (Event event : eventList) {
            sb.append(event.getId()).append(",")
                    .append(event.getName()).append(",")
                    .append(event.getDate()).append(",")
                    .append(event.getLocation()).append(";");
        }
        return sb.toString();
    }

    private List<Event> deserializeEventList(String str) {
        List<Event> eventList = new ArrayList<>();
        if (str.isEmpty()) {
            return eventList;
        }
        String[] events = str.split(";");
        for (String eventStr : events) {
            String[] parts = eventStr.split(",");
            if (parts.length == 4) {
                eventList.add(new Event(parts[0], parts[1], parts[2], parts[3]));
            }
        }
        return eventList;
    }

    public boolean eventExists(String eventName) {
        List<Event> eventList = getEventList();
        for (Event event : eventList) {
            if (event.getName().equals(eventName)) {
                return true;
            }
        }
        return false;
    }

    public Event getEvent(String eventName) {
        List<Event> eventList = getEventList();
        for (Event event : eventList) {
            if (event.getName().equals(eventName)) {
                return event;
            }
        }
        return null;
    }

    public List<Event> getAllEvents() {
        return getEventList();
    }

    public void saveEvents(List<Event> eventList) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EVENT_LIST_KEY, serializeEventList(eventList));
        editor.apply();
    }

    /*public boolean updateEvent(Event updatedEvent) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Récupérer tous les événements
        String eventsJson = sharedPreferences.getString(EVENT_LIST_KEY, "");
        List<Event> eventList = deserializeEventList(eventsJson); // Modifiez cette ligne pour utiliser votre méthode

        // Log avant la mise à jour
        Log.d("EventPreferences", "Avant mise à jour : " + eventList);

        // Trouver et mettre à jour l'événement
        for (int i = 0; i < eventList.size(); i++) {
            if (eventList.get(i).getId().equals(updatedEvent.getId())) {
                eventList.set(i, updatedEvent); // Mettre à jour l'événement
                break;
            }
        }

        // Log après la mise à jour
        Log.d("EventPreferences", "Après mise à jour : " + eventList);

        // Sauvegarder la liste d'événements mise à jour dans SharedPreferences
        String updatedEventsJson = serializeEventList(eventList); // Modifiez cette ligne pour utiliser votre méthode
        editor.putString(EVENT_LIST_KEY, updatedEventsJson);
        editor.apply();

        return true; // Indiquer que la mise à jour a réussi
    }
*/
}