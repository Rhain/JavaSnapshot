package com.rhain.lottery;

/**
 * 奖品
 * @author Rhain
 * @since 2014/10/14.
 */
public class Reward {

    /**
     * 奖品编号
     */
    private int index;

    /**
     * 奖品名称
     */
    private String name;

    /**
     * 中奖概率
     */
    private int succPercent;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSuccPercent() {
        return succPercent;
    }

    public void setSuccPercent(int succPercent) {
        this.succPercent = succPercent;
    }

    public Reward(int index,String name,int succPercent){
        this.index = index;
        this.name = name;
        this.succPercent = succPercent;
    }

    @Override
    public String toString() {
        return "Reward{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", succPercent=" + succPercent +
                '}';
    }
}
