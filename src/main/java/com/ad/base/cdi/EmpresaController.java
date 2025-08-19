package com.ad.base.cdi;

import com.ad.Generico.AbstractControllerGenerico;
import com.ad.Generico.EnumAccionABM;
import com.ad.Generico.GenericService;
import com.ad.base.ejb.EmpresaService;
import com.ad.base.modelo.Empresa;
import com.ad.base.modelo.Persona;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class EmpresaController extends AbstractControllerGenerico<Empresa> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean filtroActivo;
    private boolean modoEdicion;
    private boolean filtroActivoSet = false; // se puede remover más adelante si ya no lo usás

    @Inject
    private EmpresaService empresaService;

    @Inject
    private PersonaController personaController;

    private Empresa empresaSeleccionadaParaEliminar;

    public boolean isFiltroActivoSet() {
        return filtroActivoSet;
    }

    @Override
    public GenericService<Empresa> getService() {
        return empresaService;
    }

    @Override
    public void reiniciarVista() {
        // Evita reiniciar en cualquier postback/AJAX
        if (FacesContext.getCurrentInstance().isPostback()) {
            return;
        }

        setEntidad(new Empresa());
        List<Empresa> lista = empresaService.listarEmpresasConRepresentante();
        if (lista == null) lista = new ArrayList<>();
        setLista(lista);

        setEnEdicion(false);
        setModoEdicion(false);
        empresaSeleccionadaParaEliminar = null;

        filtroActivo = null;  // si usás el filtro en la tabla
        if (personaController != null) personaController.reiniciarVista();
    }

    @Override
    public void seleccionar() {
        setEnEdicion(true);
    }

    @Override
    public void antesABM(EnumAccionABM accion) {
        // Validaciones previas (en caso de necesitarlas)
    }

    @Override
    public List<Empresa> getLista() {
        List<Empresa> todas = super.getLista();
        if (todas == null) {
            todas = new ArrayList<>();
            setLista(todas);
        }
        return todas;
    }

    // ====== 👇 CAMBIO IMPORTANTE: Cancelar sin reiniciarVista() 👇 ======
    public void cancelarEdicion() {
        // Salir de edición y volver a lista sin relanzar init
        setEnEdicion(false);
        setModoEdicion(false);
        setEntidad(new Empresa());                 // limpia el formulario
        empresaSeleccionadaParaEliminar = null;    // limpia selección de eliminar
        // si querés, también podés limpiar selección de persona:
        // if (personaController != null) personaController.limpiarSeleccion();
    }
    // ====== 👆 CAMBIO IMPORTANTE 👆 ======

    public void prepararEdicion(Empresa empresa) {
        setEntidad(empresa);
        setEnEdicion(true);
        setModoEdicion(true);
    }

    @Override
    public void eliminar() {
        super.eliminar();
        empresaSeleccionadaParaEliminar = null;
    }

    // Lógica para cargar persona desde el diálogo
    public void seleccionarPersona(Persona persona) {
        this.entidad.setRepresentanteLegal(persona);
    }

    public void nuevo() {
        this.entidad = new Empresa();
        this.modoEdicion = true;
        this.setEnEdicion(true);
    }

    public String getNombreRepresentante() {
        Persona representante = this.entidad != null ? this.entidad.getRepresentanteLegal() : null;
        if (representante != null) {
            String nombres = representante.getNombres() != null ? representante.getNombres() : "";
            String apellidos = representante.getApellidos() != null ? representante.getApellidos() : "";
            return nombres + " " + apellidos;
        }
        return "";
    }

    /* ======== Getters y Setters ======== */

    public Empresa getEmpresaSeleccionadaParaEliminar() {
        return empresaSeleccionadaParaEliminar;
    }

    public void setEmpresaSeleccionadaParaEliminar(Empresa empresaSeleccionadaParaEliminar) {
        this.empresaSeleccionadaParaEliminar = empresaSeleccionadaParaEliminar;
    }

    public Boolean getFiltroActivo() {
        return filtroActivo;
    }

    public void setFiltroActivo(Boolean filtroActivo) {
        this.filtroActivo = filtroActivo;
        this.filtroActivoSet = true;
    }

    public boolean isModoEdicion() {
        return modoEdicion;
    }

    public void setModoEdicion(boolean modoEdicion) {
        this.modoEdicion = modoEdicion;
    }

    public PersonaController getPersonaController() {
        return personaController;
    }

    public void setPersonaController(PersonaController personaController) {
        this.personaController = personaController;
    }
}
