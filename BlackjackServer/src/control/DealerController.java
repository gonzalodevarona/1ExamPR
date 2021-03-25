package control;

import comm.Receptor.OnMessageListener;
import comm.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.UUID;

import com.google.gson.Gson;

import comm.TCPConnection;
import comm.TCPConnection.OnConnectionListener;
import javafx.application.Platform;
import model.DirectMessage;
import view.DealerWindow;

public class DealerController implements OnMessageListener, OnConnectionListener{
	
	private DealerWindow view;
	private TCPConnection connection;
	
	private ArrayList<Integer> cartas = new ArrayList<Integer>();
	
	
	public DealerController(DealerWindow view) {
		this.view = view;
		init();
	}
	
	public void init() {
		connection = TCPConnection.getInstance();
		connection.setPuerto(5000);
		connection.start();
		connection.setConnectionListener(this);
		connection.setMessageListener(this);
		
		iniciarBaraja();
	}
	
	
	private void iniciarBaraja() {
		for (int i = 2; i <= 10 ; i++) {
			for (int j = 0; j < 3; j++) {
				cartas.add(i);
			}
		}
		
		for (int i = 0; i < 16; i++) {
			cartas.add(11);
		}
		
	}
	
	private int darCartaAleatoria() {
		Random rand = new Random();
		int int_random = rand.nextInt(cartas.size());
		int_random++;
		
		int value = cartas.get(int_random);
		cartas.remove(int_random);
		
		return value;
	}

	@Override
	public void onConnection(String id) {
			Platform.runLater(
				
				()->{
					view.getMessagesArea().appendText("<<< Nuevo cliente conectado " + id + "! >>>\n");
					if (TCPConnection.getInstance().getSessions().size() ==2) {
						startGame();
					}
				}
				
				);
		
	}

	private void startGame() {
		
		for (int i = 0; i < 3; i++) {
			String carta = ""+darCartaAleatoria();
			
			if (i%2==0) {
				Session even = TCPConnection.getInstance().getSessions().get(0);
				sendMessage(even.getId(), carta);
			} else {
				Session notEven = TCPConnection.getInstance().getSessions().get(1);
				sendMessage(notEven.getId(), carta);
			}
			
			
		}
	}
	

	@Override
	public void OnMessage(String msg) {
		
		//Deserializar
		Gson gson = new Gson();
		
		DirectMessage m = gson.fromJson(msg, DirectMessage.class);
		
		
		
	}
	
	private void sendMessage(String id, String msg) {
		

		Gson gson = new Gson();
		String json = gson.toJson(new DirectMessage(msg, id));
		TCPConnection.getInstance().sendDirectMessage(id, json);
	}
	

}
