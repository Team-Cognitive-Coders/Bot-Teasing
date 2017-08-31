/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ai.api.examples;

import java.util.ArrayList;
import java.util.List;

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
	private static final int ERROR_EXIT_CODE = 1;
	private static String status = "Review";

	public static void main(String[] args) {

		/*
		 * BookTable AIConfiguration configurationDevBot = new
		 * AIConfiguration("aa7003e594be4d7f89bd9afbe09b607e");
		 */
		// RestaurantChatbot
		AIConfiguration configurationDevBot = new AIConfiguration("6e3a284cb2004268adcc63dcddbb8fde");

		AIDataService dataServiceDev = new AIDataService(configurationDevBot);

		// Add data to the DB
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

		for (Parameter p : params) {
			System.out.println(p.getParameterName() + " " + p.getRequiredFlag());
		}

/*		IntentParameter intentparam1 = new IntentParameter("get.day", "day");
		ArrayList<IntentParameter> intentparams = new ArrayList<>();
		intentparams.add(intentparam1);
		intentparam1 = new IntentParameter("get.time", "time");
		intentparams.add(intentparam1);
		intentparam1 = new IntentParameter("get.number.of.guests", "guestNumber");
		intentparams.add(intentparam1);
		intentparam1 = new IntentParameter("get.restaurant", "restaurantName");
		intentparams.add(intentparam1);
		intentparam1 = new IntentParameter("get.name", "username");
		intentparams.add(intentparam1);

		for (IntentParameter ip : intentparams) {
			System.out.println(ip.getIntentName() + " " + ip.getParameterName());
		}*/

		IntentResponse intentresponse1 = new IntentResponse("get.day", "today", false);
		ArrayList<IntentResponse> intentresponses = new ArrayList<>();
		intentresponses.add(intentresponse1);
		intentresponse1 = new IntentResponse("get.time", "9pm", false);
		intentresponses.add(intentresponse1);
		intentresponse1 = new IntentResponse("get.number.of.guests", "7", false);
		intentresponses.add(intentresponse1);
		intentresponse1 = new IntentResponse("get.restaurant", "Onesta", false);
		intentresponses.add(intentresponse1);
		intentresponse1 = new IntentResponse("get.name", "My name is Dexter", false);
		intentresponses.add(intentresponse1);
		intentresponse1 = new IntentResponse("go.to.reserve.table", "book table", false);
		intentresponses.add(intentresponse1);
		intentresponse1 = new IntentResponse("go.to.confirm", "okay", false);
		intentresponses.add(intentresponse1);
		intentresponse1 = new IntentResponse("go.to.reserve.table",
				"I want to book a table for five at CCDD on friday 8pm", false);
		intentresponses.add(intentresponse1);
		intentresponse1 = new IntentResponse("go.to.confirm", "okay", false);
		intentresponses.add(intentresponse1);

		for (IntentResponse ir : intentresponses) {
			System.out.println(ir.getIntentName() + " " + ir.getResponse() + " " + ir.getSent());
		}

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

		for (ParameterResponse pr : paramresponses) {
			System.out.println(pr.getParameterName() + " " + pr.getResponse());
		}

		// Test Bot Configuration
		AIDataService dataServiceTest = TestBotConfiguration.getInstance().getDataServiceTest();

		// Test Bot Initializes Conversation
		AIEvent startConversation = new AIEvent("TESTBOT");
		String line = null;
		int count = 1;

		AIRequest requestTest = new AIRequest();
		requestTest.setEvent(startConversation);
		AIResponse responseTest;

		System.out.println("------------------------------------------------------------------------------------------------------------------");
		try {
			responseTest = dataServiceTest.request(requestTest);
			String intentName = responseTest.getResult().getMetadata().getIntentName();
			for (IntentResponse ir : intentresponses) {
				if (ir.getIntentName().equalsIgnoreCase(intentName) && !ir.getSent()) {
					System.out.println("Test Case " + count);
					System.out.print(INPUT_PROMPT);
					List<String> respondedParams = new ArrayList<>();
					line = getTestResponse(intentName, intentresponses, paramresponses, respondedParams);
					testBot(dataServiceDev, dataServiceTest, requestTest, responseTest, line, params, 
							intentresponses, paramresponses, respondedParams);
					System.out.println(status);
					System.out.println();
					count++;
				}
			}
		} catch (AIServiceException e) {
			e.printStackTrace();
		}

		System.out.println("------------------------------------------------------------------------------------------------------------------");

		for (Parameter p : params) {
			System.out.println(p.getParameterName() + " " + p.getRequiredFlag());
		}

		/*for (IntentParameter ip : intentparams) {
			System.out.println(ip.getIntentName() + " " + ip.getParameterName());
		}*/

		for (IntentResponse ir : intentresponses) {
			System.out.println(ir.getIntentName() + " " + ir.getResponse() + " " + ir.getSent());
		}

		for (ParameterResponse pr : paramresponses) {
			System.out.println(pr.getParameterName() + " " + pr.getResponse());
		}

		System.out.println("See ya!");
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
				if (intentName.equalsIgnoreCase("Default Fallback Intent")) {
					if (checkRequiredParams(params, respondedParams)) {
						status = "Pass";
					}
						
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
			if (ir.getIntentName().equalsIgnoreCase(intentName) && !ir.getSent()) {
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
				ir.setSent(true);
				break;
			
				}
		}
		System.out.println(response);
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
	private static void showHelp(String errorMessage, int exitCode) {
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
	}
}
