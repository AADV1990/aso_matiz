package com.ad.base.cdi;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Named
@RequestScoped
public class DashboardController implements Serializable {

    private String fechaActualFormateada;

    public void init() {
        SimpleDateFormat formato = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
        fechaActualFormateada = formato.format(new Date());
    }

    public String getFechaActualFormateada() {
        return fechaActualFormateada;
    }
}
