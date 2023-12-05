package client;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import common.Message;


/**
 * In der Transceiver-Klasse wird der Empfang und das Versenden von Nachrichten realisiert, welche auch für das Verbinden und Trennen vom Server sowie für die Namenliste zust&auml;ndig ist.
 * @author Martin Kunz
 */
public class Transceiver implements Output {
	
	Date date;
	SimpleDateFormat format;


	/**
	 * Diese Methode liest den ankommenden Stream aus und vergleicht mit dem Server vereinbarte Strings.
	 * @param br: Der BufferedReader, welcher ben&ouml;tigt wird, um die Daten aus dem Stream, welcher durch den Socket festgelegt ist, wieder auszulesen
	 */
	public void readStream(BufferedReader br) {

		String msg;
		String[] msgparts = new String[4];

		try {
			while((msg = br.readLine())	!= null) {
				
				msgparts = msg.split(":", 2);

				if (msgparts[0].equals("message")) {
					writeInGUI(msgparts[1]);
				}
			
				else if (msgparts[0].equals("disconnect")) {
					writeInGUI("You are now disconnected!");
				}

				else if(msgparts[0].equals("connect")) {
					writeInGUI("You are now connected!");
				}
			
				else if (msgparts[0].equals("refused")) {
					writeInGUI("Error: " + msg);
				}	
			
				else if (msgparts[0].equals("Participants")) {
					ClientGUI.modellingList.clear();
					String[] namelistarr = msgparts[1].split(":");
				
					for (int i = 0; i < namelistarr.length; i++) {
						writeInList(namelistarr[i]);
					}				
				}	
			}
		} 
		catch (Exception e) {
			writeInGUI("Error: " + e.getMessage());
		}
	}


	/**
	 * Sends messages to the server using the provided PrintWriter.
	 * @param out the PrintWriter used to send messages via the stream associated with the socket.
	 * @param msg the Message object to be sent to the server.
	 */
	public void sendToStream(PrintWriter out, Message msg) {
		out.println(msg.toString());
		out.flush();
	}

	
	@Override
	public void writeInList(String participant) {
		ClientGUI.modellingList.addElement(participant);
	}

	
	@Override
	public void writeInGUI(String message) {
		date = new Date();
		format = new SimpleDateFormat("HH:mm");
		ClientGUI.chatArea.append(message + " (" + format.format(date) + ")" + "\n");
	}
}