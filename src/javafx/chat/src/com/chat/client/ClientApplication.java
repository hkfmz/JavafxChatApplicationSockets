package javafx.chat.src.com.chat.client;

import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ClientApplication extends Application {
	private ArrayList<Thread> threads;
	public static void main(String[] args){
		launch();
	}
	
	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
		for (Thread thread: threads){
			thread.interrupt();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		threads = new ArrayList<Thread>();

		primaryStage.setScene(makeInitScene(primaryStage));
		primaryStage.show();
	}

	public Scene makeInitScene(Stage primaryStage) {
		GridPane rootPane = new GridPane();
		rootPane.setPadding(new Insets(20));
		rootPane.setVgap(10);
		rootPane.setHgap(10);
		rootPane.setAlignment(Pos.CENTER);

		TextField nameField = new TextField();
		PasswordField passField=new PasswordField();
		TextField hostNameField = new TextField("127.0.0.1");
		hostNameField.setDisable(true);
		TextField portNumberField = new TextField("9999");
		portNumberField.setDisable(true);

		Label nameLabel = new Label("Nom ");
		Label passLabel = new Label("password ");
		Label hostNameLabel = new Label("Adresse");
		Label portNumberLabel = new Label("Numero de Port");
		Label errorLabel = new Label();
		Button submitClientInfoButton = new Button("Connexion");
		submitClientInfoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
				// TODO Auto-generated method stub
				Client client;
				try {
					client = new Client(hostNameField.getText(), Integer
							.parseInt(portNumberField.getText()), nameField
							.getText(),passField
							.getText(),1);
					
			
					String rep=client.connecte();
					if(rep.equals("valide")) {
						
						Thread clientThread = new Thread(client);
						clientThread.setDaemon(true);
						clientThread.start();
						threads.add(clientThread);
							primaryStage.close();
							primaryStage.setScene(makeChatUI(client,primaryStage));
							primaryStage.show();
						}else if(rep.equals("not exist")){
							errorLabel.setTextFill(Color.RED);
							errorLabel.setText("il faut avoir un compte ");
						}else {
							errorLabel.setTextFill(Color.RED);
							errorLabel.setText("mot de passe incorrecte");
						}
					
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					errorLabel.setTextFill(Color.RED);
					errorLabel.setText("server non connect�, R�essayer");
				}
				
			}
		});
		Button newCompte = new Button("sign up");
		newCompte.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
			
				try {
					
						primaryStage.close();
						primaryStage.setScene(makeSignUp(primaryStage));
						primaryStage.show();
					
				}
				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					errorLabel.setTextFill(Color.RED);
					errorLabel.setText("server non connect�, R�essayer");
				}
				
			}
		});

		rootPane.add(newCompte, 0, 0, 1, 1);
		rootPane.add(nameLabel , 0, 1);
		rootPane.add(nameField, 1, 1);
		rootPane.add(passLabel, 0, 2);
		rootPane.add(passField, 1, 2);
		rootPane.add(hostNameLabel, 0, 3);
		rootPane.add(hostNameField, 1, 3);
		rootPane.add(portNumberLabel, 0, 4);
		rootPane.add(portNumberField, 1, 4);
		rootPane.add(submitClientInfoButton, 0, 5, 2, 1);
		rootPane.add(errorLabel, 0, 6);
		return new Scene(rootPane, 400, 400);
	}

	
	/*sign up scene*/
	public Scene makeSignUp(Stage primaryStage) {
		GridPane rootPane = new GridPane();
		rootPane.setPadding(new Insets(20));
		rootPane.setVgap(10);
		rootPane.setHgap(10);
		rootPane.setAlignment(Pos.CENTER);

		TextField nameField = new TextField();
		PasswordField passField=new PasswordField();
		PasswordField confpassField=new PasswordField();
		
		Label nameLabel = new Label("Nom ");
		Label passLabel = new Label("password ");
		Label confpassLabel = new Label("confirmer password ");
		Label errorLabel = new Label();
		Button submitInfoButton = new Button("creation compte");
		submitInfoButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
				Client client;
				try {
					
					if(passField.getText().equals(confpassField.getText())) {
						client = new Client("127.0.0.1", 9999, nameField
								.getText(),passField
								.getText(),0);
						
						
					if(client.creation_compte()) {
						Thread clientThread = new Thread(client);
						clientThread.setDaemon(true);
						clientThread.start();
						threads.add(clientThread);
							errorLabel.setText("compte bien creer");
						}else {
							errorLabel.setTextFill(Color.RED);
							errorLabel.setText("compte deja existe");
						}
					}else {
						errorLabel.setText("mot de passe non confirmer");
					}
		
				
				}
				catch ( IOException e) {
					// TODO Auto-generated catch block
					errorLabel.setTextFill(Color.RED);
					errorLabel.setText("server non connect�, R�essayer");
				}
				
			}
		});

		Button newCompte = new Button("sign in");
		newCompte.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
			
				try {
						primaryStage.close();
						primaryStage.setScene(makeInitScene(primaryStage));
						primaryStage.show();
					
				}
				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					errorLabel.setTextFill(Color.RED);
					errorLabel.setText("server non connect�, R�essayer");
				}
				
			}
		});

		rootPane.add(newCompte, 0, 0, 1, 1);
		rootPane.add(nameLabel , 0, 1);
		rootPane.add(nameField, 1, 1);
		rootPane.add(passLabel, 0, 2);
		rootPane.add(passField, 1, 2);
		rootPane.add(confpassLabel, 0, 3);
		rootPane.add(confpassField, 1, 3);
		rootPane.add(submitInfoButton, 0, 4, 2, 1);
		rootPane.add(errorLabel, 0, 5);
		/* Make the Scene and return it */
		return new Scene(rootPane, 400, 400);
	}

	/*fin sign up*/
	
	
	public Scene makeChatUI(Client client,Stage primaryStage) {
		GridPane rootPane = new GridPane();
		rootPane.setPadding(new Insets(20));
		rootPane.setAlignment(Pos.CENTER);
		rootPane.setHgap(10);
		rootPane.setVgap(10);
		ListView<String> chatListView = new ListView<String>();
		chatListView.setItems(client.chatLog);
		TextField chatTextField = new TextField();
		chatTextField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				client.writeToServer(chatTextField.getText());
				chatTextField.clear();
			}
		});
		Button Deconnecter = new Button("Deconnecter");
		Deconnecter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent Event) {
			
				try {
						client.disconnect();
						primaryStage.close();
						primaryStage.setScene(makeInitScene(primaryStage));
						primaryStage.show();
					
				}
				catch (NumberFormatException e) {
					// TODO Auto-generated catch block
				}
				
			}
		});
		rootPane.add(Deconnecter, 0, 1, 2, 1);
		rootPane.add(chatListView, 0, 2);
		rootPane.add(chatTextField, 0, 3);

		return new Scene(rootPane, 400, 400);

	}
	
	
	
}
