package se.lundakarnevalen.extern.scheme;

import java.util.Date;

/**
 * Created by Markus on 2014-04-21.
 */
public class Event {
    public String place;
    public String title;
    public int image;
    public Date startDate;
    public Date endDate;

    public Event(String place, String title, int image, Date startDate,Date endDate) {
        this.endDate = endDate;
        this.place = place;
        this.title = title;
        this.image = image;
        this.startDate = startDate;
    }


}
