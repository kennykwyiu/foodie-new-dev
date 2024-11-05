package com.kenny.controller.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserTokenInterceptor implements HandlerInterceptor {
    /**
     * Intercept requests before accessing the controller
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


         System.out.println("Please log in...");

// System.out.println("Account logged in from a different location...");

// System.out.println("Please log in...");
        /**
         * false: The request is intercepted and rejected, there is a problem with the validation
         * true: After the request passes the validation check, it is OK and can be released
         */
        return false;
    }

    /**
     * After the request accesses the controller, before rendering the view
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    /**
     * After the request accesses the controller, after rendering the view
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
