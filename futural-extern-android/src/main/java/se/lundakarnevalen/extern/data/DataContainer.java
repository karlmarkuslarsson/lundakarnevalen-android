package se.lundakarnevalen.extern.data;

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
                R.drawable.map_barnevalen_logo,
                R.drawable.barnevalen_logo,
                R.string.barneval_question,
                "14:00-20:00", "14:00-21:00", "14:00-20:00", DataType.FUN));

        data.add(new DataElement(
                R.string.cirkus_place,
                R.string.cirkus_title,
                R.string.cirkus_info,
                55.7047557721988f, 13.19537105245979f,
                R.drawable.header_cirkusen,
                R.drawable.map_cirkusen_logo,
                R.drawable.cirkusen_logo,
                R.string.cirkus_question,
                "15:00-22:00", "15:00-23:00", "15:00-22:00", DataType.FUN));

        data.add(new DataElement(
                R.string.filmen_place,
                R.string.filmen_title,
                R.string.filmen_info,
                55.70605421252043f, 13.19455318842597f,
                R.drawable.header_filmen,
                R.drawable.map_filmen_logo,
                R.drawable.filmen_logo,
                R.string.filmen_question,
                "14:00-23:00", "14:00-00:00", "14:00-23:00", DataType.FUN));

        data.add(new DataElement(
                R.string.kabare_place,
                R.string.kabare_title,
                R.string.kabare_info,
                55.70435384485661f, 13.19383678585551f,
                R.drawable.header_kabaren,
                R.drawable.map_kabaren_logo,
                R.drawable.kabaren_logo,
                R.string.kabare_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30", DataType.FUN));

        data.add(new DataElement(
                R.string.revy_place,
                R.string.revy_title,
                R.string.revy_info,
                55.705775f, 13.193555555555555f,
                R.drawable.header_revyn,
                R.drawable.map_revyn_logo,
                R.drawable.revyn_logo,
                R.string.revy_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30", DataType.FUN));

        data.add(new DataElement(
                R.string.showen_place,
                R.string.show_title,
                R.string.show_info,
                55.70561432124412f, 13.19560148445819f,
                R.drawable.header_showen,
                R.drawable.map_showen_logo,
                R.drawable.showen_logo,
                R.string.showen_question,
                "14:00-21:30", "14:00-21:30", "14:00-21:30", DataType.FUN));

        data.add(new DataElement(
                R.string.spexet_place,
                R.string.spexet_title,
                R.string.spexet_info,
                55.705443f, 13.195835f,
                R.drawable.header_spexet,
                R.drawable.map_spexet_logo,
                R.drawable.spexet_logo,
                R.string.spexet_question,
                "14:00-21:00", "14:00-21:00", "14:00-21:00", DataType.FUN));

        data.add(new DataElement(
                R.string.dansen_place,
                R.string.dansen_title,
                R.string.dansen_info,
                55.705376f, 13.195363f,
                R.drawable.header_dansen,
                R.drawable.map_dansen_logo,
                R.drawable.dansen_logo,
                R.string.dansen_question,
                "23:00-04:00", "23:00-04:00", "23:00-04:00", DataType.FUN));

        data.add(new DataElement(
                R.string.cocktail_place,
                R.string.cocktail_title,
                R.string.cocktail_info,
                55.706362f, 13.195165f,
                R.drawable.header_nangilima,
                R.drawable.map_nagilima_logo,
                R.drawable.nagilima_logo,
                R.string.cocktail_question,
                "15:30-22:30", "15:30-23:30", "15:30-22:30",
                DataType.FOOD
        ));

        data.add(new DataElement(
                R.string.hipp_baren_place,
                R.string.hipp_baren_title,
                R.string.hipp_baren_info,
                55.706504880685f, 13.19547491457354f,
                R.drawable.header_nangilima,
                R.drawable.map_nagilima_logo,
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
                R.drawable.map_nagilima_logo,
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
                55.70508561404364f, 13.19467199442641f,
                R.drawable.header_krogundervatten,
                R.drawable.map_krog_undervatten_logo,
                R.drawable.undervatten_logo,
                R.string.krog_undervatten_question,
                "12:00-01:00", "12:00-01:00", "12:00-01:00",
                DataType.FOOD, menu, menuPrice));

        menu = new ArrayList<Integer>();
        menuPrice = new ArrayList<String>();
        menu.add(R.string.lajka_food1);
        menu.add(R.string.lajka_food2);
        //   menu.add("");
        menuPrice.add("50");
        menuPrice.add("50");

//TODO check time
        data.add(new DataElement(

                R.string.krog_lajka_place,
                R.string.krog_lajka_title,
                R.string.krog_lajka_info,
                55.70573799412114f, 13.19474001701577f,
                R.drawable.monk,
                R.drawable.map_lajka_icon,
                R.drawable.lajka_icon,
                R.string.krog_lajka_question,
                "12:00-01:00", "12:00-01:00", "12:00-24:00",
                DataType.FOOD, menu, menuPrice));
        menu = new ArrayList<Integer>();
        menuPrice = new ArrayList<String>();
        menu.add(R.string.thyme_food1);
        menu.add(R.string.thyme_food2);
        //   menu.add("");
        menuPrice.add("70");
        menuPrice.add("70");


        data.add(new DataElement(
                R.string.krog_thyme_place,
                R.string.krog_thyme_title,
                R.string.krog_thyme_info,
                55.705697f, 13.194630f,
                R.drawable.header_other,
                R.drawable.map_thyme_travel,
                R.drawable.thyme_travel_logo,
                R.string.krog_thyme_question,
                "14:00-02:00", "14:00-02:00", "14:00-02:00",
                DataType.FOOD,
                menu, menuPrice));

        /*
        data.add(new DataElement(
                R.string.atm_title,
                R.drawable.atm_logo,
                R.drawable.atm_logo,
                DataType.ATM));
        */

        data.add(new DataElement(R.string.biljetteriet,R.string.biljetteriet_place,R.string.biljetteriet_question,R.string.biljetteriet_info, 55.70502736279169f, 13.19517650439325f, R.drawable.map_biljetteriet_icon,R.drawable.biljetteriet_logo_list, DataType.BILJETTERIET));




        data.add(new DataElement(R.string.smanojen, R.string.undergangen, 55.70460881490317f, 13.19352521597889f, R.drawable.bubble_smanojen, DataType.SMALL_FUN));
        data.add(new DataElement(R.string.smanojen, R.string.sketchera, 55.70483928999759f, 13.19305851161833f, R.drawable.bubble_smanojen, DataType.SMALL_FUN));
        data.add(new DataElement(R.string.smanojen, R.string.futuralfuneral, 55.70450982359015f, 13.19322614967337f, R.drawable.bubble_smanojen, DataType.SMALL_FUN));

        data.add(new DataElement(R.string.taltnojen, R.string.studenthistoriska_riksmuseet, 55.705053732349f, 13.1930051727718f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.babe_gone_bad, 55.70670843454561f, 13.19557811034903f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.vem_vet_sex, 55.7066857659761f, 13.1956639410537f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.ga_pa_vatten_med_jesus, 55.70497849657609f, 13.19363857294538f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.the_moon_exhibition, 55.70496791748455f, 13.19374988460371f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.future, 55.70487490800033f, 13.19361516583629f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.radda_lundagard, 55.70477162149118f, 13.19366770773009f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.futurel, 55.70474327928672f, 13.19386755037008f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.gudtyckligt, 55.70487120205509f, 13.19399670036882f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.robotic_orchestra, 55.70491716219782f, 13.19410953667312f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.ristorante_del_futuro, 55.70460616403029f, 13.19484501185647f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.framtidens_foto, 55.70460846205428f, 13.1940932229268f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.max_iv, 55.70477698351564f, 13.19424548398832f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.siartaltet, 55.70471570305888f, 13.19412313135794f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.the_day_after_tomorrow, 55.70464216642544f, 13.1939776676648f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.futural_sjukcentral, 55.70455254336257f, 13.19378734132492f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.moocs, 55.70445066216406f, 13.19348825390201f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.tidsresorse, 55.70437865703956f, 13.19333871179819f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.lunara_nationen, 55.70428614757939f, 13.19436877064109f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.bucket_list, 55.70434433369048f, 13.19434999518406f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));
        data.add(new DataElement(R.string.taltnojen, R.string.karneroj, 55.70440176403623f, 13.1943178086826f, R.drawable.map_taltnojen_logo, DataType.TENT_FUN));

        data.add(new DataElement(R.string.barnevalen, R.string.festtaltet, 55.70418149695015f, 13.19458440352848f, R.drawable.map_barnevalen_logo, DataType.BARNEVALEN));
        data.add(new DataElement(R.string.barnevalen, R.string.gassimulator_3000, 55.7040650548606f, 13.19468165205387f, R.drawable.map_barnevalen_logo, DataType.BARNEVALEN));
        data.add(new DataElement(R.string.barnevalen, R.string.cafe_kosmos, 55.70409508582605f, 13.19495755442542f, R.drawable.map_barnevalen_logo, DataType.BARNEVALEN));
        data.add(new DataElement(R.string.barnevalen, R.string.forbjudna_taltet, 55.70399958255682f, 13.19495661742418f, R.drawable.map_barnevalen_logo, DataType.BARNEVALEN));
        data.add(new DataElement(R.string.barnevalen, R.string.bak_och_framtidsresan, 55.7039678443762f, 13.19482518919924f, R.drawable.map_barnevalen_logo, DataType.BARNEVALEN));
        data.add(new DataElement(R.string.barnevalen, R.string.astronautakademin, 55.7038136872786f, 13.19461463574077f, R.drawable.map_barnevalen_logo, DataType.BARNEVALEN));

        data.add(new DataElement(R.string.toilets_title, R.string.toilets_title, 55.70648780072795f, 13.1958402043337f, R.drawable.map_toilets_logo, DataType.TOILETS));
        data.add(new DataElement(R.string.toilets_title, R.string.toilets_title, 55.70603133250371f, 13.19404062814232f, R.drawable.map_toilets_logo, DataType.TOILETS));
        data.add(new DataElement(R.string.toilets_title, R.string.toilets_title, 55.70485187667411f, 13.19570665852305f, R.drawable.map_toilets_logo, DataType.TOILETS));
        data.add(new DataElement(R.string.toilets_title, R.string.toilets_title, 55.705228407599f, 13.19285637404313f, R.drawable.map_toilets_logo, DataType.TOILETS));
        data.add(new DataElement(R.string.toilets_title, R.string.toilets_title, 55.70372639868275f, 13.19470515375246f, R.drawable.map_toilets_logo, DataType.TOILETS));
        data.add(new DataElement(R.string.toilets_title, R.string.toilets_title, 55.70531936414573f, 13.19419428089188f, R.drawable.map_toilets_logo, DataType.TOILETS));
        data.add(new DataElement(R.string.toilets_title, R.string.toilets_title, 55.70438108228282f, 13.19472544482855f, R.drawable.map_toilets_logo, DataType.TOILETS));

        data.add(new DataElement(R.string.care_title, R.string.care_title, 55.70589745349834f, 13.195349039021f, R.drawable.map_health_logo, DataType.CARE));



        data.add(new DataElement(R.string.police, R.string.police, 55.70566708948963f, 13.19601302178581f, R.drawable.map_security_logo, DataType.POLICE));
        data.add(new DataElement(R.string.police, R.string.police, 55.7052602564826f, 13.19401040144301f, R.drawable.map_security_logo, DataType.POLICE));

        data.add(new DataElement(R.string.radio, R.string.radio, 55.70537955178818f, 13.19494142376257f, R.drawable.map_radio_logo, DataType.RADIO));

        data.add(new DataElement(R.string.entre, R.string.en_lundensares_guide_till_galaxen, 55.7056318688607f, 13.19298738463846f, R.drawable.map_entrance_icon, DataType.ENTRANCE));
        data.add(new DataElement(R.string.entre, R.string.futopia, 55.7060196455733f, 13.19565500654615f, R.drawable.map_entrance_icon, DataType.ENTRANCE));
        data.add(new DataElement(R.string.entre, R.string.botaniska_ar_3014, 55.7051354472479f, 13.19293492729316f, R.drawable.map_entrance_icon, DataType.ENTRANCE));
        data.add(new DataElement(R.string.entre, R.string.futuralteleportal, 55.7043920444683f, 13.19313558627814f, R.drawable.map_entrance_icon, DataType.ENTRANCE));
        data.add(new DataElement(R.string.entre, R.string.intergalaktisk_invandring, 55.7037410100921f, 13.19423114265037f, R.drawable.map_entrance_icon, DataType.ENTRANCE));
        data.add(new DataElement(R.string.entre, R.string.ett_svart_hal, 55.7044690873277f, 13.19560129896877f, R.drawable.map_entrance_icon, DataType.ENTRANCE));

        data.add(new DataElement(R.string.snacks, R.string.discokaffetalt, 55.70519298937666f, 13.19317860493478f, R.drawable.map_snaxeriet_logo, DataType.SNACKS));
        data.add(new DataElement(R.string.snacks, R.string.snacks_iv, 55.70585795050108f, 13.1943144945812f, R.drawable.map_snaxeriet_logo, DataType.SNACKS));
        data.add(new DataElement(R.string.snacks, R.string.lyxbrygghus, 55.70547249304708f, 13.19451161073117f, R.drawable.map_snaxeriet_logo, DataType.SNACKS));
        data.add(new DataElement(R.string.snacks, R.string.lyxbrygghus, 55.70479839152454f, 13.19472215029551f, R.drawable.map_snaxeriet_logo, DataType.SNACKS));
        data.add(new DataElement(R.string.snacks, R.string.kungliga_snacksboden_estrelle, 55.70520990752672f, 13.19387434532636f, R.drawable.map_snaxeriet_logo, DataType.SNACKS));
        data.add(new DataElement(R.string.snacks, R.string.bacon_oriley, 55.70642208831894f, 13.1957497314886f, R.drawable.map_snaxeriet_logo, DataType.SNACKS));

        data.add(new DataElement(R.string.tombolan, R.string.putins_bodega_put_in_putin, 55.70652897496141f, 13.19567109412686f, R.drawable.bubble_tombolan, DataType.TOMBOLAN));
        data.add(new DataElement(R.string.tombolan, R.string.rysk_roulette, 55.70598296464737f, 13.19420818471242f, R.drawable.bubble_tombolan, DataType.TOMBOLAN));
        data.add(new DataElement(R.string.tombolan, R.string.hipster_eller_hobo, 55.70597691961526f, 13.19425780556136f, R.drawable.bubble_tombolan, DataType.TOMBOLAN));
        data.add(new DataElement(R.string.tombolan, R.string.ligga_i_lund, 55.7052623716287f, 13.1933726994219f, R.drawable.bubble_tombolan, DataType.TOMBOLAN));
        data.add(new DataElement(R.string.tombolan, R.string.war_of_karnewall_street, 55.70497261402468f, 13.19400101442841f, R.drawable.bubble_tombolan, DataType.TOMBOLAN));
        data.add(new DataElement(R.string.tombolan, R.string.tillbaka_till_samtiden, 55.7046899767097f, 13.19470679837929f, R.drawable.bubble_tombolan, DataType.TOMBOLAN));
        data.add(new DataElement(R.string.tombolan, R.string.strike_for_the_future, 55.7045492497648f, 13.19459512816178f, R.drawable.bubble_tombolan, DataType.TOMBOLAN));
        data.add(new DataElement(R.string.tombolan, R.string.futuralbucks, 55.7046234675636f, 13.19465534628458f, R.drawable.bubble_tombolan, DataType.TOMBOLAN));

        data.add(new DataElement(R.string.trashcan, R.string.trashcan, 55.70688332580387f, 13.19570738052719f, R.drawable.map_soptunna_icon, DataType.TRASHCAN));
        data.add(new DataElement(R.string.trashcan, R.string.trashcan, 55.70521696656937f, 13.19372387921223f, R.drawable.map_soptunna_icon, DataType.TRASHCAN));
        data.add(new DataElement(R.string.trashcan, R.string.trashcan, 55.7039572412542f, 13.19453665220126f, R.drawable.map_soptunna_icon, DataType.TRASHCAN));
        data.add(new DataElement(R.string.trashcan, R.string.trashcan, 55.70457677586199f, 13.19544640504312f, R.drawable.map_soptunna_icon, DataType.TRASHCAN));
        data.add(new DataElement(R.string.trashcan, R.string.trashcan, 55.70556189423078f, 13.19477811180374f, R.drawable.map_soptunna_icon, DataType.TRASHCAN));
        data.add(new DataElement(R.string.trashcan, R.string.trashcan, 55.7048783266619f, 13.19446443948018f, R.drawable.map_soptunna_icon, DataType.TRASHCAN));
        data.add(new DataElement(R.string.trashcan, R.string.trashcan, 55.7046984808458f, 13.19383546152834f, R.drawable.map_soptunna_icon, DataType.TRASHCAN));

        data.add(new DataElement(R.string.shoppen, R.string.shoppen, 55.70557904967363f, 13.19512260551833f, R.drawable.bubble_shoppen, DataType.SHOPPEN));

        //TODO: Fix logo for scenes below
        data.add(new DataElement(R.string.small_scene_place,R.string.small_scene,R.string.small_scene_info, R.string.small_scene_question, 55.7071037170697f, 13.19599227169824f, R.drawable.header_showen, R.drawable.map_scene_logo, R.drawable.scene_logo_list,DataType.SCENE));
        data.add(new DataElement(R.string.big_scene_place,R.string.big_scene, R.string.big_scene_info,R.string.big_scene_question, 55.70558532232642f, 13.19365336887898f, R.drawable.header_showen, R.drawable.map_scene_logo,R.drawable.scene_logo_list, DataType.SCENE));




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
