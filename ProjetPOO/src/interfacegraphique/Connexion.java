
package interfacegraphique;

/**
 * CelsiusConverter.java is a 1.4 application that 
 * demonstrates the use of JButton, JTextField and
 * JLabel.  It requires no other files.
 */

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

import reseau.ClientUDP;

public class Connexion implements ActionListener {
    JFrame converterFrame;
    JPanel converterPanel;
    JTextField username;
    JLabel UsernameLabel, TextLabel;
    JButton connexion;
    JButton newcompte;
    ArrayList<String> LoginList=new ArrayList<>();
    
    public Connexion() {
        //Create and set up the window.
        converterFrame = new JFrame("Clavarding - Connexion");
        converterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        converterFrame.setSize(new Dimension(120, 40));
        converterFrame.setLocationRelativeTo(null);
        //Create and set up the panel.
        converterPanel = new JPanel(new GridLayout(4, 1));

        //Add the widgets.
        addWidgets();

        //Set the default button.
        converterFrame.getRootPane().setDefaultButton(connexion);

        //Add the panel to the window.
        converterFrame.getContentPane().add(converterPanel, BorderLayout.CENTER);

        //Display the window.
        converterFrame.pack();
        converterFrame.setVisible(true);
    }

    /**
     * Create and add the widgets.
     */
    private void addWidgets() {
        //Create widgets.
        username = new JTextField();
       
        connexion = new JButton("Connexion");
        
        UsernameLabel = new JLabel("<html><b>Veuillez rentrer un username</b></html>", SwingConstants.CENTER);
        TextLabel = new JLabel("<html><b>Bonjour, veuillez vous connectez</b></html>", SwingConstants.CENTER);

        //Listen to events from the Convert button.
        connexion.addActionListener(this);

        //Add the widgets to the container.
        converterPanel.add(TextLabel);
        converterPanel.add(UsernameLabel);
        converterPanel.add(username);
        converterPanel.add(connexion);
        
        UsernameLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        TextLabel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    public void actionPerformed(ActionEvent event) {
    	int utiliser=0;
    	ArrayList<String> LoginList = new ArrayList<String>();

    	ClientUDP client = new ClientUDP(username.getText(), LoginList); 
    	Thread cli1 = new Thread(client);
    	cli1.start();

    	try { 
    		Thread.sleep(3000); 
    	} 
    	catch (InterruptedException e) {
    		e.printStackTrace(); 
    	}
    	System.out.println("Liste:"+LoginList);


    	for(int j=0;j<LoginList.size();j+=3) {
    		if(LoginList.get(j).equals(username.getText())) {
    			utiliser=1;
    			Popup popup=new Popup("Username déjà utiliser!");
    		}
    	}
    	if(utiliser==0) {
    		try {
				Connected connected=new Connected(LoginList,username.getText());
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		converterFrame.dispose();
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
        Connexion converter = new Connexion();
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