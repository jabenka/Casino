package org.zxcjaba.casino.Math;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
@Component
public class PseudorandomNumbers {
    private SecureRandom rand;

    public PseudorandomNumbers() {
        rand = new SecureRandom();
    }

    public long generateValueForRoulette(){
        Long val=rand.nextLong(100);
        val=val*33;
        return val%38;
    }





}
