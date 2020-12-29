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
import java.sql.Date;
import java.util.Scanner;

public class ServerTCP {

	public int port;


	public ServerTCP(int port) {
		this.port=port;

	}

	public void SocketServerTCP(){

		Scanner sc =new Scanner(System.in);
		
		try {
			ServerSocket serveurSocket = new ServerSocket(port);
			Socket clientSocket = serveurSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
			BufferedReader in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
			
			Thread envoi= new Thread(new Runnable() {
				String msg;

				public void run() {
					while(true){
						msg = sc.nextLine();
						out.println(msg);
						out.flush();
					}
				}
			});
			envoi.start();

			Thread recevoir= new Thread(new Runnable() {
				String msg ;
				public void run() {
					try {
						msg = in.readLine();
						//tant que le client est connecté
						while(msg!=null){
							System.out.println("Client : "+msg);
							msg = in.readLine();
						}
						//sortir de la boucle si le client a déconecté
						System.out.println("Client déconecté");
						//fermer le flux et la session socket
						out.close();
						clientSocket.close();
						serveurSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			recevoir.start();
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		ServerTCP server=new ServerTCP(5000);
		server.SocketServerTCP();
	}
}


