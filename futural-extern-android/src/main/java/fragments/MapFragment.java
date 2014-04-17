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
    private float offX = 0;
    private float offY = 0;


    private ArrayList<Marker> markers = new ArrayList<Marker>();

    private Matrix matrix;
    private Matrix savedMatrix = new Matrix();

    private boolean isActive;

    // Save current dots
    private Bitmap bmOverlay;

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



        if(matrix != null ) {
            if(bmOverlay != null) {
                img.setImageBitmap(bmOverlay);
            }
            if(!firstTime) { //test
                img.setScaleType(ImageView.ScaleType.MATRIX);
                img.setImageMatrix(matrix);
            }
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
        Bitmap mapBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_map);
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

                    if(m.x==-1) {
                    lat = (m.lat - startLatMap) / diffLat;
                    lon = (m.lng - startLonMap) / diffLon;
                    x = lon * mapBitmap.getWidth();
                    y = mapBitmap.getHeight() - lat * mapBitmap.getHeight();
                    // draw canvas..
                    m.x = x;
                    m.y = y;
                }
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), m.picture);

                    canvas.drawBitmap(bitmap, m.x-bitmap.getWidth()/2, m.y-bitmap.getHeight()/2, null);
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
        firstTime = false;
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
                } else {

                    if ((start.x - event.getX()) * (start.x - event.getX()) + (start.y - event.getY()) * (start.y - event.getY()) < 10) {

                        float[] values = new float[9];
                        matrix.getValues(values);
                        // values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
                        // values[0] and values[4] are the zoom factors for the image's width and height respectively. If you zoom at the same factor, these should both be the same value.
                        float relativeX = (event.getX() - values[2]) / values[0];
                        float relativeY = (event.getY() - values[5]) / values[4];
                        Log.d("rel x and y", "x: " + relativeX + " y: " + relativeY);
                        // values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
                        float maxX = 0;
                        float maxY = 0;
                        checkClick(relativeX, relativeY);
                    } else {

                    }
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
        // v.getLeft() = 0; v.getRight() = 480
        /*
        int []location = new int[10];
        view.getLocationInWindow(location);
        for(int i = 0;i<location.length;i++) {
            Log.d("loc"+i,"get: "+location[i]+"");
        }
        */
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
     *
     * @param relativeX
     * @param relativeY
     */
    private void checkClick(float relativeX, float relativeY) {
        if(bmOverlay!=null) {
            for(Marker m:markers) {
                if(m.isClose(relativeX,relativeY)) {

                    return;
                }
            }
        }
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
        private float x = -1;
        private float y = -1;
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

        // scale to??
        public boolean isClose(float relativeX, float relativeY) {
            if((x-relativeX)*(x-relativeX)+(y-relativeY)*(y-relativeY)<600) {
                Log.d("realy close","yeah close");
                return true;
            }
            Log.d("dist:",""+((x-relativeX)*(x-relativeX)+(y-relativeY)*(y-relativeY)));
            return false;
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
