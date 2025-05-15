package br.ufpr.nc.solicitacaoseis.dto;

import br.ufpr.nc.solicitacaoseis.util.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SolicitacaoDTO {

    @CPF
    @SecureString
    private String cpf;

    @NotBlank(message = "Nome é obrigatório.")
    @SecureString
    @ValidNome
    private String nome;

    @NotBlank(message = "RG é obrigatório.")
    @SecureString
    private String rg;

    @Email(message = "Informe um e-mail válido.")
    @NotBlank(message = "E-mail é obrigatório.")
    @SecureString
    private String email;

    @NotBlank(message = "UF do RG é obrigatório.")
    @SecureString
    private String ufRg;

    @ValidDataNascimento
    @SecureString
    private String dataNascimento;

    @ValidDDD
    @SecureString
    private String ddd;

    @ValidTelefone
    @SecureString
    private String telefone;

    @NotBlank(message = "A solicitação não pode estar vazia.")
    @SecureString
    private String solicitacao;

    private Date dataSolicitacao;


    private String concurso;

    @NotNull(message = "Selecione o tipo da solicitação.")
    @SecureString
    private String tipoAssunto;

    private String tipoAssuntoDescricao;

    private Long idSolicitacao;

    @NotNull(message = "O Concurso ou o Processo Seletivo é obrigatório.")
    @SecureString
    private String concursoId;
}
