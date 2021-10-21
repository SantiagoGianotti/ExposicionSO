package com.sistemasoperativos.ejercicio_resuelto;

import java.util.concurrent.Semaphore;

public class Vehiculo extends Thread {
	private Direccion direccion;
	private int uuid;
	private Semaphore espacioEnPuente;

	public Vehiculo(int id, Direccion direccion) {
		this.direccion = direccion;
		this.uuid = id;
	}

	@Override
	public String toString() {
		return "Vehiculo[" + uuid +", " + direccion + "]";
	}

	@Override
	public void run() {
		try {
			//Simulamos el tiempo que se demora en atravesar el puente.

			System.out.println(this + ": Avanza desde el " + this.direccion);
			sleep(5000);

			System.out.println(this + ": Salio del puente.");
			//Liberamos un espacio en el semaforo
			espacioEnPuente.release();
		} catch (InterruptedException e)
		{
			System.out.println(e);
		}
	}

	public void setSemaforo(Semaphore semaforo)
	{
		this.espacioEnPuente = semaforo;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public int getUuid() {
		return uuid;
	}
}
