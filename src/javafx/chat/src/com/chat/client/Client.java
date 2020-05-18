package javafx.chat.src.com.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Client implements Runnable {
	/* The Socket of the Client */
	private Socket clientSocket;
	private BufferedReader serverToClientReader;
	private PrintWriter clientToServerWriter;
	private String name;
	private String password;
	public ObservableList<String> chatLog;

	public Client(String hostName, int portNumber, String name,String password,int exist) throws UnknownHostException, IOException {
		
			/* Try to establish a connection to the server */
			clientSocket = new Socket(hostName, portNumber);
			/* Instantiate writers and readers to the socket */
			serverToClientReader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			clientToServerWriter = new PrintWriter(
					clientSocket.getOutputStream(), true);
			chatLog = FXCollections.observableArrayList();
			/* Send name data to the server */
			this.name = name;
			this.password = password;
			if(exist==0) {
				clientToServerWriter.println(name+"_"+password+"_new");	
			}else {
				clientToServerWriter.println(name+"_"+password+"_old");
			}
		
	}

	public void writeToServer(String input) {
		clientToServerWriter.println(name + " : " + input);
	}

	public void run() {
		/* Infinite loop to update the chat log from the server */
		while (true) {
			try {

				final String inputFromServer = serverToClientReader.readLine();
				Platform.runLater(new Runnable() {
					public void run() {
						chatLog.add(inputFromServer);
					}
				});

			} catch (SocketException e) {
				Platform.runLater(new Runnable() {
					public void run() {
						chatLog.add("Error in server");
					}

				});
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String connecte() throws IOException {
		String rep= serverToClientReader.readLine();
		if(rep.equals("valide")) {
			return "valide";
			
		}else if(rep.equals("not exist")){
			return "not exist";
		}else {
			return "erreur password";
		}
		
	}
	public boolean creation_compte() throws IOException {
		String rep= serverToClientReader.readLine();
		if(rep.equals("compte creer")){
			return true;
		}
		System.out.println("client false");
		return false;
	}
	public void disconnect() {
		clientToServerWriter.println(name+"_disconnect");	
	}

	
}
