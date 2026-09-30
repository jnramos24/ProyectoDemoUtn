package ar.edu.utn.diplomatura.clase05.demos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.edu.utn.diplomatura.clase05.entidades.Cliente;

/**
 * Clase 12 - Java 8: Optional, lambdas, streams y variables temporales.
 *
 * Es Java puro: se corre con el triangulo verde del main, sin levantar Spring.
 * Usa la entidad Cliente como un objeto comun, sin base de datos.
 */
public class DemoJava8 {

    public static void main(String[] args) {
        List<Cliente> clientes = clientesDeEjemplo();

        //orElseContraOrElseGet();
        //buscarSinNullPointer(clientes);
        lambdasEInterfacesFuncionales(clientes);
        //filterMapCollect();
        //imperativoContraFuncional(clientes);
        //losStreamsSonPerezosos(clientes);
        //variablesTemporales(clientes);

    }

    // ------------------------------------------------------------------ 1
    /** orElse recibe un valor; orElseGet, una funcion. */
    static void orElseContraOrElseGet() {
        titulo("1. orElse contra orElseGet (slides 7-8)");

        Optional<String> conValor = Optional.of("mail@mail.com");

        // orElse recibe un VALOR: Java lo calcula antes de llamar al metodo,
        // haga falta o no. Aca el Optional tiene valor y el calculo se hizo igual.
        String conOrElse = conValor.orElse(mailPorDefecto("orElse"));

        // orElseGet recibe una FUNCION (un Supplier): solo la ejecuta si el
        // Optional esta vacio. Aca no hizo falta, asi que no se ejecuto.
        String conOrElseGet = conValor.orElseGet(() -> mailPorDefecto("orElseGet"));

        System.out.println("   Los dos devolvieron: " + conOrElse + " / " + conOrElseGet);
    }

    static String mailPorDefecto(String quienLoPidio) {
        System.out.println("   -> " + quienLoPidio + " calculo el mail por defecto");
        return "sin-mail@banco.com";
    }

    // ------------------------------------------------------------------ 2
    static void buscarSinNullPointer(List<Cliente> clientes) {
        titulo("2. Buscar sin NullPointerException (slide 9)");

        String encontrado = clientes.stream()
                .filter(e -> e.getApellido().equals("Peralta"))
                .findFirst()                   // Optional<Cliente>
                .map(Cliente::getNombre).orElse("desconocido");      // Optional<String>;        // String

        String noEncontrado = clientes.stream()
                .filter(c -> c.getNombre().equals("Miguel"))
                .findFirst()                   // Optional vacio: no hay ningun Miguel
                .map(Cliente::getEmail)        // sobre un vacio, map no hace nada
                .orElse("Desconocido");

        System.out.println("   Apellido Peralta -> " + encontrado);
        System.out.println("   Nombre Miguel    -> " + noEncontrado);
    }

    // ------------------------------------------------------------------ 3
    /** Las cuatro interfaces funcionales que mas se usan, y la sintaxis lambda. */
    static void lambdasEInterfacesFuncionales(List<Cliente> clientes) {
        titulo("3. Lambdas e interfaces funcionales (slides 17 y 19)");

        Cliente ana = clientes.get(0);
        Cliente diego = clientes.get(3);

        // Predicate<T>: recibe un T, devuelve boolean. Es el que usa filter().
        Predicate<Cliente> tieneSaldoAlto = c -> c.getSaldo() > 100_000;

        // Function<T, R>: recibe un T, devuelve un R. Es el que usa map().
        Function<Cliente, String> nombreCompleto = c -> c.getNombre() + " " + c.getApellido();

        // Consumer<T>: recibe un T, no devuelve nada. Es el que usa forEach() e ifPresent().
        Consumer<String> imprimir = texto -> System.out.println("   " + texto);

        // Supplier<T>: no recibe nada, devuelve un T. Es el que usa orElseGet().
        // Con CERO parametros los parentesis son obligatorios: () ->
        Supplier<String> saludo = () -> "Buen dia desde un Supplier";

        // Cuerpo de varias lineas: llaves y return obligatorios.
        Function<Cliente, String> categoria = c -> {
            if (c.getSaldo() >= 1_000_000) {
                return "premium";
            }
            return "estandar";
        };

        imprimir.accept(nombreCompleto.apply(ana) + " tiene saldo alto? " + tieneSaldoAlto.test(ana));
        imprimir.accept(nombreCompleto.apply(ana) + " es " + categoria.apply(ana));
        imprimir.accept(nombreCompleto.apply(diego) + " es " + categoria.apply(diego));
        imprimir.accept(saludo.get());

        // La misma idea escrita de tres formas. Las tres son un Predicate<Cliente>.
        Predicate<Cliente> activoClaseAnonima = new Predicate<Cliente>() {
            @Override
            public boolean test(Cliente c) {
                return c.isActivo();
            }
        };
        Predicate<Cliente> activoLambda = c -> c.isActivo();
        Predicate<Cliente> activoReferencia = Cliente::isActivo;   // referencia a metodo

        Cliente federico = clientes.get(5);
        imprimir.accept("Federico activo? " + activoClaseAnonima.test(federico)
                + " / " + activoLambda.test(federico)
                + " / " + activoReferencia.test(federico));

        // Los predicados se combinan: and, or, negate.
        Predicate<Cliente> activoYConSaldoAlto = activoReferencia.and(tieneSaldoAlto);
        imprimir.accept("Activos con saldo alto: " + clientes.stream()
                .filter(activoYConSaldoAlto)
                .map(nombreCompleto)
                .collect(Collectors.toList()));
    }

    // ------------------------------------------------------------------ 4
    /** El ejemplo de las slides 22 y 23, completo y compilable. */
    static void filterMapCollect() {
        titulo("4. filter, map y collect (slides 18, 22 y 23)");

        List<String> numbers = Arrays.asList("1", "2", "3", "4", "5", "6");
        System.out.println("   original list: " + numbers);

        List<Integer> even = numbers.stream()
                .map(s -> Integer.valueOf(s))           // "1" -> 1   (transforma)
                .filter(number -> number % 2 == 0)      // se queda con los pares
                .toList();          // lo junta en una List nueva

        System.out.println("   processed list, only even numbers: " + even);
        System.out.println("   y la original no cambio: " + numbers);
    }

    // ------------------------------------------------------------------ 5
    /** La comparativa de la slide 20, sobre clientes: saldo promedio de los activos. */
    static void imperativoContraFuncional(List<Cliente> clientes) {
        titulo("5. Imperativo contra funcional (slides 20 y 21)");

        // Imperativo: le decimos a Java COMO hacerlo, paso por paso.
        double total = 0;
        int cantidad = 0;
        for (Cliente c : clientes) {
            if (c.isActivo()) {
                total = total + c.getSaldo();
                cantidad++;
            }
        }
        double promedioImperativo = cantidad == 0 ? 0 : total / cantidad;

        // Funcional: le decimos QUE queremos.
        double promedioFuncional = clientes.stream()
                .filter(Cliente::isActivo)
                .mapToDouble(Cliente::getSaldo)   // Stream<Cliente> -> DoubleStream
                .average()                        // OptionalDouble: puede no haber ninguno
                .orElse(0);

        System.out.println("   Imperativo: " + promedioImperativo);
        System.out.println("   Funcional:  " + promedioFuncional);
    }

    // ------------------------------------------------------------------ 6
    /** Un stream no hace nada hasta la operacion final, y se usa una sola vez. */
    static void losStreamsSonPerezosos(List<Cliente> clientes) {
        titulo("6. Los streams son perezosos y de un solo uso (slide 16)");

        Stream<Cliente> conSaldoAlto = clientes.stream()
                .filter(c -> {
                    System.out.println("   evaluando a " + c.getNombre());
                    return c.getSaldo() > 100_000;
                });
        System.out.println("   Stream armado. Todavia no se evaluo a nadie.");

        // findFirst es la operacion final: recien aca se recorre la lista,
        // y se corta apenas aparece el primero que cumple.
        Optional<Cliente> primero = conSaldoAlto.findFirst();
        System.out.println("   Primero con saldo alto: " + primero.map(Cliente::getNombre).orElse("ninguno"));

        try {
            conSaldoAlto.count();   // segundo uso del mismo stream
        } catch (IllegalStateException e) {
            System.out.println("   Reusar el stream -> " + e.getMessage());
        }
    }



    // ------------------------------------------------------------------
    /** Los mismos seis del seeder. CUIT de 10 digitos, como exige ValidadorDeCuit. */
    static List<Cliente> clientesDeEjemplo() {
        Cliente federico = new Cliente("Federico", "Luna", "2033445566", "federico.luna@mail.com", 500.0);
        federico.setActivo(false);

        return List.of(
                new Cliente("Ana", "Gomez", "2730123456", "ana.gomez@mail.com", 152300.50),
                new Cliente("Bruno", "Diaz", "2028987654", "bruno.diaz@mail.com", 8400.00),
                new Cliente("Carla", "Peralta", "2735411223", "carla.peralta@mail.com", 0.00),
                new Cliente("Diego", "Gomez", "2022109876", "diego.gomez@mail.com", 1250000.00),
                new Cliente("Elena", "Suarez", "2341223344", "elena.suarez@mail.com", 76500.75),
                federico);
    }

    static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}

