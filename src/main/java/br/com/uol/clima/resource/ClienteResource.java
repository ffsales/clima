package br.com.uol.clima.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.uol.clima.model.Cliente;
import br.com.uol.clima.repository.ClienteRepository;

@CrossOrigin
@RestController
@RequestMapping("/clientes")
public class ClienteResource {

	@Autowired
	private ClienteRepository repository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<Cliente> listAll() {
		List<Cliente> clientes = this.repository.findAll();
		
		return clientes;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Cliente findById(@PathVariable("id") Long id) {
		
		Optional<Cliente> clienteOpt = this.repository.findById(id);
		
		return clienteOpt.get();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public void add(@RequestBody Cliente cliente) {
		this.repository.save(cliente);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public void update(@RequestBody Cliente cliente, @PathVariable Long id) {
		cliente.setId(id);
		this.repository.save(cliente);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") Long id) {
		this.repository.deleteById(id);
	}
}
