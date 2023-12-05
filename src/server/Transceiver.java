package server;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import common.*;


/**
 * The Transceiver class is responsible for sending and receiving messages in the chat application.
 * It handles message delivery, displaying messages in the GUI, and updating participant lists.
 */
public class Transceiver {
	
	public static Socket socket;
	public static Message message;
	public static PrintWriter printWriter;


	/**
	 * Sends a message over a PrintWriter.
	 * @param printWriter: The PrintWriter to send the message through.
	 * @param message: The Message object containing the message to be sent.
	 * @return String: Returns the string representation of the sent message.
	 */
	public String send(PrintWriter printWriter, Message message) {
		printWriter.println(message.toString());
		return message.toString();
	}


	/**
	 * Writes a message to the GUI chat area.
	 * This method appends the provided message to the chat area in the server's GUI.
	 * @param message The message to be displayed in the chat area.
	 */
	public void writeInGUI(String message) {
		ServerGUI.chatTextArea.append(message + "\n");
	}


	/**
	 * Updates the participants list in the GUI.
	 * This method retrieves all participants and sends the updated list to each participant's client.
	 */
	public void sendParticipantsList() {
		ServerGUI.list.clear();
		Participant[] p = Server.participantsList.allParticipants();
		String participantsList = "";
		String teilnehmer;

		for (Participant value: p) {
			participantsList += value.getName() + ":";
		}
		message = new Message("Participants", participantsList);

		for (Participant participant: p) {
			socket = participant.getSocket();
			teilnehmer = participant.getName();

			try {
				printWriter = new PrintWriter(socket.getOutputStream(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			send(printWriter, message);
			writeInList(teilnehmer);

		}
	}


	/**
	 * Adds a participant to the GUI's participant list.
	 * This method is used to update the server GUI with the name of a new participant.
	 * @param p: The name of the participant to be added to the list.
	 */
	public void writeInList(String p) {
		ServerGUI.list.addElement(p);
	}


	/**
	 * Sends a message to all participants in the chat.
	 * This method broadcasts a message to all connected clients, including the sender's name and the message content.
	 * @param message: The keyword for the type of message.
	 * @param rest: The content of the message.
	 * @param p: The participant who sent the message.
	 * @param ps: Participants object containing all users.
	 * @return String: Returns the username and the associated message.
	 */
	public String sendToAll(String message, String rest, Participant p, Participants ps) {
		String name = p.getName();
		Participant[] part = ps.allParticipants();
		Transceiver.message = new Message(message, name + ": " + rest);
		Date zeit = new Date();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm");
		
		for (int i = 0; i < part.length; i++) {
			socket = part[i].getSocket();
			try {
				printWriter = new PrintWriter(socket.getOutputStream(), true);
			} 
			catch (IOException e) {
				writeInGUI(e.toString());
			}
			send(printWriter, Transceiver.message);
		}
		return name + ": " + rest + " (" + format.format(zeit) + ")";
	}
}