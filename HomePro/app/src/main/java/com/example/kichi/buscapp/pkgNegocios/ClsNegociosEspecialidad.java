package com.example.kichi.buscapp.pkgNegocios;

import com.example.kichi.buscapp.pkgNegociosParticulares.ClsConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by Corei7 on 05/11/2017.
 */

public class ClsNegociosEspecialidad {

    public ClsConexion con;

    public ArrayList<String> cargarEspecialidad() throws SQLException {

        ArrayList<String> especialidad = new ArrayList<>();

        con = new ClsConexion();
        Connection connection = con.ConnectionHelper();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select * from tbl_especialidad");
        while (rs.next()) {
            //Se extraen los datos
            especialidad.add(rs.getString("id_especialidad"));
            especialidad.add(rs.getString("descripcion_especialidad"));
        }
        st.close();
        connection.close();
        return especialidad;
    }

    public ArrayList<String> cargarProfesionalesHogar(int idEspecialidad) throws SQLException{
        ArrayList<String> profresionalesHogar = new ArrayList<>();
        con = new ClsConexion();
        Connection connection = con.ConnectionHelper();
        PreparedStatement preparedStatement = connection.prepareStatement("select email_persona,nombre_persona,apellido_persona,telefono_persona,direccion_persona,foto_persona from tbl_especialidad_persona \n" +
                "inner join tbl_persona on email_persona = email_persona_especialidad_persona\n" +
                "where id_especialidad_especialidad_persona = ?");
        preparedStatement.setInt(1,idEspecialidad);
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            profresionalesHogar.add(rs.getString("email_persona"));
            profresionalesHogar.add(rs.getString("nombre_persona"));
            profresionalesHogar.add(rs.getString("apellido_persona"));
            profresionalesHogar.add(rs.getString("telefono_persona"));
            profresionalesHogar.add(rs.getString("direccion_persona"));
            profresionalesHogar.add(rs.getString("foto_persona"));
        }
        preparedStatement.close();
        connection.close();
        return profresionalesHogar;
    }

    public ArrayList<String> cargarTodosProfesionalesHogar() throws SQLException{
        ArrayList<String> todosProfresionalesHogar = new ArrayList<>();
        con = new ClsConexion();
        Connection connection = con.ConnectionHelper();
        PreparedStatement preparedStatement = connection.prepareStatement("select email_persona_especialidad_persona,nombre_persona,apellido_persona,\n" +
                "telefono_persona,direccion_persona,foto_persona \n" +
                "from (select email_persona_especialidad_persona from tbl_especialidad_persona group by email_persona_especialidad_persona) esp_persona \n" +
                "inner join tbl_persona on email_persona = esp_persona.email_persona_especialidad_persona");
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            todosProfresionalesHogar.add(rs.getString("email_persona_especialidad_persona"));
            todosProfresionalesHogar.add(rs.getString("nombre_persona"));
            todosProfresionalesHogar.add(rs.getString("apellido_persona"));
            todosProfresionalesHogar.add(rs.getString("telefono_persona"));
            todosProfresionalesHogar.add(rs.getString("direccion_persona"));
            todosProfresionalesHogar.add(rs.getString("foto_persona"));
        }
        preparedStatement.close();
        connection.close();
        return todosProfresionalesHogar;
    }

}
