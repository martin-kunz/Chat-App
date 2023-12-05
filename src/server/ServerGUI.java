package server;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import java.util.Map.*;


/**
 * This class represents the Graphical User Interface (GUI) for the server side.
 * It includes the main method for executing the server application.
 * The GUI provides controls for starting, stopping, and terminating the server,
 * as well as displaying the chat and participant information.
 */
public class ServerGUI {
	
	private JLabel port;
	private JFrame window;
	private JScrollPane scrollbar;
	private JButton terminate, start, stop;
	private JLabel participantsList, chat;
	private JList<String> participantList;
	public static JTextArea chatTextArea;
	public static JTextField portNumber;
	public static DefaultListModel<String> list = new DefaultListModel<String>();
	public Server starting = new Server();
	public Transceiver transceiver = new Transceiver();


	/**
	 * Constructor for ServerGUI.
	 * Initializes the GUI components.
	 */
	public ServerGUI() {
		initialize();
	}


	/**
	 * Initializes and arranges all the GUI components.
	 */
	private void initialize() {
		
		//neuen Frame erstellen
		window = new JFrame();
		window.setTitle("Server");
		window.setBounds(100, 100, 600, 700);
		window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window.setResizable(false);
		window.getContentPane().setLayout(null);
		
		//Port
		port = new JLabel("Port:");
		port.setBounds(10, 11, 89, 14);
		window.getContentPane().add(port);
		portNumber = new JTextField();
		portNumber.setBounds(100, 8, 97, 20);
		window.getContentPane().add(portNumber);
		portNumber.setColumns(10);
		
		//Start Button
		start = new JButton("Start");
		start.setBounds(287, 627, 89, 23);
		window.getContentPane().add(start);
		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent action) {
				try {
					ServerGUI.portNumber.setEditable(false);
					starting = new Server();
					starting.start();
					start.setEnabled(false);
					stop.setEnabled(true);
				}
				catch (Exception e) {
					transceiver.writeInGUI(e.getMessage() + "\n");
				}		
			}
		});
		
		//Stop-Button
		stop = new JButton("Stop");
		stop.setBounds(386, 627, 89, 23);
		window.getContentPane().add(stop);
		stop.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent action) {
				try{
					Date zeit = new Date();
					SimpleDateFormat format = new SimpleDateFormat("HH:mm");
					ServerGUI.portNumber.setEditable(true);
					starting.interrupt();
					
					Map<Thread,StackTraceElement[]> alleThreads;
					alleThreads = ServerThreading.getAllStackTraces();
					Thread threadKiller = new Thread();
					for(Entry<Thread, StackTraceElement[]> ste : alleThreads.entrySet()){
						threadKiller = ste.getKey();
						threadKiller.interrupt();
					}
					
					transceiver.writeInGUI("Server was terminated at " + format.format(zeit) + "h!\n");
					start.setEnabled(true);
					stop.setEnabled(false);
				} 
				catch (Exception e){
					transceiver.writeInGUI(e.getMessage() + "\n");
				}
			}
		});
	
		//Beenden-Button
		terminate = new JButton("Terminate");
		terminate.setBounds(485, 627, 89, 23);
		window.getContentPane().add(terminate);
		terminate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent action) {

				int selection = JOptionPane.showConfirmDialog(null, "Do you really want to terminate the server?", "Caution!", JOptionPane.YES_OPTION);
				if (selection == 0) {
					System.exit(0);
				}
			}
		});
		
		//Chatverlauf-Label
		chat = new JLabel("Chat:");
		chat.setBounds(10, 59, 46, 14);
		window.getContentPane().add(chat);
		
		//Scrollbar
		scrollbar = new JScrollPane();
		scrollbar.setBounds(10, 84, 327, 481);
		scrollbar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollbar.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		window.getContentPane().add(scrollbar);
		
		//Verlauf TextArea
		chatTextArea = new JTextArea();
		chatTextArea.setEditable(false);
		chatTextArea.setLineWrap(true);
		scrollbar.setViewportView(chatTextArea);
		
		//Participant-Label/-Liste
		participantsList = new JLabel("Participants:");
		participantsList.setBounds(365, 59, 112, 14);
		window.getContentPane().add(participantsList);
		
		participantList = new JList<String>();
		participantList.setBounds(365, 84, 209, 169);
		participantList.setModel(list);
		window.getContentPane().add(participantList);
	}


	/**
	 * The main method to start the Server GUI.
	 * It ensures that the GUI is launched in the Event Dispatch Thread.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				ServerGUI gui = new ServerGUI();
				gui.window.setVisible(true);
			}
		});
	}
}