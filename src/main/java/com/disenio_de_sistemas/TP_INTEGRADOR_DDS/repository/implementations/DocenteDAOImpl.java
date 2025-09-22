package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.implementations;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.DocenteDAO;

@Repository
public class DocenteDAOImpl implements DocenteDAO {

    @PersistenceContext // Esta anotación indica a JPA que el contenedor debe inyectar automáticamente un objeto EntityManager en este campo.
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Map<String, Object>> obtenerDocentes() {

        String sql = "SELECT d.id_docente AS idDocente, d.docente AS nombre, d.correo_electronico AS correo FROM docentes d";

        @SuppressWarnings("unchecked")
        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        // Mapeamos todas las entidades recuperadas a una lista de maps para transferirlas al gestor.
        List<Map<String, Object>> listaMapas = new ArrayList<>();
        for (Object[] fila : resultados) {

            Map<String, Object> mapa = new HashMap<>();
            mapa.put("idDocente", fila[0]);
            mapa.put("nombre", fila[1]);
            mapa.put("correo", fila[2]);

            listaMapas.add(mapa);
        }

        return listaMapas;
    }

}