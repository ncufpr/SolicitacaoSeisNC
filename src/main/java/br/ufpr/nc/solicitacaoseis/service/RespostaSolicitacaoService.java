package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.dto.RespostaSolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.entity.RespostaSolicitacao;
import br.ufpr.nc.solicitacaoseis.entity.Solicitacao;
import br.ufpr.nc.solicitacaoseis.repository.RespostaSolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class RespostaSolicitacaoService {
    @Autowired
    private RespostaSolicitacaoRepository respostaSolicitacaoRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public Optional<RespostaSolicitacaoDTO> buscarDtoPorCodigo(String codigoResposta) {
        return respostaSolicitacaoRepository.findByCodigoResposta(codigoResposta)
                .map(resposta -> {
                    var solicitacao = resposta.getSolicitacao();
                    return new RespostaSolicitacaoDTO(
                            resposta.getIdRespostaSolicitacao(),
                            resposta.getCodigoResposta(),
                            resposta.getResposta(),
                            resposta.getAnexoResposta(),
                            solicitacao.getSolicitacao(),
                            resposta.getAvaliacao(),
                            resposta.getComentario(),
                            solicitacao.getIdSolicitacao(),               // idSolicitacao
                            solicitacao.getNome(),  // adaptável ao seu modelo
                            solicitacao.getEmail(),
                            solicitacao.getDataSolicitacao().format(formatter),  // aqui já vem formatado  // formatar se necessário
                            solicitacao.getTipoAssunto().getDescricao(),
                            resposta.getSolicitacao().getConcurso().getDescricao()

                    );
                });
    }


    public Optional<RespostaSolicitacao> findById(Long idRespostaSolicitacao) {
        return respostaSolicitacaoRepository.findById(idRespostaSolicitacao);
    }

    public Optional<RespostaSolicitacao> findByIdSolicitacao(Long idSolicitacao) {
        return respostaSolicitacaoRepository.findByIdSolicitacao(idSolicitacao);
    }

    public boolean avaliarResposta(Long idRespostaSolicitacao, Integer nota, String comentario) {
        int atualizados = respostaSolicitacaoRepository.atualizarNotaEComentario(idRespostaSolicitacao, nota, comentario);
        return atualizados > 0;
    }


}
