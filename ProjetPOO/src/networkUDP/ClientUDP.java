package networkUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import other.User;


public class ClientUDP implements Runnable{
	String message = "";
	long sleepTime = 300;
	public List<User> ListUser;

	public ClientUDP(String message, List<User> ListUser){
		this.message = message;
		this.ListUser=ListUser;
	}

	public void run(){
		String envoi = message;
		byte[] buffer = envoi.getBytes();

		try {
			//On initialise la connexion côté client
			@SuppressWarnings("resource")
			DatagramSocket client = new DatagramSocket();
			client.setBroadcast(true);

			//On crée notre datagramme
			InetAddress adresse = InetAddress.getByName("255.255.255.255");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresse, 1024);

			//On lui affecte les données à envoyer
			packet.setData(buffer);

			//On envoie au serveur
			client.send(packet);
			System.out.println("On envoi "+message+" de "+InetAddress.getLocalHost()+" à "+adresse+" au port 1024" );

			//Et on récupère la réponse du serveur
			byte[] buffer2 = new byte[256];
			DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, adresse, 1024);
			client.setSoTimeout(500);
			while(true) {
				
				try {
				client.receive(packet2);
				}catch (SocketTimeoutException e) {
					return;
				}

				System.out.print("On a reçu une réponse de l'adresse : "+packet2.getAddress()+" par son port "+packet2.getPort());
				System.out.println(" Message:"+new String(packet2.getData()));
				
				
				
				String receivemessage= new String(packet2.getData(), 0, packet2.getLength());
						//new String(packet2.getData());
				User user= new User(receivemessage, packet2.getAddress(),packet2.getPort());
				ListUser.add(user);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
			//client.close();

			
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	

	/*
	 * public static void main(String[] args) throws IOException { List<String>
	 * LoginList = new ArrayList<>();;
	 * 
	 * ClientUDP client = new ClientUDP("Tété", LoginList); Thread cli1 = new
	 * Thread(client); cli1.start();
	 * 
	 * try { Thread.sleep(3000); } catch (InterruptedException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * System.out.println("Liste:"+LoginList);
	 * 
	 * 
	 * }
	 */
}   

