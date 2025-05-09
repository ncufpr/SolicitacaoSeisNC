package br.ufpr.nc.solicitacaoseis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class RespostaSolicitacao {
    @Id
    private Long idRespostaSolicitacao;
    private String codigoResposta;
    private String resposta;
    private String anexoResposta;

    @ManyToOne
    @JoinColumn(name = "id_solicitacao")
    private Solicitacao solicitacao;

}
