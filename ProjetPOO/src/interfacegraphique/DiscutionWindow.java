package interfacegraphique;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import autre.User;
import bdd.Bdd;
import reseau.ClientTCP;
import reseau.ClientUDP;
import reseau.ServerTCP;
import reseau.ServerUDP;
import test.TCPC;

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
    TCPC client;
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    ArrayList <ArrayList<String>> HistoryResultList;
    GridBagConstraints c;
    
	public DiscutionWindow(User user, TCPC client) {
		this.user=user;
		this.client=client;
		
		//Create and set up the window.
        Frame = new JFrame("Clavarding with "+user.getUsername());
        //Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Frame.setSize(new Dimension(300, 200));
        Frame.setLocationRelativeTo(null);
        //Create and set up the panel.
        Panel.setLayout(new GridBagLayout());
    	c = new GridBagConstraints();


        Thread hist = new Thread(new Runnable() {

        	public void run() {


        		String query; 
        		try { 
        			query ="SELECT * FROM history WHERE ipsrc=\'"+InetAddress.getLocalHost().toString()+"\' OR ipsrc=\'"+user.getAddress().toString()+"\' AND ipdest=\'"+InetAddress.getLocalHost().toString()+"\' OR ipdest=\'"+user.getAddress().toString()+"\'"; 
        			Bdd AskHistorique = new Bdd(query,"SELECT"); 
        			HistoryResultList=AskHistorique.ResultList; 
        			
        			for (ArrayList <String> S: HistoryResultList) {
        				if(S.get(0).equals(InetAddress.getLocalHost().toString())){
        					printMessage("Vous :");
        					printMessage(S.get(2)+"\n");
        					printMessage(S.get(3)+"\n"+"\n");
        				}else {
        					printMessage(user.getUsername()+" :");
        					printMessage(S.get(2)+"\n");
        					printMessage(S.get(3)+"\n"+"\n");
        				}
        			}
        			
        		} catch (UnknownHostException e1) { 
        			// TODO Auto-generated catch block 
        			e1.printStackTrace(); 
        		} 
        	} 
        });
        hist.start();

        
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
        		try {
					client.sockTCP.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        	 
        	  });
        
        //Set the default button.
        Frame.getRootPane().setDefaultButton(envoyer);
                
        Frame.setVisible(true);
        
        Thread recevoir = new Thread(new Runnable() {

			public void run() {
				boolean b =true;
				try {
					while(b) {
						//System.out.println("On a recu : "+client.input.readUTF());
						try {
							String message = client.input.readUTF();
							System.out.println("On a recu : "+message);
							printMessage(user.getUsername()+" : "+message+"\n");
							
							Calendar calendar = Calendar.getInstance();
							
						    printMessage(format.format(calendar.getTime())+"\n\n");
							new Bdd("INSERT INTO history VALUES (\'"+user.getAddress().toString()+"\',\'"+InetAddress.getLocalHost().toString()+"\',\'"+message+"\',\'"+format.format(calendar.getTime()).toString()+"\');","INSERT");

							}catch (EOFException e) {
								b=false;
								new PopupConnection("Le correspondant à quitter la conversation",user);
								Frame.dispose();
							}catch(SocketException i) {
								b=false;
								System.out.println("Connexion closed");
							}

					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		recevoir.start();

	}
	
	public DiscutionWindow(User user) {
		this.user=user;
		
		//Create and set up the window.
        Frame = new JFrame("Clavarding with "+user.getUsername());
        //Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Frame.setSize(new Dimension(300, 200));
        Frame.setLocationRelativeTo(null);
        //Create and set up the panel.
        Panel = new JPanel();
        Panel.setLayout(new GridBagLayout());
    	c = new GridBagConstraints();

        Thread hist = new Thread(new Runnable() {

        	public void run() {


        		String query; 
        		try { 
        			query ="SELECT * FROM history WHERE ipsrc=\'"+InetAddress.getLocalHost().toString()+"\' OR ipsrc=\'"+user.getAddress().toString()+"\' AND ipdest=\'"+InetAddress.getLocalHost().toString()+"\' OR ipdest=\'"+user.getAddress().toString()+"\'"; 
        			Bdd AskHistorique = new Bdd(query,"SELECT"); 
        			HistoryResultList=AskHistorique.ResultList; 
        			
        			for (ArrayList <String> S: HistoryResultList) {
        				if(S.get(0).equals(InetAddress.getLocalHost().toString())){
        					printMessage("Vous :");
        					printMessage(S.get(2)+"\n");
        					printMessage(S.get(3)+"\n"+"\n");
        				}else {
        					printMessage(user.getUsername()+" :");
        					printMessage(S.get(2)+"\n");
        					printMessage(S.get(3)+"\n"+"\n");
        				}
        			}
        			
        		} catch (UnknownHostException e1) { 
        			// TODO Auto-generated catch block 
        			e1.printStackTrace(); 
        		} 
        	} 
        });
        hist.start();
        
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
        		try {
					client.sockTCP.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					System.out.println("Socket closed bye");
				}
        	}
        	 
        	  });
        
        //Set the default button.
        Frame.getRootPane().setDefaultButton(envoyer);

        

		try {
			client=new TCPC(user.getAddress());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Thread recevoir = new Thread(new Runnable() {

			public void run() {
				boolean b =true;
				try {
					while(b) {
						//System.out.println("On a recu : "+client.input.readUTF());
						try {
							String message = client.input.readUTF();
							System.out.println("On a recu : "+message);
							printMessage(user.getUsername()+" : "+message+"\n");
							
							Calendar calendar = Calendar.getInstance();
							
						    printMessage(format.format(calendar.getTime())+"\n\n");
							new Bdd("INSERT INTO history VALUES (\'"+user.getAddress().toString()+"\',\'"+InetAddress.getLocalHost().toString()+"\',\'"+message+"\',\'"+format.format(calendar.getTime()).toString()+"\');","INSERT");

							}catch (EOFException e) {
								b=false;
								new PopupConnection("Le correspondant a quitter la conversation",user);
								Frame.dispose();
							}catch(SocketException i) {
								b=false;
								System.out.println("Connexion closed");
							}


					}
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		recevoir.start();
        
        
        Frame.setVisible(true);

	}
	
	private void addWidgets() {

    	
    	JLabel Discussion = new JLabel("<html><b>Discussion avec "+user.getUsername()+"</b></html>", SwingConstants.CENTER);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 1;
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 2;
    	Panel.add(Discussion, c);
    	
    	messages = new JTextArea();
    	messages.setEditable(false);
    	
    	JScrollPane scrollPane = new JScrollPane(
                messages,                          //Le contenu du JScrollPane
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,    //La barre verticale toujours visible
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); //La barre horizontale toujours visible
    	c.ipady = 500;      //make this component tall
    	c.ipadx = 300;
    	c.weightx = 0.0;
    	c.gridwidth = 2;
    	c.weightx = 0;
    	c.gridx = 0;
    	c.gridy = 1;
    	Panel.add(scrollPane, c);
    	
    	message = new JTextField();
    	c.gridwidth = 1;
    	c.ipady = 100;      //make this component tall
    	c.ipadx = 250;
    	c.weightx = 0;
    	c.gridx = 0;
    	c.gridy = 2;
    	Panel.add(message, c);
    	
    	envoyer = new JButton("Envoyer");
    	envoyer.addActionListener(this);
    	c.ipady = 100;      //make this component tall
    	c.ipadx = 50;
    	c.weightx = 1;
    	c.gridx = 1;
    	c.gridy = 2;
    	Panel.add(envoyer, c);
        
    	//Panel.add(Discussion);
    	//Panel.add(messages);
        //Panel.add(scrollPane, BorderLayout.CENTER);
    	//Panel.add(message);
    	//Panel.add(envoyer);

    	Discussion.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    	
		
		/*
		 * if (HistoryResultList!=null) {
		 * System.out.println("PAS VIDEEEEEEEEEEEEEEEEEE"); for
		 * (ArrayList<String>h:HistoryResultList) { try {
		 * if(h.get(0).equals(InetAddress.getLocalHost().toString())) {
		 * printMessage("Vous : "+h.get(2)); printMessage(h.get(3)); }else {
		 * printMessage(user.getUsername()+" : "+h.get(2)); printMessage(h.get(3)); } }
		 * catch (UnknownHostException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } } }
		 */
		 
    }

    public void actionPerformed(ActionEvent event) {
    	
    	System.out.println("Le message à envoyer est : " + message.getText());
    	try {
			client.sendMessage(message.getText());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	printMessage("Vous : "+message.getText()+"\n");
		
		Calendar calendar = Calendar.getInstance();
		
	    printMessage(format.format(calendar.getTime())+"\n\n");
	    
	    try {
			new Bdd("INSERT INTO history VALUES (\'"+InetAddress.getLocalHost().toString()+"\',\'"+user.getAddress().toString()+"\',\'"+message.getText()+"\',\'"+format.format(calendar.getTime()).toString()+"\');","INSERT");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    
	    message.setText("");

              
    }
    
    public void printMessage(String msg) {
    	messages.append(msg);
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
