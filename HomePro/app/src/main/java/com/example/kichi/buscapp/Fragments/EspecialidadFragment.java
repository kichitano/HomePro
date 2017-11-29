package com.example.kichi.buscapp.Fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgAdaptadores.AdaptadorEspecialidad;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadEspecialidad;
import com.example.kichi.buscapp.pkgListener.RecyclerItemClickListener;
import com.example.kichi.buscapp.pkgNegocios.ClsNegociosEspecialidad;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadFragment extends Fragment{
    //public class EspecialidadFragment extends Fragment implements SearchView.OnQueryTextListener{

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private ClsNegociosEspecialidad negociosEspecialidad;
    ArrayList<String> listaEspecialidad = null;
    private View view;
    private StaggeredGridLayoutManager mLayoutManager;

    public EspecialidadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_especialidad, container, false);


        negociosEspecialidad = new ClsNegociosEspecialidad();
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


        recycler = (RecyclerView)view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);

        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recycler.setLayoutManager(mLayoutManager);

        adapter = new AdaptadorEspecialidad(datos,getContext());
        recycler.setAdapter(adapter);

        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        int idEspecialidad = Integer.parseInt(listaEspecialidad.get(position*2));

                        // Crea el nuevo fragmento y la transacción.
                        Fragment fragment = new ProfesionalesFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        Bundle args = new Bundle();
                        args.putInt("idEspecialidad", idEspecialidad);
                        fragment.setArguments(args);

                        transaction.replace(R.id.contenedor,fragment);
                        transaction.addToBackStack(null);

                        // Commit a la transacción
                        transaction.commit();


                    }
                })
        );

        //getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.);

        return view;
    }


    /*public boolean OnCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_buscar, menu);
        MenuItem item = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        try {

            ArrayList<String> listaFiltrada = filter(listaEspecialidad, newText);

        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    private ArrayList<String> filter(ArrayList<String> lista,String texto){

        try {

            texto = texto.toLowerCase();

            for (int o = 0; o <= listaEspecialidad.size() - 2; o += 2){
                String Nombre =  listaEspecialidad.get(o + 1);

                if (Nombre.contains(texto)){
                    listaEspecialidad.add(Nombre);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
