package com.example.pharmacy;

import com.example.pharmacy.controller.PrescriptionController;
import fi.iki.elonen.NanoHTTPD;

import static fi.iki.elonen.NanoHTTPD.Method.*;
import static fi.iki.elonen.NanoHTTPD.Response.Status.NOT_FOUND;

public class RequestUrlMapper {

    private final static String GET_ALL_PRESC_URL = "/pharm/getAll";
    private final static String ADD_PRESC_URL = "/pharm/add";
    private final static String GET_PRESC_URL = "/pharm/get";
    private final static String DELETE_PRESC_URL = "/pharm/delete";

    private PrescriptionController prescriptionController = new PrescriptionController();

    public NanoHTTPD.Response delegateRequest(NanoHTTPD.IHTTPSession session){

        if(POST.equals(session.getMethod()) && ADD_PRESC_URL.equals(session.getUri())){
            return prescriptionController.serveAddPrescriptionRequest(session) ;
        }

        else if(DELETE.equals(session.getMethod()) && DELETE_PRESC_URL.equals(session.getUri())){
            return prescriptionController.serveDeletePrescriptionRequest(session);
        }

        else if(GET.equals(session.getMethod()) && GET_ALL_PRESC_URL.equals(session.getUri())){
            return prescriptionController.serveGetPrescriptionsRequest(session);
        }

        else if(GET.equals(session.getMethod()) && GET_PRESC_URL.equals(session.getUri())){
            return prescriptionController.serveGetPrescriptionRequest(session);
        }

        return NanoHTTPD.newFixedLengthResponse(NOT_FOUND,"text/plain","Not Found");
    }
}
