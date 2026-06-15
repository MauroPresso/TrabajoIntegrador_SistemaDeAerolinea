import excepciones.VueloNoDisponibleException;
import modelo.Pasajero;
import modelo.Vuelo;
import modelo.VueloCharter;
import modelo.VueloInternacional;
import modelo.VueloNacional;

import java.util.ArrayList;

/**
 * @class Main
 * @brief Punto de entrada temporal para probar el modelo de dominio.
 */
public class Main {

    /**
     * @brief Ejecuta una prueba simple de herencia, polimorfismo y reservas.
     * @param args Argumentos de consola no utilizados.
     */
    public static void main(String[] args) {
        ArrayList<Vuelo> vuelos = new ArrayList<>();

        vuelos.add(new VueloNacional(
                "AR100",
                "Neuquén",
                "Buenos Aires",
                "2026-06-20",
                180,
                "Buenos Aires"
        ));

        vuelos.add(new VueloInternacional(
                "AR200",
                "Buenos Aires",
                "Santiago",
                "2026-06-21",
                160,
                "Chile",
                true
        ));

        vuelos.add(new VueloCharter(
                "CH300",
                "Neuquén",
                "Mendoza",
                "2026-06-22",
                80,
                "IFES",
                2500000
        ));

        Pasajero pasajero = new Pasajero(
                12345678,
                "Mauro",
                "Presso",
                "A123456"
        );

        try {
            vuelos.get(0).reservarPasajero(pasajero);
        } catch (VueloNoDisponibleException e) {
            System.out.println("No se pudo reservar: " + e.getMessage());
        }

        // Polimorfismo:
        // Se trabaja con referencias de tipo Vuelo sin conocer la subclase concreta.
        for (Vuelo vuelo : vuelos) {
            vuelo.mostrarInfo();
            vuelo.embarcar();
            System.out.println();
        }
    }
}