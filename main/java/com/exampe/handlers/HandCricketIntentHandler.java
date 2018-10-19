package com.exampe.handlers;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.TableDescription;

import static com.amazon.ask.request.Predicates.intentName;

public class HandCricketIntentHandler implements RequestHandler {
 
	public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("HandCricketIntent"));
    }
	
	public Optional<Response> handle(HandlerInput input) {
		Map<String, Object> hMap = input.getAttributesManager().getSessionAttributes();
		if(hMap != null && hMap.containsKey("game")) {
			final String speechText = "That was unexpected. \n Please launch the skill again...";
			return input.getResponseBuilder()
            .withSpeech(speechText)
            .withSimpleCard("HelloWorld", speechText)
            //.withShouldEndSession(false)
            .build();
		}
		//hMap = Collections();
		hMap.put("game", (Object)"true");
		String x="1";
        Object o=(Object)x;
        hMap.put("cs", o);
        GetItemSpec spec = null;
		Table table = null;
		int randId = (int)(Math.random()*8);
		System.out.println("random id: "+ String.valueOf(randId));
		try {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
				.withRegion(Regions.US_EAST_1)
				.build();
		
        DynamoDB dynamoDB = new DynamoDB(client);
        
        table = dynamoDB.getTable("QUESTION_ANSWER");
        spec = new GetItemSpec().withPrimaryKey("ID",String.valueOf(randId));
		} catch(Exception e) {
			System.err.println("ERROR = " + e.getMessage());
		}
        
        String question = "", answer = "";
        try {
            Item outcome = table.getItem(spec);
            answer = outcome.getString("ANSWER");
            question = outcome.getString("QUESTION");
            System.out.println("GetItem succeeded: " + outcome);
            System.out.println("Ques1: "+ question);
            System.out.println("Ques2: "+ outcome.getString("QUESTION"));
            System.out.println("Ques3: "+ (String)outcome.get("QUESTION"));
        }
        catch (Exception e) {
            System.err.println("ERROR = " + e.getMessage());
        }
		
        String speechText = "Its toss time.       " + question;
        speechText += "  to answer Say value is followed by your answer";
        Object tossAns = answer;
        hMap.put("TossAns",tossAns);
        input.getAttributesManager().setSessionAttributes(hMap);
      
       return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("HelloWorld", speechText)
                .withShouldEndSession(false)
                .build();
    }
}	