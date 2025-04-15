package br.ufpr.nc.solicitacaoseis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Concurso {
    @Id
    private Long concurso;
    private String descricao;
}
