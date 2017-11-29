package com.example.kichi.buscapp.pkgAdaptadores;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadProfesionales;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Corei7 on 15/11/2017.
 */

public class AdaptadorProfesionales extends RecyclerView.Adapter<AdaptadorProfesionales.ProfesionalViewHolder>{
    private List<ClsEntidadProfesionales> items;

    public static class ProfesionalViewHolder extends RecyclerView.ViewHolder{
        //Campos respectivos del item

        public ImageView fotoEspecialista;
        public TextView datosEspecialista;
        public TextView emailProfesional;
        public TextView telefonoProfesional;
        public TextView direccionProfesional;
        public TextView fotoTexto;

        public ProfesionalViewHolder(View v){
            super(v);
            //Igualar variables con campos
            fotoEspecialista = (ImageView)v.findViewById(R.id.fotoProfesional);
            fotoTexto = (TextView) v.findViewById(R.id.fotoTexto);
            datosEspecialista = (TextView)v.findViewById(R.id.nombreapellidoProfesional);
            emailProfesional = (TextView)v.findViewById(R.id.emailProfesional);
            telefonoProfesional = (TextView)v.findViewById(R.id.telefonoProfesional);
            direccionProfesional = (TextView)v.findViewById(R.id.direccionProfesional);
        }
    }

    public AdaptadorProfesionales(List<ClsEntidadProfesionales> items){
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AdaptadorProfesionales.ProfesionalViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_profesionales, viewGroup, false);
        return new ProfesionalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ProfesionalViewHolder viewHolder, int i) {

        byte[] decodedString = Base64.decode(items.get(i).getFoto_profesional(),Base64.DEFAULT);
        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);

        viewHolder.fotoTexto.setText(items.get(i).getFoto_profesional());
        viewHolder.fotoEspecialista.setImageBitmap(decodedBitmap);
        viewHolder.datosEspecialista.setText(items.get(i).getNombre_profesional() + " " + items.get(i).getApellido_profesional());
        viewHolder.emailProfesional.setText(items.get(i).getEmail_profesional());
        viewHolder.telefonoProfesional.setText(items.get(i).getTelefono_profesional());
        viewHolder.direccionProfesional.setText(items.get(i).getDireccion_profesional());

    }
}
