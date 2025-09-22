package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository;

import java.util.Map;
import java.util.List;

public interface CatedraDAO { // Esta interfaz no extiende de JPARepository porque sólo la necesitamos para hacer una consulta nativa.

    List<Map<String, Object>> obtenerCatedras();

}