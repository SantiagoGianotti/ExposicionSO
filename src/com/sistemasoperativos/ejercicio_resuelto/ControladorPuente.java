package com.sistemasoperativos.ejercicio_resuelto;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ControladorPuente extends Thread{
	private Queue<Vehiculo> vehiculosNorte, vehiculosSur;
	private Semaphore semaforo, vehiculosEnPuente;
	private int capacidad;
	private Direccion direccionActual = Direccion.NORTE;

	/**
	 *
	 * @param tiempo - Tiempo maximo que permanece activo un semaforo.
	 */
	public ControladorPuente(
			int capacidad,
			Queue<Vehiculo> vehiculosNorte,
			Queue<Vehiculo> vehiculosSur,
			Semaphore semaforo
	) {
		this.semaforo = semaforo;
		this.capacidad = capacidad;

		//El puente tiene un tamaño maximo para 10 autos en la misma dirección
		this.vehiculosEnPuente = new Semaphore(capacidad);

		//Vehiculos esperando en el semaforo norte o sur.
		this.vehiculosNorte = vehiculosNorte;
		this.vehiculosSur = vehiculosSur;
	}

	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				vehiculosEnPuente.acquire(); //tiene que haber lugar en el puente
				semaforo.acquire();

				//Reviso si hay mas espacio para vehiculos.
				if( !filaActual().isEmpty() )
				{
					agregarVehiculo();
				}
				//Si no hay mas elementos en fila, el puente esta vacio y la fila siguiente tiene elementos
				else if(!filaSiguiente().isEmpty() && noHayVehiculosCirculando())
				{
					//cambio el sentido de circulación del puente.
					cambiarSentido();
					vehiculosEnPuente.release();
				}
				else
				{
					vehiculosEnPuente.release();
				}

			} catch (InterruptedException e){
				System.out.println("Interrupcion en agregar vehiculo");
			}

			semaforo.release();

		}
	}

	private void agregarVehiculo()
	{
		Vehiculo vehiculo;
		vehiculo = filaActual().poll();

		//Le paso el semaforo al vehiculo asi lo administra el.
		vehiculo.setSemaforo(vehiculosEnPuente);
		vehiculo.start();
	}

	private boolean noHayVehiculosCirculando()
	{
		return vehiculosEnPuente.availablePermits() == capacidad - 1;
	}

	private Queue<Vehiculo> filaActual()
	{
		return direccionActual == Direccion.NORTE?
				vehiculosNorte:
				vehiculosSur;
	}

	private Queue<Vehiculo> filaSiguiente()
	{
		return direccionActual != Direccion.NORTE?
				vehiculosNorte:
				vehiculosSur;
	}

	private void cambiarSentido()
	{
		cambiarDireccionActual();
		System.out.println("Semaforo:" + direccionActual);
	}


	private void cambiarDireccionActual()
	{
		if(direccionActual == Direccion.NORTE)
		{
			direccionActual = Direccion.SUR;
		}
		else
		{
			direccionActual = Direccion.NORTE;
		}
	}
}
