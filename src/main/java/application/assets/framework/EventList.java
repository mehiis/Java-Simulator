package application.assets.framework;

import java.util.PriorityQueue;

public class EventList {
    private PriorityQueue<Event> list = new PriorityQueue<Event>();

    public EventList() {

    }

    public Event delete() {
        Trace.out(Trace.Level.INFO, "Tapahtumalistasta poisto " + list.peek().getType() + " " + list.peek().getTime());
        return list.remove();
    }

    public void add(Event t) {
        Trace.out(Trace.Level.INFO, "Tapahtumalistaan lisätään uusi " + t.getType() + " " + t.getTime());
        list.add(t);
    }

    public Event peek() {
        return list.peek();
    }

    public double getNextTime() {
        return list.peek().getTime();
    }


}
