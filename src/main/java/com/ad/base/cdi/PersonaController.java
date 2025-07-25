package com.ad.base.cdi;

import com.ad.Generico.AbstractControllerGenerico;
import com.ad.Generico.EnumAccionABM;
import com.ad.base.ejb.PersonaService;
import com.ad.base.modelo.Persona;
import com.ad.base.cdi.UsuarioController;
import com.ad.base.cdi.EmpresaController;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@ViewScoped
public class PersonaController extends AbstractControllerGenerico<Persona> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PersonaService personaService;

    private Persona personaSeleccionadaParaEliminar;

    // ✅ NUEVO: Atributos para selección desde el diálogo
    private Persona seleccionado;  // Persona seleccionada desde la tabla del diálogo
    private Object origen;         // Componente que llamó al diálogo (usuarioController o empresaController)

    @Override
    public void reiniciarVista() {
        setEntidad(new Persona());
        setLista(personaService.obtenerTodos());
        setEnEdicion(false);
        personaSeleccionadaParaEliminar = null;
    }

    @Override
    public void seleccionar() {
        setEnEdicion(true);
    }

    @Override
    public void antesABM(EnumAccionABM accion) {
        // Validaciones previas si se desea
    }

    @Override
    public PersonaService getService() {
        return personaService;
    }

    public boolean isMostrarFormulario() {
        return enEdicion;
    }

    public void nuevo() {
        setEntidad(new Persona());
        setEnEdicion(true);
    }

    public void cancelarEdicion() {
        reiniciarVista();
    }

    public void prepararEdicion(Persona persona) {
        setEntidad(persona);
        setEnEdicion(true);
    }

    public void prepararEliminacion(Persona persona) {
        this.personaSeleccionadaParaEliminar = persona;
        setEntidad(persona);
    }

    @Override
    public void eliminar() {
        super.eliminar();
        personaSeleccionadaParaEliminar = null;
    }

    public Persona getPersonaSeleccionadaParaEliminar() {
        return personaSeleccionadaParaEliminar;
    }

    // 🔽🔽🔽 MÉTODOS NUEVOS PARA EL DIÁLOGO DE SELECCIÓN 🔽🔽🔽

    public Persona getSeleccionado() {
        return seleccionado;
    }

    public void setSeleccionado(Persona seleccionado) {
        this.seleccionado = seleccionado;
    }

    public void setOrigen(Object origen) {
        this.origen = origen;
    }

    public void seleccionarPersonaDesdeDialogo() {
        if (seleccionado == null) {
            return;
        }

        if (origen instanceof UsuarioController usuarioController) {
            usuarioController.getUsuario().setPersona(seleccionado);
        } else if (origen instanceof EmpresaController empresaController) {
            empresaController.getEntidad().setRepresentanteLegal(seleccionado);
        }

        // Limpiar después de seleccionar
        seleccionado = null;
        origen = null;
    }

}
