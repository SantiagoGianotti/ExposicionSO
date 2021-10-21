package com.sistemasoperativos.ejercicio_nuestro;

import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;

import java.util.Queue;
import java.util.concurrent.Semaphore;

public class Torre extends Thread{
	Semaphore pistasDisponibles, mutex;
	Queue<Avion> filaDespegando, filaAterrizando;

	public Torre(int pistas, Queue<Avion> filaDespegando, Queue<Avion> filaAterrizando, Semaphore mutex) {
		this.filaDespegando = filaDespegando;
		this.filaAterrizando = filaAterrizando;
		this.mutex = mutex;
		this.pistasDisponibles = new Semaphore(pistas);
	}

	@Override
	public void run() {
		while(true)
		{
			try
			{
				mutex.acquire();

				//Se reserva
				pistasDisponibles.acquire();

				//Empujar los aviones que esten en fila
				elegirAvion();

				mutex.release();
			}
			catch (InterruptedException e)
			{
				System.out.println(e);
			}
		}

	}

	public void elegirAvion()
	{
		Avion avion;
		Queue fila;

		fila = elegirFila();
		if(fila != null)
		{

			avion = elegirFila().poll();
			avion.setAvionesEnPista(pistasDisponibles);
			avion.start();
		}
		else
		{
			pistasDisponibles.release();
		}
	}

	public Queue<Avion> elegirFila()
	{
		if(!filaAterrizando.isEmpty())
		{
			return filaAterrizando;
		}

		if(!filaDespegando.isEmpty())
		{
			return filaDespegando;
		}

		return null;
	}
}
