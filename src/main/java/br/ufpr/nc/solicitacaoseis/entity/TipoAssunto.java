package br.ufpr.nc.solicitacaoseis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tipo_assunto")
public class TipoAssunto {
    @Id
    private Long id;
    private String descricao;
}
