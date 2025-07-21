package com.ad.util;

import org.hibernate.Hibernate;

public class HibernateUtil {

    public static <T> T initializeLazy(T proxy) {
        if (proxy != null) {
            Hibernate.initialize(proxy); // solo si está en el contexto
        }
        return proxy;
    }
}
