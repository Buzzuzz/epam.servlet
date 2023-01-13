package utils;

import constants.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {
    private RequestBuilder() {
    }

    public static String buildRequest(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getServletPath());
        Map<String, String[]> paramsMap = req.getParameterMap();

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

        params.forEach((k, v) -> {
            for (String s : v) {
                builder.append(k).append("=").append(s).append("&");
            }
        });
        return builder.toString();
    }

    public static Map<String, String[]> getParamsMap(HttpServletRequest req, String... paramNames) {
        Map<String, String[]> paramsMap = new HashMap<>();
        for (String param : paramNames) {
            paramsMap.put(param, new String[]{req.getParameter(param)});
        }
        return paramsMap;
    }
}
