package com.example.kichi.buscapp.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadPersona;
import com.example.kichi.buscapp.pkgNegociosParticulares.ClsEncriptacion;
import com.example.kichi.buscapp.pkgNegociosParticulares.ClsLogin;

import java.sql.SQLException;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    EditText txtUsuario,txtPassword;
    String email;
    Button btnLogin,btnRegistro;
    public ClsEntidadPersona entidadPersona;
    public ClsLogin login;
    Intent intent;
    Bundle bundle;
    private View mProgressView;
    private View mLoginFormView;
    ClsEncriptacion encriptacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Quitar Action Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Quitar Action Bar
        setContentView(R.layout.activity_login);

        // Registrar permiso de acceso de la aplicacion
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        // Registrar permiso de acceso de la aplicacion

        //PETICION DE PERMISOS
        //LLAMADAS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            //request permission from user if the app hasn't got the required permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},10);  //request specific permission from user
            return;
        }

        txtUsuario = (EditText)findViewById(R.id.TxtUsuario);
        txtPassword = (EditText)findViewById(R.id.TxtPassword);

        btnLogin = (Button)findViewById(R.id.BtnLogin);
        btnLogin.setOnClickListener(this);

        btnRegistro = (Button)findViewById(R.id.BtnConfirmarRegistro);
        btnRegistro.setOnClickListener(this);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

    }

    @Override
    public void onClick(View view) {

        encriptacion = new ClsEncriptacion();
        UserLoginTask mAuthTask;

        switch (view.getId()){
            case R.id.BtnLogin:
                showProgress(true);
                mAuthTask = new UserLoginTask(txtUsuario.getText().toString(),encriptacion.MD5(txtPassword.getText().toString()));
                mAuthTask.execute((Void) null);
                break;
            case R.id.BtnConfirmarRegistro:
                intent = new Intent(this,RegistroActivity.class);
                startActivityForResult(intent,1);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            Boolean Validacion=false;
            login = new ClsLogin();
            try {
                entidadPersona = login.verificarLogin(mEmail,mPassword);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(entidadPersona.getEmail_persona()!=null){
                Validacion = true;
            }else{
                Validacion = false;
            }
            return Validacion;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                Toast toast =  Toast.makeText(LoginActivity.this,"Bienvenido\n"+entidadPersona.getNombre_persona() + " " + entidadPersona.getApellido_persona(),Toast.LENGTH_SHORT);
                //Mostrar Toast centrado
                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                if( v != null) v.setGravity(Gravity.CENTER);
                intent = new Intent(LoginActivity.this,MenuActivity.class);
                bundle = new Bundle();
                bundle.putString("Nombre",entidadPersona.getNombre_persona());
                bundle.putString("Apellido",entidadPersona.getApellido_persona());
                bundle.putString("Email",entidadPersona.getEmail_persona());
                bundle.putString("Foto",entidadPersona.getFoto_persona());
                intent.putExtras(bundle);
                startActivity(intent);
                toast.show();
                showProgress(false);
            } else {
                showProgress(false);
                Toast.makeText(getApplicationContext(), "Error de inicio de sesión", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if ((requestCode == 1) && (resultCode == RESULT_OK)){
            email = String.valueOf(data.getDataString());
            txtUsuario.setText(email);
        }
    }

}
