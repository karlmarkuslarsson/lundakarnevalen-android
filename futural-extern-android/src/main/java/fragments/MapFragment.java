package fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;

import se.lundakarnevalen.extern.Map.MarkerType;
import se.lundakarnevalen.extern.android.R;

import static android.location.LocationManager.*;

public class MapFragment extends LKFragment implements View.OnTouchListener {

    private Handler handler;


    private ArrayList<Marker> markers = new ArrayList<Marker>();

    private Matrix matrix;
    private Matrix savedMatrix = new Matrix();

    private boolean isActive;

    // Save current dots
    private Bitmap bmOverlay;
    private Bitmap bmWithOutMyPos;

    private ImageView img;
    private int imageWidth;
    private int imageHeight;

    // States onTouchEvent
    private final int NONE = 0;
    private final int DRAG = 1;
    private final int ZOOM = 2;
    private int mode = NONE;


    // Variables for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float newDist = 1f;
    // Control scale 1 = full size
    private float scale = 1f;


    private boolean firstTime = true;

    // Context
    private Context context;

    // For gps and network
    private LocationManager locMan;
    private static final int TIME_INTERVAL = 1800000; // get gps location every 30 min
    // private static final int TIME_INTERVAL = 10000; // get gps location every 10 sec
    private static final int GPS_DISTANCE = 0; // set the distance value in meter

    private float myLat;
    private float myLng;

    // Information about the map
    private float startLonMap = (float) 12.445449839578941;
    private float startLatMap = (float) 55.33715099913018;
    private float endLonMap = (float) 14.580917368875816;
    private float endLatMap = (float) 56.52300194685981;
    private float diffLon = endLonMap - startLonMap;
    private float diffLat = endLatMap - startLatMap;

    private HashMap<Integer,Boolean> active = new HashMap<Integer,Boolean>();

    // Every time you switch to this fragment.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, null);

        if(active.size()==0) {
            active.put(MarkerType.FOOD,true);
            active.put(MarkerType.FUN,true);
            active.put(MarkerType.HELP,true);
            active.put(MarkerType.WC,true);
        }

        context = getContext();

        if(markers.size()==0) {
            addMarkers();
        }

        if(imageWidth == 0) {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            imageWidth = metrics.widthPixels;
            imageHeight = metrics.heightPixels;
        }
        img = ((ImageView) rootView.findViewById(R.id.map_id));

        if(matrix != null) {
            if(bmOverlay != null) {
                img.setImageBitmap(bmOverlay);
            }
            img.setScaleType(ImageView.ScaleType.MATRIX);
            img.setImageMatrix(matrix);
        } else{
                matrix = new Matrix();
                firstTime = true;
                isActive = true;
                PositionTask positionTask = new PositionTask();
                positionTask.execute();

        }
        if(handler == null) {
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (isActive) {
                        getPosition();
                        updatePositions();
                        handler.postDelayed(this, 10000);
                    } else {
                        handler.postDelayed(this, 10000);
                    }
                }
            }, 0);
        }

        img.setOnTouchListener(this);


        return rootView;
    }

    private void addMarkers() {

        //Barnevalen - Pappas onda tand:
        //Krafts Torg
        //Lat: 55°42'13.64"N Long: 13°11'40.73"O
        markers.add(new Marker((float)55.7037889,(float)13.194647222222223,R.drawable.ic_launcher,MarkerType.FUN));



       // Cirkusen - Cirkus 40 minuter:
       // Tegnérsplatsen
       // Lat: 55°42'17.40"N Long: 13°11'43.27"O
        markers.add(new Marker((float)55.7048333,(float)13.195352777777778,R.drawable.ic_launcher,MarkerType.FUN));

        // Spexet Atlantis:
        // Stora Scenen AF (GPS-pos ingången AF)
        //Lat: 55°42'19.48"N Long: 13°11'43.77"O
        markers.add(new Marker((float)55.7054111,(float)13.195491666666667,R.drawable.ic_launcher,MarkerType.FUN));

        //Showen - Ljuset och Lyktan:
        //Tegnérs Matsalar
        //Lat: 55°42'19.96"N Long: 13°11'44.12"O
        markers.add(new Marker((float)55.7055444,(float)13.195588888888889,R.drawable.ic_launcher,MarkerType.FUN));

        //Kabarén - Cabaret Apocalyptica:
        //Norr om domkyrkan
        //Lat: 55°42'15.36"N Long: 13°11'37.80"O
        markers.add(new Marker((float)55.7042667,(float)13.193833333333334,R.drawable.ic_launcher,MarkerType.FUN));

        //Revyn - Kottar och nötter:
        //Universitetshuset
        //Lat: 55°42'20.79"N Long: 13°11'36.80"O
        markers.add(new Marker((float)55.705775,(float)13.193555555555555,R.drawable.ic_launcher,MarkerType.FUN));

        //Filmen - Överliggaren:
        //Palaestra
        //Lat: 55°42'21.38"N Long: 13°11'41.30"Omarkers.add()
        markers.add(new Marker((float)55.7059389,(float)13.194805555555556,R.drawable.ic_launcher,MarkerType.FUN));


      //  markers.add(new Marker((float)55.7,(float)13.2,R.drawable.ic_launcher, MarkerType.FOOD));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {
        isActive = false;
        super.onPause();
    }


    @Override
    public void onResume() {
        isActive = true;
        super.onPause();
    }

    public void updatePositions() {
        Bitmap mapBitmap = null;
            mapBitmap = decodeSampledBitmapFromResource(getResources(), R.drawable.test_map, imageWidth , imageHeight);
        //Bitmap mapBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_map);
            // Create an overlay bitmap
            bmOverlay = Bitmap.createBitmap(mapBitmap.getWidth(), mapBitmap.getHeight(), mapBitmap.getConfig());
            Canvas canvas = new Canvas();
            canvas.setBitmap(bmOverlay);
            canvas.drawBitmap(mapBitmap, new Matrix(), null);

            Paint paintGray = new Paint();
            Paint paintRed = new Paint();
            paintRed.setColor(getResources().getColor(R.color.red));
            paintGray.setColor(Color.GRAY);

                float lat = (myLat - startLatMap) / diffLat;
                float lon = (myLng - startLonMap) / diffLon;
                float x = lon * mapBitmap.getWidth();
                float y = mapBitmap.getHeight() - lat * mapBitmap.getHeight();

        canvas.drawCircle(x, y, 10, paintRed);
            for(Marker m : markers) {

                if(active.get(m.type) != null && active.get(m.type)) {
                lat = (m.lat - startLatMap) / diffLat;
                lon = (m.lng - startLonMap) / diffLon;
                x = lon * mapBitmap.getWidth();
                y = mapBitmap.getHeight() - lat * mapBitmap.getHeight();
                    // draw canvas..
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), m.picture);
                canvas.drawBitmap(bitmap, x, y, null);
                }
                //canvas.dra(x, y, 10, paintRed);
            }

            img.setImageBitmap(bmOverlay);


    }

    public void getPosition() {
            if(locMan==null) {
                locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            }
            // Only turn off get position with GPS. Ok with network...
            if(locMan.isProviderEnabled(GPS_PROVIDER)){
                locMan.requestLocationUpdates(GPS_PROVIDER, TIME_INTERVAL, GPS_DISTANCE, PositionListener);
                Log.d("Updateing GPS!", "Update");
            } else {
                Log.d("GPS off", "Avst�ngd GPS");
                if (locMan.isProviderEnabled(NETWORK_PROVIDER)) {
                    locMan.requestLocationUpdates(NETWORK_PROVIDER, TIME_INTERVAL, GPS_DISTANCE, PositionListener);
                    Log.d("Updateing Position with network!", "Update");
                }
            }

            Location location = locMan.getLastKnownLocation(GPS_PROVIDER);
            if (location != null) {
                myLng = (float) location.getLongitude();
                myLat = (float) location.getLatitude();
                Log.d("Find GPS_position", myLng + " " + myLat);
            } else {
                location = locMan.getLastKnownLocation(NETWORK_PROVIDER);
                if (location != null) {
                    myLng = (float) location.getLongitude();
                    myLat = (float) location.getLatitude();
                    Log.d("Find Network_position", myLng + " " + myLat);
                } else {
                    Log.d("No GPS or Network position", "FAIL1");
                }
            }

    }

    private LocationListener PositionListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            // update location
            locMan.removeUpdates(PositionListener); // remove this listener
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }




    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        matrix.set(view.getImageMatrix());
        view.setScaleType(ImageView.ScaleType.MATRIX);
        if(firstTime) {
            matrix.set(view.getImageMatrix());
            firstTime =false;
        }
        float scale;

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted
                if(mode == ZOOM) {
                    this.scale = this.scale*newDist/oldDist;
                   // generateDots(this.scale);
                }

                mode = NONE;
                // Uppdatera mapen...

                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                oldDist = spacing(event);
                newDist = oldDist;
                if (oldDist > 5f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG)
                {
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                }
                else if (mode == ZOOM)
                {
                    // pinch zooming
                    float newDist2 = spacing(event);
                    // Lock zoom out
                    if(this.scale*newDist2/oldDist >= 1) {
                        //newDist = newDist2;
                        newDist = newDist2;
                        if (newDist > 5f) {
                            matrix.set(savedMatrix);
                            scale = newDist / oldDist;
                            // setting the scaling of the
                            // matrix...if scale > 1 means
                            // zoom in...if scale < 1 means
                            // zoom out
                            matrix.postScale(scale, scale, mid.x, mid.y);
                        }
                    }
                }
                break;
        }
        view.setImageMatrix(matrix); // display the transformation on screen
        return true; // indicate event was handled
    }
    /**
     * Calculate the space between the two fingers on touch
     */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }
    /**
     * Calculates the midpoint between the two fingers
     */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private class Marker {
        private float lat;
        private float lng;
        private int picture;
        private int type;
        public Marker(float lat, float lng, int picture, int type) {
            this.lat = lat;
            this.lng = lng;
            this.picture = picture;
            this.type = type;
        }

    }

    private class PositionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            /*
            final Handler handler;
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.d("isinactive","new position");
                        if(isActive) {
                            Log.d("isactive","new position");
                            getPosition();
                            updatePositions();

                        }
                        handler.postDelayed(this, 10000);
                    }
              }, 0);

        */

        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
        }
    }

    public void changeActive(int i, boolean activated) {
        active.put(i, activated);
    }
}














    /*
    private void updatePositions() {
        double startLonMap = 13;
        double startLatMap = 55;
        double endLonMap = 14;
        double endLatMap = 56;
        double diffLon = endLonMap - startLonMap;
        double diffLat = endLatMap - startLatMap;

        //Change this to the map.png..
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_skane);

        // Create an overlay bitmap
        Bitmap bmOverlay = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        Canvas canvas = new Canvas();
        canvas.setBitmap(bmOverlay);
        canvas.drawBitmap(mBitmap, new Matrix(), null);

        Paint p = new Paint();
        p.setColor(Color.GREEN);

        int a = (int)(30*scale);
        int b =	(int)(30*scale);

        HashMap<Marker,Marker> markers = new HashMap<Marker,Marker>();

        for(Integer i:positions.keySet()) {
            Position pos = positions.get(i);
            //
            //Modify color here and +1 for the given section matrix
            // Check within range...
            double lat = (pos.getLat()-startLatMap)/diffLat;
            double lon = (pos.getLng()-startLonMap)/diffLon;
            int x = (int)(a*lat);
            int y = (int)(b*lon);
            Marker temp = new Marker(x,y,pos.getSection());
            Marker temp2 = markers.get(temp);
            if(temp2 != null) {
                temp2.sumX += pos.getLat();
                temp2.sumY += pos.getLng();
                ++temp2.counter;
            } else {
                markers.put(temp, temp);
                temp.sumX += pos.getLat();
                temp.sumY += pos.getLng();
                ++temp.counter;
            }
            // Very sparse, change to map/double arrays
            // I think HashMap
            // Include x,y,counter,sumlat/counter,sumlng/counter..

        }
        for(Marker m: markers.keySet()) {
            // TODO
            // use sumX and sumY instead
            canvas.drawCircle((float) (((float)m.x)/((double)a) * mBitmap.getWidth()), (float) (((float)m.y)/((double)b) * mBitmap.getHeight()), m.counter,p);
        }


        img.setImageBitmap(bmOverlay);
    }




      private void generateDots(float scale){
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.map_skane);

        Bitmap bmOverlay = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap.getConfig());
        Canvas canvas = new Canvas();
        canvas.setBitmap(bmOverlay);
        canvas.drawBitmap(mBitmap, new Matrix(), null);

        Paint p = new Paint();
        p.setColor(Color.GREEN);
        int i = 0;
        Random r = new Random();
        int a = (int)(30*scale);
        int b =	(int)(30*scale);
        int counter[][] = new int[a][b];

        while(i <2000) {
            ++counter[r.nextInt(a)][r.nextInt(b)];
            i++;
        }
        for(int j = 0;j<a;j++) {
            for(int jj = 0;jj<b;jj++) {
                if(counter[j][jj]!=0) {
                    canvas.drawCircle((float) (((float)j)/((double)a) * mBitmap.getWidth()), (float) (((float)jj)/((double)b) * mBitmap.getHeight()), counter[j][jj],p);
                }
            }
        }
        img.setImageBitmap(bmOverlay);
    }
        */






