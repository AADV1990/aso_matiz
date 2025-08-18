package com.ad.base.cdi;

import com.ad.Generico.AbstractControllerGenerico;
import com.ad.Generico.EnumAccionABM;
import com.ad.base.ejb.PersonaService;
import com.ad.base.modelo.Persona;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

@Named
@ViewScoped
public class PersonaController extends AbstractControllerGenerico<Persona> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private PersonaService personaService;

    // (Pantalla principal)
    private Persona personaSeleccionadaParaEliminar;

    // Diálogo de selección
    private Persona personaSeleccionada;    // fila elegida en el diálogo
    private Object origen;                  // UsuarioController o EmpresaController
    private String formularioOrigen;        // opcional si lo usas visualmente
    private String inputFieldId;           // opcional, si quieres usar un input específico en el diálogo


    // Filtros del diálogo
    private String filtroNombre;
    private String filtroDocumento;

    // DataModel lazy para el diálogo
    private LazyDataModel<Persona> lazyListDetallePersonas;

    /* =================== Ciclo de vida base =================== */

    @Override
    public void reiniciarVista() {
        setEntidad(new Persona());
        setLista(personaService.obtenerTodos());   // listado pantalla principal
        setEnEdicion(false);
        personaSeleccionadaParaEliminar = null;

        if (lazyListDetallePersonas == null) {
            inicializarLazy();
        }
    }

    @Override
    public PersonaService getService() {
        return personaService;
    }

    @Override
    public void antesABM(EnumAccionABM accion) {
        // Validaciones previas si hace falta
    }

    @Override
    public void seleccionar() {
        setEnEdicion(true);
    }

    public boolean isMostrarFormulario() { // compat
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

    /* =================== LazyDataModel (PF 12/13+) =================== */

    private void inicializarLazy() {
        lazyListDetallePersonas = new LazyDataModel<Persona>() {

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                // Si luego tienes count paginado en el service, úsalo aquí.
                return aplicarFiltros(personaService.obtenerTodos(), filtroNombre, filtroDocumento).size();
            }

            @Override
            public List<Persona> load(int first, int pageSize,
                                      Map<String, SortMeta> sortBy,
                                      Map<String, FilterMeta> filterBy) {
                // 1) Base (puedes reemplazar por búsqueda paginada real en tu service)
                List<Persona> base = personaService.obtenerTodos();

                // 2) Filtros
                List<Persona> filtrado = aplicarFiltros(base, filtroNombre, filtroDocumento);

                // 3) Orden
                aplicarOrden(filtrado, sortBy);

                // 4) Paginación en memoria
                int total = filtrado.size();
                setRowCount(total);
                int end = Math.min(first + pageSize, total);
                if (first >= end) return new ArrayList<>();
                return filtrado.subList(first, end);
            }

            @Override
            public String getRowKey(Persona p) {
                return String.valueOf(p.getIdPersona()); // tu id es int
            }

            @Override
            public Persona getRowData(String rowKey) {
                try {
                    int id = Integer.parseInt(rowKey);
                    for (Persona p : personaService.obtenerTodos()) {
                        if (p.getIdPersona() == id) return p;
                    }
                } catch (Exception ignored) {}
                return null;
            }
        };
    }

    private List<Persona> aplicarFiltros(List<Persona> base, String byNombre, String byDoc) {
        List<Persona> out = new ArrayList<>();
        if (base == null) return out;
        String fn = byNombre != null ? byNombre.trim().toLowerCase() : "";
        String fd = byDoc != null ? byDoc.trim().toLowerCase() : "";

        for (Persona p : base) {
            boolean ok = true;

            if (!fn.isEmpty()) {
                String nombre = displayName(p).toLowerCase(); // usa nombres + apellidos
                ok &= nombre.contains(fn);
            }
            if (!fd.isEmpty()) {
                String doc = p.getNroDocumento() != null ? p.getNroDocumento().toLowerCase() : "";
                ok &= doc.contains(fd);
            }

            if (ok) out.add(p);
        }
        return out;
    }

    private void aplicarOrden(List<Persona> list, Map<String, SortMeta> sortBy) {
        if (list == null || sortBy == null || sortBy.isEmpty()) return;

        // Tomamos el primer criterio
        SortMeta sm = sortBy.values().iterator().next();
        String field = sm.getField();
        SortOrder order = sm.getOrder();

        Comparator<Persona> comp;
        if ("nroDocumento".equals(field) || "documento".equalsIgnoreCase(field)) {
            comp = Comparator.comparing(p -> nullSafe(p.getNroDocumento()));
        } else { // nombre por defecto
            comp = Comparator.comparing(this::displayName, String::compareToIgnoreCase);
        }

        if (order == SortOrder.DESCENDING) {
            comp = comp.reversed();
        }
        list.sort(comp);
    }

    /** "Nombres Apellidos" sin nulls ni dobles espacios. */
    private String displayName(Persona p) {
        String nom = nullSafe(p.getNombres());
        String ape = nullSafe(p.getApellidos());
        String both = (nom + " " + ape).trim();
        return both.isEmpty() ? String.valueOf(p.getIdPersona()) : both;
    }

    private String nullSafe(String s) { return s == null ? "" : s; }

    /* =================== Selección desde diálogo =================== */

    /** Si usas botón "Elegir" */
    public void confirmarSeleccion() {
        aplicarSeleccionEnOrigen();
    }

    /** Si usas rowSelect / rowDblselect */
    public void onSelect(SelectEvent<Persona> event) {
        if (event != null) {
            this.personaSeleccionada = event.getObject();
        }
        aplicarSeleccionEnOrigen();
    }

    private void aplicarSeleccionEnOrigen() {
        if (personaSeleccionada == null) {
            System.out.println("Error: personaSeleccionada es null");
            return;
        }

        if (origen instanceof UsuarioController usuarioController) {
            try {
                usuarioController.getUsuario().setPersona(personaSeleccionada);
                System.out.println("Persona asignada a UsuarioController: " + personaSeleccionada.getNombres());
            } catch (Exception e) {
                System.out.println("Error al asignar persona a UsuarioController: " + e.getMessage());
            }
        } else if (origen instanceof EmpresaController empresaController) {
            try {
                empresaController.getEntidad().setRepresentanteLegal(personaSeleccionada);
                System.out.println("Persona asignada a EmpresaController: " + personaSeleccionada.getNombres() + " " + personaSeleccionada.getApellidos());
            } catch (Exception e) {
                System.out.println("Error al asignar persona a EmpresaController: " + e.getMessage());
            }
        } else {
            System.out.println("Error: origen no es ni UsuarioController ni EmpresaController");
        }

        // Limpiar referencias del diálogo
        personaSeleccionada = null;
        origen = null;
    }

    public void setFormularioOrigen(String formularioOrigen, String inputFieldId) {
        this.inputFieldId = inputFieldId;
        System.out.println("Formulario origen: " + formularioOrigen + ", Campo a actualizar: " + inputFieldId);
    }

    // import org.primefaces.event.SelectEvent;


    /* =================== Getters / Setters =================== */

    public Persona getPersonaSeleccionadaParaEliminar() { return personaSeleccionadaParaEliminar; }

    public Persona getPersonaSeleccionada() { return personaSeleccionada; }
    public void setPersonaSeleccionada(Persona personaSeleccionada) { this.personaSeleccionada = personaSeleccionada; }

    public void setOrigen(Object origen) { this.origen = origen; }

    public String getFormularioOrigen() { return formularioOrigen; }
    public void setFormularioOrigen(String formularioOrigen) { this.formularioOrigen = formularioOrigen; }

    public String getFiltroNombre() { return filtroNombre; }
    public void setFiltroNombre(String filtroNombre) { this.filtroNombre = filtroNombre; }

    public String getFiltroDocumento() { return filtroDocumento; }
    public void setFiltroDocumento(String filtroDocumento) { this.filtroDocumento = filtroDocumento; }

    public String getInputFieldId() {
        return inputFieldId;
    }

    public void setInputFieldId(String inputFieldId) {
        this.inputFieldId = inputFieldId;
    }

    public LazyDataModel<Persona> getLazyListDetallePersonas() {
        if (lazyListDetallePersonas == null) inicializarLazy();
        return lazyListDetallePersonas;
    }



}
