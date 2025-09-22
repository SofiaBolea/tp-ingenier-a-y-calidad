package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.implementations;

import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.FluentQuery;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.*;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.DiaDeSemana;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.ReservaDAO;

@Repository
public class ReservaDAOImpl implements ReservaDAO {

    @PersistenceContext // Esta anotación indica a JPA que el contenedor debe inyectar automáticamente un objeto EntityManager en este campo.
    private EntityManager entityManager;

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // Metodos para obtener disponibilidad de aulas:

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Aula> List<T> obtenerDisponibilidadEsporadica(List<Integer> idsAulas, LocalDate fechaRenglon, LocalTime horaInicio, int duracion, Class<T> tipoAula) {
        LocalTime horaFin = horaInicio.plusMinutes(duracion);

        // Los transformamos a String porque sino SQL no los interpreta bien.
        String horaInicioStr = horaInicio.toString();
        String horaFinStr = horaFin.toString();

        String sql = """
             SELECT a.*
             FROM aulas a
             WHERE a.id_aula IN :idsAulas
               AND NOT EXISTS (
                 SELECT 1
                 FROM renglones_reserva_esporadica rre
                    JOIN reservas_esporadicas re ON rre.id_reserva = re.id_reserva
                     WHERE rre.id_aula = a.id_aula
                       AND rre.fecha = :fechaRenglon
                       AND (
                            TIME(:horaInicio) < ADDTIME(rre.horario_de_inicio, MAKETIME(rre.duracion DIV 60, rre.duracion % 60, 0))
                             AND TIME(:horaFin) > rre.horario_de_inicio
                       )
               )
               AND NOT EXISTS (
                 SELECT 1
                 FROM renglones_reserva_periodica rrp
                    JOIN reservas_periodicas rp ON rrp.id_reserva = rp.id_reserva
                     WHERE rrp.id_aula = a.id_aula
                       AND rrp.dia_semanal = (DAYOFWEEK(:fechaRenglon)-2)
                       AND (
                           rrp.horario_de_inicio < TIME(:horaFin)
                           AND ADDTIME(rrp.horario_de_inicio, MAKETIME(rrp.duracion DIV 60, rrp.duracion % 60, 0)) > TIME(:horaInicio)
                       )
               )
        """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("idsAulas", idsAulas);
        query.setParameter("fechaRenglon", fechaRenglon);
        query.setParameter("horaInicio", horaInicioStr);
        query.setParameter("horaFin", horaFinStr);

        // Configuramos el 'transformer' basado en el tipo de aula:
        query.unwrap(org.hibernate.query.NativeQuery.class).setResultTransformer(Transformers.aliasToBean(tipoAula));

        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends Aula> List<T> obtenerDisponibilidadPeriodica(List<Integer> idsAulas, List<Integer> idPeriodo, DiaDeSemana diaSemanal, LocalTime horaInicio, int duracion, Class<T> tipoAula) {

        LocalTime horaFin = horaInicio.plusMinutes(duracion);

        String horaInicioStr = horaInicio.toString();
        String horaFinStr = horaFin.toString();

        String sql = """
             SELECT a.*
             FROM aulas a
             WHERE a.id_aula IN (:idsAulas)
               AND NOT EXISTS (
                 SELECT 1
                 FROM renglones_reserva_esporadica rre
                    JOIN reservas re ON rre.id_reserva = re.id_reserva
                    JOIN periodo p ON p.id_periodo IN :idPeriodo
                     WHERE rre.id_aula = a.id_aula
                      AND :diaSemanal = (DAYOFWEEK(rre.fecha)-2)
                      AND rre.fecha BETWEEN p.fecha_inicio AND p.fecha_finalizacion
                      AND (
                           TIME(:horaInicio) < ADDTIME(rre.horario_de_inicio, MAKETIME(rre.duracion DIV 60, rre.duracion % 60, 0))
                           AND TIME(:horaFin) > rre.horario_de_inicio
                      )
               )
               AND NOT EXISTS (
               SELECT 1
               FROM renglones_reserva_periodica rrp
                    JOIN reservas_periodicas rp ON rrp.id_reserva = rp.id_reserva
                    JOIN reservada_en re_en ON rp.id_reserva = re_en.id_reserva
                     WHERE rrp.id_aula = a.id_aula
                      AND re_en.id_periodo IN :idPeriodo
                      AND rrp.dia_semanal = :diaSemanal
                      AND (
                           TIME(:horaInicio) < ADDTIME(rrp.horario_de_inicio, MAKETIME(rrp.duracion DIV 60, rrp.duracion % 60, 0))
                           AND TIME(:horaFin) > rrp.horario_de_inicio
                      )
               )
        """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("idsAulas", idsAulas);
        query.setParameter("diaSemanal", diaSemanal.ordinal());
        query.setParameter("horaInicio", horaInicioStr);
        query.setParameter("horaFin", horaFinStr);
        query.setParameter("idPeriodo", idPeriodo);

        query.unwrap(org.hibernate.query.NativeQuery.class).setResultTransformer(Transformers.aliasToBean(tipoAula));

        return query.getResultList();

    }

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // Metodos para obtener las reservas que generan superposición (en el caso de no obtener disponibilidad):

    // Superposición de reserva esporádica con reservas esporádicas:

    @Override
    @SuppressWarnings("unchecked")
    public List<RenglonReservaEsporadica> obtenerSuperposicionesEsporadicaEsporadicas(List<Integer> idsAulas, LocalDate fechaRenglon, LocalTime horaInicio, LocalTime horaFin) {

        String horaInicioStr = horaInicio.toString();
        String horaFinStr = horaFin.toString();

        String sql = """
            SELECT rre.id_renglon_reserva_esporadica, rre.horario_de_inicio, rre.duracion, rre.fecha,
                   a.id_aula, a.nombre_aula, a.capacidad
            FROM renglones_reserva_esporadica rre
            JOIN aulas a ON rre.id_aula = a.id_aula
            WHERE rre.id_aula IN :idsAulas
              AND rre.fecha = :fechaRenglon
              AND :horaInicio < ADDTIME(rre.horario_de_inicio, MAKETIME(rre.duracion DIV 60, rre.duracion % 60, 0))
              AND :horaFin > rre.horario_de_inicio
        """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("idsAulas", idsAulas)
                .setParameter("fechaRenglon", fechaRenglon)
                .setParameter("horaInicio", horaInicioStr)
                .setParameter("horaFin", horaFinStr)
                .getResultList();


        // Mapeamos manualmente los resultados a la entidad correspondiente:
        List<RenglonReservaEsporadica> renglones = new ArrayList<>();

        for (Object[] row : results) {
            RenglonReservaEsporadica renglon = new RenglonReservaEsporadica();

            renglon.setIdRenglonReservaEsporadica((Integer) row[0]);
            renglon.setHorarioDeInicio(((Time) row[1]).toLocalTime().plusHours(3)); // Ajustamos 3 horas por desfasaje de base de datos.
            renglon.setDuracion((Integer) row[2]);
            renglon.setFecha(((Date) row[3]).toLocalDate());

            Aula aula = new Aula() { // Clase anónima para evitar usar subclases:
                private final int idAula = (Integer) row[4];
                private final String nombreAula = (String) row[5];
                private final int capacidad = (Integer) row[6];

                @Override
                public int getIdAula() {
                    return idAula;
                }

                @Override
                public String getNombreAula() {
                    return nombreAula;
                }

                @Override
                public int getCapacidad() {
                    return capacidad;
                }
            };

            renglon.setAula(aula);
            renglones.add(renglon);

        }

        return renglones;

    }

    // Superposición de reserva esporádica con reservas periódicas:

    @Override
    @SuppressWarnings("unchecked")
    public List<RenglonReservaPeriodica> obtenerSuperposicionesEsporadicaPeriodicas(List<Integer> idsAulas, LocalDate fechaRenglon, LocalTime horaInicio, LocalTime horaFin) {

        String horaInicioStr = horaInicio.toString();
        String horaFinStr = horaFin.toString();

        String sql = """
            SELECT rrp.id_renglon_reserva_periodica, rrp.horario_de_inicio, rrp.duracion, rrp.dia_semanal,
                   a.id_aula, a.nombre_aula, a.capacidad
            FROM renglones_reserva_periodica rrp
            JOIN aulas a ON rrp.id_aula = a.id_aula
            WHERE rrp.id_aula IN :idsAulas
              AND rrp.dia_semanal = (DAYOFWEEK(:fechaRenglon) - 2)
              AND :horaInicio < ADDTIME(rrp.horario_de_inicio, MAKETIME(rrp.duracion DIV 60, rrp.duracion % 60, 0))
              AND :horaFin > rrp.horario_de_inicio
        """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("idsAulas", idsAulas)
                .setParameter("fechaRenglon", fechaRenglon)
                .setParameter("horaInicio", horaInicioStr)
                .setParameter("horaFin", horaFinStr)
                .getResultList();

        // Mapeamos manualmente los resultados a la entidad correspondiente:
        List<RenglonReservaPeriodica> renglones = new ArrayList<>();

        for (Object[] row : results) {
            RenglonReservaPeriodica renglon = new RenglonReservaPeriodica();

            renglon.setIdRenglonReservaPeriodica((Integer) row[0]);
            renglon.setHorarioDeInicio(((Time) row[1]).toLocalTime().plusHours(3));
            renglon.setDuracion((Integer) row[2]);

            // Convertimos el valor de dia_semanal (que es Byte) al Enum DiaDeSemana:
            Byte diaNumerico = (Byte) row[3];
            if (diaNumerico != null) {
                renglon.setDiaSemanal(DiaDeSemana.values()[diaNumerico]);
            }

            Aula aula = new Aula() { // Clase anónima para evitar usar subclases:
                private final int idAula = (Integer) row[4];
                private final String nombreAula = (String) row[5];
                private final int capacidad = (Integer) row[6];

                @Override
                public int getIdAula() {
                    return idAula;
                }

                @Override
                public String getNombreAula() {
                    return nombreAula;
                }

                @Override
                public int getCapacidad() {
                    return capacidad;
                }
            };

            renglon.setAula(aula);
            renglones.add(renglon);

        }

        return renglones;
    }

    // Superposición de reserva periódica con reservas esporádicas:

    @Override
    @SuppressWarnings("unchecked")
    public List<RenglonReservaEsporadica> obtenerSuperposicionesPeriodicaEsporadicas(List<Integer> idsAulas, DiaDeSemana diaSemanal, LocalTime horaInicio, LocalTime horaFin) {

        String horaInicioStr = horaInicio.toString();
        String horaFinStr = horaFin.toString();

        // Convertimos el enum diaSemanal a un valor entero que representa el día de la semana:
        int diaSemanaValor = diaSemanal.ordinal();

        String sql = """
            SELECT rre.id_renglon_reserva_esporadica, rre.horario_de_inicio, rre.duracion, rre.fecha,
                   a.id_aula, a.nombre_aula, a.capacidad
            FROM renglones_reserva_esporadica rre
            JOIN aulas a ON rre.id_aula = a.id_aula
            WHERE rre.id_aula IN :idsAulas
              AND DAYOFWEEK(rre.fecha) = :diaSemanaValor
              AND :horaInicio < ADDTIME(rre.horario_de_inicio, MAKETIME(rre.duracion DIV 60, rre.duracion % 60, 0))
              AND :horaFin > rre.horario_de_inicio
        """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("idsAulas", idsAulas)
                .setParameter("diaSemanaValor", diaSemanaValor)
                .setParameter("horaInicio", horaInicioStr)
                .setParameter("horaFin", horaFinStr)
                .getResultList();

        // Mapeamos manualmente los resultados a la entidad correspondiente:
        List<RenglonReservaEsporadica> renglones = new ArrayList<>();

        for (Object[] row : results) {
            RenglonReservaEsporadica renglon = new RenglonReservaEsporadica();

            renglon.setIdRenglonReservaEsporadica((Integer) row[0]);
            renglon.setHorarioDeInicio(((Time) row[1]).toLocalTime().plusHours(3));
            renglon.setDuracion((Integer) row[2]);
            renglon.setFecha(((Date) row[3]).toLocalDate());

            Aula aula = new Aula() { // Clase anónima para evitar usar subclases:
                private final int idAula = (Integer) row[4];
                private final String nombreAula = (String) row[5];
                private final int capacidad = (Integer) row[6];

                @Override
                public int getIdAula() {
                    return idAula;
                }

                @Override
                public String getNombreAula() {
                    return nombreAula;
                }

                @Override
                public int getCapacidad() {
                    return capacidad;
                }
            };

            renglon.setAula(aula);
            renglones.add(renglon);

        }

        return renglones;
    }

    // Superposición de reserva periódica con reservas periódicas:

    @Override
    @SuppressWarnings("unchecked")
    public List<RenglonReservaPeriodica> obtenerSuperposicionesPeriodicaPeriodicas(List<Integer> idsAulas, DiaDeSemana diaSemanal, LocalTime horaInicio, LocalTime horaFin) {

        String horaInicioStr = horaInicio.toString();
        String horaFinStr = horaFin.toString();

        int diaSemanaValor = diaSemanal.ordinal();

        String sql = """
            SELECT rrp.id_renglon_reserva_periodica, rrp.horario_de_inicio, rrp.duracion, rrp.dia_semanal,
                   a.id_aula, a.nombre_aula, a.capacidad
            FROM renglones_reserva_periodica rrp
            JOIN aulas a ON rrp.id_aula = a.id_aula
            WHERE rrp.id_aula IN :idsAulas
              AND rrp.dia_semanal = :diaSemanaValor
              AND :horaInicio < ADDTIME(rrp.horario_de_inicio, MAKETIME(rrp.duracion DIV 60, rrp.duracion % 60, 0))
              AND :horaFin > rrp.horario_de_inicio
        """;

        List<Object[]> results = entityManager.createNativeQuery(sql)
                .setParameter("idsAulas", idsAulas)
                .setParameter("diaSemanaValor", diaSemanaValor)
                .setParameter("horaInicio", horaInicioStr)
                .setParameter("horaFin", horaFinStr)
                .getResultList();

        List<RenglonReservaPeriodica> renglones = new ArrayList<>();

        for (Object[] row : results) {
            RenglonReservaPeriodica renglon = new RenglonReservaPeriodica();

            renglon.setIdRenglonReservaPeriodica((Integer) row[0]);
            renglon.setHorarioDeInicio(((Time) row[1]).toLocalTime().plusHours(3));
            renglon.setDuracion((Integer) row[2]);

            Byte diaNumerico = (Byte) row[3];
            if (diaNumerico != null) {
                renglon.setDiaSemanal(DiaDeSemana.values()[diaNumerico]);
            }

            Aula aula = new Aula() { // Clase anónima para evitar usar subclases:
                private final int idAula = (Integer) row[4];
                private final String nombreAula = (String) row[5];
                private final int capacidad = (Integer) row[6];

                @Override
                public int getIdAula() {
                    return idAula;
                }

                @Override
                public String getNombreAula() {
                    return nombreAula;
                }

                @Override
                public int getCapacidad() {
                    return capacidad;
                }
            };

            renglon.setAula(aula);
            renglones.add(renglon);

        }

        return renglones;
    }

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // Metodos complementarios:

    @Override
    public int obtenerIdReservaPorIdRenglonEsporadico(int idRenglon) {

        String sql = "SELECT id_reserva FROM renglones_reserva_esporadica WHERE id_renglon_reserva_esporadica = :idRenglon";

        try {
            return (int) entityManager.createNativeQuery(sql).setParameter("idRenglon", idRenglon).getSingleResult();
        } catch (NoResultException e) {
            return -1;
        }
    }

    @Override
    public int obtenerIdReservaPorIdRenglonPeriodico(int idRenglon) {

        String sql = "SELECT id_reserva FROM renglones_reserva_periodica WHERE id_renglon_reserva_periodica = :idRenglon";

        try {
            return (int) entityManager.createNativeQuery(sql).setParameter("idRenglon", idRenglon).getSingleResult();
        } catch (NoResultException e) {
            return -1;
        }
    }

    @Override
    public Map<String, Object> obtenerReservaPorId(int idReserva) {

        String sql = """
            SELECT r.id_reserva, d.docente, d.correo_electronico, c.nombre_catedra
            FROM reservas r
            JOIN docentes d ON r.id_docente = d.id_docente
            JOIN catedras c ON r.id_catedra = c.id_catedra
            WHERE r.id_reserva = :idReserva
        """;

        try {
            // Generamos un mapa para no tener que crear una nueva entidad de reserva; simplemente pasamos un mapa con los datos que necesitamos mostrar:
            Object[] result = (Object[]) entityManager.createNativeQuery(sql).setParameter("idReserva", idReserva).getSingleResult();

            Map<String, Object> reservaMap = new HashMap<>();
            reservaMap.put("idReserva", result[0]);
            reservaMap.put("docente", result[1]);
            reservaMap.put("correoElectronico", result[2]);
            reservaMap.put("nombreCatedra", result[3]);

            return reservaMap;

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    @Transactional
    public Periodo buscarPeriodoPorId(int idPeriodo) {

        TypedQuery<Periodo> query = entityManager.createQuery("SELECT b FROM Periodo b WHERE b.idPeriodo = :idPeriodo", Periodo.class);
        query.setParameter("idPeriodo", idPeriodo);

        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    public <S extends Reserva> S save(S entity) {
        entityManager.persist(entity);
        return entity;
    }

// ----------------------------------------------------------------------------------------------------------------------------------------------------------------

    // METODOS NO USADOS (ni implementados correctamente):

    @Override
    public void flush() {

    }

    @Override
    public <S extends Reserva> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Reserva> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Reserva> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Integer> integers) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Reserva getOne(Integer integer) {
        return null;
    }

    @Override
    public Reserva getById(Integer integer) {
        return null;
    }

    @Override
    public Reserva getReferenceById(Integer integer) {
        return null;
    }

    @Override
    public <S extends Reserva> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Reserva> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Reserva> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Reserva> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Reserva> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Reserva> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Reserva, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Reserva> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Reserva> findById(Integer integer) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Integer integer) {
        return false;
    }

    @Override
    public List<Reserva> findAll() {
        return List.of();
    }

    @Override
    public List<Reserva> findAllById(Iterable<Integer> integers) {
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
    public void delete(Reserva entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Integer> integers) {

    }

    @Override
    public void deleteAll(Iterable<? extends Reserva> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Reserva> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Reserva> findAll(Pageable pageable) {
        return null;
    }
}