package javafx.chat.src.com.chat.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Server implements Runnable {
	private int portNumber;
	private ServerSocket socket;
	private ArrayList<Socket> clients;
	private ArrayList<ClientThread> clientThreads;
	public ObservableList<String> serverLog;
	public ObservableList<String> clientNames;
	public Vector<Compte>Comptes=GestionCompte.comptes;
	
	
	public Server(int portNumber) throws IOException {
		this.portNumber = portNumber;
		serverLog = FXCollections.observableArrayList();
		clientNames = FXCollections.observableArrayList();
		clients = new ArrayList<Socket>();
		clientThreads = new ArrayList<ClientThread>();
		socket = new ServerSocket(portNumber);

		
	}

        
	public void startServer() {
		try {
			socket = new ServerSocket(this.portNumber); 
			serverLog = FXCollections.observableArrayList();
	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

        
	public void run() {
		try {
			while (true) {
				Platform.runLater(new Runnable() {

					@Override
					public void run() {
						serverLog.add("Liste des clients ");
						Vector<Compte> comptes=new Vector<Compte>();
						comptes=GestionCompte.afficher();
						System.out.println("je suis server");
						if(comptes.isEmpty()) {
							serverLog.add("Liste vide ");
						}else {
							for(int i=0; i<comptes.size(); i++){
								serverLog.add(comptes.get(i).getLogin());
							 }
						}
						
					}
				});
				
				final Socket clientSocket = socket.accept();
				clients.add(clientSocket);
				ClientThread clientThreadHolderClass = new ClientThread(
						clientSocket, this);
				Thread clientThread = new Thread(clientThreadHolderClass);
				clientThreads.add(clientThreadHolderClass);
				clientThread.setDaemon(true);
				clientThread.start();
				ServerApplication.threads.add(clientThread);
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void clientDisconnected(ClientThread client) {
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				serverLog.add(client.getNomClient()+" / "
						+ client.getClientSocket().getRemoteSocketAddress()
						+ " déconnecté");
				
				
				/*supp compte de la liste*/
				Compte c=new Compte(client.getNomClient(),client.getPassClient());
				Comptes.remove(c);
			
				clients.remove(clientThreads.indexOf(client));
				System.out.println("erreur"+clientThreads.indexOf(client));
				clientThreads.remove(clientThreads.indexOf(client));
			}
		});
		
		
	}

	public void writeToAllSockets(String input) {
		for (ClientThread clientThread : clientThreads) {
			clientThread.writeToServer(input);
		}
	}
	

}
