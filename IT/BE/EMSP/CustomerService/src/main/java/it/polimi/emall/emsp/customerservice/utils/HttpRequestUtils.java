package it.polimi.emall.emsp.customerservice.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

public class HttpRequestUtils {
    public static HttpServletRequest getCurrentHttpRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes)requestAttributes).getRequest();
        }
        return null;
    }

    public static String getAuthorizationToken(){
        return Objects.requireNonNull(getCurrentHttpRequest()).getHeader("Authorization").split("Bearer ")[1];
    }

}
