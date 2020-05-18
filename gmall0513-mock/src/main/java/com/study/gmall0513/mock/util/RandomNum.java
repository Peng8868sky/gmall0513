package com.study.gmall0513.mock.util;

import java.util.Random;

/**
 * 生成日志
 */
public class RandomNum {
    public static final int getRandInt(int fromNum, int toNum) {
        return fromNum + new Random().nextInt(toNum - fromNum + 1);
    }
}