package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.implementations;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.FluentQuery;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.Bedel;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.TurnoDeTrabajo;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.BedelDAO;

@Repository
public class BedelDAOImpl implements BedelDAO {

    @PersistenceContext // Esta anotación indica a JPA que el contenedor debe inyectar automáticamente un objeto EntityManager en este campo.
    private EntityManager entityManager;

    @Override
    @Transactional
    public Bedel findByNombreUsuario(String nombreUsuario) {

        TypedQuery<Bedel> query = entityManager.createQuery("SELECT b FROM Bedel b WHERE b.nombreUsuario = :nombreUsuario", Bedel.class);
        query.setParameter("nombreUsuario", nombreUsuario);

        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<Bedel> findByApellidoOrTurnoDeTrabajo(String apellido, TurnoDeTrabajo turnoDeTrabajo) {

        StringBuilder queryStr = new StringBuilder("SELECT b FROM Bedel b WHERE b.estado = true");

        if (apellido != null && !apellido.isEmpty()) {
            queryStr.append(" AND b.apellido LIKE :apellido");
        }
        if (turnoDeTrabajo != null) {
            queryStr.append(" AND b.turnoDeTrabajo = :turnoDeTrabajo");
        }

        TypedQuery<Bedel> query = entityManager.createQuery(queryStr.toString(), Bedel.class);

        if (apellido != null && !apellido.isEmpty()) {

            // Usamos '%' para realizar una búsqueda parcial por datos ingresados:
            query.setParameter("apellido",apellido + "%");
        }
        if (turnoDeTrabajo != null) {
            query.setParameter("turnoDeTrabajo", turnoDeTrabajo);
        }

        return query.getResultList();
    }

    @Override
    @Transactional
    public long totalBedelesEnBD() {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(b) FROM Bedel b WHERE b.estado = true", Long.class);
        return query.getSingleResult();
    }

    @Transactional
    @Override
    public void actualizarBedel(Bedel bedel) {
        entityManager.merge(bedel);
    }

    @Override
    public <S extends Bedel> S save(S entity) {
        entityManager.persist(entity);
        return entity;
    }

    @Override
    @Transactional
    public Bedel getById(Integer integer) {
        TypedQuery<Bedel> query = entityManager.createQuery("SELECT b FROM Bedel b WHERE b.idUsuario = :idUsuario", Bedel.class);
        query.setParameter("idUsuario", integer);

        return query.getResultStream().findFirst().orElse(null);
    }

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // METODOS NO USADOS (ni implementados correctamente):

    @Override
    public void flush() {
    }

    @Override
    public <S extends Bedel> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Bedel> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteInBatch(Iterable<Bedel> entities) {
        BedelDAO.super.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch(Iterable<Bedel> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    public Bedel getOne(Integer integer) {
        return null;
    }

    public Bedel getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Bedel> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Bedel> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Bedel> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Bedel> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Bedel> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Bedel> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Bedel, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Bedel> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<Bedel> findAll() {
        return List.of();
    }

    public Optional<Bedel> findById(Integer integer) {
        return Optional.empty();
    }

    public boolean existsById(Integer integer) {
        return false;
    }

    public List<Bedel> findAllById(Iterable<Integer> integers) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    public void deleteById(Integer integer) {

    }

    @Override
    public void delete(Bedel entity) {

    }


    @Override
    public void deleteAllById(Iterable<? extends Integer> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Bedel> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Bedel> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Bedel> findAll(Pageable pageable) {
        return null;
    }
}