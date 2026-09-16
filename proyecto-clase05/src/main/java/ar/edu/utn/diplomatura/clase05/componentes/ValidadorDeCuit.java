package ar.edu.utn.diplomatura.clase05.componentes;

import org.springframework.stereotype.Component;

/**
 * Herramienta para trabajar con CUIT/CUIL.
 *
 * No es controlador, ni servicio, ni repositorio: para esos casos se usa
 * @Component, que hace que Spring la cree y la pueda inyectar donde haga falta.
 */
@Component
public class ValidadorDeCuit {

    /** Saca guiones y espacios. */
    public String normalizar(String cuit) {
        if (cuit == null) {
            return null;
        }
        return cuit.replace("-", "").trim();
    }

    /** true si son 10 digitos. No valida el digito verificador. */
    public boolean esValido(String cuit) {
        String limpio = normalizar(cuit);
        if (limpio == null) {
            return false;
        }
        return limpio.matches("[0-9]{10}");
    }

    /** 2012345678 -> 20-1234567-8 */
    public String formatear(String cuit) {
        String limpio = normalizar(cuit);
        if (!esValido(limpio)) {
            return cuit;
        }
        return limpio.substring(0, 2) + "-" + limpio.substring(2, 9) + "-" + limpio.substring(9);
    }
}
