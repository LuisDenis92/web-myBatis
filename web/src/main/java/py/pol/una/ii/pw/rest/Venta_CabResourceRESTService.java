package py.pol.una.ii.pw.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
//import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.SessionContext;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import py.pol.una.ii.pw.data.ClienteRepository;
import py.pol.una.ii.pw.data.Venta_CabRepository;
import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Compra_Cab;
import py.pol.una.ii.pw.model.Compra_Det;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.model.Venta_Cab;
import py.pol.una.ii.pw.model.Venta_Det;
import py.pol.una.ii.pw.service.ProductoRegistration;
//import javax.ejb.EJBTransactionRolledbackException;
//import javax.ejb.EJB;
//import javax.ejb.EJBTransactionRolledbackException;
import py.pol.una.ii.pw.service.Venta_CabRegistration;

import javax.swing.JOptionPane;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Path("/ventas")
@ViewScoped


public class Venta_CabResourceRESTService {
	@Inject
    ProductoRegistration pr;
	
	
	
	@Inject
	ProductoResourceRESTService productoManager;


	@PersistenceContext(unitName="PersistenceApp") 
	private EntityManager em;
	
	@Inject
    private Logger log;

    @Inject
    private Validator validator;
    
    @Inject
    private ClienteRepository repo;

    @Inject
    private Venta_CabRepository repository;

    @Inject
    Venta_CabRegistration registration;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Venta_Cab> listAllVenta_Cab() throws IOException {
        return registration.getAllVenta_Cab();
    }
    
    

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Venta_Cab lookupVenta_CabById(@PathParam("id") long id) throws IOException {
    	Venta_Cab venta_Cab = registration.getVentaCabById(id);
        if (venta_Cab == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return venta_Cab;
    }

    /****************************Crear Ventas  
     * @throws IOException ********************************************/
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/crear")
    public void createVenta( @RequestBody List<Venta_Det> detalleList) throws IOException{
    	
    
    	
    	
    		registration.registrarVenta(detalleList);
		
    	
    }

    
   

}
