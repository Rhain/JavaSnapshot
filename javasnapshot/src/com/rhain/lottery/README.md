###抽象抽奖实例

```
    Lottery lottery = new LotteryBuilder().addReward(1,"Iphone",1)
                .addReward(2, "Ipad", 1)
                .addReward(3,"Vip",10)
                .addReward(4,"Ticket",88)
                .addReward(5,"Null",100)
                .build();        
    Reward = lottery.getRand();
```