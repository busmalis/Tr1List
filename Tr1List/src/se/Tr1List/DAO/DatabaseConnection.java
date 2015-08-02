package se.Tr1List.DAO;

public class DatabaseConnection {
	public static String InetAddress = "http://johanremes.se/tr1Store";
	
	public void setInetAddress(String inetAddress){
		this.InetAddress = inetAddress;
	}
	
	public String getInetAddress(){
		return this.InetAddress;
	}
}
