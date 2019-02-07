package br.com.uol.clima.resource;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.uol.clima.model.Cliente;
import br.com.uol.clima.model.Clima;
import br.com.uol.clima.repository.ClienteRepository;
import br.com.uol.clima.repository.ClimaRepository;

@CrossOrigin
@RestController
@RequestMapping("/clima")
public class ClimaResource {
	
	@Autowired
	private ClimaRepository climaRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Void> getTemperatura(@PathVariable("id") Long id) {
		
		Long lat;
		Long lon;
		Double temp;
		
		try {
			String ip = Inet4Address.getLocalHost().getHostAddress();
			RestTemplate restTemplate = new RestTemplate();
			//JsonNode localizacao = restTemplate.getForObject("https://ipvigilante.com/" + ip + "/full", JsonNode.class);
			JsonNode localizacao = restTemplate.getForObject("https://ipvigilante.com/8.8.8.8/full", JsonNode.class);
			System.out.println(localizacao);
			JsonNode data = localizacao.get("data");
			lat = data.get("latitude").asLong();
			lon = data.get("longitude").asLong();
			
			JsonNode mapaweather = restTemplate.getForObject("https://www.metaweather.com/api/location/search/?lattlong=" + lat + "," + lon, JsonNode.class);
			System.out.println(mapaweather.get(0).get("woeid"));
			Long woeid = mapaweather.get(0).get("woeid").asLong();
			
			JsonNode clima = restTemplate.getForObject("https://www.metaweather.com/api/location/" + woeid, JsonNode.class);
			JsonNode consolidatedWeather = clima.get("consolidated_weather");
			temp = consolidatedWeather.get(0).get("the_temp").asDouble();
			System.out.println(clima);
			System.out.println(consolidatedWeather.get(0).get("the_temp"));
			
		} catch(HttpClientErrorException e) {
			return ResponseEntity.badRequest().build();
		} catch(UnknownHostException e) {
			return ResponseEntity.badRequest().build();
		}
		
		Cliente cliente = clienteRepository.getOne(id);
		
		Clima clima = new Clima();
		clima.setCliente(cliente);
		clima.setData(new Date());
		clima.setLatitude(lat);
		clima.setLongitude(lon);
		clima.setTemperatura(temp);
		
		climaRepository.save(clima);
		
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Clima>> findAll() {
		List<Clima> climas = climaRepository.findAll();
		return ResponseEntity.status(HttpStatus.OK).body(climas);
	}
}
