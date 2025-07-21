package com.ad.base.cdi;

import com.ad.base.modelo.Usuario;
import com.ad.base.modelo.RolUsuario;
import com.ad.base.ejb.UsuarioService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named("usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;

    private Usuario usuario = new Usuario();
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private List<RolUsuario> listaRoles = new ArrayList<>();

    private boolean mostrarFormulario = false;

    private Long idRolSeleccionado; // NUEVO: ID del rol para trabajar sin converter

    private Usuario usuarioAEliminar;

    @PostConstruct
    public void init() {
        cargarUsuarios();
        cargarRoles();
    }

    // ========================================
    // ACCIONES ABM
    // ========================================

    public void nuevo() {
        this.usuario = new Usuario();
        this.idRolSeleccionado = null;
        this.mostrarFormulario = true;
    }

    public void editar(Usuario seleccionado) {
        if (seleccionado != null) {
            this.usuario = new Usuario();
            this.usuario.setId(seleccionado.getId());
            this.usuario.setUsername(seleccionado.getUsername());
            this.usuario.setPassword(seleccionado.getPassword());
            this.usuario.setRol(seleccionado.getRol());

            this.idRolSeleccionado = seleccionado.getRol() != null
                    ? seleccionado.getRol().getIdRol()
                    : null;

            this.mostrarFormulario = true;
        }
    }

    public void cancelarEdicion() {
        this.usuario = new Usuario();
        this.idRolSeleccionado = null;
        this.mostrarFormulario = false;
    }

    public void reiniciarVista() {
        this.usuario = new Usuario();
        this.idRolSeleccionado = null;
        this.mostrarFormulario = false;
        cargarUsuarios();
        cargarRoles();
    }

    public void guardar() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Advertencia", "El nombre de usuario es obligatorio"));
            return;
        }

        // Asignar el rol según el ID seleccionado
        if (idRolSeleccionado != null) {
            for (RolUsuario rol : listaRoles) {
                if (rol.getIdRol().equals(idRolSeleccionado)) {
                    usuario.setRol(rol);
                    break;
                }
            }
        } else {
            usuario.setRol(null);
        }

        if (usuario.getId() == null) {
            usuarioService.insertar(usuario);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Usuario creado correctamente"));
        } else {
            usuarioService.actualizar(usuario);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Actualizado", "Usuario actualizado correctamente"));
        }

        cargarUsuarios();
        usuario = new Usuario();
        idRolSeleccionado = null;
        mostrarFormulario = false;
    }


    public void prepararEliminacion(Usuario usuario) {
        this.usuarioAEliminar = usuario;
    }

    public void eliminar() {
        if (usuarioAEliminar != null) {
            usuarioService.eliminar(usuarioAEliminar);
            cargarUsuarios();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Eliminado", "Usuario eliminado correctamente"));
        }
        usuarioAEliminar = null;
    }

    public Map<String, Boolean> getOpcionesEstado() {
        Map<String, Boolean> estados = new LinkedHashMap<>();
        estados.put("Activo", true);
        estados.put("Inactivo", false);
        return estados;
    }

    // ========================================
    // CARGAS INICIALES
    // ========================================

    private void cargarUsuarios() {
        listaUsuarios = usuarioService.listarUsuarios();
    }

    private void cargarRoles() {
        listaRoles = usuarioService.listarRoles();
    }

    // ========================================
    // GETTERS Y SETTERS
    // ========================================

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public List<RolUsuario> getListaRoles() {
        return listaRoles;
    }

    public void setListaRoles(List<RolUsuario> listaRoles) {
        this.listaRoles = listaRoles;
    }

    public boolean isMostrarFormulario() {
        return mostrarFormulario;
    }

    public void setMostrarFormulario(boolean mostrarFormulario) {
        this.mostrarFormulario = mostrarFormulario;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public void setUsuarioService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public Long getIdRolSeleccionado() {
        return idRolSeleccionado;
    }

    public void setIdRolSeleccionado(Long idRolSeleccionado) {
        this.idRolSeleccionado = idRolSeleccionado;
    }
}
