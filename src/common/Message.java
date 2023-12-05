package common;


/**
 * The Message class represents a message sent within the system.
 * It encapsulates a command and its associated content.
 */
public class Message {

	private String command;
	private String message;


	/**
	 * Constructs a new Message with the specified command and message content.
	 *
	 * @param command The keyword for different actions.
	 * @param message The content of the command. If null is passed, it defaults to an empty string.
	 */
	public Message(String command, String message) {
		this.command = command;
		this.message = (message == null) ? "" : message;
	}


	/**
	 * Returns a string representation of the Message object, combining command and message content.
	 *
	 * @return A string in the format "command: message".
	 */
	@Override
	public String toString() {
		return command + ": " + message;
	}
}