package br.ufpr.nc.solicitacaoseis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RespostaSolicitacaoDTO {
    private String codigoResposta;
    private String resposta;
    private String anexo;
    private String solicitacao;

    // Dados da Solicitação
    private Long idSolicitacao;
    private String nomeSolicitante;
    private String emailSolicitante;
    private String dataSolicitacao; // ou LocalDate/LocalDateTime se preferir
    private String titulo;
}
