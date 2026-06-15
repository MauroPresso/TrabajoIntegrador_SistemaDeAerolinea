package servicio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import comparadores.ComparadorVueloPorDestino;
import comparadores.ComparadorVueloPorNumero;
import excepciones.VueloNoDisponibleException;
import modelo.Pasajero;
import modelo.Persona;
import modelo.Vuelo;

/**
 * @class Aerolinea
 * @brief Clase de servicio principal para gestionar vuelos, pasajeros y tripulantes.
 *
 * Esta clase concentra el uso principal del Java Collection Framework:
 *
 * - ArrayList<Vuelo> para almacenar los vuelos de la aerolínea.
 * - HashMap<Integer, Persona> para indexar personas por DNI.
 * - HashSet<Persona> para evitar duplicados entre pasajeros con reservas activas.
 * - Collections.sort() para ordenar personas y vuelos.
 */
public class Aerolinea {

    /** Nombre comercial de la aerolínea. */
    private String nombre;

    /** Lista de vuelos de la aerolínea. */
    private ArrayList<Vuelo> vuelos;

    /** Mapa de personas indexadas por DNI. */
    private HashMap<Integer, Persona> personasPorDni;

    /** Conjunto de pasajeros con al menos una reserva activa. */
    private HashSet<Persona> pasajerosConReservaActiva;

    /**
     * @brief Constructor de Aerolinea.
     *
     * @param nombre Nombre de la aerolínea.
     */
    public Aerolinea(String nombre) {
        this.nombre = nombre;
        this.vuelos = new ArrayList<>();
        this.personasPorDni = new HashMap<>();
        this.pasajerosConReservaActiva = new HashSet<>();
    }

    /**
     * @brief Agrega un vuelo a la aerolínea.
     *
     * @param vuelo Vuelo a agregar.
     */
    public void agregarVuelo(Vuelo vuelo) {
        vuelos.add(vuelo);
    }

    /**
     * @brief Registra una persona en el sistema usando su DNI como clave.
     *
     * Se utiliza HashMap<Integer, Persona> para acceder rápidamente
     * a pasajeros o tripulantes a partir de su DNI.
     *
     * @param persona Persona a registrar.
     * @return true si se registró correctamente, false si el DNI ya existía.
     */
    public boolean registrarPersona(Persona persona) {
        if (personasPorDni.containsKey(persona.getDni())) {
            return false;
        }

        personasPorDni.put(persona.getDni(), persona);
        return true;
    }

    /**
     * @brief Busca una persona por DNI.
     *
     * @param dni DNI buscado.
     * @return Persona encontrada o null si no existe.
     */
    public Persona buscarPersonaPorDni(int dni) {
        return personasPorDni.get(dni);
    }

    /**
     * @brief Busca un vuelo por número.
     *
     * @param numeroVuelo Número del vuelo.
     * @return Vuelo encontrado o null si no existe.
     */
    public Vuelo buscarVueloPorNumero(int numeroVuelo) {
        for (Vuelo vuelo : vuelos) {
            if (vuelo.getNumeroVuelo() == numeroVuelo) {
                return vuelo;
            }
        }

        return null;
    }

    /**
     * @brief Reserva un vuelo para un pasajero.
     *
     * Si la reserva se realiza correctamente, el pasajero se agrega al
     * HashSet de pasajeros con reserva activa. Al ser HashSet, se evitan
     * duplicados automáticamente gracias a equals() y hashCode() de Persona.
     *
     * @param dniPasajero DNI del pasajero.
     * @param numeroVuelo Número del vuelo.
     * @throws VueloNoDisponibleException Si el vuelo no está disponible.
     */
    public void reservarVuelo(int dniPasajero, int numeroVuelo) throws VueloNoDisponibleException {
        Persona persona = personasPorDni.get(dniPasajero);

        if (persona == null) {
            System.out.println("No existe una persona registrada con DNI " + dniPasajero);
            return;
        }

        if (!(persona instanceof Pasajero)) {
            System.out.println("La persona con DNI " + dniPasajero + " no es pasajero.");
            return;
        }

        Vuelo vuelo = buscarVueloPorNumero(numeroVuelo);

        if (vuelo == null) {
            System.out.println("No existe el vuelo número " + numeroVuelo);
            return;
        }

        Pasajero pasajero = (Pasajero) persona;
        pasajero.reservarVuelo(vuelo);

        pasajerosConReservaActiva.add(pasajero);
    }

    /**
     * @brief Cancela una reserva de un pasajero.
     *
     * Si luego de cancelar el pasajero no posee más reservas activas,
     * se lo elimina del HashSet correspondiente.
     *
     * @param dniPasajero DNI del pasajero.
     * @param numeroVuelo Número del vuelo.
     */
    public void cancelarReserva(int dniPasajero, int numeroVuelo) {
        Persona persona = personasPorDni.get(dniPasajero);

        if (!(persona instanceof Pasajero)) {
            System.out.println("No existe un pasajero con DNI " + dniPasajero);
            return;
        }

        Vuelo vuelo = buscarVueloPorNumero(numeroVuelo);

        if (vuelo == null) {
            System.out.println("No existe el vuelo número " + numeroVuelo);
            return;
        }

        Pasajero pasajero = (Pasajero) persona;
        boolean cancelada = pasajero.cancelarReserva(vuelo);

        if (cancelada) {
            System.out.println("Reserva cancelada correctamente.");

            if (!pasajero.tieneReservasActivas()) {
                pasajerosConReservaActiva.remove(pasajero);
            }
        } else {
            System.out.println("El pasajero no tenía reservado ese vuelo.");
        }
    }

    /**
     * @brief Obtiene una copia de la lista de vuelos.
     *
     * @return Copia de los vuelos.
     */
    public ArrayList<Vuelo> getVuelos() {
        return new ArrayList<>(vuelos);
    }

    /**
     * @brief Obtiene una copia del mapa de personas.
     *
     * @return Copia del HashMap de personas.
     */
    public HashMap<Integer, Persona> getPersonasPorDni() {
        return new HashMap<>(personasPorDni);
    }

    /**
     * @brief Obtiene una copia del conjunto de pasajeros con reserva activa.
     *
     * @return Copia del HashSet de pasajeros con reserva activa.
     */
    public HashSet<Persona> getPasajerosConReservaActiva() {
        return new HashSet<>(pasajerosConReservaActiva);
    }

    /**
     * @brief Devuelve las personas ordenadas por apellido.
     *
     * Utiliza Collections.sort() y el Comparable implementado en Persona.
     *
     * @return Lista de personas ordenadas.
     */
    public ArrayList<Persona> obtenerPersonasOrdenadasPorApellido() {
        ArrayList<Persona> personas = new ArrayList<>(personasPorDni.values());
        Collections.sort(personas);
        return personas;
    }

    /**
     * @brief Devuelve los vuelos ordenados por número usando Comparable.
     *
     * Utiliza Collections.sort(lista) y el compareTo() de Vuelo.
     *
     * @return Lista de vuelos ordenados por número.
     */
    public ArrayList<Vuelo> obtenerVuelosOrdenadosPorNumeroComparable() {
        ArrayList<Vuelo> vuelosOrdenados = new ArrayList<>(vuelos);
        Collections.sort(vuelosOrdenados);
        return vuelosOrdenados;
    }

    /**
     * @brief Devuelve los vuelos ordenados por destino usando Comparator externo.
     *
     * @return Lista de vuelos ordenados por destino.
     */
    public ArrayList<Vuelo> obtenerVuelosOrdenadosPorDestino() {
        ArrayList<Vuelo> vuelosOrdenados = new ArrayList<>(vuelos);
        Collections.sort(vuelosOrdenados, new ComparadorVueloPorDestino());
        return vuelosOrdenados;
    }

    /**
     * @brief Devuelve los vuelos ordenados por número usando Comparator externo.
     *
     * @return Lista de vuelos ordenados por número.
     */
    public ArrayList<Vuelo> obtenerVuelosOrdenadosPorNumeroComparator() {
        ArrayList<Vuelo> vuelosOrdenados = new ArrayList<>(vuelos);
        Collections.sort(vuelosOrdenados, new ComparadorVueloPorNumero());
        return vuelosOrdenados;
    }

    /**
     * @brief Muestra todos los vuelos de la aerolínea.
     */
    public void mostrarVuelos() {
        System.out.println("Vuelos de " + nombre + ":");

        for (Vuelo vuelo : vuelos) {
            vuelo.mostrarInfo();
        }
    }

    /**
     * @brief Muestra pasajeros con al menos una reserva activa.
     *
     * Usa el HashSet<Persona> pasajerosConReservaActiva.
     */
    public void mostrarPasajerosConReservaActiva() {
        System.out.println("Pasajeros con reserva activa:");

        if (pasajerosConReservaActiva.isEmpty()) {
            System.out.println("No hay pasajeros con reservas activas.");
            return;
        }

        for (Persona persona : pasajerosConReservaActiva) {
            System.out.println(persona);
        }
    }

    /**
     * @brief Demuestra polimorfismo recorriendo una lista de vuelos.
     *
     * Recorre ArrayList<Vuelo> y llama a embarcar() y mostrarInfo()
     * sin conocer el tipo concreto del vuelo.
     */
    public void operarVuelosPolimorficamente() {
        for (Vuelo vuelo : vuelos) {
            vuelo.embarcar();
            vuelo.mostrarInfo();
        }
    }
}