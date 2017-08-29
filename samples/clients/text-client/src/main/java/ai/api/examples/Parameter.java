package ai.api.examples;

public class Parameter {
	
	private String parameterName;
	private boolean requiredFlag;
	
	Parameter(String parameterName, boolean requiredFlag){
		this.parameterName = parameterName;
		this.requiredFlag = requiredFlag;
	}
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public boolean getRequiredFlag() {
		return requiredFlag;
	}
	public void setRequiredFlag(boolean requiredFlag) {
		this.requiredFlag = requiredFlag;
	}

}
