package model;

public class Message {
	
	public String type = "Message";
	private String body;
	private String clientId;
	
	
	public Message( String body, String clientId) {
		super();
		
		this.body = body;
		this.clientId = clientId;
	}



	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public String getClientId() {
		return clientId;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	
	
	

}
