package com.example.kichi.buscapp.pkgEntidad;

/**
 * Created by Corei7 on 05/11/2017.
 */

public class ClsEntidadEspecialidad {
    private String id_especialidad;
    private String descripcion_especialidad;

    public ClsEntidadEspecialidad (String id_espcialidad,String descripcion_especialidad) {
        this.id_especialidad = id_espcialidad;
        this.descripcion_especialidad = descripcion_especialidad;
    }

    public String getId_espcialidad() {
        return id_especialidad;
    }

    public void setId_espcialidad(String id_espcialidad) {
        this.id_especialidad = id_espcialidad;
    }

    public String getDescripcion_especialidad() {
        return descripcion_especialidad;
    }

    public void setDescripcion_especialidad(String descripcion_especialidad) {
        this.descripcion_especialidad = descripcion_especialidad;
    }
}
