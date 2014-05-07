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


        return data;
    }

}
