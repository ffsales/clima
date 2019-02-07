package br.com.uol.clima.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.uol.clima.model.Cliente;
import br.com.uol.clima.repository.ClienteRepository;

@CrossOrigin
@RestController
@RequestMapping("/clientes")
public class ClienteResource {

	@Autowired
	private ClienteRepository repository;
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Cliente>> listAll() {
		List<Cliente> clientes = this.repository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(clientes);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> findById(@PathVariable("id") Long id) {
		Optional<Cliente> clienteOpt = this.repository.findById(id);
		
		if (!clienteOpt.isPresent()) {
			ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(clienteOpt.get());
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> add(@RequestBody Cliente cliente) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@RequestBody Cliente cliente, @PathVariable Long id) {
		cliente.setId(id);
		this.repository.save(cliente);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		try {
			this.repository.deleteById(id);
		
		} catch (EmptyResultDataAccessException e) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}
}
