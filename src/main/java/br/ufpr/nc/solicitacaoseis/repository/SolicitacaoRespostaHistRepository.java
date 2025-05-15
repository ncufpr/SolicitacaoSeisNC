package br.ufpr.nc.solicitacaoseis.repository;

import br.ufpr.nc.solicitacaoseis.entity.SolicitacaoRespostaHist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SolicitacaoRespostaHistRepository extends JpaRepository<SolicitacaoRespostaHist, Long> {


    @Query("SELECT MAX(h.numeroInteracao) FROM SolicitacaoRespostaHist h WHERE h.numeroSolResp = :numeroSolResp")
    Optional<Long> findMaxNumeroInteracaoByNumeroSolResp(@Param("numeroSolResp") Long numeroSolResp);

    @Query("SELECT h FROM SolicitacaoRespostaHist h WHERE h.numeroSolResp = :numeroSolResp ORDER BY h.numeroInteracao ASC")
    List<SolicitacaoRespostaHist> findByNumeroSolRespOrderByNumeroInteracaoAsc(@Param("numeroSolResp") Long numeroSolResp);

    @Query("SELECT h FROM SolicitacaoRespostaHist h WHERE h.numeroSolResp = :numeroSolResp ORDER BY h.numeroInteracao DESC")
    List<SolicitacaoRespostaHist> findByNumeroSolRespOrderByNumeroInteracaoDesc(@Param("numeroSolResp") Long numeroSolResp);

    default Optional<SolicitacaoRespostaHist> findTopByNumeroSolRespOrderByNumeroInteracaoDesc(Long numeroSolResp) {
        return findByNumeroSolRespOrderByNumeroInteracaoDesc(numeroSolResp).stream().findFirst();
    }

    @Query("SELECT h.numeroSolResp FROM SolicitacaoRespostaHist h " +
            "WHERE h.solicitacao.idSolicitacao = :idSolicitacao " +
            "ORDER BY h.numeroInteracao DESC")
    Optional<Long> findUltimoNumeroSolRespBySolicitacao(@Param("idSolicitacao") Long idSolicitacao);



}


