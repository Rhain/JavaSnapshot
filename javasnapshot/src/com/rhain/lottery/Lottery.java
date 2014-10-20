package com.rhain.lottery;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 抽奖
 * @author Rhain
 * @since 2014/10/14.
 */
public class Lottery {

    private  List<Reward> rewards;

    public Lottery(List<Reward> rewards){
        this.rewards = rewards;
    }

    /**
     *
     * @return 返回null 则表示没有抽中任何东西
     */
    public  Reward getRand(){
        int arrSum = getArrSum(rewards);
        Reward result = null;
        if(arrSum <= 0){
            return result;
        }
        if(rewards != null && rewards.size() > 0){
            for(int i=0;i<rewards.size();i++){
                Random random = new Random();
                int randNum = random.nextInt(arrSum);
                int compareNum = randNum + 1;  //中奖概率范围为 [1,arrSum]
                if(compareNum <= rewards.get(i).getSuccPercent()){
                    result = rewards.get(i);
                    break;
                }else{
                    arrSum -= rewards.get(i).getSuccPercent();
                }
            }
        }

        return result;
    }

    private int getArrSum(List<Reward> rewards){
        int arrSum = 0;
        if(rewards != null && rewards.size() > 0){
            for(Reward reward : rewards){
                arrSum += reward.getSuccPercent();
            }
        }
        return arrSum;
    }

    public static class LotteryBuilder{
        private  List<Reward> rewards = new ArrayList<>();

        public LotteryBuilder addReward(int index,String name,int succPercent){
            Reward reward = new Reward(index,name,succPercent);
            this.rewards.add(reward);
            return this;
        }

        public Lottery build(){
            return new Lottery(this.rewards);
        }
    }

}
