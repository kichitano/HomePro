package com.example.kichi.buscapp.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kichi.buscapp.R;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    GoogleMap mGoogleMap;
    SupportMapFragment mapFrag;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    Marker locU,locP;
    int CONT = 0;
    int codigo;
    double lat,lng;
    Button setPosicion;

    String telefonoP;
    String latP;
    String lngP;
    String datosP;

    String latU;
    String lngU;

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
                }else {

                    LatLng latLngU = new LatLng(Double.parseDouble(latU),Double.parseDouble(lngU));
                    MarkerOptions markerU = new MarkerOptions();
                    markerU.position(latLngU);
                    markerU.title("Yo :)");
                    markerU.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                    locU = mGoogleMap.addMarker(markerU);
                    locU.showInfoWindow();

                    LatLng latLngP = new LatLng(Double.parseDouble(latP),Double.parseDouble(lngP));
                    MarkerOptions markerP = new MarkerOptions();
                    markerP.position(latLngP);
                    markerP.title(datosP);
                    markerP.snippet("Llamar a "+telefonoP);
                    markerP.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    locP = mGoogleMap.addMarker(markerP);
                    locP.showInfoWindow();
                    locP.setTag(99);

                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngU,18));

                    //LINEA RECTA EN MAPA

                    ArrayList<LatLng> points; //added
                    Polyline line; //added
                    points = new ArrayList<LatLng>(); //added

                    points.add(latLngU); //added
                    points.add(latLngP); //added

                    PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
                    for (int i = 0; i < points.size(); i++) {
                        LatLng point = points.get(i);
                        options.add(point);
                    }
                    mGoogleMap.addPolyline(options);

                    //LINEA RECTA EN MAPA

                    mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            if(marker.getTag()==locP.getTag()) {
                                MtdLlamar();
                            }
                            return false;
                        }
                    });
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
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
        Toast.makeText(this,lat + " " + lng,Toast.LENGTH_SHORT).show();
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
