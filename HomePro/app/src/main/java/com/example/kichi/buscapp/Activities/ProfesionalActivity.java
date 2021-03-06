package com.example.kichi.buscapp.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadPersona;

public class ProfesionalActivity extends AppCompatActivity implements View.OnClickListener {

        String telefonoP;
        String direccionP;
        String emailP;
        String nombreapellidoP;
        String fotoP;

        String emailU;
        String nombreU;
        String apellidoU;
        String fotoU;
        String direccionU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        emailP = extras.getString("email_profesional");
        nombreapellidoP = extras.getString("nombre_apellido_profesional");
        telefonoP = extras.getString("telefono_profesional");
        direccionP = extras.getString("direccion_profesional");
        fotoP = extras.getString("foto_texto");

        emailU = extras.getString("emailU","");
        nombreU = extras.getString("nombreU","");
        apellidoU = extras.getString("apellidoU","");
        fotoU = extras.getString("fotoU","");
        direccionU = extras.getString("direccionU","");

        byte[] decodedString = Base64.decode(fotoP, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        Button btntelefono = (Button) findViewById(R.id.telefonoProfesional);
        Button btndireccionPro = (Button) findViewById(R.id.btndireccionProfesional);
        ImageView imgfoto = (ImageView) findViewById(R.id.imagenProfesional);
        FloatingActionButton fabmensaje = (FloatingActionButton)findViewById(R.id.fabmessage);
        //TextView txtdatos = (TextView) findViewById(R.id.nombreapellidoProfesional);
        TextView txtemail = (TextView) findViewById(R.id.emailProfesional);
        TextView txtdireccion = (TextView) findViewById(R.id.direccionProfesional);
        TextView headscrooling = (TextView)findViewById(R.id.head_scrooling);

        headscrooling.setText(nombreapellidoP);
        txtemail.setText(emailP);

        //txtdatos.setText(nombreapellido);

        btntelefono.setText("Llamar a "+telefonoP);
        txtdireccion.setText(direccionP);
        imgfoto.setImageBitmap(decodedBitmap);

        fabmensaje.setOnClickListener(this);
        btntelefono.setOnClickListener(this);
        btndireccionPro.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fabmessage:
                String message = "Hola, me interesa los servicios que ofreces " + "\ud83d\ude0f" + "\ud83d\ude0f";
                String prefijo="51";

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra("jid", prefijo+telefonoP + "@s.whatsapp.net");

                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                break;
            case R.id.telefonoProfesional:
                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:" + telefonoP));    //this is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(ProfesionalActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(ProfesionalActivity.this,new String[]{Manifest.permission.CALL_PHONE},10);  //request specific permission from user
                    return;
                }else {     //have got permission
                    try{
                        startActivity(callIntent);  //call activity and make phone call
                    }
                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(getApplicationContext(),"No se puede llamar",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btndireccionProfesional:

                Bundle args = new Bundle();

                String latlngP[] = direccionP.split("@");
                String latlngU[] = direccionU.split("@");

                args.putInt("codigo",1);
                args.putString("latP",latlngP[0]);
                args.putString("lngP",latlngP[1]);
                args.putString("latU",latlngU[0]);
                args.putString("lngU",latlngU[1]);

                args.putString("datosP",nombreapellidoP);
                args.putString("telefonoP",telefonoP);

                Intent intent = new Intent(this,MapsActivity.class);
                intent.putExtras(args);

                startActivityForResult(intent,50);

                break;
        }
    }
}
