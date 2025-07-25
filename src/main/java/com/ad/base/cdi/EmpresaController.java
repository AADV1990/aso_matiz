package com.ad.base.cdi;

import com.ad.Generico.AbstractControllerGenerico;
import com.ad.Generico.EnumAccionABM;
import com.ad.Generico.GenericService;
import com.ad.base.ejb.EmpresaService;
import com.ad.base.modelo.Empresa;
import com.ad.base.modelo.Persona;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class EmpresaController extends AbstractControllerGenerico<Empresa> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean filtroActivo;
    private boolean modoEdicion;

    @Inject
    private EmpresaService empresaService;

    @Inject
    private PersonaController personaController;

    private Empresa empresaSeleccionadaParaEliminar;

    @Override
    public GenericService<Empresa> getService() {
        return empresaService;
    }

    @Override
    public void reiniciarVista() {
        setEntidad(new Empresa());
        setLista(empresaService.listarEmpresasConRepresentante());
        setEnEdicion(false);
        setModoEdicion(false);
        empresaSeleccionadaParaEliminar = null;

        // Cargar la lista de personas para el diálogo
        personaController.setLista(null);
//        personaController.setLista(personaController.getPersonaService().getActivos());
    }

    @Override
    public void seleccionar() {
        setEnEdicion(true);
    }

    @Override
    public void antesABM(EnumAccionABM accion) {
        // Por ahora vacío
    }

    @Override
    public List<Empresa> getLista() {
        List<Empresa> todas = super.getLista();
        if (filtroActivo == null) {
            return todas;
        }
        return todas.stream()
                .filter(emp -> filtroActivo.equals(emp.getActivo()))
                .toList();
    }

    public void cancelarEdicion() {
        reiniciarVista();
    }

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

    public void seleccionarPersonaDesdeDialogo() {
        this.entidad.setRepresentanteLegal(personaController.getSeleccionado());
    }

    public void nuevo() {
        this.entidad = new Empresa();
        this.modoEdicion = true;
        this.setEnEdicion(true);
    }

    public String getNombreRepresentante() {
        Persona representante = this.entidad != null ? this.entidad.getRepresentanteLegal() : null;
        return representante != null ? representante.getNombres() : "";
    }


    // Getters y Setters

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
