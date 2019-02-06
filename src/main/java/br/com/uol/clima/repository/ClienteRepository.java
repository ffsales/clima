package br.com.uol.clima.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.uol.clima.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
