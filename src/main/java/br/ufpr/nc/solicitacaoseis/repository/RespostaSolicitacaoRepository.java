package br.ufpr.nc.solicitacaoseis.repository;

import br.ufpr.nc.solicitacaoseis.entity.RespostaSolicitacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RespostaSolicitacaoRepository extends JpaRepository<RespostaSolicitacao, Long> {

    @Query("SELECT r FROM RespostaSolicitacao r WHERE r.codigoResposta = :codigo")
    Optional<RespostaSolicitacao> findByCodigoResposta(String codigo);

}
