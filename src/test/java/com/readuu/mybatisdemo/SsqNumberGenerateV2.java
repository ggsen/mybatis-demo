package com.readuu.mybatisdemo;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

public class SsqNumberGenerateV2 {
    private static final String BASE_URL = "http://www.cwl.gov.cn/cwl_admin/kjxx/findDrawNotice?name=ssq&issueCount=";

    private static final int BUY_COUNT = 5;

    private static final int RED_MOST_COUNT = 6;

    private static final int BLUE_MOST_COUNT = 6;

    private static final int LAST_COUNT = 50;

    public static void main(String[] args) {
        HttpRequest request = HttpUtil.createGet(BASE_URL + LAST_COUNT);
        request.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36");
        request.header("Referer", "http://www.cwl.gov.cn/kjxx/ssq/kjgg/");
        request.header("host", "www.cwl.gov.cn");
        request.header("Accept", "application/json");
        HttpResponse response = request.execute();
        if (!response.isOk()) {
            System.out.println(String.format("%s, %s", "请求失败T_T!!", response.body()));
            return;
        }

        String body = response.body();
        JSONObject dataJson = JSONUtil.parseObj(body);

        JSONArray dataArray = dataJson.getJSONArray("result");
        List<SsqNumber> ssqNumberList = dataArray.toList(SsqNumber.class);
        Map<String, Integer> redMap = new HashMap<>();
        Map<String, Integer> blueMap = new HashMap<>();
        ssqNumberList.stream().forEach(ssqNumber -> {
            String[] redArray = ssqNumber.getRed().split(",");
            for (String red : redArray) {
                redMap.put(red, redMap.get(red) == null ? 1 : redMap.get(red) + 1);
            }

            String blue = ssqNumber.getBlue();
            blueMap.put(blue, blueMap.get(blue) == null ? 1 : blueMap.get(blue) + 1);
        });
        List<String> mostReds = getMostNumbers(redMap, RED_MOST_COUNT);
        List<String> mostBlues = getMostNumbers(blueMap, BLUE_MOST_COUNT);
        List<String> buyReds, buyBlues;
        for (int i = 0; i < BUY_COUNT; i++) {
            buyReds = generateBuyBalls(mostReds, 3);

            while (buyReds.size() < 6) {
                int redNumber = RandomUtil.randomInt(1, 34);
                String red = String.valueOf(redNumber < 10 ? "0" + redNumber : redNumber);
                if (!buyReds.contains(red)) {
                    buyReds.add(red);
                }
            }
            Collections.sort(buyReds);
            System.out.print(buyReds);

            buyBlues = generateBuyBalls(mostBlues, 1);
            System.out.println(buyBlues);
        }
    }

    private static List<String> generateBuyBalls(List<String> mostReds, int ballSize) {
        List<String> buyReds = new ArrayList<>();
        while (buyReds.size() < ballSize) {
            int randomNumber = RandomUtil.randomInt(mostReds.size());
            String red = mostReds.get(randomNumber);
            if (!buyReds.contains(red)) {
                buyReds.add(red);
            }
        }
        return buyReds;
    }

    private static List<String> getMostNumbers(Map<String, Integer> ballMap, int limit) {
        return ballMap.entrySet()
                .stream()
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Data
    class SsqNumber {
        /**
         * 开奖日期
         */
        private String date;

        /**
         * 红球
         */
        private String red;

        /**
         * 红球列表
         */
        private List<String> redList;

        /**
         * 蓝球
         */
        private String blue;
    }
}
