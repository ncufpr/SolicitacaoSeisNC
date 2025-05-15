package br.ufpr.nc.solicitacaoseis.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_resposta_hist")
public class SolicitacaoRespostaHist {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitacao_resposta_seq")
    @SequenceGenerator(name = "solicitacao_resposta_seq", sequenceName = "SEQ_SOLICITACAO_RESPOSTA", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero_sol_resp")
    private Long numeroSolResp;

    @Column(name = "numero_interacao")
    private Long numeroInteracao;

    @ManyToOne
    @JoinColumn(name = "id_solicitacao")
    private Solicitacao solicitacao;

    @ManyToOne
    @JoinColumn(name = "id_resposta_solicitacao")
    private RespostaSolicitacao respostaSolicitacao;

    @Column(name = "status")
    private String status;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro;

    // Construtores, getters e setters
    public SolicitacaoRespostaHist() {
        this.dataRegistro = LocalDateTime.now();
    }

    public SolicitacaoRespostaHist(Solicitacao solicitacao, RespostaSolicitacao respostaSolicitacao, String status) {
        this();
        this.solicitacao = solicitacao;
        this.respostaSolicitacao = respostaSolicitacao;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroSolResp() {
        return numeroSolResp;
    }

    public void setNumeroSolResp(Long numeroSolResp) {
        this.numeroSolResp = numeroSolResp;
    }

    public Long getNumeroInteracao() {
        return numeroInteracao;
    }

    public void setNumeroInteracao(Long numeroInteracao) {
        this.numeroInteracao = numeroInteracao;
    }

    public Solicitacao getSolicitacao() {
        return solicitacao;
    }

    public void setSolicitacao(Solicitacao solicitacao) {
        this.solicitacao = solicitacao;
    }

    public RespostaSolicitacao getRespostaSolicitacao() {
        return respostaSolicitacao;
    }

    public void setRespostaSolicitacao(RespostaSolicitacao respostaSolicitacao) {
        this.respostaSolicitacao = respostaSolicitacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    // Getters e Setters
    // ...
}