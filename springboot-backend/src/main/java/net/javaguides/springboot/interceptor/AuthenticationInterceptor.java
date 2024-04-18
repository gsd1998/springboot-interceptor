package net.javaguides.springboot.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String AUTH_USERNAME="admin";
    private static final String AUTH_PASSWORD="admin";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("AuthenticationInterceptor --> PreHandle()");

        //Basic YWRtaW46YWRtaW4='
        String requestHeader = request.getHeader("Authorization");
        if(StringUtils.hasText(requestHeader) && requestHeader.startsWith("Basic ")){

            String authToken = requestHeader.substring("Basic ".length());

            byte[] decodedAuthToken = Base64.getDecoder().decode(authToken);
            String plainTextToken = new String(decodedAuthToken, StandardCharsets.UTF_8);//username:password
            System.out.println(plainTextToken);

            String[] splitText = plainTextToken.split(":");
            String userName = splitText[0];
            String password = splitText[1];

            if(AUTH_USERNAME.equalsIgnoreCase(userName) && AUTH_PASSWORD.equalsIgnoreCase(password)){
                return true;
            }
        }
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized access");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        System.out.println("AuthenticationInterceptor --> PostHandle()");
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        System.out.println("AuthenticationInterceptor --> afterCompletion()");
    }

}
