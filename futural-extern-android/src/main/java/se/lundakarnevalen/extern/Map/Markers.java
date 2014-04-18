package se.lundakarnevalen.extern.map;

import java.util.ArrayList;
import java.util.Calendar;

import se.lundakarnevalen.extern.android.R;

/**
 * Created by Filip on 2014-04-18.
 */
public class Markers {

    public static void addMarkers(ArrayList<Marker> markers) {


        //Barnevalen - Pappas onda tand:
        //Krafts Torg
        //Lat: 55°42'13.64"N Long: 13°11'40.73"O
        markers.add(new Marker(55.7037889f, 13.194647222222223f, R.drawable.ic_launcher, MarkerType.FUN));


        // Cirkusen - Cirkus 40 minuter:
        // Tegnérsplatsen
        // Lat: 55°42'17.40"N Long: 13°11'43.27"O
        markers.add(new Marker(55.7048333f, 13.195352777777778f, R.drawable.ic_launcher, MarkerType.FUN));

        // Spexet Atlantis:
        // Stora Scenen AF (GPS-pos ingången AF)
        //Lat: 55°42'19.48"N Long: 13°11'43.77"O
        markers.add(new Marker(55.7054111f, 13.195491666666667f, R.drawable.ic_launcher, MarkerType.FUN));

        //Showen - Ljuset och Lyktan:
        //Tegnérs Matsalar
        //Lat: 55°42'19.96"N Long: 13°11'44.12"O
        markers.add(new Marker(55.7055444f, 13.195588888888889f, R.drawable.ic_launcher, MarkerType.FUN));

        //Kabarén - Cabaret Apocalyptica:
        //Norr om domkyrkan
        //Lat: 55°42'15.36"N Long: 13°11'37.80"O
        markers.add(new Marker(55.7042667f, 13.193833333333334f, R.drawable.ic_launcher, MarkerType.FUN));

        //Revyn - Kottar och nötter:
        //Universitetshuset
        //Lat: 55°42'20.79"N Long: 13°11'36.80"O

        markers.add(new Marker(55.705775f, 13.193555555555555f, R.drawable.ic_launcher, MarkerType.FUN));

        //Filmen - Överliggaren:
        //Palaestra
        //Lat: 55°42'21.38"N Long: 13°11'41.30"Omarkers.add()
        markers.add(new Marker(55.7059389f, 13.194805555555556f, R.drawable.ic_launcher, MarkerType.FUN));


        //Koordinater lilla scenen:
        //Lat: 55°42'26.07"N  Long:  13°11'45.45"O

        markers.add(new Marker(55.7072417f, 13.195958333333333f, R.drawable.ic_launcher, MarkerType.SHOW));


        //Mat och Dryck

        //Foodstock
        //Cocktailbaren
        //Betalning, kort och kontant. DOCK, cash is king!
        //        Drinkar (drinkmeny om ca 10 st max), karnevöl, karnevale, cider, läsk, vin
        //Ej mat
        //55°42'22.9"N 13°11'42.6"E
        //55.706362f, 13.195165
        markers.add(new Marker(55.706362f, 13.195165f, R.drawable.ic_launcher, MarkerType.FOOD));


        //Hipp-i-baren:
        //Betalning, kort och kontant. DOCK, cash is king!
        //        karnevöl, cider, läsk, (ev vin)
        //Ej mat
        //55°42'23.5"N 13°11'43.5"E
        //55.706521f, 13.195431
        markers.add(new Marker(55.706521f, 13.195431f, R.drawable.ic_launcher, MarkerType.FOOD));

        // Folkan:
        //Betalning, kort och kontant. DOCK, cash is king!
        //        karnevöl, cider, läsk (ev vin)
        //Ej mat
        //55°42'24.6"N 13°11'45.7"E
        //55.706841, 13.196030
        markers.add(new Marker(55.706841f, 13.196030f, R.drawable.ic_launcher, MarkerType.FOOD));
    }
}
