package control;

import comm.Receptor.OnMessageListener;
import comm.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
	
	private int sum1;
	private int sum2;
	
	
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
		sum1=0;
		sum2=0;
		
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
		
		Collections.shuffle(cartas);
		
		
	}
	
	private int darCartaAleatoria() {
		Random rand = new Random();
		int int_random = rand.nextInt(cartas.size());
		
		
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
		System.out.println("Hay 2 jugadores, juego iniciado");
		for (int i = 0; i < 4; i++) {
			int cartaRandom = darCartaAleatoria();
			String carta = ""+cartaRandom;
			
			if (i%2==0) {
				Session even = TCPConnection.getInstance().getSessions().get(0);
				setSum1(getSum1()+cartaRandom);
				sendDirectMessage(even.getId(), carta);
				
			} else {
				Session notEven = TCPConnection.getInstance().getSessions().get(1);
				setSum2(getSum1()+cartaRandom);
				sendDirectMessage(notEven.getId(), carta);
			}
			
			
		}
	}
	

	@Override
	public void OnMessage(String msg) {
		
		//Deserializar
		Gson gson = new Gson();
		
		DirectMessage m = gson.fromJson(msg, DirectMessage.class);
		
		
		
	}
	
	private void sendDirectMessage(String id, String msg) {
		

		Gson gson = new Gson();
		String json = gson.toJson(new DirectMessage(msg, id));
		TCPConnection.getInstance().sendDirectMessage(id, json);
	}

	public int getSum1() {
		return sum1;
	}

	public void setSum1(int sum1) {
		this.sum1 = sum1;
	}

	public int getSum2() {
		return sum2;
	}

	public void setSum2(int sum2) {
		this.sum2 = sum2;
	}
	
	
	
	

}
