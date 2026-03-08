package com.nikhitech.decisionlogger.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.nikhitech.decisionlogger.menu.Menu;

/*
 * ===============================================================
 * ACTIVE MENU INTERCEPTOR
 * ===============================================================
 *
 * Responsibility
 * ---------------------------------------------------------------
 * Determine which sidebar menu is active.
 *
 * OCP
 * ---------------------------------------------------------------
 * This class never changes when new menus are added.
 */

public class ActiveMenuInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String uri = request.getRequestURI();

        for (Menu menu : Menu.values()) {

            if (uri.contains(menu.getUri())) {

                request.setAttribute(
                        "activeMenu",
                        menu.getMenuName()
                );

                break;
            }
        }

        return true;
    }
}