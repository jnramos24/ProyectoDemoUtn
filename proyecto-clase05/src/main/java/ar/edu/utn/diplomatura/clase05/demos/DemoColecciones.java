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

        listaContraConjunto(clientes);
        //mapaPorCuit(clientes);
    }

    // ------------------------------------------------------------------ 1
    /** List acepta repetidos y respeta el orden; Set no acepta repetidos */
    static void listaContraConjunto(List<Cliente> clientes) {
        titulo("1. List contra Set (slides 4 a 6)");

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
        porCuit.put("2730123456", new Cliente("Ana Maria", "Gomez", "2730123456", "ana@mail.com", 0));
        System.out.println("   Despues de un put con la misma clave: " + porCuit.size() + " clientes, "
                + "y el 2730123456 ahora es " + porCuit.get("2730123456").getNombre());

        // forEach del Map recibe una lambda de DOS parametros: clave y valor.
        porCuit.forEach((cuit, cliente) -> System.out.println("   " + cuit + " -> " + cliente.getApellido()));
    }

    static void titulo(String texto) {
        System.out.println();
        System.out.println("=== " + texto + " ===");
    }
}

