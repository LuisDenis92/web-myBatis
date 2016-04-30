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

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Proveedor;

@ApplicationScoped
public class ProveedorRepository {

	@PersistenceContext(unitName="PersistenceApp")
    private EntityManager em;

    public Proveedor findById(Long id) {
        return em.find(Proveedor.class, id);
    }

    public Proveedor findByDescripcion(String descripcion) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Proveedor> criteria = cb.createQuery(Proveedor.class);
        Root<Proveedor> proveedor = criteria.from(Proveedor.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).where(cb.equal(member.get(Member_.email), email));
        criteria.select(proveedor).where(cb.equal(proveedor.get("descripcion"), descripcion));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Proveedor> findAllOrderedByDescripcion() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Proveedor> criteria = cb.createQuery(Proveedor.class);
        Root<Proveedor> descripcion = criteria.from(Proveedor.class);
        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
        // feature in JPA 2.0
        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
        //debe ser         criteria.select(detalle).orderBy(cb.asc(detalle.get("producto")));

        criteria.select(descripcion).orderBy(cb.asc(descripcion.get("descripcion")));
        return em.createQuery(criteria).getResultList();
    }
    public List<Proveedor> findAllOrderedDescByDescripcion() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Proveedor> criteria = cb.createQuery(Proveedor.class);
        Root<Proveedor> descripcion = criteria.from(Proveedor.class);
        criteria.select(descripcion).orderBy(cb.desc(descripcion.get("descripcion")));
        return em.createQuery(criteria).getResultList();
    }
  //------------------------MYBATIS----------------------------------
   //--------------------------------------------------------------- 
    public List<Proveedor> getAllProveedores() throws IOException {
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		List<Proveedor> listProveedores = 
    				session.selectList("mybatis.ProveedorMapper.getAllProveedores");
    		return listProveedores;
    	}finally{
    		session.close();
    	}
    }
    
    public Proveedor getProveedorById (long id){
    	
    	Proveedor proveedor = new Proveedor();
    	try{
    		String resource = "mybatis/myBatisConfig.xml";
        	InputStream inputStream;
    	    inputStream = Resources.getResourceAsStream(resource);
        	SqlSessionFactory sqlSessionFactory = 
        			new SqlSessionFactoryBuilder().build(inputStream);
        	SqlSession session = sqlSessionFactory.openSession();
    		proveedor = 
    				session.selectOne("mybatis.ProveedorMapper.getProveedorById",id);
    		session.close();
    		
    	}catch(IOException ioe){
    		
    	}
    	return proveedor;
    }
    
    public void remove (long id) throws IOException{
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		
    				session.selectOne("mybatis.ProveedorMapper.removeProveedor",id);
    		
    	}finally{
    		session.close();
    	}
    	
    }
    
    public void createProveedor (Proveedor proveedor) throws IOException{
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		
    				session.selectOne("mybatis.ProveedorMapper.createProveedor",proveedor);
    		
    	}finally{
    		session.close();
    	}
    	
    }
    
    public void updateProveedor (Proveedor proveedor) throws IOException{
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		
    				session.selectOne("mybatis.ProveedorMapper.updateProveedor",proveedor);
    		
    	}finally{
    		session.close();
    	}
    	
    }
}
