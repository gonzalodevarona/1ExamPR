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
	
	private Label card1;
	private Label card2;
	private Label card3;
	private Label card4;
	private Label card5;
	
	private Label[] cards;
	
	
	
	
	

	public PlayerWindow() {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerWindow.fxml"));
			Parent parent = loader.load();
			
			status = (Label) loader.getNamespace().get("status");
			stand = (Button) loader.getNamespace().get("stand");
			takeCard = (Button) loader.getNamespace().get("takeCard");
			
			card1 = (Label) loader.getNamespace().get("card1");
			card2 = (Label) loader.getNamespace().get("card2");
			card3 = (Label) loader.getNamespace().get("card3");
			card4 = (Label) loader.getNamespace().get("card4");
			card5 = (Label) loader.getNamespace().get("card5");
			
			cards = new Label[]{card1,card2,card3,card4,card5};
			
			

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




	public Label[] getCards() {
		return cards;
	}

	



	
	
	

}
