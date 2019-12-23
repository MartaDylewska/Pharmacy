package com.example.pharmacy.controller;

import com.example.pharmacy.storage.PrescriptionStorage;
import com.example.pharmacy.storage.impl.StaticListPrescriptionStorageImpl;
import com.example.pharmacy.type.Prescription;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static fi.iki.elonen.NanoHTTPD.Response.Status.*;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class PrescriptionController {

    private final static String PATIENT_PESEL_PARAM_NAME = "patientPesel";
    private final static String PRESCRIPTION_ID_PARAM_NAME = "prescriptionId";
    private final static String PHARMACIST_PESEL_PARAM_NAME = "pharmacistPesel";
    private final static String DOCTOR_PESEL_PARAM_NAME = "doctorPesel";

    private PrescriptionStorage prescriptionStorage1 = new StaticListPrescriptionStorageImpl();
    //pharmacist
    public Response serveGetPrescriptionRequest(IHTTPSession session){
        Map<String, List<String>> requestParameters = session.getParameters();
        if (requestParameters.containsKey(PRESCRIPTION_ID_PARAM_NAME) && requestParameters.containsKey(PHARMACIST_PESEL_PARAM_NAME)) {

            List<String> prescriptionIdParams = requestParameters.get(PRESCRIPTION_ID_PARAM_NAME);
            String prescriptionParam = prescriptionIdParams.get(0);
            long prescriptionId;

            List<String> pharmacistPeselParams = requestParameters.get(PHARMACIST_PESEL_PARAM_NAME);
            String pharmacistPeselParam = pharmacistPeselParams.get(0);

            String response;

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                prescriptionId = Long.parseLong(prescriptionParam);
            }
            catch(Exception e){
                System.out.println("Error during converting request param: \n" + e);
                return newFixedLengthResponse(BAD_REQUEST,"text/plain","Entered prescription id must be a number");
            }

            try {
                response = objectMapper.writeValueAsString(prescriptionStorage1.getPrescription(prescriptionId, pharmacistPeselParam));
            } catch (JsonProcessingException e) {
                System.err.println("Error during process request: \n" + e);
                return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error can't read all prescriptions for this patient");
            }
            return newFixedLengthResponse(OK, "application/json", response);
        }
        return null;
    }

    //pacjent
    public Response serveGetPrescriptionsRequest(IHTTPSession session) {

        Map<String, List<String>> requestParameters = session.getParameters();
        if (requestParameters.containsKey(PATIENT_PESEL_PARAM_NAME)) {
            List<String> patientPeselParams = requestParameters.get(PATIENT_PESEL_PARAM_NAME);
            String patientPeselParam = patientPeselParams.get(0);
            String patientPesel;
            String response = "";
            ObjectMapper objectMapper = new ObjectMapper();

            patientPesel = patientPeselParam;

            try {
                response = objectMapper.writeValueAsString(prescriptionStorage1.getAll(patientPesel));
            } catch (JsonProcessingException e) {
                System.err.println("Error during process request: \n" + e);
                return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error can't read all prescriptions for this patient");
            }
            return newFixedLengthResponse(OK, "application/json", response);
        }
        return null;
    }
    //pharmacist
    public Response serveDeletePrescriptionRequest(IHTTPSession session){

        Map<String, List<String>> requestParameters = session.getParameters();
        if(requestParameters.containsKey(PHARMACIST_PESEL_PARAM_NAME) && (requestParameters.containsKey(PRESCRIPTION_ID_PARAM_NAME))) {
            List<String> pharmacistPeselParams = requestParameters.get(PHARMACIST_PESEL_PARAM_NAME);
            String pharmacistPeselParam = pharmacistPeselParams.get(0);

            List<String> prescriptionIdParams = requestParameters.get(PRESCRIPTION_ID_PARAM_NAME);
            String prescriptionParam = prescriptionIdParams.get(0);
            long prescriptionId;

            ObjectMapper objectMapper = new ObjectMapper();
            String response;

            try {
                prescriptionId = Long.parseLong(prescriptionParam);
            } catch (Exception e) {
                System.out.println("Error during converting request param: \n" + e);
                return newFixedLengthResponse(BAD_REQUEST, "text/plain", "Entered prescription id must be a number");
            }
            try {
                response = objectMapper.writeValueAsString(prescriptionStorage1.deletePrescription(prescriptionId, pharmacistPeselParam).getId());
                prescriptionStorage1.deletePrescription(prescriptionId, pharmacistPeselParam);
                StaticListPrescriptionStorageImpl.serializePrescriptions(StaticListPrescriptionStorageImpl.prescriptionStorage);
                StaticListPrescriptionStorageImpl.serializeUsers(StaticListPrescriptionStorageImpl.userStorage);

            } catch (JsonProcessingException e) {
                System.err.println("Error during process request: \n" + e);
                return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error can't remove prescription");
            }
            return newFixedLengthResponse(OK, "text/plain", "Prescription " + response +" has been successfully removed");
        }
        return null;
    }
    //doctor
    public Response serveAddPrescriptionRequest(IHTTPSession session) {

        Map<String, List<String>> requestParameters = session.getParameters();
        if (requestParameters.containsKey(DOCTOR_PESEL_PARAM_NAME)) {
            List<String> doctorPeselParams = requestParameters.get(DOCTOR_PESEL_PARAM_NAME);
            String doctorPeselParam = doctorPeselParams.get(0);

            ObjectMapper objectMapper = new ObjectMapper();
            Prescription requestPrescription;
            long randomPrescriptionId = System.currentTimeMillis();
            LocalDate currentDate = LocalDate.now();
            String response;

            String lengthHeader = session.getHeaders().get("content-length");
            int contentLength = Integer.parseInt(lengthHeader);
            byte[] buffer = new byte[contentLength];

            try {
                session.getInputStream().read(buffer, 0, contentLength);
                String requestBody = new String(buffer).trim();
                requestPrescription = objectMapper.readValue(requestBody, Prescription.class);
                requestPrescription.setId(randomPrescriptionId);
                requestPrescription.setDateOfIssue(currentDate);
                prescriptionStorage1.addUsers(requestPrescription);
                response = prescriptionStorage1.addPrescritpion(requestPrescription, doctorPeselParam);
                StaticListPrescriptionStorageImpl.serializePrescriptions(StaticListPrescriptionStorageImpl.prescriptionStorage);
                StaticListPrescriptionStorageImpl.serializeUsers(StaticListPrescriptionStorageImpl.userStorage);
            } catch (Exception e) {
                System.err.println("Error during process request: \n" + e);
                return newFixedLengthResponse(INTERNAL_ERROR, "text/plain", "Internal error prescription hasn't been added");
            }

            return newFixedLengthResponse(OK, "text/plain", response);
        }
        return null;
    }
}
