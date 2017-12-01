package com.example.kichi.buscapp.pkgAdaptadores;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadEspecialidad;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corei7 on 09/11/2017.
 */

public class AdaptadorEspecialidad extends RecyclerView.Adapter<AdaptadorEspecialidad.EspecialidadViewHolder> {

    private List<ClsEntidadEspecialidad> items;
    private Context context;
    private ArrayList<ClsEntidadEspecialidad> listaEspecialidades;


    public static class EspecialidadViewHolder extends RecyclerView.ViewHolder{
        // Campos respectivos de un item
        public TextView IdEspecialidad;
        public TextView descripcionEspecialidad;
        public ImageView imgEspecialidad;


        public EspecialidadViewHolder(View v) {
            super(v);
            IdEspecialidad = (TextView) v.findViewById(R.id.idEspecialidad);
            descripcionEspecialidad = (TextView) v.findViewById(R.id.descripcionEspecialidad);
            imgEspecialidad = (ImageView) v.findViewById(R.id.imagen);
        }
    }

    public AdaptadorEspecialidad(List<ClsEntidadEspecialidad> items,Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public EspecialidadViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view_especialidad, viewGroup, false);
        return new EspecialidadViewHolder(v);
    }

    @Override
    public void onBindViewHolder(EspecialidadViewHolder viewHolder, int i) {
        viewHolder.IdEspecialidad.setText(items.get(i).getId_espcialidad());
        viewHolder.descripcionEspecialidad.setText(items.get(i).getDescripcion_especialidad());


        String nombreEspecialidad = items.get(i).getDescripcion_especialidad().toLowerCase();

        int id = context.getResources().getIdentifier(nombreEspecialidad, "drawable", context.getPackageName());
        Drawable drawable = context.getResources().getDrawable(id);

        viewHolder.imgEspecialidad.setImageDrawable(drawable);
    }

    public void setFilter(ArrayList<ClsEntidadEspecialidad> listaEspecialidades){
        this.listaEspecialidades = new ArrayList<>();
        this.listaEspecialidades.addAll(listaEspecialidades);
        notifyDataSetChanged();
    }

}