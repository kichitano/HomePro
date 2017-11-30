package com.example.kichi.buscapp.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadEspecialidadPersona;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadPersona;
import com.example.kichi.buscapp.pkgNegocios.ClsNegocioEspecialidadPersona;
import com.example.kichi.buscapp.pkgNegocios.ClsNegocioPersona;
import com.example.kichi.buscapp.pkgNegocios.ClsNegociosEspecialidad;
import com.example.kichi.buscapp.pkgNegociosParticulares.ClsEncriptacion;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistroActivity extends AppCompatActivity implements View.OnClickListener{

    ClsEncriptacion encriptacion;
    public ClsEntidadPersona entidadPersona;
    public ClsNegocioPersona negocioPersona;
    public ClsNegocioEspecialidadPersona negocioEspecialidadPersona;
    public ClsEntidadEspecialidadPersona entidadEE;
    public ClsNegociosEspecialidad negociosEspecialidad;
    String especialidadPrincipal;
    ArrayList<String> listaEspecialidad;
    ArrayList<String> listaSeleccionada, listaSeleccionada2;
    String emailRegistro;
    int request_code = 2;
    PopupMenu popup, popup2;
    String latlng;
    private View mRegistroFormView;
    private View mProgressView;
    Boolean Validacion=false;


    private static final int CAMERA_PIC_REQUEST = 1;

    EditText txtEmail, txtNombre, txtApellido, txtTelefono, txtDireccion, txtNuevoUsuario, txtNuevaPassword;
    Button btnConfirmarRegistro, btnEspecialidad, btnElegirEspecialidad;
    ImageButton btnObtenerUbicacion;
    CircleImageView btnCamara;
    String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Quitar Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Quitar Action Bar
        setContentView(R.layout.activity_registro);

        // Registrar permiso de acceso de la aplicacion
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Registrar permiso de acceso de la aplicacion

        try {
            MtdCargarEspecialidades();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        txtEmail = (EditText) findViewById(R.id.TxtEmail);
        txtNombre = (EditText) findViewById(R.id.TxtNombre);
        txtApellido = (EditText) findViewById(R.id.TxtApellido);
        txtTelefono = (EditText) findViewById(R.id.TxtTelefono);
        txtDireccion = (EditText) findViewById(R.id.TxtDireccion);
        txtNuevoUsuario = (EditText) findViewById(R.id.TxtUsuario);
        txtNuevaPassword = (EditText) findViewById(R.id.TxtNuevaPassword);

        btnConfirmarRegistro = (Button) findViewById(R.id.BtnConfirmarRegistro);
        btnConfirmarRegistro.setOnClickListener(this);

        btnCamara = (CircleImageView) findViewById(R.id.BtnFoto);
        btnCamara.setOnClickListener(this);

        btnEspecialidad = (Button) findViewById(R.id.BtnEspecialidad);
        btnEspecialidad.setOnClickListener(this);

        btnElegirEspecialidad = (Button) findViewById(R.id.BtnElegirEspecialidad);
        btnElegirEspecialidad.setOnClickListener(this);

        btnObtenerUbicacion = (ImageButton) findViewById(R.id.btngetlocation);
        btnObtenerUbicacion.setOnClickListener(this);

        mRegistroFormView = findViewById(R.id.registro_form);
        mProgressView = findViewById(R.id.registro_progress);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.BtnConfirmarRegistro:
                showProgress(true);

                negocioPersona = new ClsNegocioPersona();
                entidadPersona = new ClsEntidadPersona();
                negocioEspecialidadPersona = new ClsNegocioEspecialidadPersona();

                RegistroActivity clase = this;
                RegistrarUsuario registrarUsuario = clase.new RegistrarUsuario();
                registrarUsuario.execute();
                break;
            case R.id.BtnFoto:
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                break;
            case R.id.BtnEspecialidad:
                PopupEspecialidades(view);
                break;
            case R.id.BtnElegirEspecialidad:
                PopupElegirEspecialidad(view);
                break;
            case R.id.btngetlocation:
                Bundle args = new Bundle();
                args.putInt("codigo",0);
                Intent intent = new Intent(this,MapsActivity.class);
                intent.putExtras(args);
                startActivityForResult(intent,request_code);
        }
    }

    public class RegistrarUsuario extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Boolean resp = false;
            MtdRegistrarUsuario();
            if(Validacion==true){
                Snackbar.make(getWindow().getDecorView().getRootView() ,"Usuario registrado",Snackbar.LENGTH_SHORT).show();
                resp = true;
            }else {
                Snackbar.make(getWindow().getDecorView().getRootView() ,"Error al registrar usuario",Snackbar.LENGTH_SHORT).show();
                resp = false;
            }
            return resp;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if(success) {
                if (listaSeleccionada.size() > 0) {
                    MtdRegistrarEspecialidades(emailRegistro);
                }
                String email = String.valueOf(txtEmail.getText());
                Intent data = new Intent();
                data.setData(Uri.parse(email));
                setResult(RESULT_OK, data);
                finish();
            }else {
                showProgress(false);
            }
        }
    }

    public void MtdRegistrarUsuario(){

        encriptacion = new ClsEncriptacion();

        emailRegistro = String.valueOf(txtEmail.getText());
        entidadPersona.setEmail_persona(String.valueOf(txtEmail.getText()));
        entidadPersona.setNombre_persona(String.valueOf(txtNombre.getText()));
        entidadPersona.setApellido_persona(String.valueOf(txtApellido.getText()));
        entidadPersona.setTelefono_persona(String.valueOf(txtTelefono.getText()));
        entidadPersona.setDireccion_persona(latlng);
        entidadPersona.setPassword_persona(encriptacion.MD5(String.valueOf(txtNuevaPassword.getText())));
        entidadPersona.setFoto_persona(encodedImage);

        Validacion =  negocioPersona.AgregarPersona(entidadPersona);
    }

    public void MtdRegistrarEspecialidades(String email) {

            ArrayList<ClsEntidadEspecialidadPersona> entidadEspecialidadPersona = new ArrayList<>();

            for(int r=0; r < listaSeleccionada.size(); r++){
                entidadEE = new ClsEntidadEspecialidadPersona();
                entidadEE.setEmail_persona_especialidad(email);
                entidadEE.setId_especialidad_especialidad_persona(String.valueOf(listaSeleccionada.get(r)));
                entidadEE.setEspecialidad_principal_especialidad_persona(especialidadPrincipal);
                entidadEspecialidadPersona.add(entidadEE);
            }
            negocioEspecialidadPersona.AgregarEspecialidadPersona(entidadEspecialidadPersona);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap thumbnail = null;
        boolean guardar = false;
        if (requestCode == CAMERA_PIC_REQUEST) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            btnCamara.setImageBitmap(thumbnail);
            guardar = true;
        }
        if(guardar==true){
            //Convertir bitmap en Base64 para enviarlo a BD
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG,75, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            encodedImage = Base64.encodeToString(byteArray,Base64.DEFAULT);
            guardar = false;
        }

        if ((requestCode == request_code) && (resultCode == RESULT_OK)){
            latlng = String.valueOf(data.getDataString());
            txtDireccion.setText("Configurada");
        }
    }

    private void MtdCargarEspecialidades() throws SQLException {
        negociosEspecialidad = new ClsNegociosEspecialidad();
        listaEspecialidad = new ArrayList<>();
        listaEspecialidad = negociosEspecialidad.cargarEspecialidad();
        listaSeleccionada = new ArrayList<>();
        listaSeleccionada2 = new ArrayList<>();
    }

    public void PopupEspecialidades(View v) {
        popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();

        for (int e = 0; e <= listaEspecialidad.size()-2 ; e += 2){
            String id = String.valueOf(listaEspecialidad.get(e));
            if(listaSeleccionada.contains(id)){
                popup.getMenu().add(1, Integer.parseInt(listaEspecialidad.get(e)), Menu.NONE,listaEspecialidad.get(e+1)).setChecked(true);
            }else{
                popup.getMenu().add(1, Integer.parseInt(listaEspecialidad.get(e)), Menu.NONE,listaEspecialidad.get(e+1)).setChecked(false);
            }
        }
        popup.getMenu().setGroupCheckable(1,true,false);

        inflater.inflate(R.menu.menu_especialidad, popup.getMenu());
        popup.show();
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Evento del click
                item.setChecked(!item.isChecked());
                //Verificar despues del cambio de estado
                if(item.isChecked()) {
                    String id = String.valueOf(item.getItemId());
                    String desc = String.valueOf(item.getTitle());
                    //AGREGA EL MARCADO
                    MtdAgrega(id,desc);
                }else {
                    //ELIMINA EL MARCADO
                    String id = String.valueOf(item.getItemId());
                    String desc = String.valueOf(item.getTitle());
                    MtdElimina(id,desc);
                }
                //Evitar que se cierre el popup
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                item.setActionView(new View(getApplicationContext()));
                return false;
            }
        });
    }

    public void PopupElegirEspecialidad(View v) {
        popup2 = new PopupMenu(this, v);
        MenuInflater inflater = popup2.getMenuInflater();

        for (int e = 0; e < listaSeleccionada.size() ; e ++){
            if(listaSeleccionada.get(e).equals(especialidadPrincipal) && listaSeleccionada.contains(especialidadPrincipal)) {
                popup2.getMenu().add(2, Integer.parseInt(listaSeleccionada.get(e)), Menu.NONE, listaSeleccionada2.get(e)).setChecked(true);
            }else{
                popup2.getMenu().add(2, Integer.parseInt(listaSeleccionada.get(e)), Menu.NONE, listaSeleccionada2.get(e));
            }

        }
        popup2.getMenu().setGroupCheckable(2,true,true);
        inflater.inflate(R.menu.menu_especialidad, popup2.getMenu());

        popup2.show();
        popup2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Evento del click
                item.setChecked(!item.isChecked());
                //Setear especialidad principal
                especialidadPrincipal = String.valueOf(item.getItemId());
                //Evitar que se cierre el popup
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
                item.setActionView(new View(getApplicationContext()));
                return false;
            }
        });
    }

    private void MtdAgrega(String id,String desc) {
        if(!Arrays.asList(listaSeleccionada).contains(id)){
            listaSeleccionada.add(id);
            listaSeleccionada2.add(desc);
        }
    }
    private void MtdElimina(String id,String desc) {
        listaSeleccionada.remove(id);
        listaSeleccionada2.remove(desc);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegistroFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegistroFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegistroFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mRegistroFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}
