package br.ufpr.nc.solicitacaoseis.util;

import br.ufpr.nc.solicitacaoseis.dto.SolicitacaoDTO;
import br.ufpr.nc.solicitacaoseis.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Component
public class Mapper {
    @Autowired
    private Util util;
    private static final DateTimeFormatter FORMATTER_DDMMYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATTER_YYYYMMDD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDate.parse(dateStr, FORMATTER_DDMMYYYY);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(dateStr, FORMATTER_YYYYMMDD);
        }
    }

    public Solicitacao toSolicitacaoToEntity(SolicitacaoDTO solicitacaoDTO, Status status, TipoAssunto tipoAssunto,
                                             Prioridade prioridade, Concurso concurso) {
        if(solicitacaoDTO == null){
            return null;
        }

        LocalDate data = parseDate(solicitacaoDTO.getDataNascimento());


        return new Solicitacao(
                Long.parseLong(solicitacaoDTO.getCpf().replace("-", "").replace(".", "").trim()),
                solicitacaoDTO.getNome().trim(),
                solicitacaoDTO.getRg().trim(),
                solicitacaoDTO.getUfRg().trim(),
                solicitacaoDTO.getEmail().trim(),
                data,
                solicitacaoDTO.getDdd().trim(),
                solicitacaoDTO.getTelefone().trim(),
                solicitacaoDTO.getSolicitacao().trim(),
                concurso,
                tipoAssunto,
                status,
                prioridade
        );
    }

    public SolicitacaoDTO toSolicitacaoToDto(Solicitacao solicitacao) {
        if (solicitacao == null) {
            return null;
        }

        SolicitacaoDTO dto = new SolicitacaoDTO();

        // Convertendo os campos básicos
        dto.setNome(solicitacao.getNome());
        dto.setCpf(util.formatarCPF( String.valueOf(solicitacao.getCpf()))); // O CPF será formatado depois
        dto.setRg(solicitacao.getRg());
        dto.setUfRg(solicitacao.getUfRg());
        dto.setEmail(solicitacao.getEmail());
        if(solicitacao.getIdSolicitacao() != null){
            dto.setIdSolicitacao(solicitacao.getIdSolicitacao());
        }


        // Formatando a data de nascimento
        if (solicitacao.getDataNascimento() != null) {
            dto.setDataNascimento(solicitacao.getDataNascimento().format(FORMATTER_DDMMYYYY));
        }

        dto.setDdd(solicitacao.getDdd());
        dto.setTelefone(solicitacao.getTelefone());
        dto.setSolicitacao(solicitacao.getSolicitacao());

        // Convertendo as entidades relacionadas para seus IDs
        if (solicitacao.getConcurso() != null) {
            dto.setConcurso(String.valueOf(solicitacao.getConcurso().getDescricao()));
            dto.setConcursoId(String.valueOf(solicitacao.getConcurso().getConcurso()));
        }



        if (solicitacao.getTipoAssunto() != null) {
            dto.setTipoAssunto(String.valueOf(solicitacao.getTipoAssunto().getId()));
            dto.setTipoAssuntoDescricao(String.valueOf(solicitacao.getTipoAssunto().getDescricao()));
        }



        // Você pode adicionar outros campos se necessário
//        dto.setStatus(String.valueOf(solicitacao.getStatus().getIdStatus()));
//        dto.setPrioridade(String.valueOf(solicitacao.getPrioridade().getIdPrioridade()));

        return dto;
    }
}
