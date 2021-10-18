package com.sistemasoperativos.ejercicio_resuelto;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

public class ControladorPuente extends Thread{
	private Queue<Vehiculo> vehiculosCirculando, vehiculosNorte, vehiculosSur;
	private Semaphore semaforo;
	private int tiempoMaximo;
	private Direccion direccionActual = Direccion.NORTE;
	private int timer = 0;

	/**
	 *
	 * @param tiempo - Tiempo maximo que permanece activo un semaforo.
	 */
	public ControladorPuente(
			int tiempo,
			Queue<Vehiculo> vehiculosNorte,
			Queue<Vehiculo> vehiculosSur,
			Semaphore semaforo
	) {
		this.semaforo = semaforo;
		this.tiempoMaximo = tiempo * 10^6;

		//Vehiculos que estan circulando en el puente
		this.vehiculosCirculando = new LinkedList<Vehiculo>();

		//Vehiculos esperando en el semaforo norte o sur.
		this.vehiculosNorte = vehiculosNorte;
		this.vehiculosSur = vehiculosSur;
	}

	@Override
	public void run()
	{
		while(true)
		{
			timer++;
			if(deboCambiarSentido())
			{
				System.out.println("Debo cambiar sentido");
				cambiarSentido();

				//El return evita que se llegue al metodo controlarAccesoDeVehiculos
			}

			controlarAccesoDeVehiculos();
			controlarEgresoDeVehiculos();
		}
	}


	private void controlarAccesoDeVehiculos()
	{
		Vehiculo vehiculo;

		try
		{

			//Si la fila actual no esta vacia, se empuja un vehiculo al puente
			if( !filaActual().isEmpty() )
			{
				semaforo.acquire();

				vehiculo = filaActual().poll();
				vehiculosCirculando.add(vehiculo);
				vehiculo.start();

				semaforo.release();

			}
		}
		catch (InterruptedException e)
		{
			System.out.println(e);
		}
	}

	private void controlarEgresoDeVehiculos()
	{
		Vehiculo vehiculo;

		try
		{
			semaforo.acquire();
			if(!hayVehiculosCirculando())
			{
				return;
			}

			vehiculo = vehiculosCirculando.peek();

			if(vehiculo.salio())
			{
				vehiculosCirculando.poll();
			}
			vehiculosCirculando.remove();

			semaforo.release();
		}catch (InterruptedException e)
		{
			System.out.println(e);
		}

	}

	private boolean deboCambiarSentido()
	{
		if(timer % (10^6) == 0)
		{

			System.out.println("Fila actual: " + filaActual().size() + "\nFila siguiente: " + filaSiguiente().size() + "\nCirculan: " + vehiculosCirculando.size());
		}
		//Si la fila actual esta vacia pero la siguiente no, se cambia el sentido.
		if(filaActual().isEmpty() && !filaSiguiente().isEmpty())
		{
			return true;
		}

		//Si la fila siguiente no esta vacia, y se termino el tiempo, se cambia el sentido.
		return timer >= tiempoMaximo && !filaSiguiente().isEmpty();
	}

	private boolean hayVehiculosCirculando()
	{
		return !vehiculosCirculando.isEmpty();
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
		System.out.println("Semaforo:" + direccionActual);

		//Se espera hasta que no hayan vehiculos en el puente
		if(hayVehiculosCirculando())
		{
			System.out.println("Circulan vehiculos, timer: " + (timer >= tiempoMaximo));
			return;
		}

		//Se abre el otro semaforo
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
