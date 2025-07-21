package com.ad.base.cdi;

import com.ad.base.dto.UsuarioDTO;
import com.ad.excepciones.ContrasenaIncorrectaException;
import com.ad.excepciones.UsuarioNoEncontradoException;
import com.ad.base.ejb.LoginService;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    private UsuarioDTO usuarioLogueado;

    @EJB
    private LoginService loginService;

    // Método para autenticar al usuario
    public String login() {
        try {
            usuarioLogueado = loginService.autenticar(username, password);
            FacesContext.getCurrentInstance().getExternalContext()
                    .getSessionMap().put("usuarioLogueado", usuarioLogueado);

            return "/pages/dashboard.xhtml?faces-redirect=true";
        } catch (UsuarioNoEncontradoException | ContrasenaIncorrectaException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de login", e.getMessage()));
            username = "";
            password = "";
            return null;
        } catch (Exception e) {
            // Por si hay un error inesperado
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error inesperado", "Usuario o Contraseña incorrecta."));
            e.printStackTrace();

            // ✅ Limpiar campos al fallar
            username = "";
            password = "";

            return null;
        }
    }


    public String logout() {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();

        try {
            HttpSession session = (HttpSession) ec.getSession(false);
            if (session != null) {
                session.invalidate();  // 💥 Invalida toda la sesión
                System.out.println("✅ Sesión invalidada correctamente.");
            }

            // 🔁 Redirige manualmente al login
            ec.redirect(ec.getRequestContextPath() + "/pages/login.xhtml");

        } catch (IOException e) {
            System.err.println("❌ Error al redirigir tras logout:");
            e.printStackTrace();
        }

        return null;  // Ya hiciste la redirección manual
    }


    public void redirigirSiYaEstaLogueado() {
        if (usuarioLogueado != null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext()
                        .redirect("dashboard.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // Getters y Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public UsuarioDTO getUsuarioLogueado() {
        return usuarioLogueado;
    }
}
