package com.example.kichi.buscapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kichi.buscapp.Fragments.EspecialidadFragment;
import com.example.kichi.buscapp.Fragments.ProfesionalesFragment;
import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadEspecialidad;
import com.example.kichi.buscapp.pkgNegocios.ClsNegociosEspecialidad;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private View mMenuFormView;
    private View mProgressView;
    int act = 0;
    String emailU;
    String nombreU;
    String apellidoU;
    String fotoU;
    String direccionU;
    ArrayList<String> listaEspecialidad = null;
    private SensorManager sm;
    private float acelVal; // current acceleration including gravity
    private float acelLast; // last acceleration including gravity
    private float shake; // acceleration apart from gravity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Quitar Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Quitar Action Bar

        setContentView(R.layout.activity_menu);

        sm = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sm.registerListener(sensorEventListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        shake = 0.00f;
        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        emailU = bundle.getString("emailU","");
        nombreU = bundle.getString("nombreU","");
        apellidoU = bundle.getString("apellidoU","");
        fotoU = bundle.getString("fotoU","");
        direccionU = bundle.getString("direccionU","");


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //MenuActivity.this.getFragmentManager().popBackStack();

                if(act == 0) {
                    act = 1;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            // Creamos un nuevo Bundle
                            showProgress(true);
                            Bundle args = new Bundle();
                            args.putInt("visualiza", 1);

                            args.putString("emailU",emailU);
                            args.putString("nombreU",nombreU);
                            args.putString("apellidoU",apellidoU);
                            args.putString("fotoU",fotoU);
                            args.putString("direccionU",direccionU);

                            //Una vez haz creado tu instancia de TestFragment y colocado el Bundle entre sus argumentos, usas el FragmentManager para iniciarla desde tu segunda actividad.
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            ProfesionalesFragment fragment = new ProfesionalesFragment();
                            fragment.setArguments(args);

                            transaction.replace(R.id.contenedor, fragment,"ProfesionalesFragment");
                            transaction.addToBackStack(null);
                            showProgress(false);
                            transaction.commit();
                            fab.setImageResource(R.drawable.vista2);
                        }
                    });
                }else{
                    act = 0;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    getFragmentManager().popBackStack();
                    fragmentManager.beginTransaction().remove(fragmentManager.findFragmentByTag("ProfesionalesFragment"));
                    fab.setImageResource(R.drawable.vista);
                }

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Bundle args = new Bundle();
        args.putInt("visualiza", 0);

        args.putString("emailU",emailU);
        args.putString("nombreU",nombreU);
        args.putString("apellidoU",apellidoU);
        args.putString("fotoU",fotoU);
        args.putString("direccionU",direccionU);

        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        EspecialidadFragment fragment = new EspecialidadFragment();
        fragment.setArguments(args);
        transaction.add(R.id.contenedor,fragment,"EspecialidadFragment");
        transaction.commit();

        mMenuFormView = findViewById(R.id.contenedor);
        mProgressView = findViewById(R.id.menu_progress);

        MenuActivity clase = this;
        CargarEspecialidades cargarEspecialidades = clase.new CargarEspecialidades();
        cargarEspecialidades.execute();

    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;

            if(shake > 64){

                Bundle args = new Bundle();
                String latlngU[] = direccionU.split("@");

                args.putInt("codigo",2);

                args.putString("emailU",emailU);
                args.putString("nombreU",nombreU);
                args.putString("apellidoU",apellidoU);
                args.putString("latU",latlngU[0]);
                args.putString("lngU",latlngU[1]);
                args.putString("fotoU",fotoU);

                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                intent.putExtras(args);

                startActivityForResult(intent,55);

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mMenuFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mMenuFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mMenuFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mMenuFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        Intent intent = getIntent();                                            //Obtenemos el intent
        Bundle extras = intent.getExtras();                                     //Obtenemos el conjunto de extras

        emailU = extras.getString("emailU");                             //Obtenemos el extra numero
        nombreU = extras.getString("nombreU");                             //Obtenemos el extra numero
        apellidoU = extras.getString("apellidoU");                         //Obtenemos el extra textoAzar
        fotoU = extras.getString("fotoU");
        direccionU =  extras.getString("direccionU");

        byte[] decodedString = Base64.decode(fotoU,Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);

        TextView textView = (TextView)findViewById(R.id.textView);                //Buscamos el TextView por id del la caja de texto al azar
        TextView textView2 = (TextView)findViewById(R.id.textView2);              //Buscamos el TextView por id de la caja de texto de numero
        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        textView.setText(emailU);                                              //Seteamos el email
        textView2.setText(nombreU+ " " + apellidoU);                            //Seteamos los datos
        imageView.setImageBitmap(decodedBitmap);

        getMenuInflater().inflate(R.menu.menu_scrolling, menu);

        return true;
    }

    public class CargarEspecialidades extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            ClsNegociosEspecialidad negociosEspecialidad = new ClsNegociosEspecialidad();
            listaEspecialidad = new ArrayList<>();

            try {
                listaEspecialidad = negociosEspecialidad.cargarEspecialidad();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            final List datos = new ArrayList<>();
            for (int o = 0; o <= listaEspecialidad.size() - 2; o += 2) {
                String idEspecialidad = listaEspecialidad.get(o);
                String Nombre =  listaEspecialidad.get(o + 1);
                datos.add(new ClsEntidadEspecialidad(idEspecialidad,Nombre));
            }
            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
