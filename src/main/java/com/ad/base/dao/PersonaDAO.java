package com.ad.base.dao;

import com.ad.base.modelo.Persona;
import com.ad.Generico.GenericDAO;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class PersonaDAO extends GenericDAO<Persona> {
    public PersonaDAO() {
        super(Persona.class);
    }

    public List<Persona> listarTodas() {
        return em.createQuery("SELECT p FROM Persona p", Persona.class).getResultList();
    }


}
