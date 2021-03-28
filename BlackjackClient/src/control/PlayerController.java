package control;

import com.google.gson.Gson;

import comm.Receptor.OnMessageListener;
import comm.TCPConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
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
		connection.setIp("127.0.0.1");
		connection.setPuerto(5000);
		connection.setConnectionListener(this);
		connection.start();
		
		

		
		
		
		
		activarPlantarse();
		activarPedir();
		
		
		
	
		
	}

	private void activarPedir() {
		view.getTakeCard().setOnAction(
				event ->{
					DirectMessage msj = new DirectMessage("mas","");
					
				});
		
	}

	private void activarPlantarse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnection() {
		connection.setListenerOfMessages(this);
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
						definirStatus(msj.getBody());
				
					} else { //recibir cartas
						DirectMessage msj = gson.fromJson(msg, DirectMessage.class);
						recibir(msj.getBody()); 
					
					}
					
				}
		);
		
	}

	private boolean recibir(String body) {
		boolean added = false;
		int value = Integer.parseInt(body);
		Label[] cards = view.getCards();
		
		for (int i = 0; i < cards.length && !added; i++) {
			if (cards[i].getText().equalsIgnoreCase("")) {
				added = true;
				cards[i].setText(body);
				view.getStatus().setText("En Partida");
			}
		}
		
		return added;
		
	}

	private void definirStatus(String body) {
		view.getStatus().setText(body);
		
	}
	
	

	

}
