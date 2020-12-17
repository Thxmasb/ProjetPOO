package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import interfacegraphique.Connected;
import interfacegraphique.Popup;
import interfacegraphique.Connexion;

/*
 * public class ClientUDP{
 * 
 * public InetAddress ipdest; public int portdest; public String message; public
 * String response; public InetAddress ipsource; public int portsource;
 * 
 * public ClientUDP(String message) throws IOException {
 * this.ipdest=InetAddress.getByName("255.255.255.255"); this.portdest=1024;
 * this.message=message;
 * 
 * System.out.println("Envoi du message:"+message+" à "+ipdest+" au port "
 * +portdest); DatagramSocket dgramSocket = new DatagramSocket();
 * 
 * 
 * DatagramPacket outPacket = new
 * DatagramPacket(message.getBytes(),message.length(),ipdest, portdest);
 * dgramSocket.send(outPacket);
 * 
 * byte[] buffer = new byte[256]; DatagramPacket inPacket = new
 * DatagramPacket(buffer, buffer.length); dgramSocket.receive(inPacket);
 * 
 * this.response = new String(inPacket.getData(), 0, inPacket.getLength());
 * this.ipsource=inPacket.getAddress(); this.portsource=inPacket.getPort();
 * System.out.println("Réponse:"+response+" de "+ipsource+" du port "+portsource
 * );
 * 
 * dgramSocket.setSoTimeout(3000); dgramSocket.close();
 * 
 * } }
 */

public class ClientUDP implements Runnable{
	String message = "";
	long sleepTime = 300;
	public List<String> LoginList;

	public ClientUDP(String message, List<String> LoginList){
		this.message = message;
		this.LoginList=LoginList;
	}

	public void run(){
		int nbre = 0;

		String envoi = message;
		byte[] buffer = envoi.getBytes();

		try {
			//On initialise la connexion côté client
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
			client.setSoTimeout(1000);
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
				LoginList.add(receivemessage);
				LoginList.add(packet2.getAddress().toString());
				LoginList.add(String.valueOf(packet2.getPort()));
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

