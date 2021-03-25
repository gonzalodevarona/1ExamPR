package model;

public class DirectMessage {
	
	public String type = "DirectMessage";
	private String body;
	private String clientId;
	
	
	public DirectMessage( String body, String clientId) {
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
