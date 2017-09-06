package ai.api.examples;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ai.api.AIConfiguration;
import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.model.AIEvent;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;

/**
 * Text client reads requests line by line from stdandard input.
 */
public class TextClientApplication {

	private static final String INPUT_PROMPT = "> ";
	/**
	 * Default exit code in case of error
	 */
	//private static final int ERROR_EXIT_CODE = 1;
	private static String status = "Review";
	private static int pass = 0;
	private static int fail = 0;
	private static int review = 0;
	private static int count = 1;

	public static void main(String args[]) {

		//BookTable 
		String devAccessToken = "aa7003e594be4d7f89bd9afbe09b607e";
		AIConfiguration configurationDevBot = new
		 AIConfiguration(devAccessToken);
		 
		// RestaurantChatbot
		//AIConfiguration configurationDevBot = new AIConfiguration("6e3a284cb2004268adcc63dcddbb8fde");

		AIDataService dataServiceDev = new AIDataService(configurationDevBot);

		// Add data to the DB
		ArrayList<Parameter> params = initializeParameter();

		for (Parameter p : params) {
			System.out.println(p.getParameterName() + " " + p.getRequiredFlag());
		}


		ArrayList<IntentResponse> intentresponses = initializeIntentResponse();

		for (IntentResponse ir : intentresponses) {
			System.out.println(ir.getIntentName() + " " + ir.getResponse() + " " + ir.getSent());
		}

		ArrayList<ParameterResponse> paramresponses = initializParamResponse();

		for (ParameterResponse pr : paramresponses) {
			System.out.println(pr.getParameterName() + " " + pr.getResponse());
		}

		// Test Bot Configuration
		AIDataService dataServiceTest = TestBotConfiguration.getInstance().getDataServiceTest();

		// Test Bot Initializes Conversation
		AIEvent startConversation = new AIEvent("TESTBOT");
		String line = null;
		

		AIRequest requestTest = new AIRequest();
		requestTest.setEvent(startConversation);
		AIResponse responseTest;

		System.out.println("------------------------------------------------------------------------------------------------------------------");
		try {
			responseTest = dataServiceTest.request(requestTest);
			String intentName = responseTest.getResult().getMetadata().getIntentName();
			for (IntentResponse ir : intentresponses) {
				if (ir.getTestCase() == count && ir.getIntentName().equalsIgnoreCase(intentName) && !ir.getSent()) {
					System.out.println("Test Case " + count);
					System.out.print(INPUT_PROMPT);
					List<String> respondedParams = new ArrayList<>();
					line = getTestResponse(intentName, intentresponses, paramresponses, respondedParams);
					testBot(dataServiceDev, dataServiceTest, requestTest, responseTest, line, params, 
							intentresponses, paramresponses, respondedParams);
					System.out.println(status);
					updateCount(status);
					System.out.println();
					count++;
				}
			}
			
			System.out.println(pass + " " + fail + " " + review);
			String appId = "App1";
			updateToDB(appId, devAccessToken);
		} catch (AIServiceException e) {
			e.printStackTrace();
		}

		System.out.println("------------------------------------------------------------------------------------------------------------------");

		for (Parameter p : params) {
			System.out.println(p.getParameterName() + " " + p.getRequiredFlag());
		}


		for (IntentResponse ir : intentresponses) {
			System.out.println(ir.getIntentName() + " " + ir.getResponse() + " " + ir.getSent());
		}

		for (ParameterResponse pr : paramresponses) {
			System.out.println(pr.getParameterName() + " " + pr.getResponse());
		}

		System.out.println("See ya!");
	}

	private static void updateToDB(String appId, String clientAccessToken) {
		FileInputStream serviceAccount = null;
		try {
			serviceAccount = new FileInputStream("firebase/chatdata.json");
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
		}

		FirebaseOptions options = null;
		try {
			options = new FirebaseOptions.Builder()
			  .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
			  .setDatabaseUrl("https://chatdata-47e88.firebaseio.com/")
			  .build();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		FirebaseApp.initializeApp(options);

		FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
		
		System.out.println(defaultAuth);
		DatabaseReference testRef  = FirebaseDatabase.getInstance()
													 .getReference("Applications")
													 .child(appId)
													 .child("Testing");
		
		Map<String, Object> testUpdates = new HashMap<String, Object>();
		testUpdates.put("Pass", pass);
		testUpdates.put("Fail", fail);
		testUpdates.put("Review", review);

		testRef.updateChildren(testUpdates);
		

	}

	private static void updateCount(String status) {
		if(status.equalsIgnoreCase("Pass"))
			pass++;
		else
			if(status.equalsIgnoreCase("Fail"))
				fail++;
			else
				review++;
	}
	

	public static ArrayList<Parameter> initializeParameter() {
		Parameter param1 = new Parameter("day", true);
		ArrayList<Parameter> params = new ArrayList<>();
		params.add(param1);
		param1 = new Parameter("time", true);
		params.add(param1);
		param1 = new Parameter("guestNumber", true);
		params.add(param1);
		param1 = new Parameter("restaurantName", false);
		params.add(param1);
		param1 = new Parameter("username", false);
		params.add(param1);
		return params;
	}

	public static ArrayList<IntentResponse> initializeIntentResponse() {
		IntentResponse intentresponse1 = new IntentResponse(1,"get.day", "today", false);
        ArrayList<IntentResponse> intentresponses = new ArrayList<>();
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(1, "go.to.reserve.table", "book table", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(1, "get.time", "9pm", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(1, "get.number.of.guests", "7", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(1, "get.restaurant", "Onesta", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(1, "get.name", "My name is Dexter", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(1, "go.to.confirm", "okay", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(1, "get.day.and.time", "tomorrow 7:30pm", false);
        intentresponses.add(intentresponse1);
        
        intentresponse1 = new IntentResponse(2, "go.to.reserve.table", "I want to book a table for five at CCDD on friday 8pm", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(2, "get.name", "Jawad", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(2, "go.to.confirm", "okay", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(2, "get.day.and.time", "tomorrow 7:30pm", false);
        intentresponses.add(intentresponse1);
        
        intentresponse1 = new IntentResponse(3, "go.to.reserve.table", "I need a table today", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(3, "get.number.of.guests", "5", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(3, "get.time", "8pm", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(3, "get.restaurant", "Truffles", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(3, "get.name", "Jawad", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(3, "go.to.confirm", "okay", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(3, "get.day.and.time", "tomorrow 7:30pm", false);
        intentresponses.add(intentresponse1);
        
        intentresponse1 = new IntentResponse(4, "go.to.reserve.table", "book a table for me", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(4, "get.number.of.guests", "5", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(4, "get.time", "8pm", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(4, "get.restaurant", "Truffles", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(4, "get.day", "today", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(4, "get.name", "Saurav", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(4, "go.to.confirm", "yes", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(4, "get.day.and.time", "tonight 7:30pm", false);
        intentresponses.add(intentresponse1);
        
        intentresponse1 = new IntentResponse(5, "go.to.reserve.table", "please book a table at CCDD at 5 pm", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(5, "get.number.of.guests", "5", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(5, "get.day", "today", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(5, "get.name", "Saurav", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(5, "go.to.confirm", "yes", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(5, "get.day.and.time", "tonight 7:30pm", false);
        intentresponses.add(intentresponse1);
        
        intentresponse1 = new IntentResponse(6, "go.to.reserve.table", "get me a table at Onesta", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(6, "get.number.of.guests", "5", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(6, "get.time", "8pm", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(6, "get.day", "today", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(6, "get.name", "Raaj", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(6, "go.to.confirm", "yes", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(6, "get.day.and.time", "tonight 7:30pm", false);
        
        intentresponse1 = new IntentResponse(7, "go.to.reserve.table", "require a table tonight for 2", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(7, "get.time", "8pm", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(7, "get.restaurant", "Truffles", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(7, "get.name", "Mukya", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(7, "go.to.confirm", "yes", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(7, "get.day.and.time", "tonight 7:30pm", false);
        intentresponses.add(intentresponse1);
        
        intentresponse1 = new IntentResponse(8, "go.to.reserve.table", "get me a table at Onesta", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(8, "get.time", "8pm", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(8, "get.day", "today", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(8, "get.name", "Raaj", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(8, "go.to.confirm", "yes", false);
        intentresponses.add(intentresponse1);
        intentresponse1 = new IntentResponse(8, "get.day.and.time", "tonight 7:30pm", false);

		return intentresponses;
	}

	public static ArrayList<ParameterResponse> initializParamResponse() {
		ParameterResponse paramresponse1 = new ParameterResponse("day",
				"I want to book a table for five at CCDD on friday 8pm");
		ArrayList<ParameterResponse> paramresponses = new ArrayList<>();
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("time", "I want to book a table for five at CCDD on friday 8pm");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("guestNumber", "I want to book a table for five at CCDD on friday 8pm");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("restaurantName",
				"I want to book a table for five at CCDD on friday 8pm");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("restaurantName", "Onesta");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("day", "today");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("time", "9pm");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("username", "My name is Dexter");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("guestNumber", "7");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("day", "tomorrow 7:30pm");
		paramresponses.add(paramresponse1);
		paramresponse1 = new ParameterResponse("time", "tomorrow 7:30pm");
		paramresponses.add(paramresponse1);
		return paramresponses;
	}

	public static String getResponse(AIResponse response) {
		String line = "";
		if (response.getStatus().getCode() == 200) {
			line = response.getResult().getFulfillment().getSpeech();
			System.out.println(line);
		} else {
			System.err.println(response.getStatus().getErrorDetails());
		}

		return line;
	}

	public static void testBot(AIDataService dataServiceDev, AIDataService dataServiceTest, AIRequest requestTest,
			AIResponse responseTest, String line, ArrayList<Parameter> params, 
			ArrayList<IntentResponse> intentresponses, ArrayList<ParameterResponse> paramresponses,
			List<String> respondedParams) {
		String intentName;

		while (null != line) {

			try {
				AIRequest requestDev = new AIRequest(line);
				AIResponse responseDev = dataServiceDev.request(requestDev);

				line = getResponse(responseDev);
				System.out.print(INPUT_PROMPT);

				requestTest = new AIRequest(line);

				responseTest = dataServiceTest.request(requestTest);

				intentName = responseTest.getResult().getMetadata().getIntentName();
				line = getTestResponse(intentName, intentresponses, paramresponses, respondedParams);
				if (intentName.equalsIgnoreCase("Default Fallback Intent") || status.equalsIgnoreCase("Fail")) {
					if (checkRequiredParams(params, respondedParams)) {
						status = "Pass";
					}
					/*else
						status = "Fail";*/
						
					break;
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

	}

	private static boolean checkRequiredParams(ArrayList<Parameter> params, List<String> respondedParams) {

		for (Parameter p : params) {
			if (p.getRequiredFlag()) {
				if (!(respondedParams.contains(p.getParameterName())))
					return false;
			}
		}
		return true;
	}

	private static String getTestResponse(String intentName, ArrayList<IntentResponse> intentresponses,
			ArrayList<ParameterResponse> paramresponses, List<String> respondedParams) {
		String response = "";
		for (IntentResponse ir : intentresponses) {
			if (ir.getTestCase() == count && ir.getIntentName().equalsIgnoreCase(intentName) && !ir.getSent()) {
				response = ir.getResponse();
				List<String> tempParamList = getParameterForResponse(paramresponses, response);
				if(tempParamList != null) {
				for (String parameter : tempParamList) {
					if (respondedParams.contains(parameter)) {
						response = "Invalid Question! Parameter is already sent";
						status = "Fail";
					} else {
						respondedParams.add(parameter);
					}
				}
				}
				System.out.println(response);
				ir.setSent(true);
				break;
			
				}
		}
		
		return response;
	}

	private static List<String> getParameterForResponse(ArrayList<ParameterResponse> paramresponses, String response) {
		List<String> result = new ArrayList<>();

		for (ParameterResponse pr : paramresponses) {
			if (pr.getResponse().equalsIgnoreCase(response)) {
				result.add(pr.getParameterName());
			}
		}

		return result;
	}

	/**
	 * Output application usage information to stdout and exit. No return from
	 * function.
	 * 
	 * @param errorMessage
	 *            Extra error message. Would be printed to stderr if not null and
	 *            not empty.
	 * 
	 */
/*	private static void showHelp(String errorMessage, int exitCode) {
		if (errorMessage != null && errorMessage.length() > 0) {
			System.err.println(errorMessage);
			System.err.println();
		}
		System.out.println("Usage: APIKEY");
		System.out.println();
		System.out.println("APIKEY  Your unique application key");
		System.out.println("        See https://docs.api.ai/docs/key-concepts for details");
		System.out.println();
		System.exit(exitCode);
	}*/
}