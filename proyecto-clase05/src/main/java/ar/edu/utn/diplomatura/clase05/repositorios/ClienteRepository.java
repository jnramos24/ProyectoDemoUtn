package ar.edu.utn.diplomatura.clase05.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.edu.utn.diplomatura.clase05.entidades.Cliente;

/**
 * Acceso a la base de datos.
 *
 * Es una interfaz vacia a proposito: al extender JpaRepository, Spring genera
 * la implementacion solo y ya vienen hechos findAll(), findById(), save(),
 * deleteById(), existsById() y count().
 *
 * Los genericos son <Entidad, TipoDelId>.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Spring arma el SQL leyendo el nombre del metodo:
    // SELECT * FROM clientes WHERE UPPER(apellido) LIKE UPPER('%?%')
    List<Cliente> findByApellidoContainingIgnoreCase(String apellido);
}
