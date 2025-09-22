package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services;

import java.util.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.repository.*;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.AulaDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.DocenteDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.ReservaDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.CatedraDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.RenglonPeriodicaDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.DTOs.RenglonEsporadicaDTO;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.*;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.models.enums.DiaDeSemana;
import com.disenio_de_sistemas.TP_INTEGRADOR_DDS.services.validationServices.ValidadorRegistroReserva;

@Service
public class GestorDeReservas {

    private final ReservaDAO reservaDAO;
    private final DocenteDAO docenteDAO; // Como la información referida a docentes es externa a nuestro sistema, no trabajamos con un gestor de docentes, razón por la cual accedemos a la base de datos mediante el DocenteDAO directamente desde el GestorDeReservas.
    private final CatedraDAO catedraDAO; // Como la información referida a cátedras es externa a nuestro sistema, no trabajamos con un gestor de cátedras, razón por la cual accedemos a la base de datos mediante el CatedraDAO directamente desde el GestorDeReservas.
    private final GestorDeAulas gestorDeAulas;
    private final GestorDeBedeles gestorDeBedeles;
    private final ValidadorRegistroReserva validadorRegistroReserva;

    // Inyección de dependencias:
    @Autowired
    public GestorDeReservas(ReservaDAO reservaDAO, DocenteDAO docenteDAO, CatedraDAO catedraDAO, GestorDeAulas gestorDeAulas, GestorDeBedeles gestorDeBedeles, ValidadorRegistroReserva validadorRegistroReserva) {
        this.reservaDAO = reservaDAO;
        this.docenteDAO = docenteDAO;
        this.catedraDAO = catedraDAO;
        this.gestorDeAulas = gestorDeAulas;
        this.gestorDeBedeles = gestorDeBedeles;
        this.validadorRegistroReserva = validadorRegistroReserva;
    }

    // Metodo para obtener todos los docentes de la base de datos y poder mostrarlos en la interfaz de usuario para seleccionar:
    public List<DocenteDTO> obtenerDocentes() {

        // Recuperamos de la base de datos todas las entidades de docentes (con sus respectivos campos) como una lista de mapas.
        List<Map<String, Object>> datos = docenteDAO.obtenerDocentes();

        // Transformamos cada mapa en un DTO:
        return datos.stream().map(mapa -> new DocenteDTO((int) mapa.get("idDocente"),(String) mapa.get("nombre"),(String) mapa.get("correo"))).toList();
    }

    // Metodo para obtener todas las cátedras, seminarios y cursos de la base de datos y poder mostrarlas en la interfaz de usuario para seleccionar:
    public List<CatedraDTO> obtenerCatedras() {

        // Recuperamos de la base de datos todas las entidades de cátedras, seminarios o cursos (con sus respectivos campos) como una lista de mapas.
        List<Map<String, Object>> datos = catedraDAO.obtenerCatedras();

        // Transformamos cada mapa en un DTO:
        return datos.stream().map(mapa -> new CatedraDTO((int) mapa.get("idCatedra"),(String) mapa.get("nombreCatedra"))).toList();
    }

    /*

    Explicación de por qué está separado así el caso de uso 'Registrar Reserva':
    - 'validarRenglones': se busca validar los datos ingresados en la primera página de la reserva (fechas y duraciones de clases válidas).
    - 'validarDatosYObtenerDisponibilidadAulas': se busca validar los datos ingresados en la segunda página de la reserva (datos generales de la reserva) y, de ser válidos, obtener la disponibilidad de aulas (o superposición de reservas).
    - 'registrarReserva': se busca, efectivamente, persistir la reserva nueva en la base de datos.

     */

    // Metodo para validar los datos correspondientes a la primera página de reservas:
    public void validarRenglones(ReservaDTO reservaDTO) {
        try {
            // Validamos para ambos tipos de reserva, todos los renglones correspondientes a días semanales (en caso de una reserva periódica) o fechas (en caso de una reserva esporádica):
            validadorRegistroReserva.validarRenglones(reservaDTO);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

    }

    // Metodo para validar los datos correspondientes a la segunda página de reservas y para obtener la disponibilidad de aulas (o superposición de reservas):
    public HashMap<String, Object> validarDatosYObtenerDisponibilidadAulas(ReservaDTO reservaDTO) {

        // Encapsulamos la lógica de validación de datos de entrada GENERALES DE LA RESERVA (segunda página del registro) dentro del mismo metodo de registro de reserva:
        try {
            validadorRegistroReserva.validarDatosReserva(reservaDTO);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        // Si los datos de entrada son válidos, procedemos a buscar disponibilidad de aulas:
        return obtenerDisponibilidad(reservaDTO);

    }

    // Metodo para registrar (persistir) una reserva (si es que no falló ninguna validación y se encontró disponibilidad de aulas para todos los renglones):
    public void registrarReserva(ReservaDTO reservaDTO) {

        // Pasadas todas las validaciones y seleccioandas las respectivas aulas para cada renglón, ya podemos convertir la reservaDTO en una entidad Reserva para persistir.
        Reserva reserva;

        // Para reservas PERIÓDICAS:
        if (Objects.equals(reservaDTO.getTipoReserva(), "PERIODICA")) {

            // Creación de tipo de reserva:
            ReservaPeriodica reservaPeriodica = new ReservaPeriodica();

            // Datos de la reserva ingresados por usuario:
            reservaPeriodica.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());
            reservaPeriodica.setIdDocente(reservaDTO.getIdDocente());
            reservaPeriodica.setDocente(reservaDTO.getDocente());
            reservaPeriodica.setIdCatedra(reservaDTO.getIdCatedra());
            reservaPeriodica.setNombreCatedra(reservaDTO.getNombreCatedra());
            reservaPeriodica.setCorreoElectronico(reservaDTO.getCorreoElectronico());

            // Datos extraídos del contexto:
            reservaPeriodica.setFechaReservacion(); // Fecha en que se hizo la reserva.
            reservaPeriodica.setBedel(gestorDeBedeles.obtenerBedelPorNombreUsuario(reservaDTO.getNombreUsuario())); // Bedel que registró la reserva.

            // Obtenemos los períodos correspondientes a los renglones para poder mapear correctamente en la base de datos:
            List<Periodo> periodos = reservaDTO.getIdPeriodos() .stream().map(id -> {
                        Periodo periodo = reservaDAO.buscarPeriodoPorId(id);
                        if (periodo == null) {
                            throw new IllegalArgumentException("No se encontró el periodo con ID " + id);
                        }
                        return periodo;
            }).collect(Collectors.toList());

            reservaPeriodica.setPeriodos(periodos);

            // Creación de Renglones de Reserva Periódica por cada día semanal seleccionado:
            ArrayList<RenglonReservaPeriodica> renglonesPeriodicos = reservaDTO.getDiasSeleccionados().stream().map(renglonDTO -> {

                        RenglonReservaPeriodica renglon = new RenglonReservaPeriodica();
                        renglon.setDiaSemanal(renglonDTO.getDiaSemanal());
                        renglon.setHorarioDeInicio(renglonDTO.getHorarioInicio().minusHours(3)); // Al usar LocalTime, el IDE genera un desfasaje de 3 horas, con lo cual lo ajustamos restándole 3 horas.
                        renglon.setDuracion(renglonDTO.getDuracion());
                        renglon.setAula(gestorDeAulas.obtenerAulaPorId(renglonDTO.getIdAula())); // Asociamos al renglón el aula elegida en la interfaz.
                        return renglon;

            }).collect(Collectors.toCollection(ArrayList::new));

            reservaPeriodica.setRenglonReservaPeriodica(renglonesPeriodicos);

            reserva = reservaPeriodica;
        }

        // Para reservas PERIÓDICAS:

        else {

            // Creación de tipo de reserva:
            ReservaEsporadica reservaEsporadica = new ReservaEsporadica();

            // Datos de la reserva ingresados por usuario:
            reservaEsporadica.setCantidadAlumnos(reservaDTO.getCantidadAlumnos());
            reservaEsporadica.setIdDocente(reservaDTO.getIdDocente());
            reservaEsporadica.setDocente(reservaDTO.getDocente());
            reservaEsporadica.setIdCatedra(reservaDTO.getIdCatedra());
            reservaEsporadica.setNombreCatedra(reservaDTO.getNombreCatedra());
            reservaEsporadica.setCorreoElectronico(reservaDTO.getCorreoElectronico());

            // Datos extraídos del contexto:
            reservaEsporadica.setFechaReservacion(); // Fecha en que se hizo la reserva.
            reservaEsporadica.setBedel(gestorDeBedeles.obtenerBedelPorNombreUsuario(reservaDTO.getNombreUsuario())); // Bedel que registró la reserva.

            // Creación de Renglones de Reserva Esporádica por cada fecha seleccionada:
            ArrayList<RenglonReservaEsporadica> renglonesEsporadicos = reservaDTO.getFechasEsporadicas().stream().map(renglonDTO -> {

                        RenglonReservaEsporadica renglon = new RenglonReservaEsporadica();
                        renglon.setFecha(renglonDTO.getFecha());
                        renglon.setHorarioDeInicio(renglonDTO.getHorarioInicio().minusHours(3)); // Al usar LocalTime, el IDE genera un desfasaje de 3 horas, con lo cual lo ajustamos restándole 3 horas.
                        renglon.setDuracion(renglonDTO.getDuracion());
                        renglon.setAula(gestorDeAulas.obtenerAulaPorId(renglonDTO.getIdAula())); // Asociamos al renglón el aula elegida en la interfaz.
                        return renglon;

            }).collect(Collectors.toCollection(ArrayList::new));

            reservaEsporadica.setRenglonReservaEsporadica(renglonesEsporadicos);

            reserva = reservaEsporadica;
        }


        reservaDAO.save(reserva); // Persistimos la reserva en la base de datos.
    }

    // Metodo para obtener la disponibilidad de aulas (o superposición de reservas) respecto de la información proveída:
    public HashMap<String, Object> obtenerDisponibilidad(ReservaDTO reservaDTO){

        // Buscamos y filtramos las aulas habilitadas por tipo y capacidad:
        List<AulaDTO> aulasCandidatas = gestorDeAulas.buscarAulasHabilitadas(reservaDTO.getTipoDeAula(),reservaDTO.getCantidadAlumnos());

        if (aulasCandidatas.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron aulas habilitadas para ese tipo de aula y capacidad.");
        }

        // Extraemos los IDs de las aulas habilitadas para pasarlas como filtro a la consulta, independientemente del tipo de reserva:
        List<Integer> idsAulasHabilitadas = aulasCandidatas.stream().map(AulaDTO::getIdAula).toList();

        // Determinamos el tipo de aula para pasar a la consulta, independientemente del tipo de reserva (nos basamos en el primer elemento de la lista porque se supone que son todas del mismo tipo):
        String tipoDeAula = aulasCandidatas.getFirst().getTipoDeAula();
        Class<? extends Aula> claseDeAula = getClasePorTipoDeAula(tipoDeAula);

        // Declaramos un mapa donde alojaremos las aulas disponibles o las reservas superpuestas:
        HashMap<String, Object> aulasOsuperposiciones;

        // Filtramos por tipo de reserva y, luego, por renglones:
        if ("ESPORADICA".equals(reservaDTO.getTipoReserva())) {

            aulasOsuperposiciones = filtrarPorFechasEsporadicas(idsAulasHabilitadas, claseDeAula, reservaDTO.getFechasEsporadicas());

            return aulasOsuperposiciones;

        } else if ("PERIODICA".equals(reservaDTO.getTipoReserva())) {

            aulasOsuperposiciones = filtrarPorFechasPeriodicas(idsAulasHabilitadas, claseDeAula, reservaDTO.getDiasSeleccionados(), reservaDTO.getIdPeriodos());

            return aulasOsuperposiciones;

        } else { // Nunca se debería de llegar a esta opción ya que el usuario selecciona la opción 'ESPORÁDICA' o 'PERIÓDICA' en la interfaz, no la ingresa (la dejamos para test).
            throw new IllegalArgumentException("Tipo de reserva no válido.");
        }

    }

    // Metodo auxiliar para mapear el tipo de aula a su clase concreta y poder realizar correctamente la consulta SQL:
    private Class<? extends Aula> getClasePorTipoDeAula(String tipoDeAula) {
        return switch (tipoDeAula) {
            case "AulaMultimedio" -> AulaMultimedio.class;
            case "AulaInformatica" -> AulaInformatica.class;
            case "AulaSinRecursosAdicionales" -> AulaSinRecursosAdicionales.class;
            default -> throw new IllegalArgumentException("Tipo de aula desconocido: " + tipoDeAula); // Esta excepción no debería de lanzarse nunca ya que la selección de aula es mediante una lista selectora pero lo ponemos para testear (si es que accedemos mediante un endpoint).
        };
    }

    // Metodo para corroborar, dada una reserva esporádica, si existe disponibilidad de aulas:
    private HashMap<String, Object> filtrarPorFechasEsporadicas(List<Integer> idsAulasHabilitadas,Class<? extends Aula> claseDeAula,List<RenglonEsporadicaDTO> fechasEsporadicas) {

        List<List<AulaDTO>> aulasTotalesDTO = new ArrayList<>(); // Para alojar las aulas disponibles por cada renglón, en el caso de haber disponibilidad.
        List<List<Object[]>> reservasSuperpuestas = new ArrayList<>(); // Para alojar las reservas superpuestas por cada renglón, en el caso de no haber disponibilidad.

        HashMap<String, Object> resultado = new HashMap<>();

        // Filtramos aulas por cada renglón de reserva:
        for (RenglonEsporadicaDTO renglon : fechasEsporadicas) {

            // Creamos una nueva lista para cada renglón:
            List<AulaDTO> aulasDTO = new ArrayList<>();

            // Creamos los parámetros para que tomen los respectivos valores por cada renglón:
            LocalDate fechaRenglon = renglon.getFecha();
            LocalTime horaInicio = renglon.getHorarioInicio();
            int duracion = renglon.getDuracion();

            // Llamamos al DAO para obtener la disponibilidad ESPORÁDICA:
            List<? extends Aula> aulasDisponiblesPorFecha = reservaDAO.obtenerDisponibilidadEsporadica(idsAulasHabilitadas, fechaRenglon, horaInicio, duracion, claseDeAula);

            // Si hay aulas disponibles para un renglón, le damos formato a la lista de disponibilidad:
            if (!aulasDisponiblesPorFecha.isEmpty()) {

                // Ordenamos la lista según la capacidad de las aulas, de forma ascendente:
                aulasDisponiblesPorFecha.sort(Comparator.comparingInt(Aula::getCapacidad));

                // Mostramos como máximo las 3 primeras aulas:
                List<? extends Aula> aulasParaMostrar = aulasDisponiblesPorFecha.subList(0, Math.min(3, aulasDisponiblesPorFecha.size()));

                // Convertimos las entidades Aula encontradas para cada renglón a DTOs:
                for (Aula aula : aulasParaMostrar) {

                    AulaDTO aulaDTOEncontrada;

                    // Según el tipo de aula encontrada, creamos el correspondiente DTO con la información buscada en la respectiva tabla:
                    switch (aula.getClass().getSimpleName()) {
                        case "AulaMultimedio":
                            AulaMultimedio aulaMultimedio = gestorDeAulas.obtenerAulaPorIDyTipo(aula.getClass().getSimpleName(), aula.getIdAula(), AulaMultimedio.class);
                            aulaDTOEncontrada = new AulaDTO(aula.getIdAula(), aula.getPiso(), aula.getClass().getSimpleName(),aula.getCapacidad(),aula.getEstado(),aula.getAireAcondicionado(), aulaMultimedio.getVentiladores(), aulaMultimedio.getTelevisor(),aulaMultimedio.getCanion(),aulaMultimedio.getComputadora());

                            aulasDTO.add(aulaDTOEncontrada); // Agregamos todas las aulas disponibles correspondientes a cada renglón en la lista de ese renglón.
                            break;

                        case "AulaInformatica":
                            AulaInformatica aulaInformatica = gestorDeAulas.obtenerAulaPorIDyTipo(aula.getClass().getSimpleName(), aula.getIdAula(), AulaInformatica.class);
                            aulaDTOEncontrada = new AulaDTO(aula.getIdAula(), aula.getPiso(), aula.getClass().getSimpleName(),aula.getCapacidad(),aula.getEstado(),aula.getAireAcondicionado(),aulaInformatica.getCanion(),aulaInformatica.getCantidadComputadora());

                            aulasDTO.add(aulaDTOEncontrada); // Agregamos todas las aulas disponibles correspondientes a cada renglón en la lista de ese renglón.
                            break;

                        case "AulaSinRecursosAdicionales":
                            AulaSinRecursosAdicionales aulaSRA = gestorDeAulas.obtenerAulaPorIDyTipo(aula.getClass().getSimpleName(), aula.getIdAula(), AulaSinRecursosAdicionales.class);
                            aulaDTOEncontrada = new AulaDTO(aula.getIdAula(), aula.getPiso(), aula.getClass().getSimpleName(),aula.getCapacidad(),aula.getEstado(),aula.getAireAcondicionado(),aulaSRA.getVentiladores());

                            aulasDTO.add(aulaDTOEncontrada); // Agregamos todas las aulas disponibles correspondientes a cada renglón en la lista de ese renglón.
                            break;

                        default:
                            throw new IllegalArgumentException("Tipo de aula no reconocido.");
                    }

                }

                aulasTotalesDTO.add(aulasDTO); // Agregamos la lista creada correspondiente a un renglón a la lista de todos los renglones.

            } else {
                // Si no hay aulas disponibles para un renglón, procedemos a buscar la superposición con reservas existentes:

                List<Object[]> superposiciones = mostrarSuperposicionesParaEsporadica(idsAulasHabilitadas, fechaRenglon, horaInicio, duracion);

                reservasSuperpuestas.add(superposiciones); // Agregamos todas las superposiciones correspondientes a un renglón a la lista de superposiciones totales para todos los renglones.

            }

        }

        // Generamos los pares clave-valor del map:
        resultado.put("aulasTotalesDTO", aulasTotalesDTO);
        resultado.put("reservasSuperpuestas", reservasSuperpuestas);
        return resultado;

    }

    // Metodo para corroborar, dada una reserva periódica, si existe disponibilidad de aulas:
    private HashMap<String, Object> filtrarPorFechasPeriodicas(List<Integer> idsAulasHabilitadas, Class<? extends Aula> claseDeAula, List<RenglonPeriodicaDTO> diasSeleccionados, List<Integer> IDsperiodos) {

        List<List<AulaDTO>> aulasTotalesDTO = new ArrayList<>(); // Para alojar las aulas disponibles por cada renglón, en el caso de haber disponibilidad.
        List<List<Object[]>> reservasSuperpuestas = new ArrayList<>(); // Para alojar las reservas superpuestas por cada renglón, en el caso de no haber disponibilidad.

        HashMap<String, Object> resultado = new HashMap<>();

        // Filtramos aulas por cada renglón de reserva:
        for (RenglonPeriodicaDTO renglon : diasSeleccionados) {

            // Creamos una nueva lista para cada renglón:
            List<AulaDTO> aulasDTO = new ArrayList<>();

            // Creamos los parámetros para que tomen los respectivos valores por cada renglón:
            DiaDeSemana diaSemanal = renglon.getDiaSemanal();
            LocalTime horaInicio = renglon.getHorarioInicio();
            int duracion = renglon.getDuracion();


            // Llamamos al DAO para obtener la disponibilidad PERIÓDICA (según el período):
            List<? extends Aula> aulasPorPeriodo = reservaDAO.obtenerDisponibilidadPeriodica(idsAulasHabilitadas, IDsperiodos, diaSemanal, horaInicio, duracion, claseDeAula);

            // Si hay aulas disponibles para un renglón, le damos formato a la lista de disponibilidad:
            if(!aulasPorPeriodo.isEmpty()){

                // Ordenamos la lista según la capacidad de las aulas, de forma ascendente:
                aulasPorPeriodo.sort(Comparator.comparingInt(Aula::getCapacidad));

                // Mostramos como máximo las 3 primeras aulas:
                List<? extends Aula> aulasParaMostrar = aulasPorPeriodo.subList(0, Math.min(3, aulasPorPeriodo.size()));

                // Convertimos las entidades Aula encontradas para cada renglón a DTOs:
                for (Aula aula : aulasParaMostrar) {
                    AulaDTO aulaDTOEncontrada;

                    // Según el tipo de aula encontrada, creamos el correspondiente DTO con la información buscada en la respectiva tabla:
                    switch (aula.getClass().getSimpleName()) {
                        case "AulaMultimedio":
                            AulaMultimedio aulaMultimedio = gestorDeAulas.obtenerAulaPorIDyTipo(aula.getClass().getSimpleName(), aula.getIdAula(), AulaMultimedio.class);
                            aulaDTOEncontrada = new AulaDTO(aula.getIdAula(), aula.getPiso(), aula.getClass().getSimpleName(),aula.getCapacidad(),aula.getEstado(),aula.getAireAcondicionado(), aulaMultimedio.getVentiladores(), aulaMultimedio.getTelevisor(),aulaMultimedio.getCanion(),aulaMultimedio.getComputadora());

                            aulasDTO.add(aulaDTOEncontrada); // Agregamos todas las aulas disponibles correspondientes a cada renglón en la lista de ese renglón.
                            break;

                        case "AulaInformatica":
                            AulaInformatica aulaInformatica = gestorDeAulas.obtenerAulaPorIDyTipo(aula.getClass().getSimpleName(), aula.getIdAula(), AulaInformatica.class);
                            aulaDTOEncontrada = new AulaDTO(aula.getIdAula(), aula.getPiso(), aula.getClass().getSimpleName(),aula.getCapacidad(),aula.getEstado(),aula.getAireAcondicionado(),aulaInformatica.getCanion(),aulaInformatica.getCantidadComputadora());

                            aulasDTO.add(aulaDTOEncontrada); // Agregamos todas las aulas disponibles correspondientes a cada renglón en la lista de ese renglón.
                            break;

                        case "AulaSinRecursosAdicionales":
                            AulaSinRecursosAdicionales aulaSRA = gestorDeAulas.obtenerAulaPorIDyTipo(aula.getClass().getSimpleName(), aula.getIdAula(), AulaSinRecursosAdicionales.class);
                            aulaDTOEncontrada = new AulaDTO(aula.getIdAula(), aula.getPiso(), aula.getClass().getSimpleName(),aula.getCapacidad(),aula.getEstado(),aula.getAireAcondicionado(),aulaSRA.getVentiladores());

                            aulasDTO.add(aulaDTOEncontrada); // Agregamos todas las aulas disponibles correspondientes a cada renglón en la lista de ese renglón.
                            break;

                        default:
                            throw new IllegalArgumentException("Tipo de aula no reconocido.");
                    }
                }

                aulasTotalesDTO.add(aulasDTO); // Agregamos la lista creada correspondiente al renglón a la lista de todos los renglones.

            } else {
                // Si no hay aulas disponibles para un renglón, procedemos a buscar la superposición con reservas existentes:

                List<Object[]> superposiciones = mostrarSuperposicionesParaPeriodica(idsAulasHabilitadas, diaSemanal, horaInicio, duracion);

                reservasSuperpuestas.add(superposiciones); // Agregamos todas las superposiciones correspondientes a un renglón a la lista de superposiciones totales para todos los renglones.

            }

        }

        // Generamos los pares clave-valor del map:
        resultado.put("aulasTotalesDTO", aulasTotalesDTO);
        resultado.put("reservasSuperpuestas", reservasSuperpuestas);
        return resultado;

    }

    // Metodo para obtener superposiciones de una reserva ESPORÁDICA con cualquier tipo de reserva:
    public List<Object[]> mostrarSuperposicionesParaEsporadica(List<Integer> idsAulas, LocalDate fechaRenglon, LocalTime horaInicio, int duracion) {

        LocalTime horaFin = horaInicio.plusMinutes(duracion);

        // Obtenemos las listas de renglones (de ambos tipos de reservas existentes) que se superponen:
        List<RenglonReservaEsporadica> superposicionesEsporadicas = reservaDAO.obtenerSuperposicionesEsporadicaEsporadicas(idsAulas, fechaRenglon, horaInicio, horaFin);
        List<RenglonReservaPeriodica> superposicionesPeriodicas = reservaDAO.obtenerSuperposicionesEsporadicaPeriodicas(idsAulas, fechaRenglon, horaInicio, horaFin);

        // Creamos una lista combinada para todas las superposiciones:
        List<Object[]> todasSuperposiciones = new ArrayList<>();

        for (RenglonReservaEsporadica rre : superposicionesEsporadicas) {

            // Calculamos el tiempo de superposición del renglón de la nueva reserva con renglones de reservas esporádicas existentes:
            LocalTime inicioSuperpuesto = rre.getHorarioDeInicio().isAfter(horaInicio) ? rre.getHorarioDeInicio() : horaInicio;
            LocalTime finSuperpuesto = rre.getHorarioDeInicio().plusMinutes(rre.getDuracion()).isBefore(horaFin) ? rre.getHorarioDeInicio().plusMinutes(rre.getDuracion()) : horaFin;
            long tiempoSuperpuesto = Duration.between(inicioSuperpuesto, finSuperpuesto).toMinutes();
            System.out.println("TIEMPO SUPERPUESTO: " + tiempoSuperpuesto);

            // Por cada superposición, creamos la respectiva ReservaDTO con el correspondiente renglón superpuesto y toda la información necesaria:

            ReservaDTO datosReserva = new ReservaDTO();
            RenglonEsporadicaDTO datosRenglon = new RenglonEsporadicaDTO();

            datosRenglon.setFecha(rre.getFecha());
            datosRenglon.setHorarioInicio(rre.getHorarioDeInicio());
            datosRenglon.setHorarioFin(rre.getHorarioDeInicio().plusMinutes(rre.getDuracion()));
            datosRenglon.setDuracion(rre.getDuracion());
            datosRenglon.setNombreAula(rre.getAula().getNombreAula());

            List<RenglonEsporadicaDTO> renglon = new ArrayList<>();
            renglon.add(datosRenglon);

            // Buscamos la reserva correspondiente al renglón que se superpone para poder mostrar la información asociada:
            int idReserva = reservaDAO.obtenerIdReservaPorIdRenglonEsporadico(rre.getIdRenglonReservaEsporadica());
            Map<String, Object> reserva = reservaDAO.obtenerReservaPorId(idReserva);

            datosReserva.setFechasEsporadicas(renglon);
            datosReserva.setTipoReserva("ESPORADICA");
            datosReserva.setDocente(reserva.get("docente").toString());
            datosReserva.setCorreoElectronico(reserva.get("correoElectronico").toString());
            datosReserva.setNombreCatedra(reserva.get("nombreCatedra").toString());

            todasSuperposiciones.add(new Object[]{datosReserva, tiempoSuperpuesto});
        }

        for (RenglonReservaPeriodica rrp : superposicionesPeriodicas) {

            // Calculamos el tiempo de superposición del renglón de la nueva reserva con renglones de reservas periódicas existentes:
            LocalTime inicioSuperpuesto = rrp.getHorarioDeInicio().isAfter(horaInicio) ? rrp.getHorarioDeInicio() : horaInicio;
            LocalTime finSuperpuesto = rrp.getHorarioDeInicio().plusMinutes(rrp.getDuracion()).isBefore(horaFin) ? rrp.getHorarioDeInicio().plusMinutes(rrp.getDuracion()) : horaFin;
            long tiempoSuperpuesto = Duration.between(inicioSuperpuesto, finSuperpuesto).toMinutes();

            // Por cada superposición, creamos la respectiva ReservaDTO con el correspondiente renglón superpuesto y toda la información necesaria:

            ReservaDTO datosReserva = new ReservaDTO();
            RenglonPeriodicaDTO datosRenglon = new RenglonPeriodicaDTO();

            datosRenglon.setDiaSemanal(rrp.getDiaSemanal());
            datosRenglon.setHorarioInicio(rrp.getHorarioDeInicio());
            datosRenglon.setHorarioFin(rrp.getHorarioDeInicio().plusMinutes(rrp.getDuracion()));
            datosRenglon.setDuracion(rrp.getDuracion());
            datosRenglon.setNombreAula(rrp.getAula().getNombreAula());

            List<RenglonPeriodicaDTO> renglon = new ArrayList<>();
            renglon.add(datosRenglon);

            // Buscamos la reserva correspondiente al renglón que se superpone para poder mostrar la información asociada:
            int idReserva = reservaDAO.obtenerIdReservaPorIdRenglonPeriodico(rrp.getIdRenglonReservaPeriodica());
            Map<String, Object> reserva = reservaDAO.obtenerReservaPorId(idReserva);

            datosReserva.setDiasSeleccionados(renglon);
            datosReserva.setTipoReserva("PERIODICA");
            datosReserva.setDocente(reserva.get("docente").toString());
            datosReserva.setCorreoElectronico(reserva.get("correoElectronico").toString());
            datosReserva.setNombreCatedra(reserva.get("nombreCatedra").toString());

            todasSuperposiciones.add(new Object[]{datosReserva, tiempoSuperpuesto});
        }

        // Ordenamos por tiempo de superposición (de menor a mayor cantidad de horas superpuestas):
        todasSuperposiciones.sort(Comparator.comparingLong(o -> (long) o[1]));

        // Filtramos para incluir únicamente el/los elemento/s con el menor tiempo superpuesto:
        if (!todasSuperposiciones.isEmpty()) {
            long menorTiempoSuperpuesto = (long) todasSuperposiciones.getFirst()[1];
            todasSuperposiciones = todasSuperposiciones.stream().filter(obj -> (long) obj[1] == menorTiempoSuperpuesto).collect(Collectors.toList());
        }



        return todasSuperposiciones;

    }

    // Metodo para obtener superposiciones de una reserva PERIÓDICA con cualquier tipo de reserva:
    public List<Object[]> mostrarSuperposicionesParaPeriodica(List<Integer> idsAulas, DiaDeSemana diaSemanal, LocalTime horaInicio, int duracion) {

        LocalTime horaFin = horaInicio.plusMinutes(duracion);

        // Obtenemos las listas de renglones (de ambos tipos de reservas existentes) que se superponen:
        List<RenglonReservaEsporadica> superposicionesEsporadicas = reservaDAO.obtenerSuperposicionesPeriodicaEsporadicas(idsAulas, diaSemanal, horaInicio, horaFin);
        List<RenglonReservaPeriodica> superposicionesPeriodicas = reservaDAO.obtenerSuperposicionesPeriodicaPeriodicas(idsAulas, diaSemanal, horaInicio, horaFin);

        // Creamos una lista combinada para todas las superposiciones:
        List<Object[]> todasSuperposiciones = new ArrayList<>();

        for (RenglonReservaEsporadica rre : superposicionesEsporadicas) {

            // Calculamos el tiempo de superposición del renglón de la nueva reserva con renglones de reservas esporádicas existentes:
            LocalTime inicioSuperpuesto = rre.getHorarioDeInicio().isAfter(horaInicio) ? rre.getHorarioDeInicio() : horaInicio;
            LocalTime finSuperpuesto = rre.getHorarioDeInicio().plusMinutes(rre.getDuracion()).isBefore(horaFin) ? rre.getHorarioDeInicio().plusMinutes(rre.getDuracion()) : horaFin;
            long tiempoSuperpuesto = Duration.between(inicioSuperpuesto, finSuperpuesto).toMinutes();

            // Por cada superposición, creamos la respectiva ReservaDTO con el correspondiente renglón superpuesto y toda la información necesaria:

            ReservaDTO datosReserva = new ReservaDTO();
            RenglonEsporadicaDTO datosRenglon = new RenglonEsporadicaDTO();

            datosRenglon.setFecha(rre.getFecha());
            datosRenglon.setHorarioInicio(rre.getHorarioDeInicio());
            datosRenglon.setHorarioFin(rre.getHorarioDeInicio().plusMinutes(rre.getDuracion()));
            datosRenglon.setDuracion(rre.getDuracion());
            datosRenglon.setNombreAula(rre.getAula().getNombreAula());

            List<RenglonEsporadicaDTO> renglon = new ArrayList<>();
            renglon.add(datosRenglon);

            // Buscamos la reserva correspondiente al renglón que se superpone para poder mostrar la información asociada:
            int idReserva = reservaDAO.obtenerIdReservaPorIdRenglonEsporadico(rre.getIdRenglonReservaEsporadica());
            Map<String, Object> reserva = reservaDAO.obtenerReservaPorId(idReserva);

            datosReserva.setFechasEsporadicas(renglon);
            datosReserva.setTipoReserva("ESPORADICA");
            datosReserva.setDocente(reserva.get("docente").toString());
            datosReserva.setCorreoElectronico(reserva.get("correoElectronico").toString());
            datosReserva.setNombreCatedra(reserva.get("nombreCatedra").toString());

            todasSuperposiciones.add(new Object[]{datosReserva, tiempoSuperpuesto});
        }

        for (RenglonReservaPeriodica rrp : superposicionesPeriodicas) {

            // Calculamos el tiempo de superposición del renglón de la nueva reserva con renglones de reservas periódicas existentes:
            LocalTime inicioSuperpuesto = rrp.getHorarioDeInicio().isAfter(horaInicio) ? rrp.getHorarioDeInicio() : horaInicio;
            LocalTime finSuperpuesto = rrp.getHorarioDeInicio().plusMinutes(rrp.getDuracion()).isBefore(horaFin) ? rrp.getHorarioDeInicio().plusMinutes(rrp.getDuracion()) : horaFin;
            long tiempoSuperpuesto = Duration.between(inicioSuperpuesto, finSuperpuesto).toMinutes();

            // Por cada superposición, creamos la respectiva ReservaDTO con el correspondiente renglón superpuesto y toda la información necesaria:

            ReservaDTO datosReserva = new ReservaDTO();
            RenglonPeriodicaDTO datosRenglon = new RenglonPeriodicaDTO();

            datosRenglon.setDiaSemanal(rrp.getDiaSemanal());
            datosRenglon.setHorarioInicio(rrp.getHorarioDeInicio());
            datosRenglon.setHorarioFin(rrp.getHorarioDeInicio().plusMinutes(rrp.getDuracion()));
            datosRenglon.setDuracion(rrp.getDuracion());
            datosRenglon.setNombreAula(rrp.getAula().getNombreAula());

            List<RenglonPeriodicaDTO> renglon = new ArrayList<>();
            renglon.add(datosRenglon);

            // Buscamos la reserva correspondiente al renglón que se superpone para poder mostrar la información asociada:
            int idReserva = reservaDAO.obtenerIdReservaPorIdRenglonPeriodico(rrp.getIdRenglonReservaPeriodica());
            Map<String, Object> reserva = reservaDAO.obtenerReservaPorId(idReserva);

            datosReserva.setDiasSeleccionados(renglon);
            datosReserva.setTipoReserva("PERIODICA");
            datosReserva.setDocente(reserva.get("docente").toString());
            datosReserva.setCorreoElectronico(reserva.get("correoElectronico").toString());
            datosReserva.setNombreCatedra(reserva.get("nombreCatedra").toString());

            todasSuperposiciones.add(new Object[]{datosReserva, tiempoSuperpuesto});
        }

        // Ordenamos por tiempo de superposición (de menor a mayor cantidad de horas superpuestas):
        todasSuperposiciones.sort(Comparator.comparingLong(o -> (long) o[1]));

        // Filtramos para incluir únicamente el/los elemento/s con el menor tiempo superpuesto:
        if (!todasSuperposiciones.isEmpty()) {
            long menorTiempoSuperpuesto = (long) todasSuperposiciones.getFirst()[1];
            todasSuperposiciones = todasSuperposiciones.stream().filter(obj -> (long) obj[1] == menorTiempoSuperpuesto).collect(Collectors.toList());
        }

        return todasSuperposiciones;

    }

}