package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.Usuario;

public interface UsuarioDAO {

    Usuario findByNombreUsuario(String nombreUsuario);

}