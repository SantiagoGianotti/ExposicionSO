package com.sistemasoperativos.ejercicio_nuestro;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AvionFactory extends Thread{
	static int id = 0;
	Semaphore mutex;
	Queue<Avion> filaDespegando, filaAterrizando;

	public AvionFactory(
		Queue<Avion> filaDespegando,
		Queue<Avion> filaAterrizando,
		Semaphore mutex
	)
	{
		this.mutex = mutex;
		this.filaAterrizando = filaAterrizando;
		this.filaDespegando = filaDespegando;
	}

	@Override
	public void run() {
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

			generarAvion();
		}
	}

	private void generarAvion()
	{
		id++;
		Random rd = new Random();
		Estado estado = rd.nextBoolean()? Estado.ATERRIZANDO : Estado.DESPEGANDO;

		Avion avion = new Avion(id, estado);

		try
		{
			//Cerrar mutex
			mutex.acquire();

			System.out.println("Avion["+avion.id+"]: hace fila para "+ (avion.estado == Estado.ATERRIZANDO? "ATERRIZAR" : "DESPEGAR") );

			//Agregar avion en una de las filas
			fila(estado).add(avion);

			//Liberar mutex
			mutex.release();

		}catch (InterruptedException e)
		{

		}
	}

	private Queue<Avion> fila(Estado estado)
	{
		return estado == Estado.ATERRIZANDO? filaAterrizando : filaDespegando;
	}
}
