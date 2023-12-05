package client;


/**
 * Interface, welches Methoden f&uuml;r die Transceiver-Klasse bereitstellt
 */
public interface Output {
	
	/**
	 * F&uuml;gt der Liste der GUI einen (weiteren) Participant hinzu.
	 * @param participant: String mit dem Participant-Namen
	 */
	public void writeInList(String participant);
	
	/**
	 * &Uuml;bermittelt die Nachrichten mit der aktuellen Uhrzeit in die Verlaufsanzeige-TextArea der GUI
	 * @param message: Eingegebener Text, der in die TextArea der GUI geschrieben wird
	 */
	public void writeInGUI(String message);
	
}