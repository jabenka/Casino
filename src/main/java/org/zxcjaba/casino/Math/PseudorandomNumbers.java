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

    public long generateValueForRandomNumber(){
        Long val=rand.nextLong(200);
        val=val*33;
        val/=56;
        val-=val/2;
        return val>0?val/4:(val*(-1))/2;

    }



}
