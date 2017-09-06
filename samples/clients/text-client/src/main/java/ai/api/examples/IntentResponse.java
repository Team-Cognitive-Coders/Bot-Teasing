package ai.api.examples;

public class IntentResponse {

	private int testCase;
	private String intentName;
	private String response;
	private boolean sent;
	
	IntentResponse(int testCase, String intentName, String response, boolean sent){
		this.testCase = testCase;
		this.intentName = intentName;
		this.response = response;
		this.sent = sent;
	}
	
	public int getTestCase() {
		return testCase;
	}

	public void setTestCase(int testCase) {
		this.testCase = testCase;
	}

	public String getIntentName() {
		return intentName;
	}
	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public boolean getSent() {
		return sent;
	}
	public void setSent(boolean sent) {
		this.sent = sent;
	}
	
	
}
