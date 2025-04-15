package br.ufpr.nc.solicitacaoseis.repository;

import br.ufpr.nc.solicitacaoseis.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}
