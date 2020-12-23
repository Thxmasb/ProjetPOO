package interfacegraphique;

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
import reseau.ServerUDP;
import autre.User;

public class Connected implements ActionListener {
    JFrame Frame;
    JPanel Panel;
    JLabel Bienvenue;
    ArrayList<User> Liste;
    String username;
    JList list;
    DefaultListModel<String> DLM;
    ServerUDP server;

    public Connected(ArrayList<User> Liste, String username) throws UnknownHostException, IOException {
    	this.Liste=Liste;
        //Create and set up the window.
        Frame = new JFrame("Clavarding - Connected");
        //Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Frame.setSize(new Dimension(120, 40));
        Frame.setLocationRelativeTo(null);
        //Create and set up the panel.
        Panel = new JPanel(new GridLayout(3, 1));

        //Add the widgets.
        addWidgets();


        //Add the panel to the window.
        Frame.getContentPane().add(Panel, BorderLayout.CENTER);

        //Display the window.
        Frame.pack();
        
        
        
        Frame.addWindowListener(new WindowAdapter() {
        	 
        	@Override
        	 
        	public void windowClosing(WindowEvent e) {
        	 
        		ClientUDP client = new ClientUDP("Deconnexion", Liste); 
            	Thread cli1 = new Thread(client);
            	cli1.start();
            	System.exit(0);
        	}
        	 
        	  });
        
        
        
        Frame.setVisible(true);
        
        DLM=new DefaultListModel<String>();
    	
		for(int i=0;i<Liste.size();i++) { 
			DLM.addElement(Liste.get(i).getUsername());
		}
		 
    	list = new JList(DLM);
    			Panel.add(list);
        //ServerUDP server = new ServerUDP(InetAddress.getLocalHost(),1024,username);
        //server.updateArrayList(Liste);
        
        server = new ServerUDP(InetAddress.getLocalHost(),1024,username,Liste,DLM);
    	Thread serv = new Thread(server);
    	serv.start();
    	
    	
    	
    }

    /**
     * Create and add the widgets.
     */
    private void addWidgets() {
        //Create widgets.

    	JMenuBar barre=new JMenuBar();
    	Panel.add(barre);
    	JMenu clavarding= new JMenu("Clavarding"); 
    	barre.add(clavarding);
    	JMenuItem changeUsername = new JMenuItem("Change Username");
    	JMenuItem exit = new JMenuItem("Exit");
    	clavarding.add(changeUsername);
    	clavarding.add(exit);
    	exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                    System.exit(0);
            }
        });
    	changeUsername.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                    Frame.dispose();
                    server.dgramSocket.close();
                    new Connexion();
            }
        });
    	
    	Bienvenue = new JLabel("<html><b>Bonjour, voici les utilisateurs connectés</b></html>", SwingConstants.CENTER);
    		   
    	Panel.add(Bienvenue);
		
    	
    	
    	
        
       
        Bienvenue.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    public void actionPerformed(ActionEvent event) {
    	
    	

              
    }


    
}