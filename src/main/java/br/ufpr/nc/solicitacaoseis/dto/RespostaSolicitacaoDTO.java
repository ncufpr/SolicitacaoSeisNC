package br.ufpr.nc.solicitacaoseis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RespostaSolicitacaoDTO {
    private Long idRespostaSolicitacao;
    private String codigoResposta;
    private String resposta;
    private String anexo;
    private String solicitacao;
    private Integer avaliacao;

    // Dados da Solicitação
    private Long idSolicitacao;
    private String nomeSolicitante;
    private String emailSolicitante;
    private String dataSolicitacao;
    private String tipoAssuntoResposta;
    private String concurso;
}
