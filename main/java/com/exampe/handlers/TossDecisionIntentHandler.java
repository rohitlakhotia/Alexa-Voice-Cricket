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

public class TossDecisionIntentHandler implements RequestHandler {

	public boolean canHandle(HandlerInput input) {
		return input.matches(intentName("TossDecisionIntent"));
	}

	public Optional<Response> handle(HandlerInput input) {
		Map<String, Object> hMap = input.getAttributesManager().getSessionAttributes();
		if (hMap == null || !hMap.containsKey("game") || !hMap.get("game").equals((Object)"true") || !hMap.containsKey("cs")
				|| !hMap.get("cs").equals((Object)"2")) {
			final String speechText = "That was unexpected. \n Please launch the skill again...";
			return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("HelloWorld", speechText)
					// .withShouldEndSession(false)
					.build();
		}
		Request request = input.getRequestEnvelope().getRequest();
		IntentRequest intentRequest = (IntentRequest) request;
		Intent intent = intentRequest.getIntent();
		Map<String, Slot> slots = intent.getSlots();
		Slot batBallSlot = slots.get("BatBall");
		String value = batBallSlot.getValue();
		String speechText = "";
		hMap.put("cs", (Object)"3");
		if (value.startsWith("bat")) {
			speechText = "So you are batting first. Meanwhile, here are the rules for you."
					+ " After the 3 2 1 countdown, say a number between 1 and 6. If I guess "
					+ "the same number as you, you're out, and it's your turn to bowl, and vice-versa";
			// Map<String, Object> hMap =
			// input.getAttributesManager().getSessionAttributes();
			hMap.put("UserScore", (Object) ("0"));
			hMap.put("AlexaScore", (Object) ("0"));
			hMap.put("batsman", (Object) ("user"));
			input.getAttributesManager().setSessionAttributes(hMap);
		} else {
			speechText = "So you are bowling first. Meanwhile, here are the rules for you."
					+ " After the 3 2 1 countdown, say a number between 1 and 6. If I guess "
					+ "the same number as you, I'm out, and it's your turn to bat, and vice-versa";
			// Map<String, Object> hMap =
			// input.getAttributesManager().getSessionAttributes();
			hMap.put("UserScore", (Object) ("0"));
			hMap.put("AlexaScore", (Object) ("0"));
			hMap.put("batsman", (Object) ("alexa"));
			input.getAttributesManager().setSessionAttributes(hMap);
		}

		return input.getResponseBuilder().withSpeech(speechText).withSimpleCard("HelloWorld", speechText)
				.withShouldEndSession(false).build();
	}

}
