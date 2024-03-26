package server;
import java.util.*;
import common.*;


/**
 /**
 * This class provides methods to manage individual participants.
 * It includes functionalities to add, remove, and manipulate participant data.
 */
public class Participants {
	
	
	private ArrayList<Participant> participant = new ArrayList<>();


	/**
	 * Default constructor for Participants class.
	 */
	public Participants() {
	}


	/**
	 * Adds a participant to the ArrayList. Ensures that the participant list does not exceed a certain size and that each participant has a unique name.
	 *
	 * @param p: The participant to be added.
	 * @return boolean: Returns true if the participant is successfully added, otherwise false.
	 */
	public synchronized boolean add(Participant p) {
		if (participant.size() == 3) {
			return false;
		}
		for (int i = 0; i < participant.size(); i++) {
			if (participant.get(i).getName().equals(p.getName())) {
				return false;
			}
		}
		participant.add(p);
		return true;
	}


	/**
	 * Deletes a participant from the ArrayList.
	 *
	 * @param p: The participant to be removed.
	 * @return boolean: Returns true if the participant is successfully deleted, otherwise false.
	 */
	public synchronized boolean delete(Participant p) {
		if (participant.contains(p)) {
			participant.remove(p);
			return true;
		} 
		else {
			return false;					
		}
	}


	/**
	 * Retrieves all participants.
	 *
	 * @return Array: Returns an array of all participants.
	 */
	public synchronized Participant[] allParticipants() {
		int size;
		size = participant.size();
		Participant[] p = new Participant[size];
		
		for (int i = 0; i < participant.size(); i++) {
			p[i] = participant.get(i);
		}
		return p;
	}


	/**
	 * Returns the size of the participants list.
	 *
	 * @return int: Returns the number of participants in the list.
	 */
	public synchronized int size() {
		if (participant == null || participant.size() == 0) {
			return 0;
		}
		else {
			return participant.size();
		}
	}
}
