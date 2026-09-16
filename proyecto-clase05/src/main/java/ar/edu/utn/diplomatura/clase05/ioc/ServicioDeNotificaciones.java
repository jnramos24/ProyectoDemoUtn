package ar.edu.utn.diplomatura.clase05.ioc;

/**
 * Contrato: dice QUE se puede hacer (notificar), sin decir COMO.
 * Las implementaciones deciden el como.
 */
public interface ServicioDeNotificaciones {

    void notificar(String destinatario, String mensaje);
}
