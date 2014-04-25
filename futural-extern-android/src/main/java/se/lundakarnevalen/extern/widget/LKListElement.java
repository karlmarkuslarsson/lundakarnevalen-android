package se.lundakarnevalen.extern.widget;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Markus on 2014-04-23.
 */
public class LKListElement implements Parcelable {
    public String timeFriday = "";
    public String timeSaturday = "";
    public String timeSunday = "";
    public int card = 1;
    public int cash = 1;
    public String title;
    public String place;
    public float lat;
    public float lng;
    public int headerPicture;
    public int picture;
    public String question;
    public String info;
    public int type;

    public LKListElement(String place, String title, String info, float lat, float lng, int headerPicture, int picture, String question,String timeFriday,String timeSaturday,String timeSunday, int type) {
        this.place = place;
        this.title = title;
        this.info = info;
        this.lat = lat;
        this.lng = lng;
        this.headerPicture = headerPicture;
        this.picture = picture;
        this.question = question;
        this.type = type;
        this.timeFriday = timeFriday;
        this.timeSaturday = timeSaturday;
        this.timeSunday = timeSunday;
    }

    public LKListElement(String place, String title, String info, float lat, float lng, int headerPicture, int picture, String question ,int type) {
        this.place = place;
        this.title = title;
        this.info = info;
        this.lat = lat;
        this.lng = lng;
        this.headerPicture = headerPicture;
        this.picture = picture;
        this.question = question;
        this.type = type;
    }

    public LKListElement(String title, int picture, int type) {
        this.title = title;
        this.picture = picture;
        this.type = type;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(lat);
        parcel.writeFloat(lng);
        parcel.writeInt(headerPicture);
        parcel.writeInt(picture);
        parcel.writeInt(type);
        parcel.writeString(info);
        parcel.writeString(question);
        parcel.writeString(title);
        parcel.writeString(place);
        parcel.writeInt(cash);
        parcel.writeInt(card);
        parcel.writeString(timeFriday);
        parcel.writeString(timeSaturday);
        parcel.writeString(timeSunday);

    }
}
