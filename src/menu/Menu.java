package menu;

import excepciones.VueloNoDisponibleException;
import modelo.Pasajero;
import modelo.Vuelo;
import modelo.VueloCharter;
import modelo.VueloInternacional;
import modelo.VueloNacional;
import servicio.Aerolinea;

import java.util.List;
import java.util.Scanner;

/**
 * @file Menu.java
 * @brief Contiene el menú interactivo por consola del sistema de aerolínea.
 */

/**
 * @class Menu
 * @brief Gestiona la interacción del usuario con el sistema mediante consola.
 *
 * Esta clase utiliza Scanner para leer datos ingresados por teclado y delega
 * las operaciones principales en la clase Aerolinea.
 */
public class Menu {

    /**
     * @brief Opción utilizada para finalizar el menú.
     */
    private static final int OPCION_SALIR = 0;

    /**
     * @brief Servicio principal de la aerolínea.
     */
    private final Aerolinea aerolinea;

    /**
     * @brief Scanner utilizado para leer datos desde consola.
     */
    private final Scanner scanner;

    /**
     * @brief Constructor del menú.
     *
     * @param aerolinea Servicio principal del sistema.
     */
    public Menu(Aerolinea aerolinea) {
        this.aerolinea = aerolinea;
        this.scanner = new Scanner(System.in);
    }

    /**
     * @brief Inicia la ejecución del menú interactivo.
     *
     * Muestra las opciones disponibles hasta que el usuario decide salir.
     */
    public void iniciar() {
        int opcion;

        do {
            mostrarOpciones();
            opcion = leerEntero("Seleccione una opción: ");
            ejecutarOpcion(opcion);
        } while (opcion != OPCION_SALIR);
    }

    /**
     * @brief Muestra las opciones principales del sistema.
     */
    private void mostrarOpciones() {
        System.out.println();
        System.out.println("========================================");
        System.out.println("       SISTEMA DE AEROLINEA IFES");
        System.out.println("========================================");
        System.out.println("1. Agregar vuelo");
        System.out.println("2. Registrar pasajero");
        System.out.println("3. Reservar vuelo");
        System.out.println("4. Cancelar reserva");
        System.out.println("5. Mostrar vuelos programados");
        System.out.println("6. Mostrar vuelos ordenados por destino");
        System.out.println("7. Mostrar total de asientos ocupados");
        System.out.println("0. Salir");
        System.out.println("========================================");
    }

    /**
     * @brief Ejecuta la opción seleccionada por el usuario.
     *
     * @param opcion Número de opción ingresado.
     */
    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                agregarVuelo();
                break;
            case 2:
                registrarPasajero();
                break;
            case 3:
                reservarVuelo();
                break;
            case 4:
                cancelarReserva();
                break;
            case 5:
                mostrarVuelosProgramados();
                break;
            case 6:
                mostrarVuelosOrdenadosPorDestino();
                break;
            case 7:
                mostrarTotalAsientosOcupados();
                break;
            case OPCION_SALIR:
                System.out.println("Saliendo del sistema...");
                break;
            default:
                System.out.println("Opción inválida. Intente nuevamente.");
                break;
        }
    }

    /**
     * @brief Permite agregar un vuelo nacional, internacional o charter.
     *
     * Solicita los datos comunes del vuelo y luego los datos específicos
     * según el tipo seleccionado.
     */
    private void agregarVuelo() {
        System.out.println();
        System.out.println("--------- AGREGAR VUELO ---------");
        System.out.println("1. Vuelo nacional");
        System.out.println("2. Vuelo internacional");
        System.out.println("3. Vuelo charter");

        int tipoVuelo = leerEntero("Seleccione el tipo de vuelo: ");

        String numero = leerTextoObligatorio("Número de vuelo: ");

        if (aerolinea.buscarVueloPorNumero(numero) != null) {
            System.out.println("Ya existe un vuelo con ese número.");
            return;
        }

        String origen = leerTextoObligatorio("Origen: ");
        String destino = leerTextoObligatorio("Destino: ");
        String fecha = leerTextoObligatorio("Fecha: ");
        int capacidad = leerEntero("Capacidad: ");

        try {
            Vuelo vuelo = crearVueloSegunTipo(tipoVuelo, numero, origen, destino, fecha, capacidad);

            if (vuelo == null) {
                System.out.println("Tipo de vuelo inválido.");
                return;
            }

            aerolinea.agregarVuelo(vuelo);
            System.out.println("Vuelo agregado correctamente.");

        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo agregar el vuelo: " + e.getMessage());
        }
    }

    /**
     * @brief Crea un vuelo según el tipo seleccionado por el usuario.
     *
     * @param tipoVuelo Tipo de vuelo seleccionado.
     * @param numero Número del vuelo.
     * @param origen Origen del vuelo.
     * @param destino Destino del vuelo.
     * @param fecha Fecha del vuelo.
     * @param capacidad Capacidad máxima del vuelo.
     * @return Vuelo creado o null si el tipo es inválido.
     */
    private Vuelo crearVueloSegunTipo(int tipoVuelo, String numero, String origen,
                                      String destino, String fecha, int capacidad) {
        switch (tipoVuelo) {
            case 1:
                String provinciaDestino = leerTextoObligatorio("Provincia de destino: ");
                return new VueloNacional(numero, origen, destino, fecha, capacidad, provinciaDestino);

            case 2:
                String paisDestino = leerTextoObligatorio("País de destino: ");
                boolean requierePasaporte = leerBooleano("¿Requiere pasaporte? (S/N): ");
                return new VueloInternacional(numero, origen, destino, fecha, capacidad,
                        paisDestino, requierePasaporte);

            case 3:
                String empresaContratante = leerTextoObligatorio("Empresa contratante: ");
                double costoTotal = leerDouble("Costo total: ");
                return new VueloCharter(numero, origen, destino, fecha, capacidad,
                        empresaContratante, costoTotal);

            default:
                return null;
        }
    }

    /**
     * @brief Permite registrar un pasajero en la aerolínea.
     *
     * Crea un objeto Pasajero y lo registra mediante el método registrarPersona()
     * de la clase Aerolinea.
     */
    private void registrarPasajero() {
        System.out.println();
        System.out.println("--------- REGISTRAR PASAJERO ---------");

        int dni = leerEntero("DNI: ");
        String nombre = leerTextoObligatorio("Nombre: ");
        String apellido = leerTextoObligatorio("Apellido: ");
        String numeroPasaporte = leerTextoOpcional("Número de pasaporte (opcional): ");

        try {
            Pasajero pasajero = new Pasajero(dni, nombre, apellido, numeroPasaporte);
            boolean registrado = aerolinea.registrarPersona(pasajero);

            if (registrado) {
                System.out.println("Pasajero registrado correctamente.");
            } else {
                System.out.println("Ya existe una persona registrada con ese DNI.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("No se pudo registrar el pasajero: " + e.getMessage());
        }
    }

    /**
     * @brief Permite reservar un vuelo para un pasajero registrado.
     *
     * Maneja la excepción checked VueloNoDisponibleException cuando el vuelo
     * está en vuelo, cancelado o sin asientos disponibles.
     */
    private void reservarVuelo() {
        System.out.println();
        System.out.println("--------- RESERVAR VUELO ---------");

        try {
            int dniPasajero = leerEntero("DNI del pasajero: ");
            String numeroVuelo = leerTextoObligatorio("Número de vuelo: ");

            aerolinea.reservarVuelo(dniPasajero, numeroVuelo);
            System.out.println("Operación de reserva procesada.");

        } catch (VueloNoDisponibleException e) {
            System.out.println("No se pudo reservar el vuelo: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error en la reserva: " + e.getMessage());
        } finally {
            System.out.println("Finalizó la operación de reserva.");
        }
    }

    /**
     * @brief Permite cancelar una reserva existente.
     */
    private void cancelarReserva() {
        System.out.println();
        System.out.println("--------- CANCELAR RESERVA ---------");

        int dniPasajero = leerEntero("DNI del pasajero: ");
        String numeroVuelo = leerTextoObligatorio("Número de vuelo: ");

        try {
            aerolinea.cancelarReserva(dniPasajero, numeroVuelo);
            System.out.println("Operación de cancelación procesada.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error al cancelar la reserva: " + e.getMessage());
        }
    }

    /**
     * @brief Muestra los vuelos que se encuentran en estado PROGRAMADO.
     */
    private void mostrarVuelosProgramados() {
        System.out.println();
        System.out.println("--------- VUELOS PROGRAMADOS ---------");

        List<Vuelo> vuelosProgramados = aerolinea.obtenerVuelosProgramadosStream();

        if (vuelosProgramados.isEmpty()) {
            System.out.println("No hay vuelos programados.");
            return;
        }

        vuelosProgramados.forEach(Vuelo::mostrarInfo);
    }

    /**
     * @brief Muestra todos los vuelos ordenados alfabéticamente por destino.
     */
    private void mostrarVuelosOrdenadosPorDestino() {
        System.out.println();
        System.out.println("--------- VUELOS ORDENADOS POR DESTINO ---------");

        if (aerolinea.getVuelos().isEmpty()) {
            System.out.println("No hay vuelos cargados.");
            return;
        }

        aerolinea.mostrarVuelosOrdenadosPorDestinoStream();
    }

    /**
     * @brief Muestra el total de asientos ocupados en vuelos programados.
     */
    private void mostrarTotalAsientosOcupados() {
        System.out.println();
        System.out.println("--------- TOTAL DE ASIENTOS OCUPADOS ---------");

        int total = aerolinea.calcularTotalAsientosOcupadosProgramadosStream();

        System.out.println("Total de asientos ocupados en vuelos programados: " + total);
    }

    /**
     * @brief Lee un número entero desde consola.
     *
     * @param mensaje Mensaje mostrado al usuario.
     * @return Número entero ingresado.
     */
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número entero válido.");
            }
        }
    }

    /**
     * @brief Lee un número decimal desde consola.
     *
     * Acepta tanto punto como coma decimal.
     *
     * @param mensaje Mensaje mostrado al usuario.
     * @return Número decimal ingresado.
     */
    private double leerDouble(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim().replace(",", ".");

            try {
                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un número decimal válido.");
            }
        }
    }

    /**
     * @brief Lee un texto obligatorio desde consola.
     *
     * @param mensaje Mensaje mostrado al usuario.
     * @return Texto no vacío ingresado por el usuario.
     */
    private String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            if (!entrada.isEmpty()) {
                return entrada;
            }

            System.out.println("El dato no puede estar vacío.");
        }
    }

    /**
     * @brief Lee un texto opcional desde consola.
     *
     * @param mensaje Mensaje mostrado al usuario.
     * @return Texto ingresado o cadena vacía.
     */
    private String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    /**
     * @brief Lee una respuesta booleana desde consola.
     *
     * @param mensaje Mensaje mostrado al usuario.
     * @return true si el usuario ingresa S, false si ingresa N.
     */
    private boolean leerBooleano(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = scanner.nextLine().trim();

            if (entrada.equalsIgnoreCase("S")) {
                return true;
            }

            if (entrada.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Debe ingresar S o N.");
        }
    }
}