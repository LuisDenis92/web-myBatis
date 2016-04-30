/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package py.pol.una.ii.pw.data;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import py.pol.una.ii.pw.model.Compra_Det;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Producto_Duplicado;
import py.pol.una.ii.pw.model.Proveedor;

@ApplicationScoped
public class Producto_DuplicadoRepository {

	@PersistenceContext(unitName="PersistenceApp")
    private EntityManager em;

    public Producto_DuplicadoRepository findById(Long id) {
        return em.find(Producto_DuplicadoRepository.class, id);
    }
    public List<Producto_Duplicado> findAllDuplicado() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Producto_Duplicado> criteria = cb.createQuery(Producto_Duplicado.class);
        Root<Producto_Duplicado> producto_duplicado = criteria.from(Producto_Duplicado.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        criteria.select(producto_duplicado).orderBy(cb.asc(producto_duplicado.get("id")));
        return em.createQuery(criteria).getResultList();
    }
    
}
