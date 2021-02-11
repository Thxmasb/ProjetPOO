package networkUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import other.User;

public class ServerUDP implements Runnable {
	InetAddress iplocal;
	int portlocal;
	InetAddress ipdest;
	int portdest;
	String message;
	public String receivedMessage;
	public DatagramSocket dgramSocket;
	ArrayList<User> ListUser;
	DefaultListModel<String> DLM;
	boolean running=true;
	
	public ServerUDP(InetAddress iplocal,int portlocal,String message, ArrayList<User> ListUser,DefaultListModel<String> DLM) throws IOException {
		
		this.iplocal=iplocal;
		this.portlocal=portlocal;
		this.message=message;
	
		try{
			//Construct a datagram socket and bind it to the local port
			this.dgramSocket = new DatagramSocket(portlocal);
		}catch (SocketException e){
			System.out.println("On close");
		}
		this.ListUser=ListUser;	
		this.DLM=DLM;
	}
	

	public void run() {
		while(running) {
			byte[] buffer = new byte[256];
			try {
				//Datagram of the received message
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
				dgramSocket.receive(inPacket);
				ipdest = inPacket.getAddress();
				portdest = inPacket.getPort();
				receivedMessage = new String(inPacket.getData(), 0, inPacket.getLength());
				System.out.println("Received Message :"+receivedMessage);
				
				//A new user is created by decomposing the information of the received datagram
				User user=new User(receivedMessage,ipdest,portdest);
				
				//if the received message is equal to "Deconnexion" 
				//we remove the user from the list of active ones otherwise we add it
				if (!receivedMessage.equals(message)) {
					if(receivedMessage.equals("Deconnexion")) {
						deconnexion(user);
					}else {
						update(user);
					}
				}
			} catch (IOException e) {
				System.out.println("Closure Exception");
				running=false;
			}

			try{
				//we create a datagram to send our information to the other user
				DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), ipdest, portdest);
				dgramSocket.send(outPacket);
			} catch (IOException | IllegalArgumentException e) {
				System.out.println("Exception");
			}
			System.out.println("Sending the message:"+message);
		}
	}
	
	//Function to refresh the list of active users
	public void update(User user) {
		boolean exist=false;
		for(User u:ListUser) {
			if (u.getUsername().equals(user.getUsername())) {
				exist=true;
				break;
			}
			if (u.getAddress().equals(user.getAddress())) {
				System.out.println("We update the user who has changed user");
				DLM.removeElement(u.getUsername());
				u.setUsername(user.getUsername());
				DLM.addElement(user.getUsername());
				exist=true;
				System.out.println(ListUser);	
			}
		}
		if(!exist) {
			System.out.println("We update the list");
			ListUser.add(user);
			DLM.addElement(user.getUsername());
			System.out.println(ListUser);
		}
	}

	//Function to delete a user who logs out
	public void deconnexion(User user) {
		for(User u:ListUser) {
			if (u.getAddress().equals(user.getAddress())) {
				System.out.println("We delete the user who disconnects");
				DLM.removeElement(u.getUsername());
				System.out.println(ListUser);
			}
		}
	}
}
