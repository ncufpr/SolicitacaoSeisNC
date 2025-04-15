package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.entity.TipoAssunto;
import br.ufpr.nc.solicitacaoseis.repository.TipoAssuntoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TipoAssuntoService {
    @Autowired
    private TipoAssuntoRepository assuntoRepository;
    public List<TipoAssunto> findAll() {
        return this.assuntoRepository.findAll();
    }

    public Optional<TipoAssunto> findById(Long id) {
        return this.assuntoRepository.findById(id);
    }
}
