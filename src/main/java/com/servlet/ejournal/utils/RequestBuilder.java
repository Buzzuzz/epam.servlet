package com.servlet.ejournal.utils;

import com.servlet.ejournal.constants.AttributeConstants;

import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {
    private RequestBuilder() {
    }

    public static String buildRequest(String controller, Map<String, String[]> paramsMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(controller);

        if (!paramsMap.isEmpty()) {
            sb.append("?");
        }

        paramsMap.forEach((param, values) -> {
            for (int i = 0; i < values.length; i++) {
                sb.append(param).append("=").append(values[i]);
                if (i != values.length - 1) {
                    sb.append("&");
                }
            }
            sb.append("&");
        });

        return sb.toString().trim();
    }

    public static String buildCommand(String controller, String command, Map<String, String[]> params) {
        StringBuilder builder = new StringBuilder();
        builder
                .append(controller)
                .append("?")
                .append(AttributeConstants.COMMAND_ATTR)
                .append("=")
                .append(command)
                .append("&");

        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (entry.getValue() == null) continue;
            for (String stringValue : entry.getValue()) {
                builder.append(entry.getKey()).append("=").append(stringValue).append("&");
            }
        }
        return builder.toString();
    }

    public static Map<String, String[]> getSpecifiedParamsMap(Map<String, String[]> generalMap, String... paramNames) {
        Map<String, String[]> paramsMap = new HashMap<>();
        for (String param : paramNames) {
            paramsMap.put(param, generalMap.get(param));
        }
        return paramsMap;
    }
}
