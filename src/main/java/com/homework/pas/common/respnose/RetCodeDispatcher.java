package com.homework.pas.common.respnose;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RetCodeDispatcher {
    public static Map<Integer, ResponseCode> servletRetCodeMap = new ConcurrentHashMap<>();
    public static Map<Integer, ResponseCode> parseRetCodeMap = new ConcurrentHashMap<>();
}
