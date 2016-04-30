package py.pol.una.ii.pw.service;

import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagementType;
import javax.ejb.TransactionManagement;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import com.google.gson.Gson;

import py.pol.una.ii.pw.data.Compra_DetRepository;
import py.pol.una.ii.pw.data.Venta_DetRepository;
import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Compra_Cab;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Venta_Cab;
import py.pol.una.ii.pw.model.Compra_Det;
import py.pol.una.ii.pw.model.Venta_Det;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.ejb.EJBTransactionRolledbackException;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.postgresql.util.PSQLException;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class Venta_CabRegistration {
	@Inject
    private Logger log;

	@Resource
	private SessionContext context;
	
	@Inject
    private EntityManager em;
	
	@Inject
    private ProductoRegistration productoRegistration;
	
	@Inject
    private Venta_DetRegistration detalleRegistration;
	
	@EJB
    private Venta_CabRegistration selfRegistration;
    
    @Inject
    private Event<Venta_Cab> venta_CabEventSrc;

    public void register(Venta_Cab venta_Cab) throws Exception {
        log.info("Registering " + venta_Cab.getId());
        em.persist(venta_Cab);
        venta_CabEventSrc.fire(venta_Cab);
    }

    public void modificar(Venta_Cab cabecera) throws Exception {
        log.info("Registering " + cabecera.getId());
        em.merge(cabecera);
        venta_CabEventSrc.fire(cabecera);
    }
    
    public void remover(Venta_Cab cabecera) throws Exception {
        em.remove(em.contains(cabecera) ? cabecera : em.merge(cabecera));
        
    }
    /**********************Registrar una venta
     * @throws Exception **********************************************/
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)  
    public void registrarVenta(List<Venta_Det> detalle){
    	
    	
    	Venta_Cab cabecera= new Venta_Cab();
    	Date fecha = new Date();
    	cabecera.setFecha(fecha);
    	selfRegistration.createVentaCab(cabecera);
    	
        for(Venta_Det aux : detalle)
        {	
        	try {
        		
        			
        		Venta_Det cdetalle= new Venta_Det();
        	   	cdetalle.setCabecera(cabecera);
        	   	cdetalle.setCantidad(aux.getCantidad());
        	   	cdetalle.setProducto(aux.getProducto());
        	 
        	   	/***Actualiza el stock***/
        			Long pid= cdetalle.getProducto().getId();
        			Producto p= productoRegistration.getProductoById(pid);
        			p.setStock(p.getStock()-aux.getCantidad());
        			
        			productoRegistration.updateProducto(p);
       
        			detalleRegistration.crearDetalle(cdetalle);
        		
        	       
        		
			} catch (Exception e){
				System.out.println("ROOOOOOOOOOOOOOOOOOOOOOOOOLLLLLLLLLLLLLLLLLLBBBBBBBBBBBBAAAAAAAAAa");
				context.setRollbackOnly();
			}
        }
        
    }
 
   
 //------------------------------MYBATIS-----------------------------
   //------------------------------------------------------------------
   
   public List<Venta_Cab> getAllVenta_Cab() throws IOException {
   	String resource = "mybatis/myBatisConfig.xml";
   	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
   	SqlSessionFactory sqlSessionFactory = 
   			new SqlSessionFactoryBuilder().build(inputStream);
   	SqlSession session = sqlSessionFactory.openSession();
   	System.out.println("QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
   	
   	try{
   		List<Venta_Cab> listcab = 
   				session.selectList("mybatis.VentaCabMapper.getAllVentaCab");
   		return listcab;
   	}finally{
   		session.close();
   	}
   }
   
   public Venta_Cab getVentaCabById (long id) throws IOException{
   	String resource = "mybatis/myBatisConfig.xml";
   	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
   	SqlSessionFactory sqlSessionFactory = 
   			new SqlSessionFactoryBuilder().build(inputStream);
   	SqlSession session = sqlSessionFactory.openSession();
   	
   	try{
   		Venta_Cab cab = 
   				session.selectOne("mybatis.VentaCabMapper.getVentaCabById",id);
   		return cab;
   	}finally{
   		session.close();
   	}
   	
   }
   
   
   @TransactionAttribute(TransactionAttributeType.REQUIRED) 
   public void createVentaCab (Venta_Cab cab){
   	String resource = "mybatis/myBatisConfig.xml";
   	InputStream inputStream;
	    try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = 
	    			new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession session = sqlSessionFactory.openSession();
			session.insert("mybatis.VentaCabMapper.createVentaCab",cab);
			session.close();
		} catch (IOException e) {
			
			e.printStackTrace();
   				    		
		}
   	
   }
    
    
    
}
