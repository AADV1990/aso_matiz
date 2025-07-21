package com.ad.seguridad;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter("*.xhtml")
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String loginURL = contextPath + "/pages/login.xhtml";

        boolean recursoLogin = uri.contains("login.xhtml");

        boolean recursoPublico = uri.contains("/jakarta.faces.resource/")
                || uri.contains("/resources/");

        boolean logueado = (session != null && session.getAttribute("usuarioLogueado") != null);

        if (logueado || recursoLogin || recursoPublico) {
            // 🛑 Evitar cache en navegadores para páginas protegidas
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0
            res.setDateHeader("Expires", 0);     // Proxies

            // 🔓 Continuar con la solicitud
            chain.doFilter(request, response);
        } else {
            System.out.println("🔁 Redirigiendo a login.xhtml");
            res.sendRedirect(loginURL);
        }
    }
}
