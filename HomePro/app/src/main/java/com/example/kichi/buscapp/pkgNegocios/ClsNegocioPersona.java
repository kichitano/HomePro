package com.example.kichi.buscapp.pkgNegocios;

import com.example.kichi.buscapp.pkgEntidad.ClsEntidadPersona;
import com.example.kichi.buscapp.pkgNegociosParticulares.ClsConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Created by Kichi on 29/08/2017.
 */

public class ClsNegocioPersona {

    public ClsConexion con;

    public ClsNegocioPersona(){
    }

    public boolean AgregarPersona(ClsEntidadPersona persona){
        Boolean validacion;
        con = new ClsConexion();
        Connection connection = con.getConnection();
        String cadenaSql = "insert into tbl_persona values (?,?,?,?,?,?,?,?)";
        try {

            PreparedStatement preparedStatement = connection.prepareStatement(cadenaSql);
            preparedStatement.setString(1,persona.getEmail_persona());
            preparedStatement.setString(2,persona.getNombre_persona());
            preparedStatement.setString(3,persona.getApellido_persona());
            preparedStatement.setString(4,persona.getTelefono_persona());

            String[] separated = persona.getDireccion_persona().split("@");
            preparedStatement.setString(5,separated[0]);
            preparedStatement.setString(6,separated[1]);

            preparedStatement.setString(7,persona.getPassword_persona());
            preparedStatement.setString(8,persona.getFoto_persona());

            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();

            validacion = true;

        }catch (Exception e){
            validacion = false;
        }
        return validacion;
    }

}
