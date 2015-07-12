package se.lundakarnevalen.extern.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import se.lundakarnevalen.extern.android.R;

public class DataMultiContainer {

    private static List<DataElement> funData = new ArrayList<>();
    private static List<DataElement> foodData = new ArrayList<>();
    private static List<DataElement> otherData = new ArrayList<>();


    public static List<DataElement> getAllFunMultiData() {

            if(!funData.isEmpty()) {
                    return funData;
            }


        funData.add(new DataElement(
                R.string.smanojen,
                R.string.smanojen_question,
                R.string.smanojen_info,
                55.704727f, 13.193241f,
                R.drawable.header_smanojen,
                R.drawable.smanojen_logo_list,
                DataType.SMALL_FUN));

        funData.add(new DataElement(
                R.string.taltnojen,
                R.string.taltnojen_question,
                R.string.taltnojen_info,
                55.70483f, 13.193881f,
                R.drawable.header_taltnojen,
                R.drawable.tent_logo_list,
                DataType.TENT_FUN));
        funData.add(new DataElement(
                R.string.tombolan,
                R.string.tombolan_question,
                R.string.tombolan_info,
                55.70505f, 13.194297f,
                R.drawable.header_tombolan,
                R.drawable.tombolan_logo_list,
                DataType.TOMBOLAN));
        funData.add(new DataElement(
                R.string.musik,
                R.string.musik_question,
                R.string.musik_info,
                55.705585f, 13.193805f,
                R.drawable.header_musiken,
                R.drawable.music_logo_list,
                DataType.MUSIC));
        funData = Collections.unmodifiableList(funData);

        return funData;
    }

    public static List<DataElement> getAllFoodMultiData() {
        if(!foodData.isEmpty()) {
            return foodData;
        }


        foodData.add(new DataElement(
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

        foodData.add(new DataElement(
                R.string.snaxeriet,
                R.string.snaxeriet_question,
                R.string.snaxeriet_info,
                55.705345f, 13.194314f,
                R.drawable.header_snaxeriet,
                R.drawable.snaxeriet_logo,
                DataType.SNACKS));
        foodData = Collections.unmodifiableList(foodData);

        return foodData;
    }


    public static List<DataElement> getAllOtherMultiData() {
        if(!otherData.isEmpty()){
                return otherData;
        }

            otherData.add(new DataElement(
                    R.string.song_book,
                    R.drawable.songs_logo_other,
                    R.drawable.songs_logo_other,
                    DataType.SONG_BOOK));

        otherData.add(new DataElement(
                R.string.karne_name,
                R.drawable.music_logo,
                R.drawable.music_logo,
                DataType.PLAYER_FUTURAL));
        otherData.add(new DataElement(
                R.string.empty,
                R.string.train,
                R.string.train_info,
                1f, 1f,
                R.drawable.header_taget,
                R.drawable.train_logo_other,
                R.drawable.train_logo_other,
                R.string.train_question,
                "LÖR/SÖN", "13:00-15:00", "13:00-15:00",
                DataType.TRAIN));

                otherData.add(new DataElement(
                        R.string.radio_title,
                        R.drawable.radio_logo,
                        R.drawable.radio_logo,
                        DataType.PLAYER_RADIO));
        otherData.add(new DataElement(
                R.string.toilets_title,
                R.string.empty,
                R.string.toilets_info,
                55.705345f, 13.194597f,
                R.drawable.header_toaletter,
                R.drawable.toilets_logo,
                DataType.TOILETS));
        otherData.add(new DataElement(
                R.string.security_title,
                R.string.empty,
                R.string.security_info,
                55.70528f, 13.194082f,
                R.drawable.header_sakerhet,
                R.drawable.security_logo,
                DataType.SECURITY));
        otherData.add(new DataElement(
                R.string.parking_title,
                R.drawable.parking_logo,
                R.drawable.parking_logo,
                DataType.PARKING));
        otherData.add(new DataElement(
                R.string.shoppen,
                R.string.shoppen_question,
                R.string.shoppen_info,
                55.70557904967363f, 13.19512260551833f,
                R.drawable.header_shoppen,
                R.drawable.shoppen_logo_other,
                DataType.SHOPPEN));

        otherData.add(new DataElement(
                R.string.care_title,
                R.string.empty,
                R.string.care_info,
                55.70593f, 13.195318f,
                R.drawable.header_sjukvard,
                R.drawable.health_logo,
                DataType.CARE));

        otherData.add(new DataElement(
                R.string.entre,
                R.string.entre_question,
                R.string.entre_info,
                55.705185f, 13.193291f,
                R.drawable.header_entre,
                R.drawable.entre_logo_other,
                DataType.ENTRANCE));
        otherData.add(new DataElement(
                R.string.trashcan,
                R.string.trash_question,
                R.string.trash_info,
                55.704998f, 13.193969f,
                R.drawable.header_soptunna,
                R.drawable.trash_logo_other,
                DataType.TRASHCAN));

        otherData = Collections.unmodifiableList(otherData);
        return otherData;
    }
}
