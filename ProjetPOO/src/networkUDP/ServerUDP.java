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
	public String messagerecu;
	public DatagramSocket dgramSocket;
	ArrayList<User> ListUser;
	DefaultListModel<String> DLM;
	boolean running=true;
	
	public ServerUDP(InetAddress iplocal,int portlocal,String message, ArrayList<User> ListUser,DefaultListModel<String> DLM) throws IOException {
		
		this.iplocal=iplocal;
		this.portlocal=portlocal;
		this.message=message;
	
		try{
			this.dgramSocket = new DatagramSocket(portlocal);
			//dgramSocket.setSoTimeout(500);
		}catch (SocketException e){
			System.out.println("On close");
		}
		this.ListUser=ListUser;	
		this.DLM=DLM;
	}
	

	@Override
	public void run() {
		while(running) {
			byte[] buffer = new byte[256];
			try {
				DatagramPacket inPacket = new DatagramPacket(buffer, buffer.length);
				dgramSocket.receive(inPacket);
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
			} catch (IOException e) {
				System.out.println("Exception de fermeture");
				running=false;
			}



			try{
				DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), ipdest, portdest);
				dgramSocket.send(outPacket);
			} catch (IOException | IllegalArgumentException e) {
				System.out.println("Exception");
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
