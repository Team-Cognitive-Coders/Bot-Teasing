package ai.api.examples;

import ai.api.AIConfiguration;
import ai.api.AIDataService;

public class TestBotConfiguration {

	private static TestBotConfiguration instance;
	
	private AIConfiguration configurationTestBot = new AIConfiguration("a178add25d2c40219d97a32722c191b4");

	private AIDataService dataServiceTest = new AIDataService(configurationTestBot);
	
	private TestBotConfiguration(){
		
	}
	
	static {
		instance = new TestBotConfiguration();
	}

	public static TestBotConfiguration getInstance() {
		return instance;
	}

	public AIDataService getDataServiceTest() {
		return dataServiceTest;
	}
	
}
