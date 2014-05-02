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
                R.drawable.test_nojen, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.orchestra), R.drawable.test_nojen, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.brothers_among_eva), R.drawable.brothersamongwera, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.sousou), R.drawable.sousoumahercissoko, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.bo_kasper),R.drawable.bokaspersorkester , myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.lucy_love), R.drawable.lucylove, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.hammar), R.drawable.perhammar, myDate, myDateEnd);
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

       e = new Event(context.getString(R.string.big_scene), context.getString(R.string.e_type), R.drawable.etype, myDate, myDateEnd);
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

        Event e = new Event(context.getString(R.string.place_train), context.getString(R.string.train), R.drawable.train_logo_white, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.ida), R.drawable.idaredig, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.hurricane), R.drawable.hurricanelove, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.linnea), R.drawable.linneahenrikssonsquare, myDate, myDateEnd);
        events.add(e);


                   /*
        Lördag:
        Emil och david, lilla scenen 19:00
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 00);
        cal.set(Calendar.HOUR_OF_DAY, 19);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 22);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.emil_och_david), R.drawable.emilochdavid, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.sandra), R.drawable.sandramosh, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.movits), R.drawable.movits, myDate, myDateEnd);
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

        Event e = new Event(context.getString(R.string.place_train), context.getString(R.string.train), R.drawable.train_logo_white, myDate, myDateEnd);
        events.add(e);

        /*
        Söndag:
        The bland band, lilla scenen 14:30
            Lat: 55°42'26.07"N  Long:  13°11'45.45"O
            55.7072417f, 13.195958333333333f
        */
        cal.set(Calendar.MINUTE, 14);
        cal.set(Calendar.HOUR_OF_DAY, 30);
        myDate = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 00);
        myDateEnd = cal.getTime();

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.the_bland_band), R.drawable.theblandband, myDate, myDateEnd);
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

        e= new Event(context.getString(R.string.big_scene), context.getString(R.string.frida), R.drawable.fridasundemo, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.small_scene), context.getString(R.string.partiet), R.drawable.partiet, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.wintergatan), R.drawable.wintergatan, myDate, myDateEnd);
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

         e = new Event(context.getString(R.string.big_scene), context.getString(R.string.timbaktu), R.drawable.timbuktuochdamn, myDate, myDateEnd);
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

        e = new Event(context.getString(R.string.big_scene), context.getString(R.string.discoteka), R.drawable.discotekayugostyle, myDate, myDateEnd);
        events.add(e);


    }

}
