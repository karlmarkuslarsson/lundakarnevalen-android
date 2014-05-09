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
                R.string.smanojen_question,
                R.string.smanojen_info,
                55.704727f, 13.193241f,
                R.drawable.header_smanojen,
                R.drawable.smanojen_logo_list,
                DataType.SMALL_FUN));

        data.add(new DataElement(
                R.string.taltnojen,
                R.string.taltnojen_question,
                R.string.taltnojen_info,
                55.70483f,13.193881f,
                R.drawable.header_showen,
                R.drawable.tent_logo_list,
                DataType.TENT_FUN));
        data.add(new DataElement(
                R.string.tombolan,
                R.string.tombolan_question,
                R.string.tombolan_info,
                55.70505f,13.194297f,
                R.drawable.header_tombolan,
                R.drawable.tombolan_logo_list,
                DataType.TOMBOLAN));
        data.add(new DataElement(
                R.string.musik,
                R.string.musik_question,
                R.string.musik_info,
                55.705585f,13.193805f,
                R.drawable.header_kabaren,
                R.drawable.music_logo_list,
                DataType.MUSIC));

        return data;
    }

    public static List<DataElement> getAllFoodMultiData() {
        List<DataElement> data = new ArrayList<DataElement>();

        data.add(new DataElement(
                R.string.nangilima_place,
                R.string.nangilima_title,
                R.string.nangilima_info,
                55.706504880685f, 13.19547491457354f,
                R.drawable.header_nagilima,
                R.drawable.map_nagilima_logo,
                R.drawable.nagilima_logo,
                R.string.nangilima_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30",
                DataType.FOODSTOCK));

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


    public static List<DataElement> getAllOtherMultiData() {
        List<DataElement> data = new ArrayList<DataElement>();

        // TODO fix coordinates

        data.add(new DataElement(
                R.string.karne_name,
                R.drawable.music_logo,
                R.drawable.music_logo,
                DataType.PLAYER_FUTURAL));
        data.add(new DataElement(
                R.string.empty,
                R.string.train,
                R.string.train_info,
                1f,1f,
                R.drawable.header_taget,
                R.drawable.train_logo_other,
                R.drawable.train_logo_other,
                R.string.train_question,
                "LÖR/SÖN", "13:00-15:00", "13:00-15:00",
                DataType.TRAIN));

                data.add(new DataElement(
                R.string.radio_title,
                R.drawable.radio_logo,
                R.drawable.radio_logo,
                DataType.PLAYER_RADIO));
        data.add(new DataElement(
                R.string.toilets_title,
                R.string.empty,
                R.string.toilets_info,
                1f,1f,
                R.drawable.header_toaletter,
                R.drawable.toilets_logo,
                DataType.TOILETS));
        data.add(new DataElement(
                R.string.security_title,
                R.string.empty,
                R.string.security_info,
                1f,1f,
                R.drawable.header_sakerhet,
                R.drawable.security_logo,
                DataType.SECURITY));
        data.add(new DataElement(
                R.string.parking_title,
                R.drawable.parking_logo,
                R.drawable.parking_logo,
                DataType.PARKING));
        data.add(new DataElement(
                R.string.shoppen,
                R.string.shoppen_question,
                R.string.shoppen_info,
                55.70557904967363f, 13.19512260551833f,
                R.drawable.header_shoppen,
                R.drawable.shoppen_logo_other,
                DataType.SHOPPEN));

        data.add(new DataElement(
                R.string.care_title,
                R.string.empty,
                R.string.care_info,
                1f,1f,
                R.drawable.header_sjukvard,
                R.drawable.health_logo,
                DataType.CARE));

        data.add(new DataElement(
                R.string.entre,
                R.string.entre_question,
                R.string.entre_info,
                1f,1f,
                R.drawable.header_entre,
                R.drawable.entre_logo_other,
                DataType.ENTRANCE));
        data.add(new DataElement(
                R.string.trashcan,
                R.string.trash_question,
                R.string.trash_info,
                1f,1f,
                R.drawable.header_soptunna,
                R.drawable.trash_logo_other,
                DataType.TRASHCAN));



        return data;
    }
}
