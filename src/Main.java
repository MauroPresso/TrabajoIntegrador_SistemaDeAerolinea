import excepciones.VueloNoDisponibleException;
import modelo.Pasajero;
import modelo.Vuelo;
import modelo.VueloCharter;
import modelo.VueloInternacional;
import modelo.VueloNacional;
import servicio.Aerolinea;

import java.util.List;

/**
 * @class Main
 * @brief Punto de entrada temporal para probar programación funcional con Streams y Lambdas.
 *
 * Esta clase ejecuta una prueba simple del sistema de aerolínea,
 * incluyendo búsqueda, filtrado, ordenamiento y cálculo de totales
 * mediante Streams, Lambdas y referencias a método.
 */
public class Main {

    /**
     * @brief Ejecuta pruebas de programación funcional aplicadas a vuelos.
     *
     * @param args Argumentos de consola no utilizados.
     */
    public static void main(String[] args) {
        Aerolinea aerolinea = new Aerolinea("Aerolínea IFES");

        Vuelo vueloNacional = new VueloNacional(
                "AR100",
                "Neuquén",
                "Buenos Aires",
                "2026-06-20",
                180,
                "Buenos Aires"
        );

        Vuelo vueloInternacional = new VueloInternacional(
                "AR200",
                "Buenos Aires",
                "Santiago",
                "2026-06-21",
                160,
                "Chile",
                true
        );

        Vuelo vueloCharter = new VueloCharter(
                "CH300",
                "Neuquén",
                "Mendoza",
                "2026-06-22",
                80,
                "IFES",
                2500000
        );

        aerolinea.agregarVuelo(vueloNacional);
        aerolinea.agregarVuelo(vueloInternacional);
        aerolinea.agregarVuelo(vueloCharter);

        Pasajero pasajero = new Pasajero(
                12345678,
                "Mauro",
                "Presso",
                "A123456"
        );

        try {
            vueloNacional.reservarPasajero(pasajero);
        } catch (VueloNoDisponibleException e) {
            System.out.println("No se pudo reservar: " + e.getMessage());
        }

        System.out.println("========================================");
        System.out.println("1) BUSCAR VUELO POR NUMERO CON STREAM");
        System.out.println("========================================");

        Vuelo vueloBuscado = aerolinea.buscarVueloPorNumero("AR100");

        if (vueloBuscado != null) {
            vueloBuscado.mostrarInfo();
        } else {
            System.out.println("No se encontró el vuelo solicitado.");
        }

        System.out.println();

        System.out.println("========================================");
        System.out.println("2) FILTRAR VUELOS PROGRAMADOS CON STREAM");
        System.out.println("========================================");

        List<Vuelo> vuelosProgramados = aerolinea.obtenerVuelosProgramadosStream();
        vuelosProgramados.forEach(Vuelo::mostrarInfo);

        System.out.println();

        System.out.println("========================================");
        System.out.println("3) MOSTRAR VUELOS ORDENADOS POR DESTINO");
        System.out.println("========================================");

        aerolinea.mostrarVuelosOrdenadosPorDestinoStream();

        System.out.println();

        System.out.println("========================================");
        System.out.println("4) TOTAL DE ASIENTOS OCUPADOS PROGRAMADOS");
        System.out.println("========================================");

        int totalOcupados = aerolinea.calcularTotalAsientosOcupadosProgramadosStream();
        System.out.println("Total de asientos ocupados en vuelos programados: " + totalOcupados);
    }
}