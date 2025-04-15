package br.ufpr.nc.solicitacaoseis.service;

import br.ufpr.nc.solicitacaoseis.component.ClobUtil;
import br.ufpr.nc.solicitacaoseis.entity.Solicitacao;
import br.ufpr.nc.solicitacaoseis.repository.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialClob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolicitacaoService {
    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private ClobUtil clobUtil;

    public Solicitacao salvar(Solicitacao solicitacao) {
        return solicitacaoRepository.save(solicitacao);
    }

    public List<Solicitacao> findByCpfSolicitacaoidConcursoidTipoAssuntoidStatus(Solicitacao solicitacao) {
        List<Solicitacao> solicitacoes = solicitacaoRepository.findByCpfAndConcursoAndTipoAssuntoAndStatus(
                solicitacao.getCpf(),
                solicitacao.getConcurso(),
                solicitacao.getTipoAssunto(),
                solicitacao.getStatus());

        // Filtrar os registros manualmente comparando o conteúdo do CLOB
        return solicitacoes.stream()
                .filter(s -> stringToClob(s.getSolicitacao()).equals(stringToClob(solicitacao.getSolicitacao())))
                .collect(Collectors.toList());
    }

    // Método para converter CLOB para String
    private String clobToString(Clob clob) {
        if (clob == null) return null;
        try {
            return clob.getSubString(1, (int) clob.length());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao converter CLOB para String", e);
        }
    }

    private Clob stringToClob(String text) {
        try {
            return new SerialClob(text.toCharArray());
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao converter String para Clob", e);
        }
    }


    public Solicitacao findById(Long id) {
        return this.solicitacaoRepository.findById(id).orElse(null);
    }

}
