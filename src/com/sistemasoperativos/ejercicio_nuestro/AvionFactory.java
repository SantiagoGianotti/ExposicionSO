package com.sistemasoperativos.ejercicio_nuestro;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class AvionFactory extends Thread{
	static int id = 0;
	Semaphore avionesEsperando;
	Queue<Avion> filaDespegando, filaAterrizando;

	public AvionFactory(
		Queue<Avion> filaDespegando,
		Queue<Avion> filaAterrizando,
		Semaphore avionesEsperando
	)
	{
		this.avionesEsperando = avionesEsperando;
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

		System.out.println("Avion["+avion.id+"]: hace fila para "+ (avion.estado == Estado.ATERRIZANDO? "ATERRIZAR" : "DESPEGAR") );
		try
		{

			avionesEsperando.acquire();
			fila(estado).add(avion);
			avionesEsperando.release();

		}catch (InterruptedException e)
		{

		}
	}

	private Queue<Avion> fila(Estado estado)
	{
		return estado == Estado.ATERRIZANDO? filaAterrizando : filaDespegando;
	}
}
