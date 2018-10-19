package com.exampe.handlers;

import static com.amazon.ask.request.Predicates.intentName;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;

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

public class QuizIntentHandler implements RequestHandler {
	 
		public boolean canHandle(HandlerInput input) {
	        return input.matches(intentName("QuizIntent"));
	    }
		
		public Optional<Response> handle(HandlerInput input) {
			Map<String, Object> hMap = input.getAttributesManager().getSessionAttributes();
			String x="1";
			Object o = (Object)x;
			if(hMap != null && ((hMap.containsKey("game") && hMap.get("game").equals((Object)("true")))
					||( (hMap.containsKey("cs") && hMap.get("cs").equals(x)) ))) {
				final String speechText = "That was unexpected. \n Please launch the skill again...";
				return input.getResponseBuilder()
	            .withSpeech(speechText)
	            .withSimpleCard("HelloWorld", speechText)
	            //.withShouldEndSession(false)
	            .build();
			}

			//if(hMap == null) hMap = Collections.singletonMap("game", (Object)"false");
			hMap.put("game",(Object)"false" );
			GetItemSpec spec = null;
			Table table = null;
			List<Integer> l = (ArrayList)hMap.get("list");
			int randId = (int)(Math.random()*20) +1;
			if(l==null) {
				l=new ArrayList<Integer>();
				l.add(randId);
			}else {
				if(l.size()==20) {
					String speechText = "I'm out of  questions\n please launch the skill again";
					 return input.getResponseBuilder()
				                .withSpeech(speechText)
				                .withSimpleCard("HelloWorld", speechText)
				                .build();
				}
				else {
					while(l.contains(randId)) randId = (int)(Math.random()*20) +1;
					l.add(randId);
				}
			}
			hMap.put("list", l);
			//int randId = (int)(Math.random()%3)+1;
			System.out.println("random id: "+ String.valueOf(randId));
			try {
			AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
					.withRegion(Regions.US_EAST_1)
					.build();
			/*String table_name = "QUIZ";
			TableDescription table_info =
		               client.describeTable(table_name).getTable();
			System.out.format("Item count  : %d\n",
                    table_info.getItemCount().longValue());*/
	        DynamoDB dynamoDB = new DynamoDB(client);
	        
	        table = dynamoDB.getTable("QUIZ");
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
			
	        String speechText = "YOUR QUESTION IS  \n" + question;
	        speechText += "  to answer say option followed by your option";
	        Object ans = answer;
	        hMap.put("cs",x);
	        hMap.put("ans", ans);
	        input.getAttributesManager().setSessionAttributes(hMap);
	        //input.getAttributesManager().setSessionAttributes(Collections.singletonMap("TossAns", tossAns));
	        
	        return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .withSimpleCard("HelloWorld", speechText)
	                .withShouldEndSession(false)
	                .build();
	    }
		
	}	