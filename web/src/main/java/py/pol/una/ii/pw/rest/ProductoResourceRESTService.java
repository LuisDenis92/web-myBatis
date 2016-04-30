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
package py.pol.una.ii.pw.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.csvreader.CsvWriter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.faces.event.ActionEvent;

import net.sf.jasperreports.components.barbecue.BarcodeProviders.NW7Provider;

import org.jboss.resteasy.annotations.Body;
import org.primefaces.model.LazyDataModel;
import org.springframework.web.bind.annotation.RequestBody;

import py.pol.una.ii.pw.controller.ProductoLazyList;
import py.pol.una.ii.pw.data.ProductoRepository;
import py.pol.una.ii.pw.data.Producto_DuplicadoRepository;
import py.pol.una.ii.pw.model.Clientes;
import py.pol.una.ii.pw.model.Producto;
import py.pol.una.ii.pw.model.Producto_Duplicado;
import py.pol.una.ii.pw.model.Proveedor;
import py.pol.una.ii.pw.service.FiltersObject;
import py.pol.una.ii.pw.service.ProductoRegistration;
import py.pol.una.ii.pw.service.Producto_DuplicadoRegistration;
import py.pol.una.ii.pw.data.ProveedorRepository;

import javax.faces.bean.ApplicationScoped;
import javax.faces.context.FacesContext;

@Path("/productos")
@ApplicationScoped
public class ProductoResourceRESTService {
	
	@PersistenceContext(unitName="PersistenceApp") 
	private EntityManager em; 
	

	
	
    @Inject
    private Logger log;

    @Inject
    private Validator validator;

    @Inject
    private ProductoRepository repository;
    
    
    @Inject
    private Producto_DuplicadoRepository duplicadorepository;

    @Inject
    ProductoRegistration registration;
    
    @Inject
    Producto_DuplicadoRegistration duplicadoRegistration;

    private List<Producto> productos;
    
    /***************Listado de Productos
     * @throws IOException ***************************************************/
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Producto> listAllProductos() throws IOException {
        return registration.getAllProductos();
    }
   
    
    
    
    /****************Buscar por Id
     * @throws IOException ********************************************************/
    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Producto lookupProductoById(@PathParam("id") long id) throws IOException {
        Producto producto = registration.getProductoById(id);
        if (producto == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return producto;
    }

    /*****************************Crear
     * @throws Exception *****************************************************/
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/crear")
    public Response createProducto(@RequestBody Producto produ) throws IOException{
    	
        Response.ResponseBuilder builder = null;
		
		
        try {
           
           
           registration.createProducto(produ);
         
            builder = Response.ok();
       
        } catch (Exception e) {
        	//********************CARGAR DUPLICADO********************
        	//********************************************************
        	Producto_Duplicado pd = new Producto_Duplicado();
            registration.cargarDuplicado(pd, produ.getDetalle());
        
        	
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }
       
        return builder.build();
    }
    
   
    
    
    
    /*****************************Modificar*******************************************/
     
     @PUT
     @Consumes(MediaType.APPLICATION_JSON)
     @Path("/modificar")
    
    public void modificarProducto(Producto producto) throws IOException{
    	
    	registration.updateProducto(producto);
    }

    

    /*****************************Eliminar***********************************************/
    @POST
    @Path("/eliminar/{id:[0-9][0-9]*}")
    public void removeProducto(@PathParam("id")Long id) throws IOException {
        Response.ResponseBuilder builder = null;

        try {
        	registration.remove(id);
        } catch (Exception e) {
            // Handle generic exceptions
            Map<String, String> responseObj = new HashMap<String, String>();
            responseObj.put("error", e.getMessage());
            builder = Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
        }

        
    }
  

    @POST
    @Path("/cargamasiva")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public void cargaMasiva(@RequestBody List<Producto> produList)  {
    	

    	
    	registration.cargaMasiva(produList);
        
    }
    
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/duplicados")
    public List<Producto_Duplicado> listaProductosDuplicados(){
    	return duplicadoRegistration.getAllProductosDuplicados();
    }

    

}