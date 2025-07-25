package com.ad.base.ejb;


import com.ad.base.dao.PersonaDAO;
import com.ad.base.modelo.Persona;
import com.ad.Generico.GenericDAO;
import com.ad.Generico.GenericService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.List;

@Stateless
public class PersonaService extends GenericService<Persona>  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Inject
    private PersonaDAO personaDAO;
    @Override
    protected GenericDAO<Persona> getDao() {
        return personaDAO;
    }

    public List<Persona> listarPersonas() {
        return personaDAO.listarTodas(); // Asegurate de que exista ese método en PersonaDAO
    }

}
