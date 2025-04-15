package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.entity.Concurso;
import br.ufpr.nc.solicitacaoseis.repository.ConcursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConcursoService {
    @Autowired
    private ConcursoRepository repo;

    public List<Concurso> findAllConcursosAtivos (){
        return this.repo.findAllConcursosAtivos();
    }

    public Optional<Concurso> findConcursoById(Long id){
        return this.repo.findById(id);
    }
}
