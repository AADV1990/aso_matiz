package com.ad.base.cdi;

import com.ad.base.ejb.PersonaService;
import com.ad.base.ejb.UsuarioService;
import com.ad.base.modelo.Persona;
import com.ad.base.modelo.RolUsuario;
import com.ad.base.modelo.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.*;

@Named("usuarioController")
@SessionScoped
public class UsuarioController implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UsuarioService usuarioService;

    @Inject
    private PersonaService personaService;

    private Usuario usuario = new Usuario();
    private List<Usuario> listaUsuarios = new ArrayList<>();
    private List<RolUsuario> listaRoles = new ArrayList<>();
    private List<Persona> listaPersonas = new ArrayList<>();

    private Long idRolSeleccionado;
    private Integer idPersonaSeleccionada;

    private Persona personaSeleccionada; // 🔹 NUEVO: para usar con el diálogo

    private boolean mostrarFormulario = false;
    private Usuario usuarioAEliminar;

    @PostConstruct
    public void init() {
        cargarUsuarios();
        cargarRoles();
        cargarPersonas();
    }

    public void nuevo() {
        this.usuario = new Usuario();
        this.idRolSeleccionado = null;
        this.idPersonaSeleccionada = null;
        this.mostrarFormulario = true;
    }

    public void editar(Usuario seleccionado) {
        if (seleccionado != null) {
            this.usuario = new Usuario();
            this.usuario.setId(seleccionado.getId());
            this.usuario.setUsername(seleccionado.getUsername());
            this.usuario.setPassword(seleccionado.getPassword());
            this.usuario.setRol(seleccionado.getRol());
            this.usuario.setActivo(seleccionado.getActivo());
            this.usuario.setPersona(seleccionado.getPersona());

            this.idRolSeleccionado = seleccionado.getRol() != null
                    ? seleccionado.getRol().getIdRol()
                    : null;

            this.idPersonaSeleccionada = seleccionado.getPersona() != null
                    ? seleccionado.getPersona().getIdPersona()
                    : null;

            this.mostrarFormulario = true;
        }
    }

    public void cancelarEdicion() {
        this.usuario = new Usuario();
        this.idRolSeleccionado = null;
        this.idPersonaSeleccionada = null;
        this.mostrarFormulario = false;
    }

    public void reiniciarVista() {
        this.usuario = new Usuario();
        this.idRolSeleccionado = null;
        this.idPersonaSeleccionada = null;
        this.mostrarFormulario = false;
        cargarUsuarios();
        cargarRoles();
        cargarPersonas();
    }

    public void guardar() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Advertencia", "El nombre de usuario es obligatorio"));
            return;
        }

        if (idRolSeleccionado == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Advertencia", "Debe seleccionar un rol"));
            return;
        }

        if (idPersonaSeleccionada == null) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Advertencia", "Debe seleccionar una persona"));
            return;
        }


        // Asignar Rol
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

        // Asignar Persona (si se seleccionó desde el diálogo)
        if (personaSeleccionada != null) {
            usuario.setPersona(personaSeleccionada);
        }

        if (usuario.getId() == null) {
            usuarioService.insertar(usuario);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Éxito", "Usuario creado correctamente"));
        } else {
            Usuario original = usuarioService.buscarPorId(usuario.getId());
            if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
                usuario.setPassword(original.getPassword());
            }
            usuarioService.actualizar(usuario);
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "Actualizado", "Usuario actualizado correctamente"));
        }

        cargarUsuarios();
        usuario = new Usuario();
        personaSeleccionada = null;
        idRolSeleccionado = null;
        idPersonaSeleccionada = null;
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

    public void seleccionarPersonaDesdeDialogo() {
        if (personaSeleccionada != null) {
            this.usuario.setPersona(personaSeleccionada);
        }
    }

    public Map<String, Boolean> getOpcionesEstado() {
        Map<String, Boolean> estados = new LinkedHashMap<>();
        estados.put("Activo", true);
        estados.put("Inactivo", false);
        return estados;
    }

    private void cargarUsuarios() {
        listaUsuarios = usuarioService.listarUsuarios();
    }

    private void cargarRoles() {
        listaRoles = usuarioService.listarRoles();
    }

    private void cargarPersonas() {
        listaPersonas = personaService.listarPersonas();
    }

    // GETTERS Y SETTERS

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getListaUsuarios() {
        return listaUsuarios;
    }

    public List<RolUsuario> getListaRoles() {
        return listaRoles;
    }

    public List<Persona> getListaPersonas() {
        return listaPersonas;
    }

    public Long getIdRolSeleccionado() {
        return idRolSeleccionado;
    }

    public void setIdRolSeleccionado(Long idRolSeleccionado) {
        this.idRolSeleccionado = idRolSeleccionado;
    }

    public Integer getIdPersonaSeleccionada() {
        return idPersonaSeleccionada;
    }

    public void setIdPersonaSeleccionada(Integer idPersonaSeleccionada) {
        this.idPersonaSeleccionada = idPersonaSeleccionada;
    }

    public Persona getPersonaSeleccionada() {
        return personaSeleccionada;
    }

    public void setPersonaSeleccionada(Persona personaSeleccionada) {
        this.personaSeleccionada = personaSeleccionada;
    }

    public boolean isMostrarFormulario() {
        return mostrarFormulario;
    }

    public void setMostrarFormulario(boolean mostrarFormulario) {
        this.mostrarFormulario = mostrarFormulario;
    }
}
