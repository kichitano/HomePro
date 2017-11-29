package com.example.kichi.buscapp.pkgNegociosParticulares;

import com.example.kichi.buscapp.pkgEntidad.ClsEntidadPersona;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Kichi on 29/08/2017.
 */

public class ClsLogin {
    public ClsConexion con;

    public ClsEntidadPersona verificarLogin(String email_persona,String contrasena_persona) throws SQLException {
        con = new ClsConexion();
        ClsEntidadPersona usuario = null;
        String cadenaSql = "select email_persona,nombre_persona,apellido_persona,telefono_persona,direccion_persona,foto_persona from tbl_persona where email_persona = ? and contrasena_persona= ?";
        Connection connection = con.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(cadenaSql);
        preparedStatement.setString(1,email_persona);
        preparedStatement.setString(2,contrasena_persona);
        ResultSet resultSet = preparedStatement.executeQuery();
        usuario = new ClsEntidadPersona();
        if(resultSet.next()){
            usuario.setEmail_persona(resultSet.getString(1));
            usuario.setNombre_persona(resultSet.getString(2));
            usuario.setApellido_persona(resultSet.getString(3));
            usuario.setTelefono_persona(resultSet.getString(4));
            usuario.setDireccion_persona(resultSet.getString(5));
            usuario.setFoto_persona(resultSet.getString(6));
        }
        preparedStatement.close();
        connection.close();
        return usuario;
    }
}
