import java.util.ArrayList;
import java.util.HashSet;

import excepciones.VueloNoDisponibleException;
import modelo.Pasajero;
import modelo.Persona;
import modelo.Tripulante;
import modelo.Vuelo;
import modelo.VueloCharter;
import modelo.VueloInternacional;
import modelo.VueloNacional;
import servicio.Aerolinea;

/**
 * @class Main
 * @brief Clase principal utilizada para probar el uso del Java Collection Framework.
 *
 * Esta clase ejecuta pruebas sobre:
 *
 * - ArrayList<Vuelo> para vuelos de la aerolínea.
 * - ArrayList<Vuelo> para vuelos reservados por pasajeros.
 * - HashMap<Integer, Persona> para gestionar personas por DNI.
 * - HashSet<Persona> para evitar pasajeros duplicados con reserva activa.
 * - Collections.sort() con Comparable.
 * - Collections.sort() con Comparator externo.
 */
public class Main {

    /**
     * @brief Punto de entrada del programa.
     *
     * @param args Argumentos de consola no utilizados.
     */
    public static void main(String[] args) {
        Aerolinea aerolinea = new Aerolinea("Aerolínea IFES");

        cargarVuelosDePrueba(aerolinea);
        cargarPersonasDePrueba(aerolinea);

        System.out.println("\n========================================");
        System.out.println("1) ARRAYLIST<Vuelo> - VUELOS CARGADOS");
        System.out.println("========================================");
        aerolinea.mostrarVuelos();

        System.out.println("\n========================================");
        System.out.println("2) HASHMAP<Integer, Persona> - BUSQUEDA POR DNI");
        System.out.println("========================================");
        probarBusquedaPorDni(aerolinea);

        System.out.println("\n========================================");
        System.out.println("3) REGISTRO DUPLICADO EN HASHMAP");
        System.out.println("========================================");
        probarRegistroDuplicado(aerolinea);

        System.out.println("\n========================================");
        System.out.println("4) RESERVAS Y HASHSET<Persona>");
        System.out.println("========================================");
        probarReservas(aerolinea);

        System.out.println("\n========================================");
        System.out.println("5) ARRAYLIST<Vuelo> EN PASAJERO");
        System.out.println("========================================");
        probarVuelosReservadosPorPasajero(aerolinea);

        System.out.println("\n========================================");
        System.out.println("6) Collections.sort() CON Comparable<Persona>");
        System.out.println("========================================");
        mostrarPersonas(aerolinea.obtenerPersonasOrdenadasPorApellido());

        System.out.println("\n========================================");
        System.out.println("7) Collections.sort() CON Comparable<Vuelo>");
        System.out.println("========================================");
        mostrarVuelos(aerolinea.obtenerVuelosOrdenadosPorNumeroComparable());

        System.out.println("\n========================================");
        System.out.println("8) Collections.sort() CON Comparator POR DESTINO");
        System.out.println("========================================");
        mostrarVuelos(aerolinea.obtenerVuelosOrdenadosPorDestino());

        System.out.println("\n========================================");
        System.out.println("9) Collections.sort() CON Comparator POR NUMERO");
        System.out.println("========================================");
        mostrarVuelos(aerolinea.obtenerVuelosOrdenadosPorNumeroComparator());

        System.out.println("\n========================================");
        System.out.println("FIN DE PRUEBAS");
        System.out.println("========================================");
    }

    /**
     * @brief Carga vuelos de prueba en la aerolínea.
     *
     * @param aerolinea Aerolínea sobre la cual se cargan los vuelos.
     */
    private static void cargarVuelosDePrueba(Aerolinea aerolinea) {
        aerolinea.agregarVuelo(new VueloNacional(
                "AR300",
                "Neuquén",
                "Buenos Aires",
                "2026-06-20",
                2,
                "Buenos Aires"
        ));

        aerolinea.agregarVuelo(new VueloInternacional(
                "AR100",
                "Buenos Aires",
                "Madrid",
                "2026-06-21",
                2,
                "España",
                true
        ));

        aerolinea.agregarVuelo(new VueloCharter(
                "CH200",
                "Neuquén",
                "Bariloche",
                "2026-06-22",
                1,
                "IFES",
                2500000
        ));

        aerolinea.agregarVuelo(new VueloNacional(
                "AR150",
                "Córdoba",
                "Mendoza",
                "2026-06-23",
                3,
                "Mendoza"
        ));
    }

    /**
     * @brief Carga pasajeros y tripulantes de prueba en la aerolínea.
     *
     * @param aerolinea Aerolínea sobre la cual se registran las personas.
     */
    private static void cargarPersonasDePrueba(Aerolinea aerolinea) {
        aerolinea.registrarPersona(new Pasajero(
                11111111,
                "Mauro",
                "Presso",
                "A123456"
        ));

        aerolinea.registrarPersona(new Pasajero(
                22222222,
                "Agustín",
                "Almirón",
                ""
        ));

        aerolinea.registrarPersona(new Pasajero(
                33333333,
                "Juan Cruz",
                "Barria",
                ""
        ));

        aerolinea.registrarPersona(new Tripulante(
                44444444,
                "Carlos",
                "Gómez",
                1001,
                "Piloto"
        ));

        aerolinea.registrarPersona(new Tripulante(
                55555555,
                "Ana",
                "Zapata",
                1002,
                "Tripulante de cabina"
        ));
    }

    /**
     * @brief Prueba la búsqueda de personas mediante HashMap<Integer, Persona>.
     *
     * @param aerolinea Aerolínea usada para la prueba.
     */
    private static void probarBusquedaPorDni(Aerolinea aerolinea) {
        Persona personaEncontrada = aerolinea.buscarPersonaPorDni(11111111);

        if (personaEncontrada != null) {
            System.out.println("Persona encontrada por DNI:");
            personaEncontrada.mostrarInfo();
        } else {
            System.out.println("No se encontró la persona.");
        }
    }

    /**
     * @brief Prueba que el HashMap no permita registrar dos personas con el mismo DNI.
     *
     * @param aerolinea Aerolínea usada para la prueba.
     */
    private static void probarRegistroDuplicado(Aerolinea aerolinea) {
        boolean registrado = aerolinea.registrarPersona(new Pasajero(
                11111111,
                "Mauro Duplicado",
                "Presso",
                "DUP123"
        ));

        if (registrado) {
            System.out.println("La persona duplicada fue registrada.");
        } else {
            System.out.println("No se registró la persona duplicada porque el DNI ya existe.");
        }
    }

    /**
     * @brief Prueba reservas de vuelos y uso de HashSet<Persona>.
     *
     * El mismo pasajero reserva más de un vuelo, pero debe aparecer una sola vez
     * en el conjunto de pasajeros con reserva activa.
     *
     * @param aerolinea Aerolínea usada para la prueba.
     */
    private static void probarReservas(Aerolinea aerolinea) {
        try {
            aerolinea.reservarVuelo(11111111, "AR300");
            aerolinea.reservarVuelo(11111111, "AR100");
            aerolinea.reservarVuelo(22222222, "CH200");

            /*
             * Esta reserva debería lanzar VueloNoDisponibleException,
             * porque el vuelo CH200 tiene capacidad 1 y ya fue reservado.
             */
            aerolinea.reservarVuelo(33333333, "CH200");

        } catch (VueloNoDisponibleException e) {
            System.out.println("No se pudo realizar una reserva: " + e.getMessage());
        } finally {
            System.out.println("Bloque finally ejecutado luego de intentar reservar vuelos.");
        }

        aerolinea.mostrarPasajerosConReservaActiva();

        HashSet<Persona> pasajerosConReserva = aerolinea.getPasajerosConReservaActiva();
        System.out.println("Cantidad de pasajeros únicos con reserva activa: " + pasajerosConReserva.size());
    }

    /**
     * @brief Prueba que cada pasajero tenga su propio ArrayList<Vuelo> de reservas.
     *
     * @param aerolinea Aerolínea usada para la prueba.
     */
    private static void probarVuelosReservadosPorPasajero(Aerolinea aerolinea) {
        Persona persona = aerolinea.buscarPersonaPorDni(11111111);

        if (persona instanceof Pasajero) {
            Pasajero pasajero = (Pasajero) persona;
            pasajero.mostrarReservas();
        } else {
            System.out.println("La persona buscada no es un pasajero.");
        }
    }

    /**
     * @brief Muestra una lista de personas.
     *
     * @param personas Lista de personas a mostrar.
     */
    private static void mostrarPersonas(ArrayList<Persona> personas) {
        for (Persona persona : personas) {
            persona.mostrarInfo();
        }
    }

    /**
     * @brief Muestra una lista de vuelos.
     *
     * @param vuelos Lista de vuelos a mostrar.
     */
    private static void mostrarVuelos(ArrayList<Vuelo> vuelos) {
        for (Vuelo vuelo : vuelos) {
            vuelo.mostrarInfo();
        }
    }
}