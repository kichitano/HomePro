package com.example.kichi.buscapp.pkgNegocios;

import com.example.kichi.buscapp.pkgEntidad.ClsEntidadEspecialidadPersona;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadMapa;
import com.example.kichi.buscapp.pkgNegociosParticulares.ClsConexion;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Corei7 on 20/11/2017.
 */

public class ClsNegocioEspecialidadPersona {

    public ClsConexion con;

    public void AgregarEspecialidadPersona(ArrayList<ClsEntidadEspecialidadPersona> epersona){
        con = new ClsConexion();
        Connection connection = con.ConnectionHelper();
        String cadenaSql = "insert into tbl_especialidad_persona values (?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(cadenaSql);
            for(int t = 0; t < (epersona.size()); t++){
                preparedStatement.setString(1,epersona.get(t).getEmail_persona_especialidad());
                preparedStatement.setInt(2,Integer.parseInt(epersona.get(t).getId_especialidad_especialidad_persona()));
                preparedStatement.setString(3,epersona.get(t).getEspecialidad_principal_especialidad_persona());
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
            connection.close();
        }catch (Exception e){
        }
    }

    public ArrayList<ClsEntidadMapa> CargarPersonasEspecialidad() throws SQLException{
        ArrayList<ClsEntidadMapa> todosProfMapa = new ArrayList<>();
        con = new ClsConexion();
        Connection connection = con.ConnectionHelper();
        PreparedStatement preparedStatement = connection.prepareStatement("select nombre_persona,apellido_persona,telefono_persona,lat_persona,lng_persona,especialidad_principal_especialidad_persona,descripcion_especialidad from tbl_persona  inner join tbl_especialidad_persona on email_persona=email_persona_especialidad_persona inner join tbl_especialidad on id_especialidad = especialidad_principal_especialidad_persona\n" +
                "where id_especialidad_especialidad_persona = especialidad_principal_especialidad_persona");
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            ClsEntidadMapa clsEntidadMapa = new ClsEntidadMapa();
            clsEntidadMapa.setNombre_persona(rs.getString(1));
            clsEntidadMapa.setApellido_persona(rs.getString(2));
            clsEntidadMapa.setTelefono_persona(rs.getString(3));
            clsEntidadMapa.setLat_persona(rs.getString(4));
            clsEntidadMapa.setLng_persona(rs.getString(5));
            clsEntidadMapa.setEspecialidad_principal_especialidad_persona(rs.getString(6));
            clsEntidadMapa.setDescripcion_especialidad(rs.getString(7));
            todosProfMapa.add(clsEntidadMapa);
        }
        preparedStatement.close();
        connection.close();
        return todosProfMapa;
    }
}
