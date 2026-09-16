package ar.edu.utn.diplomatura.clase05;

import ar.edu.utn.diplomatura.clase05.componentes.ValidadorDeCuit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada. Ejecutar el main de esta clase levanta la aplicacion.
 *
 * @SpringBootApplication hace tres cosas:
 *   - marca esta clase como configuracion,
 *   - configura la aplicacion sola segun las librerias del pom.xml
 *     (por eso arranca Tomcat en el 8080 y se conecta a H2),
 *   - recorre este paquete y los de abajo buscando clases anotadas
 *     (@Component, @Service, @Repository, @RestController) para crearlas.
 *
 * Por eso toda clase que Spring deba gestionar tiene que estar dentro de
 * ar.edu.utn.diplomatura.clase05 o en un subpaquete.
 */
@SpringBootApplication
public class Clase05Application {

    public static void main(String[] args) {
        SpringApplication.run(Clase05Application.class, args);

        System.out.println();
        System.out.println("===========================================================");
        System.out.println("  Clase 05 - API REST de clientes");
        System.out.println("  App:         http://localhost:8080/clientes");
        System.out.println("  Consola H2:  http://localhost:8080/h2-console");
        System.out.println("               JDBC URL: jdbc:h2:mem:clase05   user: sa");
        System.out.println("===========================================================");
        System.out.println();

    }
}
