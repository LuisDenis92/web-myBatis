package py.pol.una.ii.pw.service;

import py.pol.una.ii.pw.model.Compra_Det;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.model.Venta_Det;

import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless

public class Venta_DetRegistration {

	@Inject
    private Logger log;

	@PersistenceContext(unitName="PersistenceApp")
    private EntityManager em;

    @Inject
    private Event<Venta_Det> venta_DetEventSrc;
///////////////CREAR UN CLIENTE//////////////////////////////
    public void register(Venta_Det venta_Det) throws Exception {
        log.info("Registering " + venta_Det.getId());
        em.persist(venta_Det);
        venta_DetEventSrc.fire(venta_Det);
    }
    public void modificar(Venta_Det detalle) throws Exception {
        log.info("Registering " + detalle.getId());
        em.merge(detalle);
        venta_DetEventSrc.fire(detalle);
    }
    
    public void remover(Venta_Det detalle) throws Exception {
        em.remove(em.contains(detalle) ? detalle : em.merge(detalle));
        
    }
//-------------------------MYBATIS---------------------------
    
    
    public void crearDetalle (Venta_Det det){
    	String resource = "mybatis/myBatisConfig.xml";
    	InputStream inputStream;
	    try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactory sqlSessionFactory = 
	    			new SqlSessionFactoryBuilder().build(inputStream);
			SqlSession session = sqlSessionFactory.openSession();
			session.selectOne("mybatis.detalleVentaMapper.createVentaDet",det);
			session.close();
		} catch (IOException e) {
			
			e.printStackTrace();
    				    		
		}
    	
    }
}
