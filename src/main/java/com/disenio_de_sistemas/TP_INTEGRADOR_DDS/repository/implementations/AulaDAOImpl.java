package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.implementations;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.FluentQuery;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.*;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.AulaDAO;

public class AulaDAOImpl implements AulaDAO {

    @PersistenceContext // Esta anotación indica a JPA que el contenedor debe inyectar automáticamente un objeto EntityManager en este campo.
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<Aula> buscarAulasHabilitadas(String tipoDeAula, int capacidad) {
        String consulta;

        // Dependiendo del tipo de aula, se elige la tabla asociada en la base de datos:
        switch (tipoDeAula.toLowerCase()) {
            case "informatica":
                consulta = "SELECT a FROM AulaInformatica a WHERE a.estado = true AND a.capacidad >= :capacidad";
                break;
            case "multimedio":
                consulta = "SELECT a FROM AulaMultimedio a WHERE a.estado = true AND a.capacidad >= :capacidad";
                break;
            case "sra":
                consulta = "SELECT a FROM AulaSinRecursosAdicionales a WHERE a.estado = true AND a.capacidad >= :capacidad";
                break;
            default:
                throw new IllegalArgumentException("Tipo de aula no reconocido: " + tipoDeAula); // Lanzamiento de excepción por si queremos acceder a la consulta directamente mediante el endpoint (con PostMan).
        }

        TypedQuery<Aula> query = entityManager.createQuery(consulta, Aula.class);
        query.setParameter("capacidad", capacidad);

        return query.getResultList();
    }

    @Override
    @Transactional
    public List<Aula> buscarAula(int numeroAula, String tipoDeAula, int capacidad) {
        StringBuilder queryStr;

        // Dependiendo del tipo de aula, se elige la tabla asociada en la base de datos, o, si no se ingresó tipo de aula, se hace una consulta genérica.
        if (tipoDeAula != null && !tipoDeAula.isEmpty()) {
            switch (tipoDeAula.toLowerCase()) {
                case "informatica":
                    queryStr = new StringBuilder("SELECT a FROM AulaInformatica a WHERE 1=1");
                    break;
                case "multimedio":
                    queryStr = new StringBuilder("SELECT a FROM AulaMultimedio a WHERE 1=1");
                    break;
                case "sra":
                    queryStr = new StringBuilder("SELECT a FROM AulaSinRecursosAdicionales a WHERE 1=1");
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de aula no reconocido: " + tipoDeAula);
            }
        } else {
            queryStr = new StringBuilder("SELECT a FROM Aula a WHERE 1=1");
        }

        // Agregamos filtros dinámicos según los parámetros no vacíos:
        if (numeroAula != 0) {
            queryStr.append(" AND a.idAula = :numeroAula");
        }
        if (capacidad != -1) {
            queryStr.append(" AND a.capacidad >= :capacidad");
        }

        // Creamos la consulta tipada, seleccionando la clase adecuada si se ingresó tipoDeAula:
        Class<? extends Aula> queryType = tipoDeAula != null && !tipoDeAula.isEmpty() ? determinarClaseAula(tipoDeAula) : Aula.class;

        @SuppressWarnings("unchecked")
        TypedQuery<Aula> query = entityManager.createQuery(queryStr.toString(), (Class<Aula>) queryType);

        // Establecemos parámetros dinámicamente:
        if (numeroAula != 0) {
            query.setParameter("numeroAula", numeroAula);
        }
        if (capacidad != -1) {
            query.setParameter("capacidad", capacidad);
        }

        return query.getResultList();
    }

    /**
     * Con el siguiente metodo se determina la clase correspondiente a la subclase de Aula según el tipo.
     *
     * @param tipoDeAula Tipo de aula (informatica, multimedio, sra).
     * @return Clase correspondiente a la subclase de Aula.
     */
    private Class<? extends Aula> determinarClaseAula(String tipoDeAula) {
        switch (tipoDeAula.toLowerCase()) {
            case "informatica":
                return AulaInformatica.class;
            case "multimedio":
                return AulaMultimedio.class;
            case "sra":
                return AulaSinRecursosAdicionales.class;
            default:
                throw new IllegalArgumentException("Tipo de aula no reconocido: " + tipoDeAula);
        }
    }

    @Override
    @Transactional
    public Aula buscarPorId(int idAula) {
        TypedQuery<Aula> query = entityManager.createQuery("SELECT a FROM Aula a WHERE a.id_aula = :idAula", Aula.class);
        query.setParameter("idAula", idAula);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public <T> T obtenerAulaPorIDyTipo(String tipo, int idAula, Class<T> tipoClase) {
        String query;

        // Definimos la consulta según el tipo de aula:
        switch (tipo) {
            case "AulaMultimedio":
                query = "SELECT a FROM AulaMultimedio a WHERE a.id = :id";
                break;
            case "AulaInformatica":
                query = "SELECT a FROM AulaInformatica a WHERE a.id = :id";
                break;
            case "AulaSinRecursosAdicionales":
                query = "SELECT a FROM AulaSinRecursosAdicionales a WHERE a.id = :id";
                break;
            default:
                throw new IllegalArgumentException("Tipo de aula no reconocido: " + tipo);
        }

        TypedQuery<T> typedQuery = entityManager.createQuery(query, tipoClase);
        typedQuery.setParameter("id", idAula);

        return typedQuery.getResultStream().findFirst().orElseThrow(() -> new IllegalArgumentException("Aula no encontrada con ID: " + idAula));
    }

    @Transactional
    @Override
    public void actualizarAula(Aula aula) {
        entityManager.merge(aula); // Persistimos los cambios.
    }

    // Metodo para poder devolver todas las aulas de la base de datos a la interfaz de usuario.
    @Override
    @Transactional
    public List<Aula> findAll() {
        TypedQuery<Aula> query = entityManager.createQuery("SELECT a FROM Aula a", Aula.class);
        return query.getResultList();
    }

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // METODOS NO USADOS (ni implementados correctamente):

    @Override
    public void flush() {

    }

    @Override
    public <S extends Aula> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Aula> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Aula> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Aula getOne(Integer integer) {
        return null;
    }

    @Override
    public Aula getById(Integer integer) {
        return null;
    }

    @Override
    public Aula getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Aula> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Aula> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Aula> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Aula> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Aula> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Aula> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Aula, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Aula> S save(S entity) {
        return null;
    }

    @Override
    public <S extends Aula> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Aula> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<Aula> findAllById(Iterable<Integer> integers) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Aula entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Aula> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Aula> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Aula> findAll(Pageable pageable) {
        return null;
    }
}