package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.entity.TipoAssunto;
import br.ufpr.nc.solicitacaoseis.entity.Estados;
import br.ufpr.nc.solicitacaoseis.repository.EstadosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadosService {

    @Autowired
    private EstadosRepository estadosRepository;

    public List<Estados> findAll() {
        return estadosRepository.findAll();
    }
}
