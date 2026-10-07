package ar.edu.utn.diplomatura.clase05.excepciones;

public class ClienteNoEncontradoException extends RuntimeException{

    public ClienteNoEncontradoException(Long id){
        super("No existe un cliente con el id " + id);
    }
}
