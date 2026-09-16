package ar.edu.utn.diplomatura.clase05.servicios;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.edu.utn.diplomatura.clase05.componentes.ValidadorDeCuit;
import ar.edu.utn.diplomatura.clase05.entidades.Cliente;
import ar.edu.utn.diplomatura.clase05.repositorios.ClienteRepository;

/**
 * Reglas del negocio. Esta entre el controlador y el repositorio.
 *
 * Cuando algo no se puede hacer (el cliente no existe, el importe es invalido)
 * estos metodos devuelven null.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ValidadorDeCuit validadorDeCuit;

    // Inyeccion por constructor: Spring busca esos dos beans en el contenedor
    // y los pasa como argumentos. Nunca escribimos "new ClienteRepository()".
    // Con un solo constructor, @Autowired no hace falta.
    public ClienteService(ClienteRepository clienteRepository, ValidadorDeCuit validadorDeCuit) {
        this.clienteRepository = clienteRepository;
        this.validadorDeCuit = validadorDeCuit;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    /** Devuelve el cliente, o null si ese id no existe. */
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    public List<Cliente> buscarPorApellido(String apellido) {
        return clienteRepository.findByApellidoContainingIgnoreCase(apellido);
    }

    /** Crea un cliente. El CUIT se guarda siempre sin guiones. */
    public Cliente crear(Cliente cliente) {
        cliente.setCuit(validadorDeCuit.normalizar(cliente.getCuit()));
        // El id lo pone la base: si viene uno en el JSON, lo ignoramos.
        cliente.setId(null);

        return clienteRepository.save(cliente);
    }

    /** Actualiza nombre, apellido y email. Devuelve null si el id no existe. */
    public Cliente actualizar(Long id, Cliente datosNuevos) {
        Cliente existente = buscarPorId(id);
        if (existente == null) {
            return null;
        }

        existente.setNombre(datosNuevos.getNombre());
        existente.setApellido(datosNuevos.getApellido());
        existente.setEmail(datosNuevos.getEmail());

        // save() sobre un objeto que ya tiene id genera un UPDATE, no un INSERT.
        return clienteRepository.save(existente);
    }

    /** Elimina un cliente. Si el id no existe, no hace nada. */
    public void eliminar(Long id) {
        if (clienteRepository.existsById(id)) {
            clienteRepository.deleteById(id);
        }
    }

    /**
     * Suma un importe al saldo.
     * Devuelve null si el cliente no existe o si el importe no es positivo.
     */
    public Cliente acreditar(Long id, double importe) {
        Cliente cliente = buscarPorId(id);
        if (cliente == null || importe <= 0) {
            return null;
        }

        cliente.setSaldo(cliente.getSaldo() + importe);
        return clienteRepository.save(cliente);
    }
}
