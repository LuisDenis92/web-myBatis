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

import py.pol.una.ii.pw.data.ProductoRepository;
import py.pol.una.ii.pw.data.Venta_CabRepository;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Producto_Duplicado;
import py.pol.una.ii.pw.model.Venta_Cab;

import javax.annotation.Resource;
import javax.ejb.EJB;
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

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ProductoRegistration {

    @Inject
    private Logger log;
    
    @EJB
    private ProductoRegistration myRegistration;
	
    @Inject
    Producto_DuplicadoRegistration duplicadoRegistration;
    
    @Inject
    private ProductoRepository repository;
    

    @PersistenceContext(unitName="PersistenceApp")
    private EntityManager em;

    @Inject
    private Event<Producto> productoEventSrc;
   

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void register(Producto producto) {
    	
    		log.info("Registering " + producto.getDetalle());
			em.persist(producto);
			
           
        
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void cargaMasiva(List<Producto> productos) {
    	for(Producto produ : productos){
    		try {
    			myRegistration.createProducto(produ);
    		} catch (Exception e) {
    			myRegistration.cargarDuplicado(new Producto_Duplicado(), produ.getDetalle());

    		}
    	}
    }
    
    public void modificar(Producto producto) throws Exception {
        log.info("Registering " + producto.getDetalle());
        em.merge(producto);
        productoEventSrc.fire(producto);
    }
    ///////////////////////////eliminar
    public void remover(Producto producto) throws Exception {
        em.remove(em.contains(producto) ? producto : em.merge(producto));
 
    }
    
 
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void cargarDuplicado(Producto_Duplicado pd, String detalle){
    	boolean coincidencia=false;
    	List<Producto> productosList = new ArrayList<Producto>();
    	System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
    	productosList = myRegistration.getAllProductos();
    	Producto aux = new Producto();

    	System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
    	for(Producto pro : productosList){
    		if(detalle.equals(pro.getDetalle())){
    			aux=pro;
    			break;
    		}
    	}
    	List<Producto_Duplicado> duplicadoList = new ArrayList<Producto_Duplicado>();  
    	duplicadoList	=	duplicadoRegistration.getAllProductosDuplicados(); 
    	for(Producto_Duplicado produ:duplicadoList){
    		if(produ.getProducto().getId().equals(aux.getId())){
    			coincidencia=true;
    			pd = duplicadoRegistration.getDuplicadoById(produ.getId());
    			pd.setCantidad(pd.getCantidad()+1);
    			//pd.setProducto(aux);
    			duplicadoRegistration.updateDuplicado(pd);
    			
    			break;
    		}
    	}
    	if(coincidencia==false){
    		pd.setProducto(aux);
    		pd.setCantidad(1);
    		duplicadoRegistration.createProducto(pd);
    	}
    }
    
  //------------------------------MYBATIS-----------------------------
    //------------------------------------------------------------------
  
    public List<Producto> getAllProductos() {
    
    	List<Producto> listProductos = new ArrayList<Producto>();
    	try{
    		
    		String resource = "mybatis/myBatisConfig.xml";
        	InputStream inputStream ;
    	    inputStream = Resources.getResourceAsStream(resource);
        	SqlSessionFactory sqlSessionFactory = 
        			new SqlSessionFactoryBuilder().build(inputStream);
        	SqlSession session = sqlSessionFactory.openSession();
        	System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
    		 listProductos = 
    				session.selectList("mybatis.ProductoMapper.getAllProductos");
    		session.close();
    		System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWw");
    	}catch(IOException ioe){
    			
    	}
    	return listProductos;
    }
    
    public Producto getProductoById (long id){
    	Producto producto = new Producto();
    	
    	try{
    		String resource = "mybatis/myBatisConfig.xml";
        	InputStream inputStream;
    	    inputStream = Resources.getResourceAsStream(resource);
        	SqlSessionFactory sqlSessionFactory = 
        			new SqlSessionFactoryBuilder().build(inputStream);
        	SqlSession session = sqlSessionFactory.openSession();
    		 producto = 
    				session.selectOne("mybatis.ProductoMapper.getProductoById",id);
    		
    	}catch(IOException ioe){
    		
    	}
    	return producto;
    }
    
    public void remove (long id) throws IOException{
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		
    				session.selectOne("mybatis.ProductoMapper.removeProducto",id);
    		
    	}finally{
    		session.close();
    	}
    	
    }
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void createProducto (Producto producto){
    	
    	
    	try{
    		String resource = "mybatis/myBatisConfig.xml";
        	InputStream inputStream;
    	    inputStream = Resources.getResourceAsStream(resource);
        	SqlSessionFactory sqlSessionFactory = 
        			new SqlSessionFactoryBuilder().build(inputStream);
        	SqlSession session = sqlSessionFactory.openSession();
    		
    				session.selectOne("mybatis.ProductoMapper.createProducto",producto);
    				session.close();
    	}catch(IOException ioe){
    		
    	}
    	
    }
    
    public void updateProducto (Producto producto) throws IOException{
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		
    				session.selectOne("mybatis.ProductoMapper.updateProducto",producto);
    		
    	}finally{
    		session.close();
    	}
    	
    }
  
}
