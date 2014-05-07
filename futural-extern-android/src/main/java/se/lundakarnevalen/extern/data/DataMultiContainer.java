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

        // TODO fix coordinates
        data.add(new DataElement(
                R.string.smanojen,
                R.string.smanojen_question,
                R.string.smanojen_info,
                1f,1f,
                R.drawable.header_other,
                R.drawable.smanojen_logo_list,
                DataType.SMALL_FUN));

        data.add(new DataElement(
                R.string.taltnojen,
                R.string.taltnojen_question,
                R.string.taltnojen_info,
                1f,1f,
                R.drawable.header_showen,
                R.drawable.tent_logo_list,
                DataType.TENT_FUN));
        data.add(new DataElement(
                R.string.tombolan,
                R.string.tombolan_question,
                R.string.tombolan_info,
                1f,1f,
                R.drawable.header_tombolan,
                R.drawable.tombolan_logo_list,
                DataType.TOMBOLAN));
        data.add(new DataElement(
                R.string.musik,
                R.string.musik_question,
                R.string.musik_info,
                1f,1f,
                R.drawable.header_kabaren,
                R.drawable.music_logo_list,
                DataType.MUSIC));

        return data;
    }

    public static List<DataElement> getAllFoodMultiData() {
        List<DataElement> data = new ArrayList<DataElement>();

        // TODO fix coordinates
        data.add(new DataElement(
                R.string.snaxeriet,
                R.string.snaxeriet_question,
                R.string.snaxeriet_info,
                1f,1f,
                R.drawable.header_snaxeriet,
                R.drawable.snaxeriet_logo,
                DataType.SNACKS));


        return data;
    }
}
