package se.lundakarnevalen.extern.widget;

/**
 * Created by Markus on 2014-04-23.
 */
public class LKListElement {
    public String title;
    public String place;
    public float lat;
    public float lng;
    public int headerPicture;
    public int picture;
    public String question;
    public String info;

    public LKListElement(String place, String title, String info, float lat, float lng, int headerPicture, int picture, String question) {
        this.place = place;
        this.title = title;
        this.info = info;
        this.lat = lat;
        this.lng = lng;
        this.headerPicture = headerPicture;
        this.picture = picture;
        this.question = question;
    }


}