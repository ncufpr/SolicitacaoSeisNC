package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.entity.Status;
import br.ufpr.nc.solicitacaoseis.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StatusService {
    @Autowired
    private StatusRepository statusRepository;

    public Optional<Status> getStatus(Integer id) {
        return statusRepository.findById(id);
    }
}
