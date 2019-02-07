package br.com.uol.clima.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.uol.clima.model.Clima;

public interface ClimaRepository extends JpaRepository<Clima, Long> {

}
