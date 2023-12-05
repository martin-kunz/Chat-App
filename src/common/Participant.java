package common;
import java.net.*;


/**
 * The Participant class is responsible for maintaining properties of a chat participant.
 * It enforces constraints on the participant's name and associates a socket with the participant.
 */
public class Participant {
	
	private String participant;
	private Socket socket;

	/**
	 * Constructs a new Participant with the provided name and socket.
	 * Validates the participant name according to specified rules.
	 *
	 * @param p The name of the participant. Must be non-null, not longer than 30 characters, and not contain ":".
	 * @param s The socket on which the server is communicating.
	 * @throws IllegalArgumentException if the participant name is invalid.
	 */
	public Participant(String p, Socket s) {
		if (p.equals("") || p.equals(null) || p.length() > 30 || p.contains(":")) {
			throw new IllegalArgumentException("refused: invalid_name");
		} 
		else {
			this.participant = p;
			this.socket = s;
		}
	}


	/**
	 * Returns the socket associated with this participant.
	 *
	 * @return The socket through which the participant is connected.
	 */
	public Socket getSocket() {
		return socket;
	}


	/**
	 * Returns the name of the participant.
	 *
	 * @return The name of the participant.
	 */
	public String getName() {
		return participant;
	}


	/**
	 * Returns a string representation of the Participant object, combining participant name and socket information.
	 *
	 * @return A string representing the participant and their socket.
	 */
	@Override
	public String toString() {
		return this.participant + this.socket;
	}
}