package autre;

import java.net.InetAddress;

public class User {
	
	public String username;
	public InetAddress address;
	public int port;
	
	public User(String username,InetAddress address,int port) {
		this.username=username;
		this.address=address;
		this.port=port;
	}

	public String getUsername() {
		return username;
	}

	public InetAddress getAddress() {
		return address;
	}

	public int getPort() {
		return port;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
