package com.ruralwomen.platform.service;



import com.ruralwomen.platform.model.Request;
import com.ruralwomen.platform.repository.RequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RequestService {

    private final RequestRepository requestRepo;

    public RequestService(RequestRepository requestRepo) {
        this.requestRepo = requestRepo;
    }

    public Request createRequest(Request req) {
        return requestRepo.save(req);
    }

    public List<Request> getRequestsForEntrepreneur(String entrepreneurId) {
        return requestRepo.findByEntrepreneurId(entrepreneurId);
    }

    public List<Request> getRequestsByCustomer(String customerId) {
        return requestRepo.findByCustomerId(customerId);
    }

    public Optional<Request> acceptRequest(String id) {
        Optional<Request> reqOpt = requestRepo.findById(id);
        if (reqOpt.isPresent()) {
            Request req = reqOpt.get();
            req.setStatus("accepted");
            requestRepo.save(req);
        }
        return reqOpt;
    }

    public Optional<Request> rejectRequest(String id) {
        Optional<Request> reqOpt = requestRepo.findById(id);
        if (reqOpt.isPresent()) {
            Request req = reqOpt.get();
            req.setStatus("rejected");
            requestRepo.save(req);
        }
        return reqOpt;
    }
}
