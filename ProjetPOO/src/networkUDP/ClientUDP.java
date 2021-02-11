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
			//We initialize the client side connection
			@SuppressWarnings("resource")
			DatagramSocket client = new DatagramSocket();
			//Activate the broadcast mode 
			client.setBroadcast(true);

			//We create our broadcast datagram on port 1024
			InetAddress adresse = InetAddress.getByName("255.255.255.255");
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresse, 1024);

			//It is assigned the data to be sent
			packet.setData(buffer);

			//We send to the server
			client.send(packet);
			System.out.println("We send "+message+" from "+InetAddress.getLocalHost()+" to "+adresse+" by the port 1024" );

			//Finally, we get the answer from the server
			byte[] buffer2 = new byte[256];
			DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, adresse, 1024);
			client.setSoTimeout(500);
			while(true) {	
				try {
				client.receive(packet2);
				}catch (SocketTimeoutException e) {
					return;
				}

				System.out.print("We received a response from : "+packet2.getAddress()+" by the port "+packet2.getPort());
				System.out.println("Message: "+new String(packet2.getData()));
				
				//We decompose the answer to be able to create a user 
				String receivemessage= new String(packet2.getData(), 0, packet2.getLength());
				User user= new User(receivemessage, packet2.getAddress(),packet2.getPort());
				ListUser.add(user);
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}   

