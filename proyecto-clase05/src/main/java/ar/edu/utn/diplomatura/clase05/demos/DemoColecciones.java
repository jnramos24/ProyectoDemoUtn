package ar.edu.utn.diplomatura.clase05.demos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import ar.edu.utn.diplomatura.clase05.entidades.Cliente;

/**
 * Clase 21 - Colecciones, genericos y Map con streams.
 *
 * Es Java puro: se corre con el triangulo verde del main, sin levantar Spring.
 * Usa los mismos seis clientes de DemoJava8.
 */
public class DemoColecciones {

    public static void main(String[] args) {
        List<Cliente> clientes = DemoJava8.clientesDeEjemplo();

        //listaContraConjunto(clientes);
        //mapaPorCuit(clientes);
        //genericos();
        mapaConStreams(clientes);

    }

    // ------------------------------------------------------------------ 1
    /** List acepta repetidos y respeta el orden; Set no acepta repetidos */
    static void listaContraConjunto(List<Cliente> clientes) {
        titulo("1. List contra Set");

        List<String> apellidos = new ArrayList<>();
        for (Cliente c : clientes) {
            apellidos.add(c.getApellido());
        }
        // Gomez aparece dos veces: Ana y Diego.
        System.out.println("   ArrayList (" + apellidos.size() + "): " + apellidos);

        // Mismos datos en un HashSet: el repetido no entra y el orden no esta garantizado.
        Set<String> sinRepetir = new HashSet<>(apellidos);
        System.out.println("   HashSet   (" + sinRepetir.size() + "): " + sinRepetir);

        // TreeSet: tampoco acepta repetidos, y los mantiene ordenados.
        Set<String> ordenados = new TreeSet<>(apellidos);
        System.out.println("   TreeSet   (" + ordenados.size() + "): " + ordenados);

        // add() de un Set devuelve false si el elemento ya estaba.
        System.out.println("   Agregar Gomez otra vez al TreeSet: " + ordenados.add("Gomez"));
    }

    // ------------------------------------------------------------------ 2
    /** Map: pares clave -> valor. La clave no se repite */
    static void mapaPorCuit(List<Cliente> clientes) {
        titulo("2. Map<String, Cliente>: buscar por CUIT (slide 6)");

        // K = String (el CUIT), V = Cliente.
        Map<String, Cliente> porCuit = new HashMap<>();
        for (Cliente c : clientes) {
            porCuit.put(c.getCuit(), c);
        }
        System.out.println("   Claves: " + porCuit.keySet());

        // get(): sin recorrer la lista, directo por la clave.
        System.out.println("   get(\"2730123456\")  -> " + porCuit.get("2730123456").getNombre());

        // Si la clave no existe, get() devuelve null. Ojo con el NullPointerException.
        System.out.println("   get(\"9999999999\")  -> " + porCuit.get("9999999999"));
        System.out.println("   containsKey(\"9999999999\") -> " + porCuit.containsKey("9999999999"));

        // put() con una clave que ya existe NO agrega: reemplaza el valor.
        porCuit.put("2730123456", new Cliente("Ana Maria", "Ramos", "2730123456", "ana@mail.com", 0));
        System.out.println("   Despues de un put con la misma clave: " + porCuit.size() + " clientes, "
                + "y el 2730123456 ahora es " + porCuit.get("2730123456").getNombre());

        // forEach del Map recibe una lambda de DOS parametros: clave y valor.
        porCuit.forEach((cuit, cliente) -> System.out.println("   " + cuit + " -> " + cliente.getApellido()));
    }

    // ------------------------------------------------------------------ 3
    /**
     * T es "un tipo que se elige al usarla".
     */
    static class Caja<T> {
        private T contenido;

        public T get() {
            return contenido;
        }

        public void set(T contenido) {
            this.contenido = contenido;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static void genericos() {
        titulo("3. Genericos");

        // La misma clase sirve para un Cliente y para un String.
        Caja<Cliente> cajaDeCliente = new Caja<>();
        cajaDeCliente.set(new Cliente("Ana", "Gomez", "2730123456", "ana@mail.com", 100));
        Cliente usuario = cajaDeCliente.get();          // sin casteo: el compilador ya sabe que es un Cliente
        System.out.println("   En la caja de Cliente hay: " + usuario.getNombre());

        Caja<String> cajaDeTexto = new Caja<>();
        cajaDeTexto.set("hola");
        // cajaDeTexto.set(42);   // NO COMPILA: la caja es de String. El error aparece en el IDE.
        System.out.println("   En la caja de String hay: " + cajaDeTexto.get().toUpperCase());

        // Asi era antes de los genericos (Java 1.4): una lista "de cualquier cosa".
        List sinGenerico = new ArrayList();
        sinGenerico.add("texto");
        sinGenerico.add(42);                         // el compilador no se queja
        try {
            String segundo = (String) sinGenerico.get(1);   // casteo a mano... y explota al correr
            System.out.println("   " + segundo);
        } catch (ClassCastException e) {
            System.out.println("   Sin genericos -> ClassCastException: " + e.getMessage());
        }
    }

    // ------------------------------------------------------------------ 4

    static void mapaConStreams(List<Cliente> clientes) {
        titulo("4. Armar un Map con un stream y buscar el maximo");

        // filter -> se queda con los activos; collect(toMap) -> arma el Map.
        Map<String, BigDecimal> saldos = clientes.stream()
                .filter(Cliente::isActivo)
                .collect(Collectors.toMap(
                        Cliente::getCuit,  // la clave
                        c -> BigDecimal.valueOf(c.getSaldo())));   // el valor
        System.out.println("   Saldos de los activos: " + saldos);

        // Un Map no tiene stream(); su entrySet() si. Cada elemento es un par clave/valor.
        Optional<Map.Entry<String, BigDecimal>> mayor = saldos.entrySet().stream()
                .max(Map.Entry.comparingByValue());
        System.out.println("   Mayor saldo: " + mayor.map(Map.Entry::getKey).orElse("ninguno")
                + " con " + mayor.map(Map.Entry::getValue).orElse(BigDecimal.ZERO));

        // sorted() con el mismo comparador, al reves: de mayor a menor.
        List<String> ranking = saldos.entrySet().stream()
                .sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();
        System.out.println("   CUITs de mayor a menor saldo: " + ranking);

        // El Map original no cambio: los streams no modifican la fuente.
        System.out.println("   El Map sigue teniendo " + saldos.size() + " entradas");

        // Por que BigDecimal y no double: la plata no se suma con double.
        System.out.println("   0.1 + 0.2 con double:     " + (0.1 + 0.2));
        System.out.println("   0.1 + 0.2 con BigDecimal: " + new BigDecimal("0.1").add(new BigDecimal("0.2")));
    }

        static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}

