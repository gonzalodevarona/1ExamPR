package control;

import com.google.gson.Gson;

import comm.Receptor.OnMessageListener;
import comm.TCPConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import model.*;
import view.PlayerWindow;

public class PlayerController implements TCPConnection.OnConnectionListener, OnMessageListener{
	
	private PlayerWindow view;
	private TCPConnection connection;
	
	private String myId;
	
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
					Gson gson = new Gson();
					String json = gson.toJson(new DirectMessage("", getMyId()));
					connection.sendMessage(json);
					disableButtons();
					
					
				});
		
	}

	private void activarPlantarse() {
		view.getStand().setOnAction(
				event ->{
					
					
					Gson gson = new Gson();
					String json = gson.toJson(new Message("", getMyId()));
					connection.sendMessage(json);
					disableButtons();
					
					
				});
		
	}

	@Override
	public void onConnection() {
		connection.setListenerOfMessages(this);
		Platform.runLater(
				() -> {
					view.getStatus().setText("Esperando al otro jugador...");
					
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
						
						//recibir cartas
					} else if (msjObj.getType().equalsIgnoreCase("DirectMessage")) { 
						DirectMessage msj = gson.fromJson(msg, DirectMessage.class);
						recibir(msj.getBody()); 
						
						
						//recibir ID
					} else if (msjObj.getType().equalsIgnoreCase("Id")) { 
						ID msj = gson.fromJson(msg, ID.class);
						setMyId(msj.getBody());
					
						//recibir turno
					} else if (msjObj.getType().equalsIgnoreCase("On")) { 
						ableButtons();
					
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
				view.getStatus().setText("En Partida - Esperando turno");
				
			}
		}
		
		return added;
		
	}

	private void definirStatus(String body) {
		view.getStatus().setText(body);
		disableButtons();
		
		
	}

	public String getMyId() {
		return myId;
	}

	public void setMyId(String myId) {
		this.myId = myId;
	}
	
	private void disableButtons() {
		Platform.runLater(
				() -> {
					view.getStand().setDisable(true);
					view.getTakeCard().setDisable(true);
					
				}
		);
	}
	
	private void ableButtons() {
		Platform.runLater(
				() -> {
					view.getStand().setDisable(false);
					view.getTakeCard().setDisable(false);
					view.getStatus().setText("En Partida - Es tu turno");
					
				}
		);
	}
	

	
	
	

	

}
