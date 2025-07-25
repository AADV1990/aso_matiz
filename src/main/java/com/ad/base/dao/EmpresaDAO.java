package com.ad.base.dao;

import com.ad.Generico.GenericDAO;
import com.ad.base.modelo.Empresa;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class EmpresaDAO extends GenericDAO<Empresa> {
    public EmpresaDAO() {
        super(Empresa.class);
    }

    public List<Empresa> listarEmpresas() {
        return em.createQuery("SELECT e FROM Empresa e LEFT JOIN FETCH e.representanteLegal", Empresa.class).getResultList();

    }


}
