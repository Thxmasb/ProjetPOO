package reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ClientTCP {

	InetAddress host;
	int port;

	public ClientTCP(InetAddress host, int port) {
		this.host=host;
		this.port=port;
	}

	public void SocketClientTCP(String message) {

	      final Scanner sc = new Scanner(System.in);//pour lire à partir du clavier
		
	      try {

	    	 Socket clientSocket = new Socket(host,port);
	   
	         //flux pour envoyer
	    	 PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
	         //flux pour recevoir
	         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	   
	         Thread envoyer = new Thread(new Runnable() {
	             String msg;
	              @Override
	              public void run() {
	                while(true){
	                  msg = sc.nextLine();
	                  out.println(msg);
	                  out.flush();
	                }
	             }
	         });
	         envoyer.start();
	   
	        Thread recevoir = new Thread(new Runnable() {
	            String msg;
	            @Override
	            public void run() {
	               try {
	                 msg = in.readLine();
	                 while(msg!=null){
	                    System.out.println("Serveur : "+msg);
	                    msg = in.readLine();
	                 }
	                 System.out.println("Serveur déconecté");
	                 out.close();
	                 clientSocket.close();
	               } catch (IOException e) {
	                   e.printStackTrace();
	               }
	            }
	        });
	        recevoir.start();
	   
	      } catch (IOException e) {
	           e.printStackTrace();
	      }
	}
	
	/*
	 * public static void main(String[] args) throws IOException { ClientTCP
	 * client=new ClientTCP(InetAddress.getLocalHost(),5000);
	 * client.SocketClientTCP(); }
	 */
}
