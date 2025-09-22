package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.implementations;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import jakarta.transaction.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.CatedraDAO;

@Repository
public class CatedraDAOImpl implements CatedraDAO {

    @PersistenceContext // Esta anotación indica a JPA que el contenedor debe inyectar automáticamente un objeto EntityManager en este campo.
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Map<String, Object>> obtenerCatedras() {

        // Como no tenemos una entidad Cátedra y sólo necesitamos recuperar las instancias de la tabla 'catedras', realizamos una consulta nativa:
        String sql = "SELECT c.id_catedra,c.nombre_catedra FROM catedras c";

        @SuppressWarnings("unchecked") // Suprimimos la advertencia de 'retorno de tipo genérico (List)' porque nosotros sabemos que la consulta devuelve exactamente una lista de arreglos de objetos (List<Object[]>) entonces no hay peligro en tiempo de compilación.
        List<Object[]> resultados = entityManager.createNativeQuery(sql).getResultList();

        // Mapeamos todas las entidades recuperadas a una lista de maps para transferirlas al gestor.
        List<Map<String, Object>> listaMapas = new ArrayList<>();
        for (Object[] fila : resultados) {

            Map<String, Object> mapa = new HashMap<>();
            mapa.put("idCatedra", fila[0]);
            mapa.put("nombreCatedra", fila[1]);

            listaMapas.add(mapa);
        }

        return listaMapas;
    }

}