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
package py.pol.una.ii.pw.service;


import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Producto_Duplicado;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class Producto_DuplicadoRegistration {

    @Inject
    private Logger log;
    
    
    @PersistenceContext(unitName="PersistenceApp")
    private EntityManager em;

    @Inject
    private Event<Producto_Duplicado> productoEventSrc;
   

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void register(Producto_Duplicado producto){
    	
            em.persist(producto);
            productoEventSrc.fire(producto);
        
    }
   
    public void modificar(Producto_Duplicado producto) {
        em.merge(producto);
        productoEventSrc.fire(producto);
    }
    
    
  //------------------------------MYBATIS-----------------------------
    //------------------------------------------------------------------
    
    public List<Producto_Duplicado> getAllProductosDuplicados() {
    	
    	List<Producto_Duplicado> listProductosDuplicados = new ArrayList<Producto_Duplicado>();
    	try{
    		String resource = "mybatis/myBatisConfig.xml";
        	InputStream inputStream;
    	    inputStream = Resources.getResourceAsStream(resource);
        	SqlSessionFactory sqlSessionFactory = 
        			new SqlSessionFactoryBuilder().build(inputStream);
        	SqlSession session = sqlSessionFactory.openSession();
    		listProductosDuplicados = 
    				session.selectList("mybatis.duplicadoMapper.getAllDuplicados");
    		session.close();
    		
    	}catch(IOException ioe){
    		
    	}
    	return listProductosDuplicados;
    }
    
    public Producto_Duplicado getDuplicadoById (long id){
    	
    	Producto_Duplicado duplicado = new Producto_Duplicado();
    	try{
    		String resource = "mybatis/myBatisConfig.xml";
        	InputStream inputStream;
    	    inputStream = Resources.getResourceAsStream(resource);
        	SqlSessionFactory sqlSessionFactory = 
        			new SqlSessionFactoryBuilder().build(inputStream);
        	SqlSession session = sqlSessionFactory.openSession();
    		 duplicado = 
    				session.selectOne("mybatis.duplicadoMapper.getDuplicadoById",id);
    		session.close();
    		
    	}catch(IOException ioe){
    		
    	}
    	return duplicado;
    }
    
    public void remove (long id) throws IOException{
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		
    				session.selectOne("mybatis.duplicadoMapper.removeDuplicado",id);
    		
    	}finally{
    		session.close();
    	}
    	
    }
    
    public void createProducto (Producto_Duplicado duplicado){
    	
    	
    	try{
    		String resource = "mybatis/myBatisConfig.xml";
        	InputStream inputStream;
    	    inputStream = Resources.getResourceAsStream(resource);
        	SqlSessionFactory sqlSessionFactory = 
        			new SqlSessionFactoryBuilder().build(inputStream);
        	SqlSession session = sqlSessionFactory.openSession();
    				session.selectOne("mybatis.duplicadoMapper.createDuplicado",duplicado);
    				session.close();
    	}catch(IOException ioe){
    		
    	}
    	
    }
    
    public void updateDuplicado (Producto_Duplicado duplicado){
    	
    	
    	try{
    		String resource = "mybatis/myBatisConfig.xml";
        	InputStream inputStream;
    	    inputStream = Resources.getResourceAsStream(resource);
        	SqlSessionFactory sqlSessionFactory = 
        			new SqlSessionFactoryBuilder().build(inputStream);
        	SqlSession session = sqlSessionFactory.openSession();
    				session.selectOne("mybatis.duplicadoMapper.updateDuplicado",duplicado);
    				session.close();
    		
    	}catch(IOException ioe){
    		
    	}
    	
    }
  
  
}
