package se.lundakarnevalen.extern.scheme;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import se.lundakarnevalen.extern.android.R;
import se.lundakarnevalen.extern.widget.LKSchemeAdapter;

/**
 * Created by Markus on 2014-04-21.
 */
public class Events {

    public static void getFridayEvents(ArrayList<Event> events, Context context) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DATE, 16);
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 13);

        Date myDate;
        Date myDateEnd;
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 14);

        myDateEnd = cal.getTime();
        /*
        Fredag:
        Invigning, stora scenen 13:00-14:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        Event e = new Event(context.getString(R.string.big_scene),
                context.getString(R.string.inauguration),
                R.drawable.invigning_icon, myDate, myDateEnd,1);
        events.add(e);

                    /*
        Fredag:
        Lillebror charlie, lilla scenen 13:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 15);
        cal.set(Calendar.MINUTE, 30);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.lillebror), R.drawable.lillebrorcharlie_rounded, myDate, myDateEnd,2);
        events.add(e);



/*
        Fredag:
        Orkesterkamp, stora scenen 14:30-15:30
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 15);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.orchestra), R.drawable.orkesterkampen_icon, myDate, myDateEnd,3);
        events.add(e);

            /*
        Fredag:
        Brothers among Eva, lilla scenen 15:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 15);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 17);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.brothers_among_eva), R.drawable.brothersamongwera_rounded_corners, myDate, myDateEnd,4);
        events.add(e);


            /*
        Fredag:
        Kings Lounge, lilla scenen 17:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.HOUR_OF_DAY, 17);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 30);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.kings), R.drawable.kingslounge_rounded, myDate, myDateEnd,5);
        events.add(e);



/*
        Fredag:
        Sousou, Maher Cissoko, stora scenen 17:15
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.HOUR_OF_DAY, 17);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 19);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.sousou), R.drawable.sousoumahercissoko_rounded_corners, myDate, myDateEnd,6);
        events.add(e);

                    /*
        Fredag:
        Bobbe, lilla scenen 18:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.bobbe), R.drawable.bobbiebigband_rounded, myDate, myDateEnd,7);
        events.add(e);


/*
        Fredag:
        Bo Kaspers, stora scenen 19:15
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.HOUR_OF_DAY, 19);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 21);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.bo_kasper),R.drawable.bokaspersorkester_rounded_corners , myDate, myDateEnd,8);
        events.add(e);

                    /*
        Fredag:
        Bernhard, lilla scenen 20:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.HOUR_OF_DAY, 20);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.barnard), R.drawable.bernardhorn_rounded, myDate, myDateEnd,9);
        events.add(e);


/*
        Fredag:
        Bo Kaspers, stora scenen 21:15
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.HOUR_OF_DAY, 21);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.lucy_love), R.drawable.lucylove_rounded_corners, myDate, myDateEnd,10);
        events.add(e);

                    /*
        Fredag:
        Per Hammar, lilla scenen 22:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.HOUR_OF_DAY, 22);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.hammar), R.drawable.perhammar_rounded_corners, myDate, myDateEnd,11);
        events.add(e);



/*
        Fredag:
        E-type, stora scenen 23:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

       e = new Event(context.getString(R.string.big_scene), context.getString(R.string.e_type), R.drawable.etype_rounded_corners, myDate, myDateEnd,12);
        events.add(e);

    }

    public static void getSaturdayEvents(ArrayList<Event> events, Context context) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DATE, 17);
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 13);

        Date myDate;
        Date myDateEnd;

        
        
/*
        Lördag:
        Tåget avgår 13:00, åter ca 15:00
        Se tågväg bifogad om ni vill lägga in den i appen i någon kartfunktion
*/
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        myDate = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 15);
        myDateEnd = cal.getTime();

        Event e = new Event(context.getString(R.string.place_train), context.getString(R.string.train), R.drawable.train_logo_white, myDate, myDateEnd,13);
        events.add(e);


                   /*
        Lördag:
        syskonen roth, lilla scenen 14:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.roth), R.drawable.syskonenroth_rounded, myDate, myDateEnd,14);
        events.add(e);

                   /*
        Lördag:
        first love, lilla scenen 16:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 17);
        cal.set(Calendar.MINUTE, 30);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.love), R.drawable.firstlove_rounded, myDate, myDateEnd,15);
        events.add(e);


            /*
        Lördag:
        Ida Redig, stora scenen 17:15
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.HOUR_OF_DAY, 17);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 19);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.ida), R.drawable.idaredig_rounded_corners, myDate, myDateEnd,16);
        events.add(e);


                   /*
        Lördag:
        arts factory, lilla scenen 17:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 17);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 19);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();
        //TODO
        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.arts), R.drawable.arts_rounded, myDate, myDateEnd,17);
        events.add(e);



            /*
        Lördag:
        Hurricane Love, stora scenen 19:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 19);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 21);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.hurricane), R.drawable.hurricanelove_rounded_corners, myDate, myDateEnd,18);
        events.add(e);
/*
        Lördag:
        Linnea, stora scenen 21:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 21);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.linnea), R.drawable.linneahenrikssonsquare_rounded_corners, myDate, myDateEnd,19);
        events.add(e);


                   /*
        Lördag:
        david, lilla scenen 19:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.HOUR_OF_DAY, 19);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 30);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.david), R.drawable.david_ikon_rounded_corners, myDate, myDateEnd,20);
        events.add(e);

                   /*
        Lördag:
        Emil och david, lilla scenen 20:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 20);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.emil), R.drawable.emil_icon_rounded_corners, myDate, myDateEnd,21);
        events.add(e);


/*
        Lördag:
        Sandra Mosh, lilla scenen 22:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.HOUR_OF_DAY, 22);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.sandra), R.drawable.sandramosh_rounded_corners, myDate, myDateEnd,22);
        events.add(e);

/*
        Lördag:
        MOVITS!, stora scenen 23:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.movits), R.drawable.movits_rounded_corners, myDate, myDateEnd,23);
        events.add(e);



    }

    public static void getSundayEvents(ArrayList<Event> events, Context context) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, Calendar.MAY);
        cal.set(Calendar.DATE, 18);
        cal.set(Calendar.YEAR, 2014);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 13);

        Date myDate;
        Date myDateEnd;


        /*
        Söndag:

        Tåget avgår 13:00, åter ca 15:00
        Se tågväg bifogad om ni vill lägga in den i appen i någon kartfunktion
        Se bifogat dokument om generell tidsplan för detta, artisterna och dess tider samt öppettider.
                Koordinater lilla scenen:
        Lat: 55°42'26.07"N Long: 13°11'45.45"O
*/
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 13);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 15);
        myDateEnd = cal.getTime();

        Event e = new Event(context.getString(R.string.place_train), context.getString(R.string.train), R.drawable.train_logo_white, myDate, myDateEnd,24);
        events.add(e);

        /*
        Söndag:
        The bland band, lilla scenen 14:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.the_bland_band), R.drawable.theblandband_rounded_corners, myDate, myDateEnd,25);
        events.add(e);

                /*
        Söndag:
        The stomping, lilla scenen 16:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 17);
        cal.set(Calendar.MINUTE, 30);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.stomping), R.drawable.stompingacademy_rounded, myDate, myDateEnd,26);
        events.add(e);



            /*
        Söndag:
        FRIDA SUNDEMO, stora scenen 17:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 17);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 18);
        cal.set(Calendar.MINUTE, 30);
        myDateEnd = cal.getTime();

        e= new Event(context.getString(R.string.big_scene), context.getString(R.string.frida), R.drawable.fridasundemo_rounded_corners, myDate, myDateEnd,27);
        events.add(e);


            /*
        Söndag:
        Partiet, lilla scenen 17:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 17);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 19);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.partiet), R.drawable.partiet_rounded_corners, myDate, myDateEnd,28);
        events.add(e);


            /*
        Söndag:
        WINTERGATAN, stora scenen 18:30
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 30);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.wintergatan), R.drawable.wintergatan_rounded_corners, myDate, myDateEnd,29);
        events.add(e);

      /*
        Söndag:
        Musse, lilla scenen 19:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 19);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 20);
        cal.set(Calendar.MINUTE, 30);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.musse), R.drawable.musse_rounded, myDate, myDateEnd,30);
        events.add(e);



/*
        Söndag:
        TIMBAKTU, stora scenen 20:30
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 20);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

         e = new Event(context.getString(R.string.big_scene), context.getString(R.string.timbaktu), R.drawable.timbuktuochdamn_rounded_corners, myDate, myDateEnd,31);
        events.add(e);

              /*
        Söndag:
        Hampus Lavin, lilla scenen 20:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 30);
        cal.set(Calendar.HOUR_OF_DAY, 20);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.hampus), R.drawable.hampuslavin_rounded, myDate, myDateEnd,32);
        events.add(e);


              /*
        Söndag:
        AXel, lilla scenen 22:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 22);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.axel), R.drawable.axelboman_rounded, myDate, myDateEnd,33);
        events.add(e);

            /*
        Söndag:
        Discoteka, stora scenen 23:00
        Lat: 55°42'20.53"N Long: 13°11'37.55"O
        */
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.MINUTE, 0);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.discoteka), R.drawable.discotekayugostyle_rounded_corners, myDate, myDateEnd,34);
        events.add(e);


    }

}
