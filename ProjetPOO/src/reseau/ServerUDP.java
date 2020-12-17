package reseau;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class ServerUDP implements Runnable {
	InetAddress iplocal;
	int portlocal;
	InetAddress ipdest;
	int portdest;
	String message;
	public String messagerecu;
	DatagramSocket dgramSocket;
	
	public ServerUDP(InetAddress iplocal,int portlocal,String message) throws IOException {
		
		this.iplocal=iplocal;
		this.portlocal=portlocal;
		this.message=message;
	
		this.dgramSocket = new DatagramSocket(portlocal);
		
	}
	
	public void updateArrayList (ArrayList<String> LoginList) {
		for(int i=0;i<=LoginList.size();i+=3) {
			if(messagerecu.equals(LoginList.get(i))) {
				//On fais rien
			}else {
				LoginList.add(messagerecu);
				LoginList.add(ipdest.toString());
				LoginList.add(String.valueOf(portdest));
			}
		}
	}

	@Override
	public void run() {
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
		
		DatagramPacket outPacket = new DatagramPacket(message.getBytes(), message.length(), ipdest, portdest);
		try {
			dgramSocket.send(outPacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Envoi du message:"+message);

		dgramSocket.close();	
		
	}
}
