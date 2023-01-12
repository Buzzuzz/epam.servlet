package utils;

import constants.AttributeConstants;
import jakarta.servlet.http.HttpServletRequest;

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

    public static String buildCommand(String controller, String command) {
        return controller + "?" + AttributeConstants.COMMAND_ATTR + "=" + command;
    }
}
