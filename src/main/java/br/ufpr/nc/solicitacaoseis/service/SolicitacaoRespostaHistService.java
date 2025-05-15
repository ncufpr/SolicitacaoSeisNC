package br.ufpr.nc.solicitacaoseis.service;


import br.ufpr.nc.solicitacaoseis.entity.RespostaSolicitacao;
import br.ufpr.nc.solicitacaoseis.entity.Solicitacao;
import br.ufpr.nc.solicitacaoseis.entity.SolicitacaoRespostaHist;
import br.ufpr.nc.solicitacaoseis.repository.SolicitacaoRespostaHistRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitacaoRespostaHistService {

    @PersistenceContext
    private EntityManager entityManager;

    private final SolicitacaoRespostaHistRepository repository;

    @Autowired
    public SolicitacaoRespostaHistService(SolicitacaoRespostaHistRepository repository) {
        this.repository = repository;
    }

    /**
     * Inicia um novo ciclo de solicitação-resposta
     */
    public SolicitacaoRespostaHist iniciarNovoCiclo(Solicitacao solicitacao) {
        // Obtém um novo número de solicitação-resposta da sequence
        Long novoNumeroSolResp = ((Number) entityManager.createNativeQuery(
                "SELECT SEQ_SOL_RESP.NEXTVAL from dual").getSingleResult()).longValue();

        SolicitacaoRespostaHist hist = new SolicitacaoRespostaHist();
        hist.setNumeroSolResp(novoNumeroSolResp); // Número único para todo o ciclo
        hist.setSolicitacao(solicitacao);
        hist.setStatus("SOLICITACAO_CRIADA");
        hist.setNumeroInteracao(1L); // Primeira interação do ciclo
        return repository.save(hist);
    }

    /**
     * Adiciona uma nova interação ao ciclo existente
     */
    public SolicitacaoRespostaHist adicionarInteracaoCiclo(Long numeroSolResp,
                                                           Solicitacao solicitacao,
                                                           RespostaSolicitacao resposta,
                                                           String status) {
        // Busca a última interação para obter o próximo número de interação
        Long proximoNumeroInteracao = repository.findMaxNumeroInteracaoByNumeroSolResp(numeroSolResp)
                .orElse(0L) + 1L;

        SolicitacaoRespostaHist hist = new SolicitacaoRespostaHist();
        hist.setNumeroSolResp(numeroSolResp); // Mantém o mesmo número do ciclo
        hist.setSolicitacao(solicitacao);
        hist.setRespostaSolicitacao(resposta);
        hist.setStatus(status);
        hist.setNumeroInteracao(proximoNumeroInteracao);
        return repository.save(hist);
    }

    public SolicitacaoRespostaHist registrarReabertura(Long numeroSolResp, Solicitacao novaSolicitacao) {
        return adicionarInteracaoCiclo(
                numeroSolResp,
                novaSolicitacao, // Nova solicitação de reabertura
                null,
                "SOLICITACAO_REABERTA"
        );
    }

    @Transactional
    public void registrarEventoSolicitacao(Solicitacao solicitacao, Solicitacao solicitacaoNova, boolean reabrindo) {
        if (reabrindo) {
            Long numeroSolResp = repository
                    .findUltimoNumeroSolRespBySolicitacao(solicitacao.getIdSolicitacao())
                    .orElseThrow(() -> new IllegalStateException("Número de ciclo não encontrado."));

            registrarReabertura(numeroSolResp, solicitacaoNova);
        } else {
            iniciarNovoCiclo(solicitacaoNova);
        }
    }

}
