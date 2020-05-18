package javafx.chat.src.com.chat.server;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class ServerApplication extends Application {
	public static ArrayList<Thread> threads;
	public static void main(String[] args){
		launch();
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		threads = new ArrayList<Thread>();
		primaryStage.setTitle("Application de Chat");
		primaryStage.setScene(makePortUI(primaryStage));
		primaryStage.show();
		
	}

	
	public Scene makePortUI(Stage primaryStage) {
		/* Make the root and set properties */
		GridPane rootPane = new GridPane();

		rootPane.setPadding(new Insets(20));
		rootPane.setVgap(10);
		rootPane.setHgap(10);
		rootPane.setAlignment(Pos.CENTER);

		/* Text label and field for port Number */
		Text portText = new Text("Numero de Port");
		Label errorLabel = new Label();
		errorLabel.setTextFill(Color.RED);
		TextField portTextField = new TextField("9999");
		portTextField.setDisable(true);
		portText.setFont(Font.font("Tahoma"));
		/*
		 * "Done" button and its click handler When clicked, another method is
		 * called
		 */
		Button portApprovalButton = new Button("Demarrer");
		portApprovalButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				/* Make the server and it's thread, and run it */
				try {
					Server server = new Server(Integer.parseInt(portTextField
							.getText()));
					Thread serverThread = (new Thread(server));
					serverThread.setName("Serveur Thread");
					serverThread.setDaemon(true);
					serverThread.start();
					threads.add(serverThread);
					/* Change the view of the primary stage */
					primaryStage.hide();
					primaryStage.setScene(makeServerUI(server));
					primaryStage.show();
				}catch(IllegalArgumentException e){
					errorLabel.setText("Numero de port invalide");
				}
				catch (IOException e) {
					// TODO Auto-generated catch block
					
				}
				
			}
		});
		
		/* Add the views to the pane */
		rootPane.add(portText, 0, 0);
		rootPane.add(portTextField, 0, 1);
		rootPane.add(portApprovalButton, 0, 2);
		rootPane.add(errorLabel, 0, 3);
		/*
		 * Make the Scene and return it Scene has constructor (Parent, Width,
		 * Height)
		 */
		return new Scene(rootPane, 400, 300);
	}
        
        
	public Scene makeServerUI(Server server){
		/* Make the root pane and set properties */
		GridPane rootPane = new GridPane();
                ToggleButton toggle = new ToggleButton("Toggle color");
		rootPane.setAlignment(Pos.CENTER);
		rootPane.setPadding(new Insets(20));
		rootPane.setHgap(10);
		rootPane.setVgap(10);
		rootPane.backgroundProperty().bind(Bindings.when(toggle.selectedProperty())
                .then(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, CornerRadii.EMPTY, Insets.EMPTY)))
                .otherwise(new Background(new BackgroundFill(Color.SKYBLUE, CornerRadii.EMPTY, Insets.EMPTY))));
                
		/* Make the server log ListView */
		Label logLabel = new Label("Connexions au serveur");
		ListView<String> logView = new ListView<String>();
		ObservableList<String> logList = server.serverLog;
		logView.setItems(logList);
                logView.setPrefSize(700, 400);
		
		/* Make the client list ListView */
		Label clientLabel = new Label("Clients Connect�s");
		ListView<String> clientView = new ListView<String>();
		ObservableList<String> clientList = server.clientNames;
		clientView.setItems(clientList);
		
		/* Add the view to the pane */
		rootPane.add(logLabel, 0, 0);
		rootPane.add(logView, 0, 1);
		rootPane.add(clientLabel, 0, 2);
		rootPane.add(clientView, 0, 3);
		
		/* Make scene and return it,
		 * Scene has constructor (Parent, Width, Height)
		 *  */
		return new Scene(rootPane, 800, 600);
	}
}
