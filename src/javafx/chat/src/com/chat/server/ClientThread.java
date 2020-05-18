package javafx.chat.src.com.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;

import javafx.application.Platform;

public class ClientThread implements Runnable {

	private Socket clientSocket;
	private Server server;
	private BufferedReader buffer;
	private PrintWriter print;
	private String nomClient;
	private String passClient;
	private GestionCompte gestion=new GestionCompte();

	public String getPassClient() {
		return passClient;
	}

	public void setPassClient(String passClient) {
		this.passClient = passClient;
	}

	public String getNomClient() {
		return nomClient;
	}

	public void setNomClient(String nomClient) {
		this.nomClient = nomClient;
	}

	public ClientThread(Socket clientSocket, Server server) {
		this.setClientSocket(clientSocket);
		this.server = server;
		try {
			buffer = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			print= new PrintWriter(
					clientSocket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			String info=getClientNameFromNetwork();
			String[] decoupe = info.split("_");
			this.nomClient = decoupe[0];
			this.passClient= decoupe[1];
			String exist= decoupe[2];
			String ouvrir=gestion.ouvrirCompte(nomClient, passClient);
			
			if(exist.equals("disconnect")){
				System.out.println("clientthread disconnect");
				server.clientNames.remove(this.nomClient);
				server.clientDisconnected(this);
			}
		if(exist.equals("old")) {
			
			if(ouvrir.equals("valide")) {
				print.println("valide");
				
			}else if(ouvrir.equals("not exist")){
				print.println("not exist");
			}else {
				print.println("erreur password");
			}
		}else if(exist.equals("new")){
			boolean creation=gestion.creation_compte(nomClient, passClient);
			if(creation) {
				print.println("compte creer");
			}else {
				print.println("not creer");
			}
			
		}
			
			if(ouvrir.equals("valide")) {
				
			
				Platform.runLater(new Runnable() {
	
					@Override
					public void run() {
						/*clientSocket.getRemoteSocketAddress()
						 * pass et login  du client
						 * */
				
								server.clientNames.add(nomClient + " - "
								+ clientSocket.getRemoteSocketAddress());
						
					}
	
				});
			
			String inputToServer;
			while (true) {
				inputToServer =buffer.readLine();
				server.writeToAllSockets(inputToServer);
			}
		}	
			
	
		} catch (SocketException e) {
			server.clientNames.remove(this.nomClient);

			server.clientDisconnected(this);

		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeToServer(String input) {
		print.println(input);
	}

	public String getClientNameFromNetwork() throws IOException {
		return buffer.readLine();
	}

	public String getClientName() {
		return this.nomClient;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}


	
}
