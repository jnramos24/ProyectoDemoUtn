package ar.edu.utn.diplomatura.clase05.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ar.edu.utn.diplomatura.clase05.entidades.Cliente;
import ar.edu.utn.diplomatura.clase05.repositorios.ClienteRepository;

/**
 * Carga clientes de ejemplo al arrancar.
 *
 * Hace falta porque la base H2 vive en memoria: al apagar la aplicacion se
 * pierde todo, asi que cada arranque empieza con estos cinco.
 */
@Component
@Order(2)
public class CargaDatosIniciales implements CommandLineRunner {

    private final ClienteRepository clienteRepository;

    public CargaDatosIniciales(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public void run(String... args) {
        if (clienteRepository.count() > 0) {
            return;
        }

        // saveAll hace un INSERT por cada elemento de la lista.
        clienteRepository.saveAll(List.of(
                new Cliente("Ana", "Gomez", "27301234567", "ana.gomez@mail.com", 152300.50),
                new Cliente("Bruno", "Diaz", "20289876543", "bruno.diaz@mail.com", 8400.00),
                new Cliente("Carla", "Peralta", "27354112233", "carla.peralta@mail.com", 0.00),
                new Cliente("Diego", "Gomez", "20221098765", "diego.gomez@mail.com", 1250000.00),
                new Cliente("Elena", "Suarez", "23412233445", "elena.suarez@mail.com", 76500.75)));

        Cliente inactivo = new Cliente("Federico", "Luna", "2033445566", "federico.luna@mail.com", 500.0);
        inactivo.setActivo(false);
        clienteRepository.save(inactivo);

        System.out.println("=== " + clienteRepository.count() + " clientes cargados en H2 ===");
    }
}
