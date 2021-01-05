package test;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import autre.User;
import interfacegraphique.DiscutionWindow;
import reseau.ClientTCP;

public class TCPS extends Thread{


	private ServerSocket sockListenTCP;
	TCPC client; ArrayList<User> Liste;
	
	public TCPS(ArrayList<User> Liste) throws UnknownHostException, IOException {
		this.sockListenTCP = new ServerSocket(3500, 5, InetAddress.getLocalHost());
		this.Liste=Liste;
	}
	
	public void run() {
		System.out.println("TCP running");
		Socket sockAccept;
		while(true) {
			try {
				sockAccept = this.sockListenTCP.accept();
				TCPC clients=new TCPC(sockAccept);
				for (User u:Liste){
					if (u.getAddress().equals(sockAccept.getInetAddress())){
						new DiscutionWindow(u,clients);
					}
				}
				
					
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	/*
	 * public static void main(String[] args) { try { TCPS tcp=new TCPS();
	 * tcp.start();
	 * 
	 * } catch (IOException e) { e.printStackTrace(); } }
	 */

}