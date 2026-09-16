package ar.edu.utn.diplomatura.clase05.ioc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Compara las dos formas de obtener una dependencia. Se ejecuta al arrancar
 * la aplicacion y escribe el resultado en la consola.
 *
 * No forma parte de la API de clientes: se puede borrar sin romper nada.
 *
 * CommandLineRunner: cualquier bean que implemente esta interfaz corre una vez,
 * apenas la aplicacion termina de arrancar.
 */
@Component
@Order(1)
public class DemoInyeccion implements CommandLineRunner {

    // Forma recomendada: la dependencia se recibe, no se crea.
    // El tipo es la INTERFAZ, asi que esta clase no sabe si el mensaje sale
    // por mail o por SMS. Cambiar la implementacion no la obliga a cambiar.
    private final ServicioDeNotificaciones notificador;

    public DemoInyeccion(ServicioDeNotificaciones notificador) {
        this.notificador = notificador;
    }

    @Override
    public void run(String... args) {
        System.out.println();
        System.out.println("=== Comparacion: crear la dependencia vs recibirla ===");

        // La de abajo hay que construirla a mano.
        AltaSinInyeccion sinInyeccion = new AltaSinInyeccion();
        sinInyeccion.darDeAltaCliente("ana@banco.com");

        // Esta ya vino armada desde el contenedor de Spring.
        System.out.println("   Alta de cliente (con inyeccion): bruno@banco.com");
        notificador.notificar("bruno@banco.com", "Bienvenido al banco.");

        System.out.println();
    }

    /**
     * Forma tradicional: la clase se fabrica su propia dependencia con "new".
     *
     * Funciona, pero queda rigida: para cambiar el mail por SMS hay que editar
     * este archivo, y en un test no se puede reemplazar el notificador por uno
     * falso porque el "new" esta fijo adentro.
     *
     * No tiene anotaciones de Spring: no es un bean, la creamos nosotros.
     */
    static class AltaSinInyeccion {

        private final ServicioDeNotificaciones notificador = new NotificadorPorEmail();

        void darDeAltaCliente(String email) {
            System.out.println("   Alta de cliente (sin inyeccion): " + email);
            notificador.notificar(email, "Bienvenido al banco.");
        }
    }
}
