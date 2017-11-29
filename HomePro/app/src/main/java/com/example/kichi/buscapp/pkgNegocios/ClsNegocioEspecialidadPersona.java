package com.example.kichi.buscapp.pkgNegocios;

import com.example.kichi.buscapp.pkgEntidad.ClsEntidadEspecialidadPersona;
import com.example.kichi.buscapp.pkgNegociosParticulares.ClsConexion;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
