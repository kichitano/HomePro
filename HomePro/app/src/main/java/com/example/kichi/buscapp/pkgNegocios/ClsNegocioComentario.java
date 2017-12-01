package com.example.kichi.buscapp.pkgNegocios;

import com.example.kichi.buscapp.pkgEntidad.ClsEntidadComentario;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadMapa;
import com.example.kichi.buscapp.pkgEntidad.ClsEntidadPersona;
import com.example.kichi.buscapp.pkgNegociosParticulares.ClsConexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Gary on 01/12/2017.
 */

public class ClsNegocioComentario {

    public ClsConexion con;

    public void AgregarComentario (ArrayList<ClsEntidadComentario> ecomentario){
        con = new ClsConexion();
        Connection connection = con.ConnectionHelper();
        String cadenaSql = "insert into tbl_comentarios_rating values (?,?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(cadenaSql);
            for(int t = 0; t < (ecomentario.size()); t++){
                preparedStatement.setString(1,ecomentario.get(t).getEmail_persona_comentario());
                preparedStatement.setString(2,ecomentario.get(t).getEmail_persona_especialista());
                preparedStatement.setString(3,ecomentario.get(t).getComentario_persona());
                preparedStatement.setString(4,ecomentario.get(t).getRating_especialista());
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
            connection.close();
        }catch (Exception e){
        }
    }

    //select email_persona_comentario,email_persona_especialista,comentario_persona,rating_especialista from tbl_comentarios_rating where email_persona_especialista = ?

    public ArrayList<ClsEntidadComentario> ConsultarComentario(String email_persona) throws SQLException {
        ArrayList<ClsEntidadComentario> arrayList = new ArrayList<>();
        con = new ClsConexion();
        String cadenaSql = "select email_persona_comentario,email_persona_especialista,comentario_persona,rating_especialista,foto_persona from tbl_comentarios_rating inner join tbl_persona on email_persona = email_persona_comentario where email_persona_especialista = ?";
        Connection connection = con.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(cadenaSql);
        preparedStatement.setString(1,email_persona);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()){
            ClsEntidadComentario clsEntidadComentario = new ClsEntidadComentario();
            clsEntidadComentario.setEmail_persona_comentario(resultSet.getString(1));
            clsEntidadComentario.setEmail_persona_especialista(resultSet.getString(2));
            clsEntidadComentario.setComentario_persona(resultSet.getString(3));
            clsEntidadComentario.setRating_especialista(resultSet.getString(4));
            clsEntidadComentario.setFoto_persona(resultSet.getString(5));
            arrayList.add(clsEntidadComentario);
        }
        preparedStatement.close();
        connection.close();
        return arrayList;
    }
}
