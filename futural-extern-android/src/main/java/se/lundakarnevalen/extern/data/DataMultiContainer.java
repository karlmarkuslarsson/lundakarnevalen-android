package se.lundakarnevalen.extern.data;

import java.util.ArrayList;
import java.util.List;

import se.lundakarnevalen.extern.android.R;

/**
 * Created by Markus on 2014-05-07.
 */
public class DataMultiContainer {
    public static List<DataElement> getAllFunMultiData() {
        List<DataElement> data = new ArrayList<DataElement>();


        data.add(new DataElement(
                R.string.smanojen,
                R.string.smanojen_place,
                R.string.smanojen_question,
                R.string.smanojen_info,
                1f,1f,
                R.drawable.header_showen,
                R.drawable.smanojen_logo_list,
                DataType.SMALL_FUN));

        data.add(new DataElement(
                R.string.taltnojen,
                R.string.taltnojen_place,
                R.string.taltnojen_question,
                R.string.taltnojen_info,
                1f,1f,
                R.drawable.header_showen,
                R.drawable.taltnojen_logo,
                DataType.TENT_FUN));
        data.add(new DataElement(
                R.string.tombolan,
                R.string.tombolan_place,
                R.string.tombolan_question,
                R.string.tombolan_info,
                1f,1f,
                R.drawable.header_tombolan,
                R.drawable.bubble_tombolan,
                DataType.TOMBOLAN));
        data.add(new DataElement(
                R.string.musik,
                R.string.musik_place,
                R.string.musik_question,
                R.string.musik_info,
                1f,1f,
                R.drawable.header_tombolan,
                R.drawable.music_logo,
                DataType.MUSIC));

        return data;
    }

}
