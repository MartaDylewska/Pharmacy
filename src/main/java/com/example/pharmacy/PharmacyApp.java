package com.example.pharmacy;

import com.example.pharmacy.storage.impl.StaticListPrescriptionStorageImpl;
import com.example.pharmacy.type.Prescription;
import com.example.pharmacy.type.User;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.List;

import static com.example.pharmacy.storage.impl.StaticListPrescriptionStorageImpl.prescriptionStorage;
import static com.example.pharmacy.storage.impl.StaticListPrescriptionStorageImpl.userStorage;

public class PharmacyApp extends NanoHTTPD {

    RequestUrlMapper requestUrlMapper = new RequestUrlMapper();

    public PharmacyApp(int port) throws IOException {
        super(port);
        start(5000,false);
        System.out.println("Server has been started");
    }

    public static void main(String[] args) {
        try {
            new PharmacyApp(8080);
        } catch (IOException ex) {
            System.err.println("Server can't start because of error: \n" + ex);
        }

/*        StaticListPrescriptionStorageImpl prescriptionStorage1 = new StaticListPrescriptionStorageImpl();
        prescriptionStorage1.addAllPrescritions();
        StaticListPrescriptionStorageImpl.serializePrescriptions(prescriptionStorage);
        StaticListPrescriptionStorageImpl.serializeUsers(userStorage);*/

        prescriptionStorage = StaticListPrescriptionStorageImpl.deserializePrescriptions();
        for (Prescription prescription : prescriptionStorage) {
            System.out.println(prescription.toString());
        }
        userStorage = StaticListPrescriptionStorageImpl.deserializeUsers();

        for(User u:userStorage){
            System.out.println(u.toString());
        }
    }
        @Override
        public Response serve(IHTTPSession session){
            return requestUrlMapper.delegateRequest(session);
        }
    }
