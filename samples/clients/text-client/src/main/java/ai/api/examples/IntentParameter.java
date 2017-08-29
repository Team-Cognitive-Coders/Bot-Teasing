package ai.api.examples;

public class IntentParameter {

	private String intentName;
	private String parameterName;
	
	IntentParameter(String intentName, String parameterName){
		this.intentName = intentName;
		this.parameterName = parameterName;
	}
	
	public String getIntentName() {
		return intentName;
	}
	public void setIntentName(String intentName) {
		this.intentName = intentName;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	
	
}
