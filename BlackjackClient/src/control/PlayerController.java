package control;

import com.google.gson.Gson;

import comm.TCPConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import view.PlayerWindow;

public class PlayerController implements TCPConnection.OnConnectionListener{
	
	private PlayerWindow view;
	private TCPConnection connection;
	
	public PlayerController(PlayerWindow view) {
		this.view = view;
		init();
		
	}
	
	public void init() {
		connection = TCPConnection.getInstance();
		
		connection.setConnectionListener(this);
		connection.setIp("127.0.0.1");
		connection.setPuerto(5000);
		connection.start();
		
		
		
	
		
	}

	@Override
	public void onConnection() {
		Platform.runLater(
				() -> {
					
					Gson gson = new Gson();
				}
		);
		
	}
	
	

	

}
