// First, create a SecurityConfig class
package com.bal.bal_management_system.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SecurityConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthenticationInterceptor())
                .excludePathPatterns("/", "/login", "/register", "/logout",
                        "/aboutBal", "/matches", "/standings",
                        "/stats", "/players", "/gallery",
                        "/css/**", "/js/**", "/images/**", "/uploads/**");
    }
}

// Authentication Interceptor
class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        // Check if user is authenticated
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            response.sendRedirect("/login");
            return false;
        }

        // Check admin access
        if (requestURI.startsWith("/admin")) {
            String role = (String) session.getAttribute("role");
            if (!"ADMIN".equalsIgnoreCase(role)) {
                response.sendRedirect("/access-denied");
                return false;
            }
        }

        return true;
    }
}