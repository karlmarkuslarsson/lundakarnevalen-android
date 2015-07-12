package se.lundakarnevalen.extern.scheme;

import java.util.Date;

public class Event {
    public String place;
    public String title;
    public int image;
    public Date startDate;
    public Date endDate;
    public int id;

    public Event(String place, String title, int image, Date startDate,Date endDate,int id) {
        this.endDate = endDate;
        this.place = place;
        this.title = title;
        this.image = image;
        this.startDate = startDate;
        this.id = id;
    }
}
