package com.background.system.util;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/10/3 12:38 PM
 */
public class Test {
    public static void main(String[] args) {

        int remain = 100;

        Mr mr = new Mr() {
            int remain_ ;
            @Override
            public void run() {
                remain_--;
                System.out.println("当前库存：" + remain);
            }
            @Override
            public Mr setP(int x) {
                remain_ = x;
                return this;
            }
        };

        new Thread(mr.setP(remain)).start();

    }
}
