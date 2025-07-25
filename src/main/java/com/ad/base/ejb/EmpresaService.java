package com.ad.base.ejb;

import com.ad.Generico.GenericDAO;
import com.ad.Generico.GenericService;
import com.ad.base.dao.EmpresaDAO;
import com.ad.base.modelo.Empresa;
import com.ad.base.modelo.Persona;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.List;

@Stateless
public class EmpresaService extends GenericService implements Serializable {
    private static final long serialVersionUID = 1L;

    @Inject
    EmpresaDAO empresaDAO;


    @Override
    protected GenericDAO<Empresa> getDao() {
        return empresaDAO;
    }

    public List<Empresa> listarEmpresasConRepresentante() {
        return empresaDAO.listarEmpresas(); // ya tiene el LEFT JOIN FETCH
    }

}
