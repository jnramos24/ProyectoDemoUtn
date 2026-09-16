package ar.edu.utn.diplomatura.clase05.ioc;

import org.springframework.stereotype.Component;

/**
 * Implementacion "por mail" del contrato. No manda mails de verdad:
 * imprime en consola.
 *
 * Al estar anotada con @Component, Spring la crea una sola vez y se la entrega
 * a quien pida un ServicioDeNotificaciones.
 */
@Component
public class NotificadorPorEmail implements ServicioDeNotificaciones {

    @Override
    public void notificar(String destinatario, String mensaje) {
        System.out.println("   [EMAIL] para " + destinatario + ": " + mensaje);
    }
}
