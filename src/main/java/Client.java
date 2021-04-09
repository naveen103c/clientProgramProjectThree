import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;




public class Client extends Thread{

	
	Socket socketClient;
	
	ObjectOutputStream out;
	ObjectInputStream in;
	private String ipAddress; //"127.0.0.1"
	private int portNumber; //5555
	String roundOver = "";
	String message = "";
	
	private Consumer<Serializable> callback;
	
	Client(Consumer<Serializable> call, String ip, int portNum){
		ipAddress = ip;
		portNumber = portNum;
		callback = call;
	}
	
	public void run() {
		
		try {
		socketClient= new Socket(ipAddress, portNumber);
	    out = new ObjectOutputStream(socketClient.getOutputStream());
	    in = new ObjectInputStream(socketClient.getInputStream());
	    socketClient.setTcpNoDelay(true);
		}
		catch(Exception e) {}
		
		while(true) {
			try {
			message = in.readObject().toString();
			callback.accept(message);
			roundOver = message;
			in.reset();
			}
			catch(Exception e) {}
		}
	
    }
	
	public void send(char data) {
		
		try {
			out.writeObject(data);
			out.reset();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setUpdates(ClientGui.readFromClient reader) {
		reader.setGamePhase(roundOver);
	}

}
