package graphicalInterface;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import bdd.Bdd;
import networkTCP.TCPC;
import networkUDP.ServerUDP;
import other.User;

public class DiscutionWindow implements ActionListener {
	
	User user;
    JFrame Frame;
    JPanel Panel;
    ArrayList<User> Liste;
    String username;
    DefaultListModel<String> DLM;
    ServerUDP server;
    JTextArea messages ;
    JTextField message;
    JButton envoyer;
    TCPC client;
    DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    ArrayList <ArrayList<String>> HistoryResultList;
    GridBagConstraints c;
    JLabel Discussion;
    
    
    //DiscutionWindow 
    //It has two constructors, 
    //both of which take into account the user with whom they want to communicate.

    //But, the first constructor also gives in parameter the TCP client server 
    //(when the server that listens in "Connected" must create a client because it has been asked for a connection).

    //In case we press the user we want to talk to it is not necessary because we create the client by opening the chat window.
	public DiscutionWindow(User user, TCPC client) {
		this.user=user;
		this.client=client;
		Panel=new JPanel();
		//Create and set up the window.
        Frame = new JFrame("Chat session");
        //Create and set up the panel.
        Panel.setLayout(new GridBagLayout());
    	//Create a grid bag constraint to dispose the widgets like we want
        c = new GridBagConstraints();

        //We create a new thread that will allow us to retrieve the history of messages when the window is opened. 
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
        //We start the thread
        hist.start();

        
        //Add the widgets.
        addWidgets();

        //Add the panel to the window.
        Frame.getContentPane().add(Panel, BorderLayout.CENTER);

        //Display the window.
        Frame.pack();

        //We choose what happens when the window is closed
        Frame.addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent e) {
        		//WHEN WE CLOSE WE CLOSE THE TCP CONNECTION
        		try {
        			client.sockTCP.close();
        		} catch (IOException e1) {
        			e1.printStackTrace();
        		}
        	}

        });
        
        //Set the default button.
        Frame.getRootPane().setDefaultButton(envoyer);
                
        Frame.setVisible(true);
        
        //We create a thread that allows us to receive messages via TCP.
        Thread recevoir = new Thread(new Runnable() {
			public void run() {
				boolean b =true;
				try {
					while(b) {
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
        Frame = new JFrame("Chat session");
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
        	public void windowClosing(WindowEvent e) {
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

        //We create the TCP client
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
    	Discussion = new JLabel("<html><b>Discussion avec "+user.getUsername()+"</b></html>", SwingConstants.CENTER);
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 1;
    	c.gridx = 0;
    	c.gridy = 0;
    	c.gridwidth = 2;
    	Panel.add(Discussion, c);
    	
    	messages = new JTextArea();
    	messages.setEditable(false);
    	
    	JScrollPane scrollPane = new JScrollPane(
                messages,                          //The contents of the JScrollPane
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,    //The vertical bar always visible
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); //The horizontal bar always visible
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

    	Discussion.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

    	//We refresh the username in case you change your user name in the meantime.
		Thread refreshName = new Thread(new Runnable() {

			public void run() {
				while(true) {
					Discussion.setText("<html><b>Discussion avec "+user.getUsername()+"</b></html>");
				}
			}
			});
		refreshName.start();
    }

	//When we press the send button we display it and we also add it to the database.
    public void actionPerformed(ActionEvent event) {
    	
    	System.out.println("Le message à envoyer est : " + message.getText());
    	try {
			client.sendMessage(message.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	printMessage("Vous : "+message.getText()+"\n");
		
		Calendar calendar = Calendar.getInstance();
		
	    printMessage(format.format(calendar.getTime())+"\n\n");
	    
	    try {
			new Bdd("INSERT INTO history VALUES (\'"+InetAddress.getLocalHost().toString()+"\',\'"+user.getAddress().toString()+"\',\'"+message.getText()+"\',\'"+format.format(calendar.getTime()).toString()+"\');","INSERT");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} 
	    
	    message.setText("");       
    }
    
    public void printMessage(String msg) {
    	messages.append(msg);
    }

}
