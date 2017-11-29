package com.example.kichi.buscapp.pkgEntidad;

/**
 * Created by Corei7 on 15/11/2017.
 */

public class ClsEntidadProfesionales {

    private String email_profesional;
    private String nombre_profesional;
    private String apellido_profesional;
    private String telefono_profesional;
    private String direccion_profesional;
    private String foto_profesional;

    public ClsEntidadProfesionales(String email_profesional, String nombre_profesional, String apellido_profesional, String telefono_profesional, String direccion_profesional, String foto_profesional) {
        this.email_profesional = email_profesional;
        this.nombre_profesional = nombre_profesional;
        this.apellido_profesional = apellido_profesional;
        this.telefono_profesional = telefono_profesional;
        this.direccion_profesional = direccion_profesional;
        this.foto_profesional = foto_profesional;
    }

    public String getEmail_profesional() {
        return email_profesional;
    }

    public void setEmail_profesional(String email_profesional) {
        this.email_profesional = email_profesional;
    }

    public String getNombre_profesional() {
        return nombre_profesional;
    }

    public void setNombre_profesional(String nombre_profesional) {
        this.nombre_profesional = nombre_profesional;
    }

    public String getApellido_profesional() {
        return apellido_profesional;
    }

    public void setApellido_profesional(String apellido_profesional) {
        this.apellido_profesional = apellido_profesional;
    }

    public String getTelefono_profesional() {
        return telefono_profesional;
    }

    public void setTelefono_profesional(String telefono_profesional) {
        this.telefono_profesional = telefono_profesional;
    }

    public String getDireccion_profesional() {
        return direccion_profesional;
    }

    public void setDireccion_profesional(String direccion_profesional) {
        this.direccion_profesional = direccion_profesional;
    }

    public String getFoto_profesional() {
        return foto_profesional;
    }

    public void setFoto_profesional(String foto_profesional) {
        this.foto_profesional = foto_profesional;
    }
}
