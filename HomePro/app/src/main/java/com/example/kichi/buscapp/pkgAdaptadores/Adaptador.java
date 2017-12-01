package com.example.kichi.buscapp.pkgAdaptadores;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kichi.buscapp.Activities.ProfesionalActivity;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadEspecialidad;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corei7 on 06/11/2017.
 */

public abstract class Adaptador extends BaseAdapter{
    private ArrayList<?> entradas;
    private int R_layout_IdView;
    private Context contexto;

    public Adaptador(ProfesionalActivity contexto, int R_layout_IdView, ArrayList<?> entradas) {
        super();
        this.contexto = contexto;
        this.entradas = entradas;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }
        onEntrada (entradas.get(posicion), view);
        return view;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Object getItem(int posicion) {
        return entradas.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    public abstract void onEntrada(Object var1, View var2);
}

