package com.sistemasoperativos;

import com.sistemasoperativos.ejercicio_nuestro.Avion;
import com.sistemasoperativos.ejercicio_nuestro.AvionFactory;
import com.sistemasoperativos.ejercicio_nuestro.Torre;
import com.sistemasoperativos.ejercicio_resuelto.ControladorPuente;
import com.sistemasoperativos.ejercicio_resuelto.Ruta;
import com.sistemasoperativos.ejercicio_resuelto.Vehiculo;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {
//        simularEjercicioResuelto();

        simularEjercicioNuestro();

    }

    private static void simularEjercicioResuelto()
    {
        //Inicializo el semaforo
        Semaphore semaforo = new Semaphore(1);

        //Inicializo las filas
        LinkedList<Vehiculo> filaNorte = new LinkedList<>();
        LinkedList<Vehiculo> filaSur = new LinkedList<>();

        //Inicializo el puente y la ruta
        ControladorPuente puente = new ControladorPuente(10, filaNorte, filaSur, semaforo);
        Ruta ruta = new Ruta(filaNorte,filaSur, semaforo);

        //Ejecuto el sistema.
        puente.start();
        ruta.start();


    }

    private static void simularEjercicioNuestro()
    {
        //Inicializo el semaforo
        Semaphore avionesEsperando = new Semaphore(1);

        //Inicializo las filas
        LinkedList<Avion> filaAterrizando = new LinkedList<>();
        LinkedList<Avion> filaDespegando = new LinkedList<>();

        Torre torre = new Torre(2, filaDespegando, filaAterrizando, avionesEsperando);
        AvionFactory factory = new AvionFactory(filaDespegando, filaAterrizando, avionesEsperando);

        torre.start();
        factory.start();
    }
}
