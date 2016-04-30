package py.pol.una.ii.pw.service;

import py.pol.una.ii.pw.data.Compra_CabRepository;
import py.pol.una.ii.pw.data.Compra_DetRepository;
import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Compra_Cab;
import py.pol.una.ii.pw.model.Compra_Det;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Proveedor;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.ejb.SessionContext;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

@Stateless
public class Compra_CabRegistration {

    @Inject
    private Logger log;
    

	

    @PersistenceContext(unitName="PersistenceApp")
    private EntityManager em;
    
    @Resource
    private  Compra_CabRegistration cabRegister;

    @Inject
    private Event<Compra_Cab> cabeceraEventSrc;
    
    @Inject
    private ProductoRegistration productoRegistration;
    
    @Inject
    private Compra_DetRegistration detalleRegistration;
    
    @EJB
    private Compra_CabRegistration cabRegistration;
    
    
 
    
    public void registrarCabecera(Compra_Cab cabecera) throws Exception {
        log.info("Registering " + cabecera.getFecha());
        em.persist(cabecera);
        cabeceraEventSrc.fire(cabecera);
    }
    
    
    public void modificar(Compra_Cab cabecera) throws Exception {
        log.info("Registering " + cabecera.getFecha());
        em.merge(cabecera);
        cabeceraEventSrc.fire(cabecera);
    }
    public void remover(Compra_Cab cabecera) throws Exception {
        em.remove(em.contains(cabecera) ? cabecera : em.merge(cabecera));
        
    }
    /**********************Registrar una compra
     * @throws IOException **********************************************/
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public String registrarCompra(List<Compra_Det> detalleList) throws IOException  {
     	Compra_Cab cabecera= new Compra_Cab();
     	long id=80;
    	Date fecha = new Date();
    	cabecera.setFecha(fecha);
    	createCompraCab(cabecera);
        for (Compra_Det aux : detalleList)
        {	
        	try{
        	
        		Compra_Det cdetalle= new Compra_Det();
        		cdetalle.setCabecera(cabecera);
        		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXxx");
        		cdetalle.setCantidad(aux.getCantidad());
        		cdetalle.setProducto(aux.getProducto());
        		/***Actualiza el stock***/
        		Long pid= cdetalle.getProducto().getId();
        		Producto p= productoRegistration.getProductoById(pid);
        		p.setStock(p.getStock()+aux.getCantidad());
        		p.setProveedor(aux.getProducto().getProveedor());
        		productoRegistration.updateProducto(p);
        		System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP");
        		cabRegistration.crearDetalle(cdetalle);
        		System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        	
        	}
        	catch(Exception e){
        		
        	}
        }
    	
        return "exito";
        
    }
    

  //------------------------------MYBATIS-----------------------------
    //------------------------------------------------------------------
    
    public void crearDetalle (Compra_Det det){
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = 
	    			new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession session = sqlSessionFactory.openSession();
			session.insert("mybatis.detalleCompraMapper.createCompraDet",det);
			
		} catch (IOException e) {
			
			e.printStackTrace();
    				    		
		}
	    
    	
    }
    
    public List<Compra_Cab> getAllCompra_Cab() throws IOException {
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		List<Compra_Cab> listcab = 
    				session.selectList("mybatis.CompraCabMapper.getAllCompraCab");
    		return listcab;
    	}finally{
    		session.close();
    	}
    }
    
    public Compra_Cab getCompraCabById (long id) throws IOException{
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    inputStream = Resources.getResourceAsStream(resource);
    	SqlSessionFactory sqlSessionFactory = 
    			new SqlSessionFactoryBuilder().build(inputStream);
    	SqlSession session = sqlSessionFactory.openSession();
    	
    	try{
    		Compra_Cab cab = 
    				session.selectOne("mybatis.CompraCabMapper.getCompraCabById",id);
    		return cab;
    	}finally{
    		session.close();
    	}
    	
    }
    
    
    public void createCompraCab (Compra_Cab cab){
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = 
	    			new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession session = sqlSessionFactory.openSession();
			session.insert("mybatis.CompraCabMapper.createCompraCab",cab);
			session.close();
		} catch (IOException e) {
			
			e.printStackTrace();
    				    		
		}
    	
    }
    
    
    
}
