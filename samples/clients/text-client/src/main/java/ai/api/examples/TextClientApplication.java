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

	public static void main(String[] args) {

		/*
		 * BookTable AIConfiguration configurationDevBot = new
		 * AIConfiguration("aa7003e594be4d7f89bd9afbe09b607e");
		 */
		// RestaurantChatbot
		AIConfiguration configurationDevBot = new AIConfiguration("6e3a284cb2004268adcc63dcddbb8fde");

		AIDataService dataServiceDev = new AIDataService(configurationDevBot);
		
		//Add data to the DB
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
		
		for(Parameter p: params) {
			System.out.println(p.getParameterName() + " " + p.getRequiredFlag());
		}
		
		
		IntentParameter intentparam1 = new IntentParameter("get.day","day");
		ArrayList<IntentParameter> intentparams = new ArrayList<>();
		intentparams.add(intentparam1);
		intentparam1 = new IntentParameter("get.time","time");
		intentparams.add(intentparam1);
		intentparam1 = new IntentParameter("get.number.of.guests", "guestNumber");
		intentparams.add(intentparam1);
		intentparam1 = new IntentParameter("get.restaurant", "restaurantName");
		intentparams.add(intentparam1);
		intentparam1 = new IntentParameter("get.name","username");
		intentparams.add(intentparam1);
		
		for(IntentParameter ip: intentparams) {
			System.out.println(ip.getIntentName() + " " + ip.getParameterName());
		}
		
		
		//Test Bot Configuration
		AIDataService dataServiceTest = TestBotConfiguration.getInstance().getDataServiceTest();

		// Test Bot Initializes Conversation
		AIEvent startConversation = new AIEvent("TESTBOT");
		String line = null;
		System.out.print(INPUT_PROMPT);

		AIRequest requestTest = new AIRequest();
		requestTest.setEvent(startConversation);
		AIResponse responseTest;
		try {
			responseTest = dataServiceTest.request(requestTest);
			line = getResponse(responseTest);
			testBot(dataServiceDev, dataServiceTest, requestTest, responseTest, line);
		} catch (AIServiceException e) {
			e.printStackTrace();
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
			AIResponse responseTest, String line) {
		String intentName;

		while (null != line) {

			try {
				AIRequest requestDev = new AIRequest(line);
				AIResponse responseDev = dataServiceDev.request(requestDev);

				line = getResponse(responseDev);
				System.out.print(INPUT_PROMPT);

				requestTest = new AIRequest(line);

				responseTest = dataServiceTest.request(requestTest);
				line = getResponse(responseTest);
				
				
				intentName = responseTest.getResult().getMetadata().getIntentName();
				if (intentName.equalsIgnoreCase("Default Fallback Intent"))
					break;
				
				//checkRequiredParameters();

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}

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
