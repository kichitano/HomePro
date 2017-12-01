package com.example.kichi.buscapp.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadMapa;
import com.example.kichi.buscapp.pkgNegocios.ClsNegocioEspecialidadPersona;
import com.example.kichi.buscapp.pkgNegocios.ClsNegociosEspecialidad;
import com.example.kichi.buscapp.pkgNegociosParticulares.DirectionsJSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    ArrayList<LatLng> markerPoints;
    Marker mCurrLocationMarker;
    Marker locU,locP;
    int CONT = 0;
    int codigo;
    double lat,lng;
    Button setPosicion;
    private Context context;
    String datosP,telefonoP,latP,lngP;
    String emailU,nombreU,apellidoU,fotoU,latU,lngU;
    ArrayList<ClsEntidadMapa> arrayListMapa = null;
    List<Marker> markers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        codigo = extras.getInt("codigo",0);
        if(codigo==1){
            CONT = 1;
            latP = extras.getString("latP", "");
            lngP = extras.getString("lngP", "");
            latU = extras.getString("latU", "");
            lngU = extras.getString("lngU", "");
            datosP = extras.getString("datosP", "");
            telefonoP = extras.getString("telefonoP", "");
            CONT = 1;
            setPosicion =(Button)findViewById(R.id.btnSetLatLng);
            setPosicion.setVisibility(View.GONE);
        }else if(codigo==2){
            CONT = 3;
            emailU = extras.getString("emailU","");
            nombreU = extras.getString("nombreU","");
            apellidoU = extras.getString("apellidoU","");
            fotoU = extras.getString("fotoU","");
            latU = extras.getString("latU", "");
            lngU = extras.getString("lngU", "");
            setPosicion =(Button)findViewById(R.id.btnSetLatLng);
            setPosicion.setVisibility(View.GONE);
        }else{
            CONT = 0;
            setPosicion=(Button)findViewById(R.id.btnSetLatLng);
            setPosicion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GuardarUbicacion(v);
                }
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, MapsActivity.this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap)
    {
        mGoogleMap=googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                buildGoogleApiClient();
                UiSettings uiSettings = mGoogleMap.getUiSettings();
                uiSettings.setZoomControlsEnabled(true);
                if(CONT == 0) {
                    mGoogleMap.setMyLocationEnabled(true);
                    uiSettings.setMapToolbarEnabled(false);
                    mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(LatLng latLng) {
                            if (mCurrLocationMarker != null) {
                                mCurrLocationMarker.remove();
                                mCurrLocationMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(latLng)
                                        .draggable(true)
                                        .title("Ubicacion nueva"));

                                lat = mCurrLocationMarker.getPosition().latitude;
                                lng = mCurrLocationMarker.getPosition().longitude;

                                mCurrLocationMarker.showInfoWindow();
                            }
                        }
                    });
                }else if(CONT==1){

                    LatLng latLngU = new LatLng(Double.parseDouble(latU),Double.parseDouble(lngU));
                    MarkerOptions markerU = new MarkerOptions();
                    markerU.position(latLngU);
                    markerU.title("Yo :)");
                    markerU.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    locU = mGoogleMap.addMarker(markerU);
                    locU.showInfoWindow();

                    LatLng latLngP = new LatLng(Double.parseDouble(latP),Double.parseDouble(lngP));
                    MarkerOptions markerP = new MarkerOptions();
                    markerP.position(latLngP);
                    markerP.title(datosP);
                    markerP.snippet("Llamar a "+telefonoP);
                    markerP.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    locP = mGoogleMap.addMarker(markerP);
                    locP.showInfoWindow();
                    locP.setTag(99);

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngU,18));

                    LatLng origin = latLngU;
                    LatLng dest = latLngP;
                    String url = getDirectionsUrl(origin, dest);
                    DownloadTask downloadTask = new DownloadTask();
                    downloadTask.execute(url);

                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if(marker.getTag()==locP.getTag()) {
                                MtdLlamar();
                            }
                            return false;
                        }
                    });
                }else if(CONT==3){
                    MapsActivity clase = this;
                    CargarTodosProfesionales cargarTodosProfesionales = clase.new CargarTodosProfesionales();
                    cargarTodosProfesionales.execute();
                }

            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            buildGoogleApiClient();
            mGoogleMap.setMyLocationEnabled(true);

        }
    }

    public class CargarTodosProfesionales extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Boolean resp = true;
            ClsNegocioEspecialidadPersona clsNegocioEspecialidadPersona = new ClsNegocioEspecialidadPersona();
            try {
                arrayListMapa = new ArrayList<>(clsNegocioEspecialidadPersona.CargarPersonasEspecialidad());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return resp;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                context = getApplicationContext();
                LatLng latLngU = new LatLng(Double.parseDouble(latU),Double.parseDouble(lngU));
                MarkerOptions markerU = new MarkerOptions();
                markerU.position(latLngU);
                markerU.title("Yo :)");
                markerU.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                locU = mGoogleMap.addMarker(markerU);
                locU.showInfoWindow();
                locU.setTag("x");

                markers = new ArrayList<Marker>();

                for(int i=0;i<arrayListMapa.size();i++){
                    Marker locE;

                    LatLng latLngP = new LatLng(Double.parseDouble(arrayListMapa.get(i).getLat_persona()),Double.parseDouble(arrayListMapa.get(i).getLng_persona()));
                    MarkerOptions markerP = new MarkerOptions();
                    markerP.position(latLngP);
                    markerP.title(arrayListMapa.get(i).getNombre_persona() + " " + arrayListMapa.get(i).getApellido_persona());

                    String imagen = arrayListMapa.get(i).getDescripcion_especialidad().toLowerCase() + "_mrk";
                    //markerU.icon(BitmapDescriptorFactory.fromPath("src/main/res/drawable/"+imagen+".png"));

                    markerP.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    markerP.snippet(arrayListMapa.get(i).getTelefono_persona());

                    locE = mGoogleMap.addMarker(markerP);
                    locE.setTag(arrayListMapa.get(i).getTelefono_persona());

                    markers.add(locE);

                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            telefonoP = String.valueOf(marker.getTag());
                            if(!telefonoP.equals("x"))
                                MtdLlamar();
                            return false;
                        }
                    });

                }

                mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngU,15));
            }
        }
    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception downloading", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){

                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);

                }

                // añadiendo patrones de punteo
                int PATTERN_DASH_LENGTH_PX = 25;
                int PATTERN_GAP_LENGTH_PX = 25;
                PatternItem DOT = new Dot();
                PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
                PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);
                List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);

                lineOptions.pattern(PATTERN_POLYGON_ALPHA);
                lineOptions.addAll(points);
                lineOptions.width(14);
                lineOptions.color(Color.parseColor("#009688"));
            }

            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, MapsActivity.this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onLocationChanged(Location location)
    {
        if (CONT == 0) {
            mLastLocation = location;
            if (mCurrLocationMarker != null) {
                mCurrLocationMarker.remove();
            }

            //Place current location marker
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Mi ubicacion actual");
            markerOptions.draggable(true);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            mCurrLocationMarker = mGoogleMap.addMarker(markerOptions);
            //move map camera
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            mCurrLocationMarker.showInfoWindow();
            lng = location.getLongitude();
            lat = location.getLatitude();

            CONT = 1;
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mGoogleMap.setMyLocationEnabled(true);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    public void GuardarUbicacion(View view) {
        String latlng = String.valueOf(lat) + "@" + String.valueOf(lng) ;
        Intent data = new Intent();
        data.setData(Uri.parse(latlng));
        setResult(RESULT_OK, data);
        finish();
    }

    public void MtdLlamar(){
        Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
        callIntent.setData(Uri.parse("tel:" + telefonoP));    //this is the phone number calling
        //check permission
        //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
        //the system asks the user to grant approval.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},10);  //request specific permission from user
            return;
        }else {     //have got permission
            try{
                startActivity(callIntent);  //call activity and make phone call
            }
            catch (android.content.ActivityNotFoundException ex){
                Toast.makeText(getApplicationContext(),"No se puede llamar",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
