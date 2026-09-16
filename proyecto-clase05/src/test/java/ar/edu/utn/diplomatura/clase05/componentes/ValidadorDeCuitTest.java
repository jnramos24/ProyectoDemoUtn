package ar.edu.utn.diplomatura.clase05.componentes;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorDeCuitTest {

    private ValidadorDeCuit validador;

    @BeforeAll
    static void antesQueTodosLosTests(){
        System.out.println("Esto es antes que todo");
    }

    @BeforeEach
    void antesQueCadaTests(){
        validador = new ValidadorDeCuit();
        System.out.println("Antes que cada test");
    }

    @AfterEach
    void despuesQueCadaTest(){
        System.out.println("Termino el test");
    }

    @AfterAll
    static void alFinalizarTodo(){
        System.out.println("Aca termino todo");
    }

    @Test
    void cuitConGuionesEsValido(){
        //given
        //Lo hace BeforeEach
        // when
        boolean resultado = validador.esValido("20-1234567-9");
        // then
        assertTrue(resultado);
    }

    @Test
    void cuitConLetrasNoEValido(){
        //given
        //Lo hace BeforeEach
        // when
        boolean resultado = validador.esValido("AB09tydfdY");
        // then
        assertFalse(resultado);
    }

    @Test
    void normalizarDeNullDevuelveNull(){
        //when y then
        assertNull(validador.normalizar(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"2012345678", "HZ-1234567-B", "  2730123456  ", "27-3541122-3"})
    void variosFormatosDeCuit(String cuit){
        assertTrue(validador.esValido(cuit),"Debería ser válido"+ cuit);
    }

}