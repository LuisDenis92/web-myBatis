///*
// * JBoss, Home of Professional Open Source
// * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
// * contributors by the @authors tag. See the copyright.txt in the
// * distribution for a full listing of individual contributors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * http://www.apache.org/licenses/LICENSE-2.0
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package py.pol.una.ii.pw.controller;
//
//import java.util.List;
//
//import javax.annotation.PostConstruct;
//import javax.enterprise.inject.Model;
//import javax.enterprise.inject.Produces;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.ViewScoped;
//import javax.faces.context.FacesContext;
//import javax.inject.Inject;
//import javax.inject.Named;
//
//import py.pol.una.ii.pw.data.ClienteRepository;
//import py.pol.una.ii.pw.data.Compra_CabRepository;
//import py.pol.una.ii.pw.model.Clientes;
//import py.pol.una.ii.pw.model.Compra_Cab;
//import py.pol.una.ii.pw.service.Compra_CabRegistration;
//
//// The @Model stereotype is a convenience mechanism to make this a request-scoped bean that has an
//// EL name
//// Read more about the @Model stereotype in this FAQ:
//// http://sfwk.org/Documentation/WhatIsThePurposeOfTheModelAnnotation
////@Model
//@ManagedBean(name="beancompras")
//@ViewScoped
//public class Compra_CabController {
//
//    @Inject
//    private FacesContext facesContext;
//
//    @Inject
//    private Compra_CabRegistration cabeceraRegistration;
//    
//    @Inject
//    private Compra_CabRepository repository;
//
//    private Compra_Cab newCabecera;
//    
//
//	private List<Compra_Cab> compra_CabFilteringList;
//
//	public List<Compra_Cab> getCompra_CabFilteringList() {
//		return compra_CabFilteringList;
//	}
//
//	public void setCompra_CabFilteringList(List<Compra_Cab> compra_CabFilteringList) {
//		this.compra_CabFilteringList = compra_CabFilteringList;
//	}
//
//    @Produces
//    @Named
//    public Compra_Cab getNewCabecera() {
//        return newCabecera;
//    }
//
//    public void register() throws Exception {
//        try {
//            cabeceraRegistration.registrarCabecera(newCabecera);
//            facesContext.addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado!", "Registro exitoso"));
//            initNewCabecera();
//        } catch (Exception e) {
//            String errorMessage = getRootErrorMessage(e);
//            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registro fallido");
//            facesContext.addMessage(null, m);
//        }
//    }
//
//    @PostConstruct
//    public void initNewCabecera() {
//        newCabecera = new Compra_Cab();
//    }
//
//    
//    public List<Compra_Cab> listAllCompra_Cab() {
//        return repository.findAllOrderedByFecha();
//    }
//    
//    private String getRootErrorMessage(Exception e) {
//        // Default to general error message that registration failed.
//    String errorMessage = "La operacion ha fallado";
//        if (e == null) {
//            // This shouldn't happen, but return the default messages
//            return errorMessage;
//        }
//
//        // Start with the exception and recurse to find the root cause
//        Throwable t = e;
//        while (t != null) {
//            // Get the message from the Throwable class instance
//            errorMessage = t.getLocalizedMessage();
//            t = t.getCause();
//        }
//        // This is the root cause message
//        return errorMessage;
//    }
//    /*
//    ///////////////Eliminar
//    public void remover() {
//		try {
//			cabeceraRegistration.remover(newCabecera);
//		 } catch (Exception e) {
//	            String errorMessage = getRootErrorMessage(e);
//	            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registro fallido");
//	            facesContext.addMessage(null, m);
//	     }
//	}*/
//    
//}
