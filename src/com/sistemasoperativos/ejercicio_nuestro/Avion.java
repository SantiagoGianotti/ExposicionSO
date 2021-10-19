package com.sistemasoperativos.ejercicio_nuestro;

import java.util.UUID;
import java.util.concurrent.Semaphore;

public class Avion extends Thread
{
	Estado estado;
	Semaphore avionesEnPista;
	int id;

	public Avion(int id, Estado estado) {
		this.id = id;
		this.estado = estado;
	}

	@Override
	public void run() {

		System.out.println("El avion " + this.id + " esta " + this.estado + ", se ocupo una pista.");
		System.out.println("Pistas restantes: " + avionesEnPista.availablePermits());

		try
		{
			//Simulamos que esta avanzando en la pista
			sleep(4500/2);
		} catch (InterruptedException e)
		{
			System.out.println("Excepcion en avion");
		}

		avionesEnPista.release();
		System.out.println("El avion " + this.id + " ha " + (this.estado == Estado.ATERRIZANDO? "ATERRIZADO" : "DESPEGADO") + ", se libero una pista.");
		System.out.println("Pistas restantes: " + avionesEnPista.availablePermits());
	}

	public void setAvionesEnPista(Semaphore avionesEnPista) {
		this.avionesEnPista = avionesEnPista;
	}

	@Override
	public String toString() {
		return "Avion["+id+"]: " +estado;
	}
}
