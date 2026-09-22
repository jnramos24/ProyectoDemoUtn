package ar.edu.utn.diplomatura.clase05.demos;

import java.util.Optional;

public class DemoExcepciones {

    public static void main(String[] args) {
        referenciasYEquals();
        laNullPointerException();
        laMismaCosaConOptional();
        divisionPorCero();
        elOrdenDeLosBloques();
        checkedVsUnchecked();
        elCatchQueTapaBugs();
    }

    // ------------------------------------------------------------------ 1
    static void referenciasYEquals() {
        titulo("1. Referencias: == contra equals");

        String a = "20123456789";
        String b = "20123456789";                  // el mismo literal
        String c = new String("20123456789");      // objeto nuevo, forzado

        System.out.println("   a == b          -> " + (a == b) + "   <- OJO: da true");
        System.out.println("   a == c          -> " + (a == c) + "  <- el mismo texto, otra direccion de memoria");
        System.out.println("   a.equals(c)     -> " + a.equals(c) + "   <- esto es lo que siempre queriamos preguntar");
    }

    // ------------------------------------------------------------------ 2
    /** el metodo devuelve null y el que lo usa se entera tarde. */
    static void laNullPointerException() {
        titulo("2. NullPointerException");

        String emailDelCliente = buscarEmailEstiloViejo(999L);   // devuelve null

        try {
            // La firma dice "String": nada avisa de que puede venir null.
            System.out.println("   Dominio del mail: " + emailDelCliente.split("@")[1]);
        } catch (NullPointerException npe) {
            System.out.println("   Se cayo. Mensaje de Java:");
            System.out.println("   " + npe.getMessage());
        }
    }

    /** Estilo viejo: null significa "no lo encontre"... y tambien "toma, null". */
    static String buscarEmailEstiloViejo(Long id) {
        if (id == 1L) {
            return "ana.gomez@mail.com";
        }
        return null;
    }

    // ------------------------------------------------------------------ 3
    static void laMismaCosaConOptional() {
        titulo("3. Lo mismo con Optional");

        // El compilador no deja usar esto como si fuera un String: hay que
        // decidir que se hace con el caso vacio. Es la ventaja del tipo.
        Optional<String> email = buscarEmailConOptional(2L);

        System.out.println("   isPresent()                 -> " + email.isPresent());
        System.out.println("   orElse(\"sin mail\")          -> " + email.orElse("sin mail"));
        System.out.println("   map(String::length)         -> " + email.map(String::length).orElse(0));

        // ifPresent: si hay valor corre el bloque; si no, no pasa nada. Sin if.
        buscarEmailConOptional(1L).ifPresent(e -> System.out.println("   ifPresent del cliente 1     -> " + e));

        // orElseThrow: el que usa nuestro ClienteService.
        try {
            String obligatorio = email.orElseThrow(() -> new IllegalStateException("El cliente 999 no tiene mail"));
            System.out.println("   nunca llega aca: " + obligatorio);
        } catch (IllegalStateException e) {
            System.out.println("   orElseThrow lanzo           -> " + e.getMessage());
        }

        // get() sin validar
        try {
            System.out.println(email.get());
        } catch (java.util.NoSuchElementException e) {
            System.out.println("   get() sin validar lanzo     -> " + e.getClass().getSimpleName());
        }
    }

    static Optional<String> buscarEmailConOptional(Long id) {
        if (id == 1L) {
            // Optional.of NO acepta null: si el valor puede ser null, va
            // Optional.ofNullable(...). Confundirlos es el error tipico.
            return Optional.of("ana.gomez@mail.com");
        }
        return Optional.empty();
    }

    // ------------------------------------------------------------------ 4
    static void divisionPorCero() {
        titulo("4. Division por cero");

        dividir(8, 2);
        dividir(8, 0);
    }

    static void dividir(int numerador, int denominador) {
        try {
            int resultado = numerador / denominador;
            System.out.println("   " + numerador + " / " + denominador + " = " + resultado);
        } catch (ArithmeticException ae) {
            System.out.println("   " + numerador + " / " + denominador + " -> no se puede dividir por cero (" + ae.getMessage() + ")");
        } finally {
            System.out.println("      (el finally corrio igual)");
        }
    }

    // ------------------------------------------------------------------ 5
    static void elOrdenDeLosBloques() {
        titulo("5. Orden de ejecucion");

        System.out.println("   Sin excepcion:  " + conOSinFalla(false));
        System.out.println("   Con excepcion:  " + conOSinFalla(true));
    }

    static String conOSinFalla(boolean fallar) {
        try {
            System.out.println("      try");
            if (fallar) {
                throw new IllegalArgumentException("fallo a proposito");
            }
            return "devuelto desde el try";
        } catch (IllegalArgumentException e) {
            System.out.println("      catch: " + e.getMessage());
            return "devuelto desde el catch";
        } finally {
            System.out.println("      finally (corre igual, incluso con el return ya decidido)");
        }
    }

    // ------------------------------------------------------------------ 6
    static void checkedVsUnchecked() {
        titulo("6. Checked contra unchecked");

        // Unchecked: esta llamada compila sin try. Si falla, se cae en ejecucion.
        try {
            validarImporteUnchecked(-50);
        } catch (IllegalArgumentException e) {
            System.out.println("   unchecked -> " + e.getMessage());
        }

        // Checked: sin este try, el proyecto NO COMPILA. Probalo: borra el try y
        // mira el error del compilador. Eso es lo que quiere decir "checked".
        try {
            validarImporteChecked(-50);
        } catch (ImporteRechazado e) {
            System.out.println("   checked   -> " + e.getMessage());
        }
    }

    static void validarImporteUnchecked(double importe) {
        if (importe <= 0) {
            throw new IllegalArgumentException("Importe invalido: " + importe);
        }
    }

    /** El "throws" es parte de la firma: avisa al que llame que tiene que decidir. */
    static void validarImporteChecked(double importe) throws ImporteRechazado {
        if (importe <= 0) {
            throw new ImporteRechazado("Importe rechazado por el banco: " + importe);
        }
    }

    /** Checked porque extiende Exception y no RuntimeException. */
    static class ImporteRechazado extends Exception {
        ImporteRechazado(String mensaje) {
            super(mensaje);
        }
    }

    // ------------------------------------------------------------------ 7

    static void elCatchQueTapaBugs() {
        titulo("7. El catch que tapa bugs");

        try {
            String sinInicializar = null;
            System.out.println(sinInicializar.length());   // bug nuestro
        } catch (Exception e) {
            // Este mensaje miente: no hay ningun problema con los datos, hay un
            // bug en la linea de arriba. Y quedo tapado.
            System.out.println("   catch (Exception e) dijo: 'hubo un problema con los datos'");
            System.out.println("   pero lo que paso en realidad fue: " + e.getClass().getSimpleName());
        }

        System.out.println();
        System.out.println("   Regla: atrapa la excepcion mas especifica que puedas, y solo");
        System.out.println("   si podes hacer algo con ella. Un catch vacio es peor que no tenerlo.");
    }

    // ------------------------------------------------------------------
    static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}

