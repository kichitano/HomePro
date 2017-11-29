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

public class ProfesionalActivity extends AppCompatActivity {

    String telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesional);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        String email = extras.getString("email_profesional");
        String nombreapellido = extras.getString("nombre_apellido_profesional");
        telefono = extras.getString("telefono_profesional");
        String direccion = extras.getString("direccion_profesional");
        String foto = extras.getString("foto_texto");


        byte[] decodedString = Base64.decode(foto, Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        TextView txtemail = (TextView) findViewById(R.id.emailProfesional);
        //TextView txtdatos = (TextView) findViewById(R.id.nombreapellidoProfesional);
        Button btntelefono = (Button) findViewById(R.id.telefonoProfesional);
        TextView txtdireccion = (TextView) findViewById(R.id.direccionProfesional);
        ImageView imgfoto = (ImageView) findViewById(R.id.imagenProfesional);
        FloatingActionButton fabmensaje = (FloatingActionButton)findViewById(R.id.fabmessage);

        TextView headscrooling = (TextView)findViewById(R.id.head_scrooling);

        headscrooling.setText(nombreapellido);

        txtemail.setText(email);
        //txtdatos.setText(nombreapellido);

        //btntelefono.setText(telefono);
        txtdireccion.setText(direccion);
        imgfoto.setImageBitmap(decodedBitmap);

        btntelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:" + telefono));    //this is the phone number calling
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
            }
        });

        fabmensaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = "Hola, me interesa los servicios que ofreces " + "\ud83d\ude0f" + "\ud83d\ude0f";
                String prefijo="51";

                Intent sendIntent = new Intent("android.intent.action.MAIN");
                sendIntent.putExtra("jid", prefijo+telefono + "@s.whatsapp.net");

                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setPackage("com.whatsapp");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);



            }
        });
    }
}
