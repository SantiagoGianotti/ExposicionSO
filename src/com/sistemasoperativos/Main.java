package com.sistemasoperativos;

import com.sistemasoperativos.ejercicio_resuelto.ControladorPuente;
import com.sistemasoperativos.ejercicio_resuelto.Ruta;
import com.sistemasoperativos.ejercicio_resuelto.Vehiculo;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class Main {

    public static void main(String[] args) {

        System.out.println("Inicia la simulación del ejercicio resuelto.");

        simularEjercicioResuelto();

        System.out.println("\n\n\n\n \t\t Finalizo la simulación del ejercicio resuelto");

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
}
