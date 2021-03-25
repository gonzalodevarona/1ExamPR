package view;

import java.io.IOException;

import control.PlayerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PlayerWindow extends Stage {

	// UI Elements
	private Scene scene;
	private PlayerController control;
	private Label status;
	private Button stand;
	private Button takeCard;
	
	
	

	public PlayerWindow() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerWindow.fxml"));
			Parent parent = loader.load();
			
			status = (Label) loader.getNamespace().get("status");
			stand = (Button) loader.getNamespace().get("stand");
			takeCard = (Button) loader.getNamespace().get("takeCard");

			scene = new Scene(parent, 477, 422);
			this.setScene(scene);

			control = new PlayerController(this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}




	public Label getStatus() {
		return status;
	}




	public Button getStand() {
		return stand;
	}




	public Button getTakeCard() {
		return takeCard;
	}
	
	

}
