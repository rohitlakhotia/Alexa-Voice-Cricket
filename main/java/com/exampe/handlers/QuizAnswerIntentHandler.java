package com.exampe.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class QuizAnswerIntentHandler implements RequestHandler {
	 
		public boolean canHandle(HandlerInput input) {
	        return input.matches(intentName("QuizAnswerIntent"));
	    }
		
		public Optional<Response> handle(HandlerInput input) {
			Map<String, Object> hMap = input.getAttributesManager().getSessionAttributes();
			String x="0";
			Object o = (Object)x;
			if(hMap != null && (hMap.containsKey("game") && hMap.get("game").equals((Object)("true")))
					&& (hMap.containsKey("cs") && hMap.get("cs").equals(x) )) {
				final String speechText = "That was unexpected. \n Please launch the skill again...";
				return input.getResponseBuilder()
	            .withSpeech(speechText)
	            .withSimpleCard("HelloWorld", speechText)
	            //.withShouldEndSession(false)
	            .build();
			}
			Request request = input.getRequestEnvelope().getRequest();
			IntentRequest intentRequest = (IntentRequest) request;
			Intent intent = intentRequest.getIntent();
			Map<String, Slot> slots = intent.getSlots();
			Slot batBallSlot = slots.get("ans");
			Object value = (Object)batBallSlot.getValue();
			String speechText = "";
			System.out.println(value.toString() + " " + hMap.get("ans"));
			if(value.toString().toLowerCase().startsWith((String)hMap.get("ans"))) {
				speechText = "Your answer is correct!! \n\n Do you want to try another question?";
			}
			else {
				speechText = "Sorry! that was incorrect.\n The correct answer is "+
						(String) hMap.get("ans") 	
						+ " \n\n Do you want to try another question?";
			}
			hMap.put("cs",x);
			input.getAttributesManager().setSessionAttributes(hMap);
			/*hMap.put("cs", (Object)3);
			if (value.startsWith("bat")) {
				speechText = "So you are batting first. Meanwhile, here are the rules for you."
						+ " After the 3 2 1 countdown, say a number between 1 and 10. If I guess "
						+ "the same number as you, you're out, and it's your turn to bowl, and vice-versa";
				// Map<String, Object> hMap =
				// input.getAttributesManager().getSessionAttributes();
				hMap.put("UserScore", (Object) ("0"));
				hMap.put("AlexaScore", (Object) ("0"));
				hMap.put("batsman", (Object) ("user"));
				input.getAttributesManager().setSessionAttributes(hMap);
			} else {
				speechText = "So you are bowling first. Meanwhile, here are the rules for you."
						+ " After the 3 2 1 countdown, say a number between 1 and 10. If I guess "
						+ "the same number as you, you're out, and it's your turn to bowl, and vice-versa";
				// Map<String, Object> hMap =
				// input.getAttributesManager().getSessionAttributes();
				hMap.put("UserScore", (Object) ("0"));
				hMap.put("AlexaScore", (Object) ("0"));
				hMap.put("batsman", (Object) ("alexa"));
				input.getAttributesManager().setSessionAttributes(hMap);
			}*/

			
	        return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .withSimpleCard("HelloWorld", speechText)
	                .withShouldEndSession(false)
	                .build();
	    }
		
	}	