package com.exampe.handlers;

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
import static com.amazon.ask.request.Predicates.intentName;

public class TossIntentHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("TossIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		Map<String, Object> hMap = input.getAttributesManager().getSessionAttributes();
		if (hMap == null || !hMap.containsKey("game") || !hMap.get("game").equals((Object)"true") || !hMap.containsKey("cs")
				|| !(hMap.get("cs").equals((Object)"1") || hMap.get("ScoreChecker")!= null)) {
			final String speechText = "That was unexpected. \n Please launch the skill again...";
			return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("HelloWorld", speechText)
					// .withShouldEndSession(false)
					.build();
		}
		
		Request request = input.getRequestEnvelope().getRequest();
		IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();
        Slot valueSlot = slots.get("Value");
        String value = valueSlot.getValue().toString();
        String ans = (String) input.getAttributesManager().getSessionAttributes().get("TossAns");
        System.out.println("logging... "+value+" "+ans);
        String speechText = "";
        //Map<String, Object> hMap = input.getAttributesManager().getSessionAttributes();
        if(hMap.get("ScoreChecker") == null) {
	        if(ans.equals(value)) {
	        	speechText = "You won the toss. What do you want to do?";
	        	hMap.put("cs",(Object)("2"));
	        }
	        else {
	        	double v1 = Math.random();
	        	hMap.put("cs",(Object)("3"));
        		System.out.println("TossRand: " + v1);
	        	int choice = (int)(v1*2);
	        	if(choice == 0) {
		        	speechText = "That's Incorrect. I will bat first. Meanwhile, here are the rules for you."
		        			+ " After the 3 2 1 countdown, say a number between 1 and 6. If I guess "
		        			+ "the same number as you, i'm out, and it's your turn to bat, and vice-versa";
		        	//hMap.clear();
		        	hMap.put("batsman", (Object)("alexa"));
	        	} else {
	        		speechText = "That's Incorrect. I will bowl first. Meanwhile, here are the rules for you."
		        			+ " After the 3 2 1 countdown, say a number between 1 and 6. If I guess "
		        			+ "the same number as you, you're out, and it's your turn to bowl, and vice-versa";
		        	//hMap.clear();
		        	hMap.put("batsman", (Object)("user"));
	        	}
	        	hMap.put("UserScore", (Object)("0"));
	        	hMap.put("AlexaScore", (Object)("0"));
	        	//input.getAttributesManager().setSessionAttributes(hMap);   
	        }
        }/* else {
        	   String batsman = (String)hMap.get("batsman");
        	   if(batsman.equals("user")) {
        		   int score = Integer.parseInt((String)hMap.get("UserScore"));
        		   if(score == Integer.parseInt(value)) {
        			   score = (int)(score*(105.0/100));
        			   speechText = speechText + " Correct Answer! Your increased total is "+score+ " runs. "
        			   		+ "Now its your time to bowl ";
        			   hMap.remove("ScoreChecker");
        			   //(Integer)sc).toString()
        			   hMap.put("Target",(Object)((Integer)(score+1)).toString());
        			   hMap.put("batsman", (Object)("alexa"));
        		   } else {
        			   score = (int)(score*(95.0/100));
        			   speechText = speechText + " Incorrect Answer! Your reduced total is "+score+ " runs. "
        			   		+ "Now its your time to bowl ";
        			   hMap.remove("ScoreChecker");
        			   hMap.put("Target",(Object)((Integer)(score+1)).toString());
        			   //hMap.put("Target",(Object)(score+1));
        			   hMap.put("batsman", (Object)("alexa"));
        		   }
        	   } else {
        		   int score = (Integer)hMap.get("AlexaScore");
        		   if(score == Integer.parseInt(value)) {
        			   score = (int)(score*(95.0/100)) + 1;
        			   speechText = speechText + " Correct Answer! Your reduced target is "+score+ " runs. "
        			   		+ "Now its your time to bat ";
        			   hMap.remove("ScoreChecker");
        			   hMap.put("Target",(Object)((Integer)(score)).toString());
        			   //hMap.put("Target",(Object)(score));
        			   hMap.put("batsman", (Object)("user"));
        		   } else {
        			   score = (int)(score*(105.0/100)) + 1;
        			   speechText = speechText + " Incorrect Answer! Your increased target is "+score+ " runs. "
        			   		+ "Now its your time to bat ";
        			   hMap.remove("ScoreChecker");
        			   hMap.put("Target",(Object)((Integer)(score)).toString());
        			   //hMap.put("Target",(Object)(score));
        			   hMap.put("batsman", (Object)("user"));
        		   }
        	   }
        	   
        }*/
        input.getAttributesManager().setSessionAttributes(hMap);	
        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .withShouldEndSession(false)
                .build();
	}

}
