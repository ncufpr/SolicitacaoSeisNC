package br.ufpr.nc.solicitacaoseis.dto;

import br.ufpr.nc.solicitacaoseis.util.SecureString;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "A nota é obrigatória.")
    private Integer avaliacao;


    @SecureString
    @Size(max = 1000, message = "O comentário não pode conter mais de 1000 caracteres.")
    private String comentario;

    // Dados da Solicitação
    private Long idSolicitacao;
    private String nomeSolicitante;
    private String emailSolicitante;
    private String dataSolicitacao;
    private String tipoAssuntoResposta;
    private String concurso;
}
