package com.sistemasoperativos.ejercicio_resuelto;

import java.util.UUID;

public class Vehiculo extends Thread {
	private Direccion direccion;
	private UUID uuid;
	private Boolean termino;

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

			//Simulamos el tiempo que se demora en atravezar el puente.
			sleep(5000);

			termino = true;

			System.out.println(this.toString() + ": Salio del puente");
		} catch (InterruptedException e)
		{
			System.out.println(e);
		}


	}

	public Boolean salio()
	{
		return termino;
	}
}
