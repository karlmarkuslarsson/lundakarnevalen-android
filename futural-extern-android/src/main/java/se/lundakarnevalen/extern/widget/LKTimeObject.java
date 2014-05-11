package se.lundakarnevalen.extern.widget;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Markus on 2014-04-25.
 */
public class LKTimeObject implements Parcelable {
    public int startHourFriday;
    public int startHourSaturday;
    public int startHourSunday;
    public int endHourFriday;
    public int endHourSaturday;
    public int endHourSunday;

    public LKTimeObject(int endHourSunday, int startHourFriday, int startHourSaturday, int startHourSunday, int endHourFriday, int endHourSaturday) {
        this.endHourSunday = endHourSunday;
        this.startHourFriday = startHourFriday;
        this.startHourSaturday = startHourSaturday;
        this.startHourSunday = startHourSunday;
        this.endHourFriday = endHourFriday;
        this.endHourSaturday = endHourSaturday;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(startHourFriday);
        parcel.writeInt(startHourSaturday);
        parcel.writeInt(startHourSunday);

        parcel.writeInt(endHourFriday);
        parcel.writeInt(endHourSaturday);
        parcel.writeInt(endHourSunday);

    }
}
