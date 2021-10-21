package com.sistemasoperativos.ejercicio_nuestro;

import java.util.UUID;
import java.util.concurrent.Semaphore;

public class Avion extends Thread
{
	Estado estado;
	Semaphore pistasDisponibles;
	int id;

	public Avion(int id, Estado estado) {
		this.id = id;
		this.estado = estado;
	}

	@Override
	public void run() {

		try
		{
			System.out.println( "La torre reservo la pista para el avion["+this.id + "] que desea " + (this.estado == Estado.ATERRIZANDO? "ATERRIZAR" : "DESPEGAR") );
			System.out.println("Pistas disponibles:" + pistasDisponibles.availablePermits());
			//Tiempo que permanece en pista un avion
			sleep(4500/2);

			System.out.println( "El avion["+this.id + "] salio de pista \n Pistas disponibles: " + (pistasDisponibles.availablePermits() +1));
			//El avion ya uso la pista y la libera.
			pistasDisponibles.release();

		} catch (InterruptedException e)
		{
			System.out.println("Excepcion en avion");
		}
	}

	public void setAvionesEnPista(Semaphore pistasDisponibles) {
		this.pistasDisponibles = pistasDisponibles;
	}

	@Override
	public String toString() {
		return "Avion["+id+"]: " +estado;
	}
}
