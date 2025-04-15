package br.ufpr.nc.solicitacaoseis.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class Solicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "solicitacao_seq")
    @SequenceGenerator(name = "solicitacao_seq", sequenceName = "SEQ_SOLICITACAO", allocationSize = 1)
    @Column(name="id_solicitacao")
    private Long idSolicitacao;

    private Long cpf;

    private String nome;

    private String rg;

    private String ufRg;

    private String email;

    @Column(name="data_nascimento")
    private LocalDate dataNascimento;

    private String ddd;

    private String telefone;

    @Lob
    @Column(columnDefinition = "CLOB")
    private String solicitacao;


    @Column(name="data_solicitacao")
    private LocalDateTime dataSolicitacao;

    @ManyToOne
    @JoinColumn(name = "id_concurso")
    private Concurso concurso;

    @ManyToOne
    @JoinColumn(name = "id_tipo_assunto")
    private TipoAssunto tipoAssunto;

    @ManyToOne
    @JoinColumn(name = "id_status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "id_prioridade")
    private Prioridade prioridade;

    public Solicitacao() {}

    public Solicitacao(Long cpf, String nome, String rg, String ufRg, String email, LocalDate dataNascimento, String ddd, String telefone, String solicitacao, Concurso concurso, TipoAssunto tipoAssunto, Status status, Prioridade prioridade) {
        this.cpf = cpf;
        this.nome = nome;
        this.rg = rg;
        this.ufRg = ufRg;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.ddd = ddd;
        this.telefone = telefone;
        this.solicitacao = solicitacao;
        this.concurso = concurso;
        this.tipoAssunto = tipoAssunto;
        this.status = status;
        this.prioridade = prioridade;
    }
}
