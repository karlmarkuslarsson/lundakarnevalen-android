package se.lundakarnevalen.extern.data;

import android.text.Html;

import java.util.ArrayList;
import java.util.List;

import se.lundakarnevalen.extern.android.R;

/**
 * Created by Fredrik on 2014-05-04.
 */
public class DataContainer {

    public static List<DataElement> getAllData() {
        List<DataElement> data = new ArrayList<DataElement>();
        data.add(new DataElement(
                R.string.barneval_place,
                R.string.barneval_title,
                R.string.barneval_info,
                55.7037889f, 13.194647222222223f,
                R.drawable.header_barnevalen,
                R.drawable.barnevalen_logo,
                R.string.barneval_question,
                "14:00-20:00", "14:00-21:00", "14:00-20:00", DataType.FUN));

        data.add(new DataElement(
                R.string.cirkus_place,
                R.string.cirkus_title,
                R.string.cirkus_info,
                55.7048333f, 13.195352777777778f,
                R.drawable.header_cirkusen,
                R.drawable.cirkusen_logo,
                R.string.cirkus_question,
                "15:00-22:00", "15:00-23:00", "15:00-22:00", DataType.FUN));

        data.add(new DataElement(
                R.string.filmen_place,
                R.string.filmen_title,
                R.string.filmen_info,
                55.7059389f, 13.194805555555556f,
                R.drawable.monk,
                R.drawable.filmen_logo,
                R.string.filmen_question,
                "14:00-23:00", "14:00-00:00", "14:00-23:00", DataType.FUN));

        data.add(new DataElement(
                R.string.kabare_place,
                R.string.kabare_title,
                R.string.kabare_info,
                55.7042667f, 13.193833333333334f,
                R.drawable.header_kabaren,
                R.drawable.kabaren_logo,
                R.string.kabare_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30", DataType.FUN));

        data.add(new DataElement(
                R.string.revy_place,
                R.string.revy_title,
                R.string.revy_info,
                55.705775f, 13.193555555555555f,
                R.drawable.header_revyn,
                R.drawable.revyn_logo,
                R.string.revy_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30", DataType.FUN));

        data.add(new DataElement(
                R.string.showen_place,
                R.string.show_title,
                R.string.show_info,
                55.7055444f, 13.195588888888889f,
                R.drawable.monk,
                R.drawable.showen_logo,
                R.string.showen_question,
                "14:00-21:30", "14:00-21:30", "14:00-21:30", DataType.FUN));

        data.add(new DataElement(
                R.string.spexet_place,
                R.string.spexet_title,
                R.string.spexet_info,
                55.7054111f, 13.195491666666667f,
                R.drawable.monk,
                R.drawable.spexet_logo,
                R.string.spexet_question,
                "14:00-21:00", "14:00-21:00", "14:00-21:00", DataType.FUN));

        data.add(new DataElement(
                R.string.dansen_place,
                R.string.dansen_title,
                R.string.dansen_info,
                55.70572f, 13.19544f,
                R.drawable.header_dansen,
                R.drawable.dansen_logo,
                R.string.dansen_question,
                "23:00-04:00", "23:00-04:00", "23:00-04:00", DataType.FUN));

        data.add(new DataElement(
                R.string.cocktail_place,
                R.string.cocktail_title,
                R.string.cocktail_info,
                55.706362f, 13.195165f,
                R.drawable.header_nangilima,
                R.drawable.nagilima_logo,
                R.string.cocktail_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30",
                DataType.FOOD
        ));

        data.add(new DataElement(
                R.string.hipp_baren_place,
                R.string.hipp_baren_title,
                R.string.hipp_baren_info,
                55.706521f, 13.195431f,
                R.drawable.header_nangilima,
                R.drawable.nagilima_logo,
                R.string.hipp_baren_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30",
                DataType.FOOD));


        data.add(new DataElement(
                R.string.folkan_place,
                R.string.folkan_title,
                R.string.folkan_info,
                55.706841f, 13.196030f,
                R.drawable.header_nangilima,
                R.drawable.nagilima_logo,
                R.string.folkan_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30",
                DataType.FOOD));

        ArrayList<Integer> menu = new ArrayList<Integer>();
        ArrayList<String> menuPrice = new ArrayList<String>();
        menu.add(R.string.undervatten_food1);
        menu.add(R.string.undervatten_food2);
        menu.add(R.string.undervatten_food3);
        //   menu.add("");
        menuPrice.add("??");
        menuPrice.add("??");
        menuPrice.add("??");

        data.add(new DataElement(
                R.string.krog_undervatten_place,
                R.string.krog_undervatten_title,
                R.string.krog_undervatten_info,
                55.705154f, 13.19458f,
                R.drawable.header_krogundervatten,
                R.drawable.undervatten_logo,
                R.string.krog_undervatten_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30",
                DataType.FOOD, menu, menuPrice));

        data.add(new DataElement(
                R.string.krog_lajka_place,
                R.string.krog_lajka_title,
                R.string.krog_lajka_info,
                55.705697f, 13.194630f,
                R.drawable.monk,
                R.drawable.undervatten_logo,
                R.string.krog_lajka_question,
                "12:00-01:00", "12:00-01:00", "12:00-24:00",
                DataType.FOOD));
        menu = new ArrayList<Integer>();
        menuPrice = new ArrayList<String>();
        menu.add(R.string.thyme_food1);
        menu.add(R.string.thyme_food2);
        //   menu.add("");
        menuPrice.add("??");
        menuPrice.add("??");


        data.add(new DataElement(
                R.string.krog_thyme_place,
                R.string.krog_thyme_title,
                R.string.krog_lajka_info,
                55.705697f, 13.194630f,
                R.drawable.monk,
                R.drawable.fine_dine_logo,
                R.string.krog_thyme_question,
                "14:00-02:00", "14:00-02:00", "14:00-02:00",
                DataType.FOOD,
                menu, menuPrice));

        data.add(new DataElement(
                R.string.karne_name,
                R.drawable.music_logo,
                DataType.FUTURAL));
        data.add(new DataElement(
                R.string.train,
                R.drawable.train_logo_other,
                DataType.TRAIN));
        data.add(new DataElement(
                R.string.radio_title,
                R.drawable.radio_logo,
                DataType.RADIO));
        data.add(new DataElement(
                R.string.toilets_title,
                R.drawable.toilets_logo,
                DataType.OTHER));
        data.add(new DataElement(
                R.string.security_title,
                R.drawable.security_logo,
                DataType.OTHER));
        data.add(new DataElement(
                R.string.atm_title,
                R.drawable.atm_logo,
                DataType.OTHER));
        data.add(new DataElement(
                R.string.parking_title,
                R.drawable.parking_logo,
                DataType.OTHER));
        data.add(new DataElement(
                R.string.care_title,
                R.drawable.health_logo,
                DataType.OTHER));

        data.add(new DataElement(R.string.smanojen, R.string.sketchera, 55.70483928999759f, 13.19305851161833f, R.drawable.bubble_smanojen, DataType.SMALL_FUN));
        data.add(new DataElement(R.string.smanojen, R.string.undergangen, 55.70460881490317f, 13.19352521597889f, R.drawable.bubble_smanojen, DataType.SMALL_FUN));
        data.add(new DataElement(R.string.smanojen, R.string.futuralfuneral, 55.70450982359015f, 13.19322614967337f, R.drawable.bubble_smanojen, DataType.SMALL_FUN));


        return data;
    }

    public static List<DataElement> getDataOfType(DataType type) {
        List<DataElement> data = new ArrayList<DataElement>();
        for (DataElement element : getAllData()) {
            if (element.type == type) {
                data.add(element);
            }
        }
        return data;
    }
}
