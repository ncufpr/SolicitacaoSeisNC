package br.ufpr.nc.solicitacaoseis.repository;

import br.ufpr.nc.solicitacaoseis.entity.RespostaSolicitacao;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RespostaSolicitacaoRepository extends JpaRepository<RespostaSolicitacao, Long> {

    @Query("SELECT r FROM RespostaSolicitacao r WHERE r.codigoResposta = :codigo")
    Optional<RespostaSolicitacao> findByCodigoResposta(String codigo);


    @Modifying
    @Transactional
    @Query("UPDATE RespostaSolicitacao r SET r.avaliacao = :nota WHERE r.idRespostaSolicitacao = :id")
    int atualizarNota(@Param("id") Long id, @Param("nota") Integer nota);

}
