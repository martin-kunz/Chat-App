package server;
import java.net.*;
import common.*;
import java.io.*;


/**
 * The ServerThreading class is responsible for handling multi-threaded operations of the server.
 * It extends the Thread class and manages communication with individual clients through sockets.
 * This class is crucial for enabling concurrent handling of multiple client connections.
 */
public class ServerThreading extends Thread {
	
	public Socket socket;
	public Transceiver transceiver = new Transceiver();


	/**
	 * Constructor for ServerThreading.
	 * Initializes a new thread for handling client-server communication.
	 * @param socket: The socket associated with the client connection.
	 */
	public ServerThreading(Socket socket) {
		this.socket = socket;
	}


	/**
	 * The main method executed when the thread starts.
	 * It handles the client-server communication, processing messages, and managing the connection.
	 */
	@Override
	public void run() {
		// The run method includes code for handling different types of messages:
		// 1. Connect - to handle new client connections.
		// 2. Disconnect - to manage client disconnection requests.
		// 3. Message - to process and relay messages to other clients.
		// It also includes exception handling to manage errors during communication.

		String input = null;
		Message message = null;
		Participant participant = null;
		
	try {
			
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		while((input = in.readLine()) != null) {
			try {
				if(isInterrupted()){
					in.close();
					out.close();
					socket.close();
					Server.participantsList.delete(participant);
					break;
				}
			}
			catch(IOException e) {
				transceiver.writeInGUI(e.toString());
			}
		
			String[] splittedInput = input.split(":", 2);
			String name = splittedInput[1];
			
			try {
				
				//Connect
				if ((splittedInput[0].equals("connect"))) {
	
					participant = new Participant(name, socket);
					transceiver.writeInGUI(input);
					
					if (Server.participantsList.size() < 3) {
						boolean added;
						added = Server.participantsList.add(participant);
						if (added == false) {
							message = new Message("refused", "name_in_use");
							transceiver.writeInGUI(transceiver.send(out, message));
							socket.close();
						} 
						else if (added == true) {
							message = new Message("connect", "ok");
							transceiver.writeInGUI(transceiver.send(out, message));
							transceiver.sendParticipantsList();
						}	
					}
					else if (Server.participantsList.size() == 3) {
						message = new Message("refused", "too_many_users");
						transceiver.writeInGUI(transceiver.send(out, message));
						socket.close();
					}
				}
			
				//Disconnect
				else if (splittedInput[0].equals("disconnect")) {
					if (splittedInput[1].equals("")) {
						message = new Message("disconnect", "ok");
						transceiver.writeInGUI(transceiver.send(out, message));
						socket.close();
						Server.participantsList.delete(participant);
						transceiver.sendParticipantsList();
					} 
					else if (!splittedInput[1].equals("")) {
						message = new Message("disconnect", "invalid_command");
						transceiver.writeInGUI(transceiver.send(out, message));
						socket.close();
						Server.participantsList.delete(participant);
						transceiver.sendParticipantsList();
					}
				}
			
				//Message
				else if (splittedInput[0].equals("message")) {
					transceiver.writeInGUI(transceiver.sendToAll("message", splittedInput[1], participant, Server.participantsList));
				}
				
			}
			catch (IllegalArgumentException e) {
				message = new Message("refused", "invalid_name");
				transceiver.writeInGUI(transceiver.send(out, message));
				socket.close();
			}
		}
	} 
	catch (Exception e) {
		transceiver.writeInGUI(e.getMessage());
	}
	
	
	try {
		socket.close();
	} 
	catch (IOException e) {
		e.printStackTrace();
	}

	}
}