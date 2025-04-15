package br.ufpr.nc.solicitacaoseis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Prioridade {
    @Id
    private Integer idPrioridade;
    private String descricao;
}
