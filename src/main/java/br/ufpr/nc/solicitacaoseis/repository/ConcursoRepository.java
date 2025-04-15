package br.ufpr.nc.solicitacaoseis.repository;

import br.ufpr.nc.solicitacaoseis.entity.Concurso;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ConcursoRepository extends ViewRepository<Concurso, Long> {
    @Query(value = "SELECT b.concurso, b.descricao \n" +
            "FROM concurso b  \n" +
            "WHERE b.ativo = 'S' \n" +
            "ORDER BY \n" +
            "    CASE \n" +
            "        WHEN b.concurso = 888888 THEN 1  -- Move 888888 para o final\n" +
            "        ELSE 0 \n" +
            "    END,\n" +
            "    b.concurso DESC", nativeQuery = true)
    List<Concurso> findAllConcursosAtivos();


}
