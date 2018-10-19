package com.main;
import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.exampe.handlers.CancelandStopIntentHandler;
import com.exampe.handlers.GameIntentHandler;
import com.exampe.handlers.HandCricketIntentHandler;
import com.exampe.handlers.HelpIntentHandler;
import com.exampe.handlers.LaunchRequestHandler;
import com.exampe.handlers.QuizAnswerIntentHandler;
import com.exampe.handlers.QuizIntentHandler;
import com.exampe.handlers.SessionEndedRequestHandler;
import com.exampe.handlers.TossDecisionIntentHandler;
import com.exampe.handlers.TossIntentHandler;


public class HelloWorldStreamHandler extends SkillStreamHandler {
 
	private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new CancelandStopIntentHandler(),
                        new HandCricketIntentHandler(),
                        new HelpIntentHandler(),
                        new LaunchRequestHandler(),
                        new SessionEndedRequestHandler(),
                        new GameIntentHandler(),
                        new TossDecisionIntentHandler(),
                		new QuizIntentHandler(),
                		new QuizAnswerIntentHandler(),
                        new TossIntentHandler()
                		)
                .withSkillId("amzn1.ask.skill.9eb09d1a-5288-4457-b382-3a5652570316")
                .build();
    }
	
	public HelloWorldStreamHandler() {
        super(getSkill());
    }
	
}
