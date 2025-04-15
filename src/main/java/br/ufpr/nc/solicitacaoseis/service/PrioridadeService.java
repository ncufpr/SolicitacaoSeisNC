package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.entity.Prioridade;
import br.ufpr.nc.solicitacaoseis.repository.PrioridadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PrioridadeService {
    @Autowired
    private PrioridadeRepository prioridadeRepository;

    public Optional<Prioridade> findById(Integer id) {
        return this.prioridadeRepository.findById(id);
    }
}
