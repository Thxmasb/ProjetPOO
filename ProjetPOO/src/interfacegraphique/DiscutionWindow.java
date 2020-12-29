package interfacegraphique;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import autre.User;
import reseau.ClientTCP;
import reseau.ClientUDP;
import reseau.ServerTCP;
import reseau.ServerUDP;

public class DiscutionWindow implements ActionListener {
	
	User user;
    JFrame Frame;
    JPanel Panel;
  
    ArrayList<User> Liste;
    String username;
    JList list;
    DefaultListModel<String> DLM;
    ServerUDP server;
    JTextArea messages ;
    JTextField message;
    JButton envoyer;
    ClientTCP client;
	
	public DiscutionWindow(User user) {
		this.user=user;
		
		//Create and set up the window.
        Frame = new JFrame("Clavarding with "+user.getUsername());
        //Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(new Dimension(120, 40));
        Frame.setLocationRelativeTo(null);
        //Create and set up the panel.
        Panel = new JPanel(new GridLayout(4, 1));

        //Add the widgets.
        addWidgets();


        //Add the panel to the window.
        Frame.getContentPane().add(Panel, BorderLayout.CENTER);

        //Display the window.
        Frame.pack();
        
        Frame.addWindowListener(new WindowAdapter() {
        	 
        	@Override
        	 
        	public void windowClosing(WindowEvent e) {
        	 
        		//QUAND ON FERME ON CLOSE LA CONNEXION TCP
        	}
        	 
        	  });
        
        //Set the default button.
        Frame.getRootPane().setDefaultButton(envoyer);

        
    	Thread clientTCP = new Thread(new Runnable() {

    		public void run() { 
    			client=new ClientTCP(user.getAddress(),5000);
    			client.SocketClientTCP(message.getText());
    		} 
    	}); 

        clientTCP.start();
       
        
        
        Frame.setVisible(true);

	}
	
	private void addWidgets() {

    	
    	JLabel Discussion = new JLabel("<html><b>Discussion avec "+user.getUsername()+"</b></html>", SwingConstants.CENTER);
    		   
    	messages = new JTextArea();
    	messages.setEditable(false);
    	 
    	message = new JTextField();
         
    	envoyer = new JButton("Envoyer");
    	envoyer.addActionListener(this);
    	
        
    	Panel.add(Discussion);
    	Panel.add(messages);
    	Panel.add(message);
    	Panel.add(envoyer);

    	Discussion.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    public void actionPerformed(ActionEvent event) {
    	
    	System.out.println("Le message à envoyer est : " + message.getText());
    	
		

              
    }
    
	
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     * @throws UnknownHostException 
     */
	/*
	 * private static void createAndShowGUI() throws UnknownHostException { //Make
	 * sure we have nice window decorations.
	 * JFrame.setDefaultLookAndFeelDecorated(true); DiscutionWindow discutionWindow
	 * = new DiscutionWindow(new User("thotho",InetAddress.getLocalHost(),5000)); }
	 * 
	 * public static void main(String[] args) { //Schedule a job for the
	 * event-dispatching thread: //creating and showing this application's GUI.
	 * javax.swing.SwingUtilities.invokeLater(new Runnable() { public void run() {
	 * try { createAndShowGUI(); } catch (UnknownHostException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } } }); }
	 */
	 
}
