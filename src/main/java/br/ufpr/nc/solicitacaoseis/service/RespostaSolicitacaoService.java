package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.dto.RespostaSolicitacaoDTO;
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
                            resposta.getCodigoResposta(),
                            resposta.getResposta(),
                            resposta.getAnexoResposta(),
                            solicitacao.getSolicitacao(),
                            solicitacao.getIdSolicitacao(),               // idSolicitacao
                            solicitacao.getNome(),  // adaptável ao seu modelo
                            solicitacao.getEmail(),
                            solicitacao.getDataSolicitacao().format(formatter),  // aqui já vem formatado  // formatar se necessário
                            solicitacao.getTipoAssunto().getDescricao()
                    );
                });
    }

}
