
package py.pol.una.ii.pw.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.web.bind.annotation.RequestBody;

import com.csvreader.CsvWriter;

import py.pol.una.ii.pw.data.Compra_CabRepository;
import py.pol.una.ii.pw.data.ProveedorRepository;
import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Compra_Cab;
import py.pol.una.ii.pw.model.Compra_Det;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.service.Compra_CabRegistration;
import py.pol.una.ii.pw.service.Compra_DetRegistration;
import py.pol.una.ii.pw.service.ProductoRegistration;


@Path("/compras")
@ViewScoped
public class Compra_CabResourceRESTService {
	

	
	@Inject
    ProductoRegistration pr;
	
	
	
	@Inject
	ProductoResourceRESTService productoManager;
	
	
	@PersistenceContext(unitName="PersistenceApp") 
	private EntityManager em;
	
    
    @Inject
    private ProveedorRepository proveedorRepository;

    @Inject
    Compra_CabRegistration registration;
    
    @Inject
    Compra_DetRegistration registrationdetalle;

 

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Compra_Cab> listAllCabeceras() throws IOException {
        return registration.getAllCompra_Cab();
        
       
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public Compra_Cab listAllCabeceraById(@PathParam("id") long id) throws IOException {
        return registration.getCompraCabById(id);
        
       
    }

    
	
    
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/crear")
    public void createCompra(@RequestBody List<Compra_Det> detalleCompra)throws IOException{
		
    	
    
    	
    	 try {
 			registration.registrarCompra(detalleCompra);
 		} catch (Exception e) {
 			
 		}
    	 
     }
    
   
}