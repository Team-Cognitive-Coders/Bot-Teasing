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

	/**
	 * @param args
	 *            List of parameters:<br>
	 *            First parameters should be valid api key<br>
	 *            Second and the following args should be file names containing
	 *            audio data.
	 */
	public static void main(String[] args) {


		/*
		 * BookTable AIConfiguration configurationDevBot = new
		 * AIConfiguration("aa7003e594be4d7f89bd9afbe09b607e");
		 */
		// RestaurantChatbot
		AIConfiguration configurationDevBot = new AIConfiguration("6e3a284cb2004268adcc63dcddbb8fde");

		AIDataService dataServiceDev = new AIDataService(configurationDevBot);

		AIConfiguration configurationTestBot = new AIConfiguration("a178add25d2c40219d97a32722c191b4");

		AIDataService dataServiceTest = new AIDataService(configurationTestBot);
		AIEvent startConversation = new AIEvent("TESTBOT");
		String line = null;
		String intentName = new String();
		System.out.print(INPUT_PROMPT);
		// System.out.println(line);

		AIRequest requestTest = new AIRequest();
		requestTest.setEvent(startConversation);
		AIResponse responseTest;
		try {
			responseTest = dataServiceTest.request(requestTest);
			// responseTest.getResult().getFulfillment().getFollowupEvent();
			if (responseTest.getStatus().getCode() == 200) {
				line = responseTest.getResult().getFulfillment().getSpeech();
				intentName = responseTest.getResult().getMetadata().getIntentName();
				System.out.println(line);
				System.out.println(intentName);
			} else {
				System.err.println(responseTest.getStatus().getErrorDetails());
			}

		} catch (AIServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		line = testBot(dataServiceDev, dataServiceTest, line);

		System.out.println("See ya!");
	}

	public static String testBot(AIDataService dataServiceDev, AIDataService dataServiceTest, String line) {
		String intentName;
		AIRequest requestTest;
		AIResponse responseTest;
		while (null != line) {

			try {
				AIRequest requestDev = new AIRequest(line);
				AIResponse responseDev = dataServiceDev.request(requestDev);

				if (responseDev.getStatus().getCode() == 200) {
					line = responseDev.getResult().getFulfillment().getSpeech();
					System.out.println(line);
				} else {
					System.err.println(responseDev.getStatus().getErrorDetails());
				}

				System.out.print(INPUT_PROMPT);

				requestTest = new AIRequest(line);

				responseTest = dataServiceTest.request(requestTest);

				if (responseTest.getStatus().getCode() == 200) {
					line = responseTest.getResult().getFulfillment().getSpeech();
					intentName = responseTest.getResult().getMetadata().getIntentName();
					System.out.println(line);
					if (intentName.equalsIgnoreCase("Default Fallback Intent"))
						break;
				} else {
					System.err.println(responseTest.getStatus().getErrorDetails());
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		}
		return line;
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
