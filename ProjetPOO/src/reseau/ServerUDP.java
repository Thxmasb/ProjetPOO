package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import autre.User;

public class ServerUDP implements Runnable {
	InetAddress iplocal;
	int portlocal;
	InetAddress ipdest;
	int portdest;
	String message;
	public String messagerecu;
	public DatagramSocket dgramSocket;
	ArrayList<User> ListUser;
	DefaultListModel<String> DLM;
	
	public ServerUDP(InetAddress iplocal,int portlocal,String message, ArrayList<User> ListUser,DefaultListModel<String> DLM) throws IOException {
		
		this.iplocal=iplocal;
		this.portlocal=portlocal;
		this.message=message;
	
		try{
			this.dgramSocket = new DatagramSocket(portlocal);
		}catch (SocketException e){
			System.out.println("On close");
		}
		this.ListUser=ListUser;	
		this.DLM=DLM;
	}
	

	@Override
	public void run() {
		while(true) {
			byte[] buffer = new byte[256];
			DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
			try {
				dgramSocket.receive(inPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}

			ipdest = inPacket.getAddress();
			portdest = inPacket.getPort();

			messagerecu = new String(inPacket.getData(), 0, inPacket.getLength());
			System.out.println("Message reçu:"+messagerecu);
			
			User user=new User(messagerecu,ipdest,portdest);
			
			if (!messagerecu.equals(message)) {
				if(messagerecu.equals("Deconnexion")) {
					deconnexion(user);
				}else {
					update(user);
				}
			}

			DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), ipdest, portdest);
			try {
				dgramSocket.send(outPacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Envoi du message:"+message);
		}
	}
	
	public void update(User user) {
		boolean exist=false;
		for(User u:ListUser) {
			if (u.getUsername().equals(user.getUsername())) {
				exist=true;
				break;
			}
			if (u.getAddress().equals(user.getAddress())) {
				System.out.println("On met à jour l'utilisateur qui a changer de user");
				DLM.removeElement(u.getUsername());
				u.setUsername(user.getUsername());
				DLM.addElement(user.getUsername());
				exist=true;
				System.out.println(ListUser);	
			}
		}
		if(!exist) {
			System.out.println("On met à jour la liste");
			ListUser.add(user);
			DLM.addElement(user.getUsername());
			System.out.println(ListUser);
		}
	}

	public void deconnexion(User user) {
		for(User u:ListUser) {
			if (u.getAddress().equals(user.getAddress())) {
				System.out.println("On supprime l'utilisateur qui se déconnecte");
				DLM.removeElement(u.getUsername());
				//ListUser.remove(u);
				System.out.println(ListUser);
			}
		}
	}
}
