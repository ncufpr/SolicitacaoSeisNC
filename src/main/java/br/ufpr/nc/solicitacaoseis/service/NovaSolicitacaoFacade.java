package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.config.JwtTokenService;
import br.ufpr.nc.solicitacaoseis.dto.SolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.entity.*;
import br.ufpr.nc.solicitacaoseis.util.Mapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NovaSolicitacaoFacade {

    @Autowired
    private SolicitacaoService solicitacaoService;

    @Autowired
    private SolicitacaoRespostaHistService solicitacaoRespostaHistService;

    @Autowired
    private JwtTokenService tokenService;

    @Autowired
    private StatusService statusService;

    @Autowired
    private ConcursoService concursoService;

    @Autowired
    private TipoAssuntoService tipoAssuntoService;

    @Autowired
    private PrioridadeService prioridadeService;

    @Autowired
    private Mapper mapper;

    /**
     * Método transacional que garante atomicidade
     */
    @Transactional(rollbackFor = Exception.class)
    public Solicitacao processarNovaSolicitacao(SolicitacaoDTO solicitacaoDTO, HttpSession session, boolean reabrir) {
        // Valida dependências ANTES de criar a solicitação
        Status status = statusService.getStatus(6)
                .orElseThrow(() -> new IllegalArgumentException("Status inválido"));
        Concurso concurso = concursoService.findConcursoById(Long.parseLong(solicitacaoDTO.getConcursoId()))
                .orElseThrow(() -> new IllegalArgumentException("Concurso inválido"));
        TipoAssunto tipoAssunto = tipoAssuntoService.findById(Long.parseLong(solicitacaoDTO.getTipoAssunto()))
                .orElseThrow(() -> new IllegalArgumentException("Tipo de Assunto inválido"));
        Prioridade prioridade = prioridadeService.findById(1)
                .orElseThrow(() -> new IllegalArgumentException("Prioridade inválida"));

        // Mapeia DTO → Entidade
        Solicitacao novaSolicitacao = mapper.toSolicitacaoToEntity(
                solicitacaoDTO, status, tipoAssunto, prioridade, concurso
        );
        novaSolicitacao.setDataSolicitacao(LocalDateTime.now());

        // Validação de duplicidade
        if (!solicitacaoService.findByCpfSolicitacaoidConcursoidTipoAssuntoidStatus(novaSolicitacao).isEmpty()) {
            throw new IllegalStateException("Solicitação já enviada para este concurso e assunto.");
        }

        // Persiste no banco
        Solicitacao solicitacaoSalva = solicitacaoService.salvar(novaSolicitacao);

        // Gera token e adiciona na sessão (não depende de banco, mas faz parte da lógica)
        String token = tokenService.gerarToken(solicitacaoSalva.getIdSolicitacao());
        session.setAttribute("token", token);

        // Vincula solicitação anterior ao histórico
        // 6. Decide se é reabertura ou novo ciclo
        if (reabrir && solicitacaoDTO.getIdSolicitacao() != null) {
            Solicitacao solicitacaoAnterior = solicitacaoService.findById(solicitacaoDTO.getIdSolicitacao());
            solicitacaoRespostaHistService.registrarEventoSolicitacao(solicitacaoAnterior, solicitacaoSalva, true);
        } else {
            solicitacaoRespostaHistService.registrarEventoSolicitacao(null, solicitacaoSalva, false);
        }

        return solicitacaoSalva;
    }
}