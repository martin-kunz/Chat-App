package client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.*;
import common.Message;


/**
 * Represents the client-side Graphical User Interface for the chat application.
 * This class is responsible for initializing and displaying the chat window,
 * handling user interactions, and managing connections to the server.
 */
public class ClientGUI extends JFrame {


	/**
	 * Main method that initializes the GUI.
	 * @param args Command line arguments, not used in this application.
	 */
	public static void main(String[] args) {
		ClientGUI frame = new ClientGUI();
		frame.window.setVisible(true);
	}
	
	
	private static final long serialVersionUID = 1L;
	public JFrame window;
	public JLabel ipAddressLabel, portLabel, nameLabel, participantsListLabel, chatLabel, messageLabel;
	public JTextField portField, ipAddressField, nameField, messageField;
	public JButton connectButton, disconnectButton, sendButton;
	public static JTextArea chatArea;
	public JScrollPane scrollbar;
	public static DefaultListModel<String> modellingList = new DefaultListModel<String>();
	public JList<String> participantsList;
	
	Transceiver transceiver = new Transceiver();
	PrintStream printStream;
	Socket socket;
	PrintWriter printWriter;
	BufferedReader bufferedReader;
	BufferedReader br;
	Message message;


	/**
	 * Constructs a new ClientGUI instance and initializes the GUI components.
	 */
	public ClientGUI() {
		initialize();
	}

	/**
	 * Initializes and sets up the GUI components and layout of the chat window.
	 */
	public void initialize() {

		window = new JFrame();
		window.setTitle("Chat-Client");
		window.setBounds(100, 100, 500, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setLayout(null);

		//Port
		portLabel = new JLabel("Port:");
		portLabel.setBounds(10, 15, 90, 10);
		window.add(portLabel);
		portField = new JTextField();
		portField.setBounds(95, 10, 80, 20);
		window.add(portField);

		//IP-Address:
		ipAddressLabel = new JLabel("IP-Address:");
		ipAddressLabel.setBounds(210, 15, 90, 10);
		window.add(ipAddressLabel);
		ipAddressField = new JTextField();
		ipAddressField.setBounds(280, 10, 140, 20);
		window.add(ipAddressField);
		portField.setColumns(10);

		//Username
		nameLabel = new JLabel("Username:");
		nameLabel.setBounds(10, 50, 90, 10);
		window.add(nameLabel);
		nameField = new JTextField();
		nameField.setBounds(95, 45, 80, 20);
		window.add(nameField);

		//Connect button
		connectButton = new JButton("Connect");
		connectButton.setBounds(210, 45, 100, 20);
		window.add(connectButton);
		connectButton.addActionListener(new ActionListener() {
			/**
			 * Anonyme Klasse f&uuml;r den Connect-Button
			 */
			public void actionPerformed(ActionEvent ae) {
				try {
					socket = new Socket(ipAddressField.getText(), Integer.parseInt(portField.getText()));

					printWriter = new PrintWriter(socket.getOutputStream(), true);
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					message = new Message("connect", nameField.getText());
					transceiver.sendToStream(printWriter, message);
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					/**
					 * Thread, sowie Start des Threads, f&uuml;r das Auslesen des Streams, zum Empfang von kommenden Nachrichten vom Server.
					 *
					 */
					Thread readerThread = new Thread(new Runnable() {
						public void run() {
							transceiver.readStream(br);
						}
					});
					readerThread.start();

					connectButton.setEnabled(false);
					disconnectButton.setEnabled(true);
				} catch (Exception e) {
					transceiver.writeInGUI("Error: " + e);
				}
			}
		});

		//Disconnect buttopm
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setBounds(320, 45, 100, 20);
		window.add(disconnectButton);
		disconnectButton.addActionListener(new ActionListener() {
			/**
			 * Anonyme Klasse f&uuml;r den Disconnect-Button, zum Trennen vom Server
			 */
			public void actionPerformed(ActionEvent ae) {
				try {
					message = new Message("disconnect", nameField.getText());
					transceiver.sendToStream(printWriter, message);
					connectButton.setEnabled(true);
					disconnectButton.setEnabled(false);
					modellingList.clear();
				} catch (NullPointerException npe) {
					transceiver.writeInGUI("Es besteht keine Verbindung zu einem Server. (" + npe + ")");
				}
			}
		});

		//Chat
		chatLabel = new JLabel("Chat:");
		chatLabel.setBounds(10, 80, 50, 20);
		window.add(chatLabel);
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setBounds(10, 100, 280, 390);
		chatArea.setEditable(false);
		window.add(chatArea);

		//Participant label/list
		participantsListLabel = new JLabel("Participants:");
		participantsListLabel.setBounds(325, 80, 100, 15);
		window.add(participantsListLabel);
		participantsList = new JList<String>();
		participantsList.setBounds(325, 100, 150, 150);
		participantsList.setModel(modellingList);
		window.add(participantsList);

		//Message/Send button
		messageLabel = new JLabel("Message:");
		messageLabel.setBounds(10, 500, 460, 15);
		window.add(messageLabel);

		//Scrollbar Area
		scrollbar = new JScrollPane();
		scrollbar.setBounds(10, 100, 300, 400);
		scrollbar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollbar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollbar.setViewportView(chatArea);
		window.add(scrollbar);

		//Send button
		sendButton = new JButton("Send");
		sendButton.setBounds(345, 460, 100, 40);
		window.add(sendButton);
		sendButton.addActionListener(new ActionListener() {
			/**
			 * Anonynme Klasse f&uuml;r den Senden-Button von eingegeben Nachrichten
			 */
			public void actionPerformed(ActionEvent ae) {
				try {
					message = new Message("message", messageField.getText());
					transceiver.sendToStream(printWriter, message);
					messageField.setText("");
				} catch (NullPointerException npe) {
					transceiver.writeInGUI("Sending not possible: " + npe);
				}
			}
		});

		messageField = new JTextField();
		messageField.setBounds(10, 520, 465, 22);
		window.add(messageField);
		messageField.addActionListener(new ActionListener() {
			/**
			 * Anonyme Klasse zum Senden von Nachrichten &uuml;ber das Eingabefield (Enter-Taste)
			 */
			public void actionPerformed(ActionEvent e) {
				message = new Message("message", messageField.getText());
				transceiver.sendToStream(printWriter, message);
				messageField.setText("");
			}
		});
	}
}