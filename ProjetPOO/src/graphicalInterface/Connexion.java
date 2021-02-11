
package graphicalInterface;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

import networkUDP.ClientUDP;
import other.User;

public class Connexion implements ActionListener {
    JFrame Frame;
    JPanel Panel;
    JTextField username;
    JLabel UsernameLabel, TextLabel;
    JButton connection;
    ArrayList<String> LoginList=new ArrayList<>();
    Boolean newCo;
    
    public Connexion(Boolean newCo) {
    	this.newCo=newCo;
        //Create and set up the window.
        Frame = new JFrame("Clavarding - Connexion");
        Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(new Dimension(120, 40));
        Frame.setLocationRelativeTo(null);
        
        //Create and set up the panel.
        Panel = new JPanel(new GridLayout(4, 1));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        Frame.getRootPane().setDefaultButton(connection);

        //Add the panel to the window.
        Frame.getContentPane().add(Panel, BorderLayout.CENTER);

        //Display the window.
        Frame.pack();
        Frame.setVisible(true);
    }

    /**
     * Create and add the widgets.
     */
    private void addWidgets() {
        //Create widgets.
    	//Text box to write the username
        username = new JTextField();
       
        //Connection button
        connection = new JButton("Connexion");
        
        //Labels
        UsernameLabel = new JLabel("<html><b>Veuillez rentrer un username</b></html>", SwingConstants.CENTER);
        TextLabel = new JLabel("<html><b>Bonjour, veuillez vous connectez</b></html>", SwingConstants.CENTER);

        //Listen to events from the connection button.
        connection.addActionListener(this);

        //Add the widgets to the container.
        Panel.add(TextLabel);
        Panel.add(UsernameLabel);
        Panel.add(username);
        Panel.add(connection);
        
        //Set label' borders
        UsernameLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        TextLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    //Quise trigger function when clicking on the connection button
    public void actionPerformed(ActionEvent event) {
    	int utiliser=0;
    	//List where we're going to store all the connected users who will answer us when we log in
    	ArrayList<User> UserList = new ArrayList<User>();
    	
    	//Creation of a new UDP client that will allow the computer to send a login message to all connected users
    	//and receive a message from all connected users. 
    	//The client is a thread in order to allow the interface and the client to work at the same time.
    	ClientUDP client = new ClientUDP(username.getText(), UserList); 
    	Thread cli1 = new Thread(client);
    	cli1.start();
    	try { 
    		Thread.sleep(3000); 
    	} 
    	catch (InterruptedException e) {
    		e.printStackTrace(); 
    	}
    	//List printed
    	System.out.println("List: "+UserList);

    	//We check in the list of users that no other user is wearing the same username.
    	//If a user has the same one, a popup appears saying that the username is already taken, the user has to choose another one.
    	//If no user has the same username the connection is made
    	//Finally, the connected interface is opened and the connection interface is closed.
    	for(int j=0;j<UserList.size();j++) {
    		if(UserList.get(j).getUsername().equals(username.getText())) {
    			utiliser=1;
    			new Popup("Username déjà utiliser!");
    		}
    	}
    	if(utiliser==0) {
    		try {
				new Connected(UserList,username.getText(),newCo);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
    		Frame.dispose();
    	}


    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        new Connexion(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}