package com.porkerspicks.horses;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * This is a demonstration class to show a quick demo of the new Betfair API-NG.
 * When you execute the class will: <li>find a market (next horse race in the
 * UK)</li> <li>get prices and runners on this market</li> <li>place a bet on 1
 * runner</li> <li>handle the error</li>
 *
 */
@SpringBootApplication(scanBasePackages = {"com.porkerspicks.horses","com.betfair.aping"})
@EnableScheduling
public class Horses {

    public static void main(String[] args) {
    	SpringApplication.run(Horses.class,args);
    }

}
