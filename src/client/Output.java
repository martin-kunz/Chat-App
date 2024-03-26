package client;

/**
 * Interface that provides methods for the Transceiver class
 */
public interface Output {

	/**
	 * Adds another participant to the GUI list.
	 *
	 * @param participant: String containing the participant's name
	 */
	public void writeInList(String participant);

	/**
	 * Transmits the messages with the current time to the history display TextArea of the GUI.
	 *
	 * @param message: Entered text that is written into the GUI's TextArea
	 */
	public void writeInGUI(String message);

}
