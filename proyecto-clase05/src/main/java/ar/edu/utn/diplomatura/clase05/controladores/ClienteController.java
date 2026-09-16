package ar.edu.utn.diplomatura.clase05.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ar.edu.utn.diplomatura.clase05.entidades.Cliente;
import ar.edu.utn.diplomatura.clase05.servicios.ClienteService;

/**
 * Puerta de entrada HTTP. Recibe la peticion, llama al service y devuelve el
 * resultado. No lleva reglas de negocio.
 *
 * @RestController convierte a JSON lo que devuelven los metodos.
 * @RequestMapping("/clientes") es el prefijo comun de todas las rutas.
 */
@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * GET /clientes              -> todos
     * GET /clientes?apellido=xxx -> filtra por apellido
     *
     * @RequestParam es lo que va despues del "?" en la URL.
     */
    @GetMapping
    public List<Cliente> listar(@RequestParam(required = false) String apellido) {
        if (apellido == null || apellido.isBlank()) {
            return clienteService.listarTodos();
        }
        return clienteService.buscarPorApellido(apellido);
    }

    /**
     * GET /clientes/1
     *
     * @PathVariable toma el "1" de la URL.
     */
    @GetMapping("/{id}")
    public Cliente buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    /**
     * POST /clientes
     *
     * @RequestBody convierte el JSON del cuerpo en un objeto Cliente.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@RequestBody Cliente cliente) {
        return clienteService.crear(cliente);
    }

    /** PUT /clientes/1 */
    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Long id, @RequestBody Cliente datosNuevos) {
        return clienteService.actualizar(id, datosNuevos);
    }

    /** DELETE /clientes/1 */
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
    }

    /** POST /clientes/1/acreditaciones?importe=15000 */
    @PostMapping("/{id}/acreditaciones")
    public Cliente acreditar(@PathVariable Long id, @RequestParam double importe) {
        return clienteService.acreditar(id, importe);
    }
}
