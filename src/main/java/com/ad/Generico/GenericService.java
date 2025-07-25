package com.ad.Generico;

import java.util.List;

public abstract class GenericService<T> {

    protected abstract GenericDAO<T> getDao();

    public T guardar(T entidad) {
        return getDao().guardar(entidad);
    }

    public void eliminar(T entidad) {
        getDao().eliminar(entidad);
    }

    public T buscarPorId(Object id) {
        return getDao().buscarPorId(id);
    }

    public List<T> obtenerTodos() {
        return getDao().obtenerTodos();
    }
}
