package graphicalInterface;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.*;

import bdd.Bdd;
import networkTCP.TCPS;
import networkUDP.ClientUDP;
import networkUDP.ServerUDP;
import other.User;

public class Connected {
    JFrame Frame;
    JPanel Panel;
    JLabel Bienvenue;
    ArrayList<User> Liste;
    String username;
    JList<String> list;
    DefaultListModel<String> DLM;
    ServerUDP server;

    public Connected(ArrayList<User> Liste, String username, Boolean newCo) throws UnknownHostException, IOException {
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
		 
    	list = new JList<String>(DLM);
    	Panel.add(list);
    			
    	MouseListener mouseListener = new MouseAdapter() {
    		public void mouseClicked(MouseEvent e) {
    			int index = list.locationToIndex(e.getPoint());
    			
    			new DiscutionWindow(Liste.get(index));
    			
    		}
    	};
    	list.addMouseListener(mouseListener);


    	server = new ServerUDP(InetAddress.getLocalHost(),1024,username,Liste,DLM);
    	Thread serv = new Thread(server);
    	serv.start();
    	
    	if(newCo==true) {
	    	TCPS tcp=new TCPS(Liste);
	   	    tcp.start();
    	}


    	new Bdd("","CREATE");
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
               
                    new Connexion(false);
            }
        });
    	
    	Bienvenue = new JLabel("<html><b>Bonjour, voici les utilisateurs connectés</b></html>", SwingConstants.CENTER);
    		   
    	Panel.add(Bienvenue);
       
        Bienvenue.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }   
}