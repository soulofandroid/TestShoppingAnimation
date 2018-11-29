package com.testshoppinganimation;

/**
 * <p>作者   wurui</p>
 * <p>时间   2018/11/29 0029</p>
 * <p>包名   com.testshoppinganimation</p>
 * <p>描述   商品</p>
 */
public class Shopping {

    String name;//商品名称
    int price;//商品价格
    int count;//数量

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
