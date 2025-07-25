    package com.ad.Generico;

    import jakarta.annotation.PostConstruct;

    import java.io.Serializable;
    import java.util.List;

    public abstract class AbstractControllerGenerico<T> implements Serializable {

        protected T entidad;
        protected List<T> lista;
        protected boolean enEdicion = false;

        public abstract GenericService<T> getService(); // Debe ser implementado por cada controller
        public abstract void reiniciarVista();          // Inicializa entidad y lista
        public abstract void seleccionar();             // Al hacer click en la tabla
        public abstract void antesABM(EnumAccionABM accion); // Validaciones opcionales

        @PostConstruct
        public void init() {
            reiniciarVista();
        }

        public void guardar() {
            try {
                antesABM(EnumAccionABM.GUARDAR);
                entidad = getService().guardar(entidad);
                mostrarMensaje("Registro guardado exitosamente.");
                reiniciarVista();
            } catch (Exception e) {
                mostrarMensajeError("Error al guardar: " + e.getMessage());
            }
        }

        public void eliminar() {
            try {
                antesABM(EnumAccionABM.ELIMINAR);
                getService().eliminar(entidad);
                mostrarMensaje("Registro eliminado.");
                reiniciarVista();
            } catch (Exception e) {
                mostrarMensajeError("Error al eliminar: " + e.getMessage());
            }
        }

        public void cancelar() {
            reiniciarVista();
        }

        // Métodos auxiliares para mensajes JSF
        protected void mostrarMensaje(String msg) {
            jakarta.faces.context.FacesContext.getCurrentInstance()
                    .addMessage(null, new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_INFO, msg, null));
        }

        protected void mostrarMensajeError(String msg) {
            jakarta.faces.context.FacesContext.getCurrentInstance()
                    .addMessage(null, new jakarta.faces.application.FacesMessage(jakarta.faces.application.FacesMessage.SEVERITY_ERROR, msg, null));
        }

        public boolean isMostrarFormulario() {
            return enEdicion;
        }

        public boolean isMostrarTabla() {
            return !enEdicion;
        }


        // Getters y Setters
        public T getEntidad() { return entidad; }
        public void setEntidad(T entidad) { this.entidad = entidad; }

        public List<T> getLista() { return lista; }
        public void setLista(List<T> lista) { this.lista = lista; }

        public boolean isEnEdicion() { return enEdicion; }
        public void setEnEdicion(boolean enEdicion) { this.enEdicion = enEdicion; }
    }