package test;

import java.net.Socket;

import javax.swing.JTextArea;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;
import java.net.Socket;

public class TCPC extends Thread{

	public Socket sockTCP;
	private ObjectOutputStream output;
	public ObjectInputStream input;


	public TCPC(Socket sockTCP) throws IOException {
		this.sockTCP = sockTCP;

		this.output = new ObjectOutputStream(sockTCP.getOutputStream());
		this.input = new ObjectInputStream(sockTCP.getInputStream());
        
		
		/*
		 * Thread recevoir = new Thread(new Runnable() { String msg;
		 * 
		 * @Override public void run() {
		 * 
		 * try { while(true) { System.out.println("Serveur : "+input.readUTF()); }
		 * 
		 * } catch (IOException e) { e.printStackTrace(); } } }); recevoir.start();
		 */
	}

	public TCPC(InetAddress addr) throws IOException {
		sockTCP=new Socket(addr, 3500);
		
		this.output = new ObjectOutputStream(sockTCP.getOutputStream());
		this.input = new ObjectInputStream(sockTCP.getInputStream());
        

		
		
	}
	
	public void run() {

		try {
			this.output = new ObjectOutputStream(sockTCP.getOutputStream());
			this.input = new ObjectInputStream(sockTCP.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void sendMessage(String contenu) throws IOException {
		System.out.println("Write");
		
		this.output.writeUTF(contenu);
		this.output.flush();

		//writeObject(contenu);
	}


	public void destroyAll() {
		try {
			if (!this.sockTCP.isClosed()) {
				this.output.close();
				this.sockTCP.close();
			}
			this.sockTCP = null;
			this.output = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/*
	 * public static void main(String[] args) { try { TCPC client=new
	 * TCPC(InetAddress.getLocalHost()); int i=1; while(i<100) {
	 * client.sendMessage("NTM");i++;} } catch (IOException e) {
	 * e.printStackTrace(); } }
	 */
}
