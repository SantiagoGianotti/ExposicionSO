package com.sistemasoperativos.ejercicio_resuelto;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ControladorPuente extends Thread{
	private Queue<Vehiculo> vehiculosNorte, vehiculosSur;
	private Semaphore mutex, vehiculosEnPuente;
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
			Semaphore mutex
	) {
		this.mutex = mutex;
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

				//Reservo un espacio en el puente
				vehiculosEnPuente.acquire();
				mutex.acquire();

				if( !filaActual().isEmpty() )
				{
					agregarVehiculo();
					sleep(1000);
				}
				else if(!filaSiguiente().isEmpty() && noHayVehiculosCirculando())
				{
					cambiarSentido();
					vehiculosEnPuente.release();	//Libero el espacio
				}
				else
				{
					vehiculosEnPuente.release();	//Libero el espacio
				}

				mutex.release();

			} catch (InterruptedException e){
				System.out.println("Interrupcion en agregar vehiculo");
			}



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
		System.out.println("Los vehiculos que vienen del " + direccionActual + " estan circulando.");
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
