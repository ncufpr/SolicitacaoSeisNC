package br.ufpr.nc.solicitacaoseis.repository;

import br.ufpr.nc.solicitacaoseis.entity.Concurso;
import br.ufpr.nc.solicitacaoseis.entity.Solicitacao;
import br.ufpr.nc.solicitacaoseis.entity.Status;
import br.ufpr.nc.solicitacaoseis.entity.TipoAssunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Clob;
import java.util.List;
import java.util.Optional;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
    @Query(value = """
    SELECT * FROM solicitacao s 
    WHERE s.cpf = :cpf 
    AND s.solicitacao = :solicitacao
    AND s.id_concurso = :concurso 
    AND s.id_tipo_assunto = :tipoAssunto 
    AND s.id_status = :status
""", nativeQuery = true)
    List<Solicitacao> findByCpfAndSolicitacaoAndConcursoAndTipoAssuntoAndStatus(
            @Param("cpf") Long cpf,
            @Param("solicitacao")  Clob solicitacao,  // ⚠️ Agora recebe um CLOB
            @Param("concurso") Long concurso,
            @Param("tipoAssunto") Long tipoAssunto,
            @Param("status") Long status);

    @Query("SELECT s FROM Solicitacao s WHERE s.cpf = :cpf AND s.concurso = :concurso AND s.tipoAssunto = :tipoAssunto AND s.status = :status")
    List<Solicitacao> findByCpfAndConcursoAndTipoAssuntoAndStatus(
            @Param("cpf") Long cpf,
            @Param("concurso") Concurso concurso,
            @Param("tipoAssunto") TipoAssunto tipoAssunto,
            @Param("status") Status status);

}
