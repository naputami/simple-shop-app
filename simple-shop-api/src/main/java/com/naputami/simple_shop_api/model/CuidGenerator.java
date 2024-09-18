package com.naputami.simple_shop_api.model;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import cool.graph.cuid.Cuid;

import java.io.Serializable;

public class CuidGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return Cuid.createCuid();
    }
}
