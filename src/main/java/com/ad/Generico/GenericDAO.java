package com.ad.Generico;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

public abstract class GenericDAO<T> {

    @PersistenceContext(unitName = "asoPU")
    protected EntityManager em;

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T guardar(T entidad) {
        return em.merge(entidad);
    }

    public void eliminar(T entidad) {
        if (!em.contains(entidad)) {
            entidad = em.merge(entidad);
        }
        em.remove(entidad);
    }

    public T buscarPorId(Object id) {
        return em.find(entityClass, id);
    }

    public List<T> obtenerTodos() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, entityClass).getResultList();
    }
}