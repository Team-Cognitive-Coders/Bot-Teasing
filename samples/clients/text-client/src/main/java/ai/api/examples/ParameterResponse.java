package ai.api.examples;

public class ParameterResponse {

	private String parameterName;
	private String response;
	
	public ParameterResponse(String parameterName, String response) {
		this.parameterName = parameterName;
		this.response = response;
	}
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	
	
}
