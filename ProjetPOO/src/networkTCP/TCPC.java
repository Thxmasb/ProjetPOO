package networkTCP;

import java.net.Socket;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.InetAddress;

public class TCPC extends Thread{

	public Socket sockTCP;
	private ObjectOutputStream output;
	public ObjectInputStream input;


	public TCPC(Socket sockTCP) throws IOException {
		this.sockTCP = sockTCP;
		//We create channels to listen to and send our messages.
		this.output = new ObjectOutputStream(sockTCP.getOutputStream());
		this.input = new ObjectInputStream(sockTCP.getInputStream());
	}

	public TCPC(InetAddress addr) throws IOException {
		sockTCP=new Socket(addr, 3500);
		//We create channels to listen to and send our messages.
		this.output = new ObjectOutputStream(sockTCP.getOutputStream());
		this.input = new ObjectInputStream(sockTCP.getInputStream());		
	}
	
	public void run() {
		try {
			this.output = new ObjectOutputStream(sockTCP.getOutputStream());
			this.input = new ObjectInputStream(sockTCP.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Allows you to send messages
	public void sendMessage(String contenu) throws IOException {
		System.out.println("Write");
		this.output.writeUTF(contenu);
		this.output.flush();
	}

	
	//Closing the sockets
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

}
