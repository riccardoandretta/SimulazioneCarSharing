package it.polito.tdp.carsharing;

import java.util.PriorityQueue;

public class Simulatore {

	// tipi di eventi gestiti dal simulatore
	enum EventType { // classe accessibile solo dall'interno della classe Simulatore
		CUSTOMER_IN, // arriva un nuovo cliente
		CAR_RETURNED // viene restituita un'auto
	}

	class Event implements Comparable<Event> {
		// un evento contiene due info obbligatorie (tempo e tipo) ed una facoltativa
		// (variabile)
		private int minuti; // minuti a partire dall'inizio della simulazione
		private EventType tipo;

		public Event(int minuti, EventType tipo) {
			super();
			this.minuti = minuti;
			this.tipo = tipo;
		}

		public int getMinuti() {
			return minuti;
		}

		public EventType getTipo() {
			return tipo;
		}

		// i setter non ci servono, perch� gli oggetti verranno inseriti nella coda in
		// base ai loro valori, per cui se li cambiassi sarebbe un casino
		@Override
		public int compareTo(Event other) {
			return this.minuti - other.minuti;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + minuti;
			result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Event other = (Event) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (minuti != other.minuti)
				return false;
			if (tipo != other.tipo)
				return false;
			return true;
		}

		private Simulatore getOuterType() {
			return Simulatore.this;
		}

		@Override
		public String toString() {
			return "Event [minuti=" + minuti + ", tipo=" + tipo + "]";
		}

	}

	// Coda degli eventi
	private PriorityQueue<Event> coda = new PriorityQueue<>();

	// Parametri di simulazione // Impostati all'inizio // Costanti durante la
	// simulazione
	private int NC = 20; // numero auto
	private int T_IN = 10; // tempo arrivo nuovi clienti
	private int T_TRAVEL_BASE = 60; // durata minima del viaggio
	private int T_TRAVEL_DURATA = 3; // quanti periodi di durata base pu� durare un viaggio

	// Modello del mondo (stato del sistema, i valori cambiano in continuazione)
	private int disponibili; // numero auto disponibili

	// Valori da calcolare
	private int clientiArrivati; // clienti arrivati al noleggio
	private int clientiInsoddisfatti; // numero di clienti insoddisfatti

	/**
	 * Crea la coda iniziale di eventi ed inizializza correttamente tutte le
	 * variabili di simulazione
	 * 
	 * @param durataMax
	 *            durata complessiva della simulazione, in minuti
	 */
	public void init(int durataMax) {

		// inizializza la coda
		coda.clear();
		int time = 0;
		while (time <= durataMax) {
			Event e = new Event(time, EventType.CUSTOMER_IN);
			coda.add(e);
			time = time + T_IN;
		}
		// inizializzo le variabili di simulazione
		disponibili = NC;
		clientiArrivati = 0;
		clientiInsoddisfatti = 0;
	}

	public void run() {

		Event e;
		while ((e = coda.poll()) != null) {
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		System.out.println(e);
		switch (e.getTipo()) {

		case CUSTOMER_IN:

			clientiArrivati++;

			if (disponibili > 0) {
				// clienti soddisfatto (--> gli diamo l'auto e prevediamo quando torner�)
				disponibili--;
				int durata = T_TRAVEL_BASE * (1 + (int) (Math.random() * T_TRAVEL_DURATA));
				// schedulo l'evento di rientro, che � scatenato dal fatto che un cliente abbia
				// preso un auto
				Event rientro = new Event(e.getMinuti() + durata, EventType.CAR_RETURNED);
				coda.add(rientro);
			} else {
				// cliente insoddisfatto
				clientiInsoddisfatti++;
			}
			break;

		case CAR_RETURNED:
			disponibili++;
			break;

		}
	}

	public int getNC() {
		return NC;
	}

	public void setNC(int nC) {
		NC = nC;
	}

	public int getT_IN() {
		return T_IN;
	}

	public void setT_IN(int t_IN) {
		T_IN = t_IN;
	}

	public int getT_TRAVEL_BASE() {
		return T_TRAVEL_BASE;
	}

	public void setT_TRAVEL_BASE(int t_TRAVEL_BASE) {
		T_TRAVEL_BASE = t_TRAVEL_BASE;
	}

	public int getT_TRAVEL_DURATA() {
		return T_TRAVEL_DURATA;
	}

	public void setT_TRAVEL_DURATA(int t_TRAVEL_DURATA) {
		T_TRAVEL_DURATA = t_TRAVEL_DURATA;
	}

	public int getClientiArrivati() {
		return clientiArrivati;
	}

	public int getClientiInsoddisfatti() {
		return clientiInsoddisfatti;
	}

}
