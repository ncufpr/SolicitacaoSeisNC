package br.ufpr.nc.solicitacaoseis.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class RespostaSolicitacao {
    @Id
    private Long idRespostaSolicitacao;
    private String codigoResposta;
    private String resposta;
    private String anexoResposta;
    private Integer avaliacao;
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "id_solicitacao")
    private Solicitacao solicitacao;

    @OneToMany(mappedBy = "respostaSolicitacao")
    private List<SolicitacaoRespostaHist> solicitacaoRespostasHist;

}
