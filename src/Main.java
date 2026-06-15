import menu.Menu;
import servicio.Aerolinea;

/**
 * @file Main.java
 * @brief Punto de entrada principal del sistema de aerolínea.
 */

/**
 * @class Main
 * @brief Clase principal desde donde inicia la aplicación.
 *
 * Crea la instancia de Aerolinea y ejecuta el menú interactivo por consola.
 */
public class Main {

    /**
     * @brief Método principal del programa.
     *
     * @param args Argumentos de consola no utilizados.
     */
    public static void main(String[] args) {
        Aerolinea aerolinea = new Aerolinea("Aerolínea IFES");

        Menu menu = new Menu(aerolinea);
        menu.iniciar();
    }
}