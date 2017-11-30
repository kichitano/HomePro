package com.example.kichi.buscapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kichi.buscapp.Activities.ProfesionalActivity;
import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgAdaptadores.AdaptadorProfesionales;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadProfesionales;
import com.example.kichi.buscapp.pkgListener.RecyclerItemClickListener;
import com.example.kichi.buscapp.pkgNegocios.ClsNegociosEspecialidad;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Corei7 on 10/11/2017.
 */

public class ProfesionalesFragment extends Fragment {

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    ClsNegociosEspecialidad negociosEspecialidad;
    ArrayList<String> listaProfesionales;
    private View view;
    String emailU;
    String nombreU;
    String apellidoU;
    String fotoU;
    String direccionU;

    public ProfesionalesFragment(){

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //int idEspecialidad = getArguments().getInt("idEspecialidad");

        view = inflater.inflate(R.layout.fragment_profesionales, container, false);

        final Bundle bundle = this.getArguments();
        int idEspecialidad = bundle.getInt("idEspecialidad", 0);
        int Visualiza = bundle.getInt("visualiza",0);

        emailU = bundle.getString("emailU","");
        nombreU = bundle.getString("nombreU","");
        apellidoU = bundle.getString("apellidoU","");
        fotoU = bundle.getString("fotoU","");
        direccionU = bundle.getString("direccionU","");


        negociosEspecialidad = new ClsNegociosEspecialidad();
        listaProfesionales = new ArrayList<>();

        if(Visualiza == 0) {
            try {
                listaProfesionales = negociosEspecialidad.cargarProfesionalesHogar(idEspecialidad);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                listaProfesionales = negociosEspecialidad.cargarTodosProfesionalesHogar();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        final List datos = new ArrayList<>();
        for (int o = 0; o <= listaProfesionales.size() - 6; o += 6) {

            String email_profesional = listaProfesionales.get(o);
            String nombre_profesional =  listaProfesionales.get(o+1);
            String apellido_profesional = listaProfesionales.get(o+2);
            String telefono_profesional = listaProfesionales.get(o+3);
            String direccion_profesional = listaProfesionales.get(o+4);
            String foto_profesional = listaProfesionales.get(o+5);

            datos.add(new ClsEntidadProfesionales(email_profesional,nombre_profesional,apellido_profesional,telefono_profesional,direccion_profesional,foto_profesional));
        }

        recycler = (RecyclerView)view.findViewById(R.id.reciclador2);
        recycler.setHasFixedSize(true);

        lManager = new LinearLayoutManager(getContext());
        recycler.setLayoutManager(lManager);

        adapter = new AdaptadorProfesionales(datos);
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(

                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        String email_profesional = listaProfesionales.get((position*6));
                        String nombre_profesional =  listaProfesionales.get((position*6)+1);
                        String apellido_profesional = listaProfesionales.get((position*6)+2);
                        String telefono_profesional = listaProfesionales.get((position*6)+3);
                        String direccion_profesional = listaProfesionales.get((position*6)+4);
                        String foto_texto = listaProfesionales.get((position*6)+5);

                        Bundle args = new Bundle();
                        args.putString("email_profesional",email_profesional);
                        args.putString("nombre_apellido_profesional",nombre_profesional + " " + apellido_profesional);
                        args.putString("telefono_profesional",telefono_profesional);
                        args.putString("direccion_profesional",direccion_profesional);
                        args.putString("foto_texto",foto_texto);

                        args.putString("emailU",emailU);
                        args.putString("nombreU",nombreU);
                        args.putString("apellidoU",apellidoU);
                        args.putString("fotoU",fotoU);
                        args.putString("direccionU",direccionU);

                        Intent intent = new Intent(getActivity(), ProfesionalActivity.class);

                        intent.putExtras(args);

                        startActivity(intent);

                    }
                })
        );

        return view;
    }
}
