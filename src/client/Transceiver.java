package client;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import common.Message;

/**
 * The Transceiver class is responsible for receiving and sending messages. It also handles connecting and disconnecting from the server as well as managing the name list.
 */
public class Transceiver implements Output {

	Date date;
	SimpleDateFormat format;

	/**
	 * This method reads the incoming stream and compares predefined strings agreed with the server.
	 *
	 * @param br: The BufferedReader required to read the data from the stream established through the socket
	 */
	public void readStream(BufferedReader br) {

		String msg;
		String[] msgparts = new String[4];

		try {
			while((msg = br.readLine()) != null) {

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
	 *
	 * @param out the PrintWriter used to send messages via the stream associated with the socket.
	 * @param msg the Message object to be sent to the server.
	 */
	public void sendToStream(PrintWriter out, Message msg) {
		out.println(msg.toString());
		out.flush();
	}

	/**
	 * Adds a participant's name to the GUI list. This method is used to update the list of participants in the GUI.
	 *
	 * @param participant the name of the participant to be added to the list.
	 */
	@Override
	public void writeInList(String participant) {
		ClientGUI.modellingList.addElement(participant);
	}

	/**
	 * Writes a message to the GUI. This method formats the message with the current time and appends it to the chat area in the GUI, providing users with a real-time update of conversations.
	 *
	 * @param message the message to be displayed in the GUI.
	 */
	@Override
	public void writeInGUI(String message) {
		date = new Date();
		format = new SimpleDateFormat("HH:mm");
		ClientGUI.chatArea.append(message + " (" + format.format(date) + ")" + "\n");
	}
}