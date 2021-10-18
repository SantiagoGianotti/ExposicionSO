package com.sistemasoperativos.ejercicio_resuelto;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Ruta extends Thread {
	private Queue<Vehiculo> vehiculosNorte, vehiculosSur;
	private Semaphore semaforo;

	//La clase ruta produce los vehiculos en el norte y el sur.
	public Ruta(
			Queue<Vehiculo> vehiculosNorte,
			Queue<Vehiculo> vehiculosSur,
			Semaphore semaforo
	){
		this.vehiculosNorte = vehiculosNorte;
		this.vehiculosSur = vehiculosSur;
		this.semaforo = semaforo;
	}

	@Override
	public void run()
	{
		while (true)
		{
			//Tiempo al azar para generar vehiculos
			int time = (int) (Math.random() * 4500 + 10);

			try
			{
				sleep(time);
			}
			catch (InterruptedException e)
			{
				System.out.println(e);
			}

			generarVehiculo();
		}
	}

	private void generarVehiculo()
	{
		Random rd = new Random();
		Direccion direccion = rd.nextBoolean()? Direccion.NORTE : Direccion.SUR;
		System.out.println(direccion);

		Vehiculo vehiculo = new Vehiculo(direccion);

		try
		{
			semaforo.acquire();
			fila(direccion).add(vehiculo);
			semaforo.release();

			System.out.println(vehiculosNorte);
		}
		catch (InterruptedException e)
		{
			System.out.println(e);
		}
	}

	private Queue<Vehiculo> fila(Direccion direccion)
	{
		return direccion == Direccion.NORTE?
				vehiculosNorte:
				vehiculosSur;
	}
}
