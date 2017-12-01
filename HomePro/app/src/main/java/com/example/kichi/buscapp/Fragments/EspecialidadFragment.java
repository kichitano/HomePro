package com.example.kichi.buscapp.Fragments;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.kichi.buscapp.R;
import com.example.kichi.buscapp.pkgAdaptadores.AdaptadorEspecialidad;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadEspecialidad;
import com.example.kichi.buscapp.pkgListener.RecyclerItemClickListener;
import com.example.kichi.buscapp.pkgNegocios.ClsNegociosEspecialidad;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadFragment extends Fragment implements SearchView.OnQueryTextListener{
    //public class EspecialidadFragment extends Fragment implements SearchView.OnQueryTextListener{

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;
    private ClsNegociosEspecialidad negociosEspecialidad;
    ArrayList<String> listaEspecialidad;
    ArrayList<ClsEntidadEspecialidad> busqueda = null;
    private View view;
    private StaggeredGridLayoutManager mLayoutManager;
    String emailU;
    MenuItem searchMenuItem;
    SearchView searchView;
    String nombreU;
    String apellidoU;
    String fotoU;
    String direccionU;
    AdaptadorEspecialidad adaptadorEspecialidad;

    public EspecialidadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_especialidad, container, false);


        final Bundle bundle = this.getArguments();

        emailU = bundle.getString("emailU","");
        nombreU = bundle.getString("nombreU","");
        apellidoU = bundle.getString("apellidoU","");
        fotoU = bundle.getString("fotoU","");
        direccionU = bundle.getString("direccionU","");

        negociosEspecialidad = new ClsNegociosEspecialidad();
        listaEspecialidad = new ArrayList<>();

        try {
            listaEspecialidad = negociosEspecialidad.cargarEspecialidad();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        final List datos = new ArrayList<>();
        busqueda = new ArrayList<>();
        for (int o = 0; o <= listaEspecialidad.size() - 2; o += 2) {
            String idEspecialidad = listaEspecialidad.get(o);
            String Nombre =  listaEspecialidad.get(o + 1);
            datos.add(new ClsEntidadEspecialidad(idEspecialidad,Nombre));
            busqueda.add(new ClsEntidadEspecialidad(idEspecialidad,Nombre));
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

                        args.putString("emailU",emailU);
                        args.putString("nombreU",nombreU);
                        args.putString("apellidoU",apellidoU);
                        args.putString("fotoU",fotoU);
                        args.putString("direccionU",direccionU);

                        transaction.replace(R.id.contenedor,fragment,"ProfesionalesFragment");
                        transaction.addToBackStack(null);

                        // Commit a la transacción
                        transaction.commit();
                    }
                })
        );

        setHasOptionsMenu(true);
        return view;

    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){

        inflater.inflate(R.menu.menu_buscar,menu);

        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        SearchManager searchManager = (SearchManager)getContext().getSystemService(Context.SEARCH_SERVICE);


        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setSubmitButtonEnabled(false);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adaptadorEspecialidad.setFilter(busqueda);
                return true;
            }
        });


        super.onCreateOptionsMenu(menu, inflater);

    }

    private ArrayList<ClsEntidadEspecialidad> filter(ArrayList<ClsEntidadEspecialidad> especialidad,String texto){
        ArrayList<ClsEntidadEspecialidad> listafiltrada = new ArrayList<>();
        try{
            texto = texto.toLowerCase();
            for(ClsEntidadEspecialidad especialidade: especialidad){
                String idespecialidades = especialidade.getId_espcialidad().toLowerCase();
                String especialidades = especialidade.getDescripcion_especialidad().toLowerCase();
                if(especialidades.contains(texto)){
                    ClsEntidadEspecialidad entidadEspecialidad = new ClsEntidadEspecialidad(idespecialidades,especialidades);
                    listafiltrada.add(entidadEspecialidad);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return listafiltrada;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        try{
            ArrayList<ClsEntidadEspecialidad> listafiltrada = filter(busqueda, newText);
            adaptadorEspecialidad.setFilter(listafiltrada);
        }catch (Exception e){

        }

        return true;
    }
}
