package se.lundakarnevalen.extern.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Markus on 2014-04-23.
 */
public class DataElement implements Parcelable {
    public String timeFriday = "";
    public String timeSaturday = "";
    public String timeSunday = "";
    public int card = 1;
    public int cash = 1;
    public int title;
    public int place;
    public float lat;
    public float lng;
    public int headerPicture;
    public int picture;
    public int question;
    public int info;
    public DataType type;
    public ArrayList<Integer> menu;
    public ArrayList<String> menuPrice;

    public DataElement(int place, int title, int info, float lat, float lng, int headerPicture, int picture, int question, String timeFriday, String timeSaturday, String timeSunday, DataType type) {
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


    public DataElement(int place, int title, int info, float lat, float lng, int headerPicture, int picture, int question, String timeFriday, String timeSaturday, String timeSunday, DataType type, ArrayList<Integer> menu, ArrayList<String> menuPrice) {
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
        this.menu = menu;
        this.menuPrice = menuPrice;
    }

    public DataElement(int place, int title, int info, float lat, float lng, int headerPicture, int picture, int question, DataType type) {
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

    public DataElement(int title, int picture, DataType type) {
        this.title = title;
        this.picture = picture;
        this.type = type;
    }

    public DataElement(int place, int title, float lat, float lng, int picture, DataType type) {
        this.place = place;
        this.title = title;
        this.lat = lat;
        this.lng = lng;
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
        parcel.writeSerializable(type);
        parcel.writeInt(info);
        parcel.writeInt(question);
        parcel.writeInt(title);
        parcel.writeInt(place);
        parcel.writeInt(cash);
        parcel.writeInt(card);
        parcel.writeString(timeFriday);
        parcel.writeString(timeSaturday);
        parcel.writeString(timeSunday);
        parcel.writeSerializable(menu);
        parcel.writeStringList(menuPrice);
    }

    public boolean hasLandingPage() {
        return info > 0;
    }
}
