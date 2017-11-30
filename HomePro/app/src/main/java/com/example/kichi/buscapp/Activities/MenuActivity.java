package com.example.kichi.buscapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kichi.buscapp.Fragments.EspecialidadFragment;
import com.example.kichi.buscapp.Fragments.ProfesionalesFragment;
import com.example.kichi.buscapp.R;
import com.google.android.gms.drive.query.SearchableField;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private View mMenuFormView;
    private View mProgressView;
    Boolean Validacion=false;
    int act = 0;
    SearchView buscador;
    EditText buscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Quitar Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Quitar Action Bar


        setContentView(R.layout.activity_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

                            //Una vez haz creado tu instancia de TestFragment y colocado el Bundle entre sus argumentos, usas el FragmentManager para iniciarla desde tu segunda actividad.
                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            ProfesionalesFragment fragment = new ProfesionalesFragment();
                            fragment.setArguments(args);

                            transaction.replace(R.id.contenedor, fragment);
                            transaction.addToBackStack(null);
                            showProgress(false);
                            transaction.commit();
                            fab.setImageResource(R.drawable.vista2);
                        }
                    });
                }else{
                    act = 0;
                    MenuActivity.this.getFragmentManager().popBackStack();
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
        FragmentManager fragmentManager  = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        EspecialidadFragment fragment = new EspecialidadFragment();
        fragment.setArguments(args);
        transaction.add(R.id.contenedor,fragment);
        transaction.commit();

        mMenuFormView = findViewById(R.id.contenedor);
        mProgressView = findViewById(R.id.menu_progress);

        /*
        buscador = (SearchView)findViewById(R.id.buscar);
        int id = buscador.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        buscar = (EditText) buscador.findViewById(id);
        buscar.addTextChangedListener(textWatcher);
        */
    }


    public class CargarTodosProfesionales extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Boolean resp = false;
            MtdCargarProfesionales();
            if(Validacion==true){
                resp = true;
            }else {
                resp = false;
            }
            return resp;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                showProgress(false);
                finish();
            }else {
                showProgress(false);
            }
        }
    }

    private void MtdCargarProfesionales() {
        showProgress(true);

        // Creamos un nuevo Bundle
        Bundle args = new Bundle();
        args.putInt("visualiza", 1);

        //Una vez haz creado tu instancia de TestFragment y colocado el Bundle entre sus argumentos, usas el FragmentManager para iniciarla desde tu segunda actividad.
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ProfesionalesFragment fragment = new ProfesionalesFragment();
        fragment.setArguments(args);

        transaction.replace(R.id.contenedor,fragment);
        transaction.commit();
    }

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

        String email = extras.getString("Email");                             //Obtenemos el extra numero
        String nombre = extras.getString("Nombre");                             //Obtenemos el extra numero
        String apellido = extras.getString("Apellido");                         //Obtenemos el extra textoAzar
        String foto = extras.getString("Foto");
        String direccionP =  extras.getString("Direccion");

        byte[] decodedString = Base64.decode(foto,Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);

        TextView textView = (TextView)findViewById(R.id.textView);                //Buscamos el TextView por id del la caja de texto al azar
        TextView textView2 = (TextView)findViewById(R.id.textView2);              //Buscamos el TextView por id de la caja de texto de numero
        ImageView imageView = (ImageView)findViewById(R.id.imageView);

        textView.setText(email);                                              //Seteamos el email
        textView2.setText(nombre+ " " + apellido);                            //Seteamos los datos
        imageView.setImageBitmap(decodedBitmap);

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.MostrarTodos) {
            return true;
        } else if (id == R.id.MostrarPrincipales){

        } else if (id == R.id.buscar){

            buscar.addTextChangedListener(textWatcher);

        }

        return super.onOptionsItemSelected(item);
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



    //second, we create the TextWatcher
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }


        @Override
        public void afterTextChanged(Editable editable) {
            //here, after we introduced something in the EditText we get the string from it
            String answerString = buscar.getText().toString();
            //and now we make a Toast
            //modify "yourActivity.this" with your activity name .this
            Toast.makeText(MenuActivity.this,"The string from EditText is: "+answerString,Toast.LENGTH_SHORT).show();

        }
    };

}
