package com.exampe.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

public class GameIntentHandler implements RequestHandler{
	
	public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("GameIntent"));
    }
	
	public Optional<Response> handle(HandlerInput input) {
		Map<String, Object> hMap = input.getAttributesManager().getSessionAttributes();
		if (hMap == null || !hMap.containsKey("game") || !hMap.get("game").equals((Object)"true") || !hMap.containsKey("cs")
				|| !hMap.get("cs").equals((Object)"3")) {
			final String speechText = "That was unexpected. \n Please launch the skill again...";
			return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("HelloWorld", speechText)
					// .withShouldEndSession(false)
					.build();
		}
		AttributesManager aM = input.getAttributesManager();
	       double v1 = Math.random()*6;
		   int myVal = (int)(v1) +1;
		   System.out.println("GAMERAND: " + v1);
		   String speechText = "";
		   Request request = input.getRequestEnvelope().getRequest();
		   IntentRequest intentRequest = (IntentRequest) request;
	       Intent intent = intentRequest.getIntent();
	       Map<String, Slot> slots = intent.getSlots();
	       Slot valueSlot = slots.get("Value");
	       String value = valueSlot.getValue();
	       System.out.println("logging... "+myVal);
	       //Map<String, Object> hMap = aM.getSessionAttributes();
	       String batsman = (String)hMap.get("batsman");
	       int batsmanScore;
	       if(batsman.equals("user")) batsmanScore= Integer.parseInt((String)hMap.get("UserScore"));
	       else batsmanScore=  Integer.parseInt((String)hMap.get("AlexaScore"));
	       int sc1 = Integer.parseInt(value);
	       if( sc1 > 6 || sc1 < 1) {
	    	   speechText = "That is an invalid number. Please speak a number between 1 and 6";
	       }
	       else if(myVal != Integer.parseInt(value)) {
	    	   speechText = "3 2 1 " + String.valueOf(myVal);
		       	   int sc = batsmanScore;
		    	   if(batsman.equals("user"))sc += sc1;
		    	   else sc+=myVal;
		    	   String target = (String)hMap.get("Target");
	    		   if(target != null) {
	    			   int targetRuns = Integer.parseInt(target);
	    			   if(targetRuns <= sc) {
	    				   if(batsman.equals("user")) {
	    					   speechText = speechText + " Wow! You have won the match!";
	    				   } else {
	    					   speechText = speechText + " Commiserations to you! I have won the match";
	    				   }
	    			   } else {
	    				   if(batsman.equals("user")) {
	    		    		   hMap.put("UserScore", ((Integer)sc).toString());
	    		    		  // aM.setSessionAttributes(hMap);
	    		    	   }
	    		           else {
	    		        	   hMap.put("AlexaScore", ((Integer)sc).toString());
	    		        	   //aM.setSessionAttributes(hMap);
	    		           }
	    		    	   speechText = speechText + " Your turn";
	    			   }
	    		   } else {
	    			   if(batsman.equals("user")) {
			    		   hMap.put("UserScore", ((Integer)sc).toString());
			    		   //aM.setSessionAttributes(hMap);
			    	   }
			           else {
			        	   hMap.put("AlexaScore", ((Integer)sc).toString());
			        	   //aM.setSessionAttributes(hMap);
			           }
			    	   speechText = speechText + " Your turn";
	    		   }
		    	   
		       
	       }
	       else {
	    	   if(batsman.equals("user")) {
	    		   String target = (String)hMap.get("Target");
	    		   if(target == null) {
	    			   speechText = "3 2 1 "+myVal+"  Ohh! You are out. ";

	    			   hMap.put("ScoreChecker", (Object)true);
	    			   hMap.put("Target",(Object)((Integer)(batsmanScore+1)).toString());
	    			   speechText += "target is " + ((Integer)(batsmanScore+1)).toString() + " now you'll bowl";
	    			   hMap.put("batsman", (Object)("alexa"));
	    			   // aM.setSessionAttributes(hMap);   			   
	    		   } else {
	    			   int targetRuns = Integer.parseInt(target);
	    			   int lossMargin = targetRuns - batsmanScore - 1;
	    			   speechText = "3 2 1 "+myVal+"  Ohh! You are out.";
	    			   if(lossMargin == 0) {
	    				   speechText = speechText + " The match has ended in a scintillating tie!";
	    			   } else {
	    				   speechText = speechText + " You have lost by " + lossMargin + "runs!";
	    			   }
	    		   }
	    	   } else {
	    		   String target = (String)hMap.get("Target");
	    		   if(target == null) {
	    			   speechText = "3 2 1 "+myVal+"  Ohh! I am out. ";
	    					 
	    			   hMap.put("ScoreChecker", (Object)true);
	    			   hMap.put("Target",(Object)((Integer)(batsmanScore+1)).toString());
	    			   speechText += "target is " + ((Integer)(batsmanScore+1)).toString() + " now you'll bat";
	    			   hMap.put("batsman", (Object)("user"));
	    			   //aM.setSessionAttributes(hMap);   	 			   
	    		   } else {
	    			   int targetRuns = Integer.parseInt(target);
	    			   int lossMargin = targetRuns - batsmanScore - 1;
	    			   speechText = "3 2 1 "+myVal+"  Ohh! I am out.";
	    			   if(lossMargin == 0) {
	    				   speechText = speechText + " The match has ended in a scintillating tie!";
	    			   } else {
	    				   speechText = speechText + " You have won by " + lossMargin + "runs!";
	    			   }
	    		   }
	    	   }
	    	   
	       }
	       input.getAttributesManager().setSessionAttributes(hMap);
	      
	       return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .withSimpleCard("HelloWorld", speechText)
	                .withShouldEndSession(false)
	                .build();
    }
}
