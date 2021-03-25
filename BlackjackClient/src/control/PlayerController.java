package control;

import com.google.gson.Gson;

import comm.Receptor.OnMessageListener;
import comm.TCPConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import model.DirectMessage;
import model.Generic;
import model.Message;
import view.PlayerWindow;

public class PlayerController implements TCPConnection.OnConnectionListener, OnMessageListener{
	
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
					
					
				}
		);
		
	}

	@Override
	public void OnMessage(String msg) {
		Platform.runLater(
				() -> {
					Gson gson = new Gson();
					Generic msjObj = gson.fromJson(msg, Generic.class);
					
						//gano o empato o perdio
					if (msjObj.getType().equalsIgnoreCase("Message")) {					
						Message msj = gson.fromJson(msg, Message.class);
				
					} else { //recibir cartas
						DirectMessage msj = gson.fromJson(msg, DirectMessage.class);
						String x = ""; 
					}
					
				}
		);
		
	}
	
	

	

}
