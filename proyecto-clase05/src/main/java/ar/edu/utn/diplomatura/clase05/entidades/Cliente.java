package ar.edu.utn.diplomatura.clase05.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Un cliente del banco.
 *
 * @Entity le dice a JPA que esta clase se guarda en una tabla:
 * la clase es la tabla, cada atributo es una columna y cada objeto es una fila.
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    // El id lo genera la base sola. Por eso no se manda al crear un cliente.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String apellido;

    /** CUIT sin guiones. */
    private String cuit;

    private String email;

    private double saldo;

    /** Constructor vacio: JPA lo necesita para reconstruir el objeto. */
    public Cliente() {
    }

    public Cliente(String nombre, String apellido, String cuit, String email, double saldo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.cuit = cuit;
        this.email = email;
        this.saldo = saldo;
    }

    // Getters y setters: se usan para armar el JSON de las respuestas.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCuit() {
        return cuit;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }
}
