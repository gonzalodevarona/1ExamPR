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
import model.*;
import view.DealerWindow;

public class DealerController  implements OnMessageListener, OnConnectionListener{
	
	private DealerWindow view;
	private TCPConnection connection;
	
	private ArrayList<Integer> cartas = new ArrayList<Integer>();
	
	private int sum1;
	private int sum2;
	
	private boolean planta1;
	private boolean planta2;
	
	
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
		
		planta1=false;
		planta2=false;
		
		iniciarBaraja();
	}
	
	public void checkWinners() {
		
		
		if (sum1>=22) {
			setPlanta1(true);
			
		}
		
		if (sum2>=22) {
			setPlanta2(true);
			
		}
		
		if (isPlanta1()) {
			sendMessage(connection.getSessions().get(0).getId(), "Pasaste 21, espera al otro jugador...");
		}
		
		if (isPlanta2()) {
			sendMessage(connection.getSessions().get(1).getId(), "Pasaste 21, espera al otro jugador...");
		}
		if (isPlanta2() && isPlanta1()) {
			if (getSum1() == getSum2()) {
				sendMessage(connection.getSessions().get(1).getId(), "Empate");
				sendMessage(connection.getSessions().get(0).getId(), "Empate");
			}
			
			else if (getSum1()<=21 && getSum2() >=22) {
				sendMessage(connection.getSessions().get(0).getId(), "GANASTE");
				sendMessage(connection.getSessions().get(1).getId(), "PERDISTE");
			}
			
			else if (getSum2()<=21 && getSum1() >=22){
				sendMessage(connection.getSessions().get(1).getId(), "GANASTE");
				sendMessage(connection.getSessions().get(0).getId(), "PERDISTE");
			}
			
			else{
				sendMessage(connection.getSessions().get(1).getId(), "Perdiste");
				sendMessage(connection.getSessions().get(0).getId(), "Perdiste");
			}
			
			
		}
	}
	
	
	private void iniciarBaraja() {
		for (int i = 2; i <= 10 ; i++) {
			for (int j = 0; j <= 3; j++) {
				cartas.add(i);
			}
		}
		
		for (int i = 0; i < 16; i++) {
			cartas.add(11);
		}
		System.out.println(cartas.size());
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
						checkWinners();
				 
						
					}
				}
				
				);
		
	}

	private void startGame() {
		System.out.println("Hay 2 jugadores, juego iniciado");
		Session even = TCPConnection.getInstance().getSessions().get(0);
		Session notEven = TCPConnection.getInstance().getSessions().get(1);
		
		String idEven = even.getId();
		String idNotEven = notEven.getId();
		
		sendID(idEven);
		sendID(idNotEven);
		
		
		
		for (int i = 0; i < 4; i++) {
			int cartaRandom = darCartaAleatoria();
			String carta = ""+cartaRandom;
			
			if (i%2==0) {
				
				setSum1(getSum1()+cartaRandom);
				sendDirectMessage(idEven, carta);
				
				
			} else {
				
				setSum2(getSum2()+cartaRandom);
				sendDirectMessage(idNotEven, carta);
			}
			
			
		}
		
		
	
		
	
	}
	

	@Override
	public void OnMessage(String msg) {
		
		Gson gson = new Gson();
		Generic msjObj = gson.fromJson(msg, Generic.class);
		
			//se planta
		if (msjObj.getType().equalsIgnoreCase("Message")) {					
			Message msj = gson.fromJson(msg, Message.class);
			sePlanta(msj);
			
			//piden carta
		} else if (msjObj.getType().equalsIgnoreCase("DirectMessage")) { 
			DirectMessage msj = gson.fromJson(msg, DirectMessage.class);
			darOtraCarta(msj);
			
		}
		checkWinners();
		
		
	}
	
	private void darOtraCarta(DirectMessage msj) {
		
		int cartaRandom = darCartaAleatoria();
		String carta = ""+cartaRandom;
		
		boolean doIt = true;
		int i = connection.findSessionPos(msj.getClientId());
		if (i==1 && isPlanta1() ) {
			doIt = false;
		} else if (i==2 && isPlanta2() ) {
			doIt = false;
		}
		if (doIt) {
			if (i==1) {
				setSum1(getSum1()+cartaRandom);
			} else {
				setSum2(getSum2()+cartaRandom);
			}
			
			sendDirectMessage(msj.getClientId(), carta);
		}
		checkWinners();
		
		
	}

	private void sePlanta(Message msj) {
		int i = connection.findSessionPos(msj.getClientId()); 
		
		switch (i) {
		case 1:
			setPlanta1(true);
			break;
			
		case 2:
			setPlanta2(true);
			break;

		default:
			break;
		}
		
		checkWinners();
		
	}

	private void sendDirectMessage(String id, String msg) {
		

		Gson gson = new Gson();
		String json = gson.toJson(new DirectMessage(msg, id));
		TCPConnection.getInstance().sendDirectMessage(id, json);
	}
	
	private void sendID(String id) {
		

		Gson gson = new Gson();
		String json = gson.toJson(new ID(id));
		TCPConnection.getInstance().sendDirectMessage(id, json);
	}
	
	private void sendMessage(String id, String msg) {
		

		Gson gson = new Gson();
		String json = gson.toJson(new Message(msg, id));
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

	public boolean isPlanta1() {
		return planta1;
	}

	public void setPlanta1(boolean planta1) {
		this.planta1 = planta1;
	}

	public boolean isPlanta2() {
		return planta2;
	}

	public void setPlanta2(boolean planta2) {
		this.planta2 = planta2;
	}
	
	
	
	

}
