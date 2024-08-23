package com.example.makotomurase;

public class BlackJack {
    public int calcScore(int haveScore, int Score) {
        int getScore;
        if (Score==1){
            getScore=50*0;
        }else{
            getScore=50*Score;
        }
        haveScore += getScore;
        return haveScore;
    }
}
