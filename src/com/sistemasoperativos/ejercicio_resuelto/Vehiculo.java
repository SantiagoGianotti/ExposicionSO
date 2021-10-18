package com.sistemasoperativos.ejercicio_resuelto;

import java.util.UUID;
import java.util.concurrent.Semaphore;

public class Vehiculo extends Thread {
	private Direccion direccion;
	private UUID uuid;
	private Semaphore espacioEnPuente;

	public Vehiculo(Direccion direccion) {
		this.direccion = direccion;
		this.uuid = UUID.randomUUID();
	}

	@Override
	public String toString() {
		return "Vehiculo[" + uuid +", " + direccion + "]";
	}

	@Override
	public void run() {
		try {
			System.out.println(this.toString() + ": Avanza hacia el " + direccion);

			//Simulamos el tiempo que se demora en atravesar el puente.
			sleep(5000);

			espacioEnPuente.release();

			System.out.println(this.toString() + ": Salio del puente");
		} catch (InterruptedException e)
		{
			System.out.println(e);
		}


	}

	public void setSemaforo(Semaphore semaforo)
	{
		this.espacioEnPuente = semaforo;
	}
}
