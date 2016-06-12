package com.google.kurachi.shintaro.aiwolf2;

import java.util.List;
import java.util.Random;

class Util {
    public static <T> T randomSelect(List<T> agentList) {
        int num = new Random().nextInt(agentList.size());
        return agentList.get(num);
    }
}

