package utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class RequestBuilder {
    private RequestBuilder(){}

    public static String buildGet(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        Map<String, String[]> paramsMap = req.getParameterMap();

        sb.append(req.getServletPath());
        if (!paramsMap.isEmpty()) {
            sb.append("?");
        }

        paramsMap.forEach((param, values) -> {
            for (int i = 0; i< values.length; i++) {
                sb.append(param).append("=").append(values[i]);
                if (i != values.length - 1) {
                    sb.append("&");
                }
            }
            sb.append("&");
        });

        return sb.toString().trim();
    }
}
