package es.santander.ascender.ejerc007;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import es.santander.ascender.ejerc007.model.Libros;
import es.santander.ascender.ejerc007.repository.LibrosRepository;

@SpringBootTest
class Ejerc007ApplicationTests {

	@Autowired
	private LibrosRepository expedienteRepository;

	@Test
	@Transactional
	void contextLoads() {
		Libros expediente = new Libros();
	
		expediente = expedienteRepository.save(expediente);

		assertNotNull(expediente.getId());
	
		Libros expediente2 = expedienteRepository.findById(expediente.getId()).orElse(null);

		assertNotNull(expediente.getId());

		assertEquals(0, expediente2.getDocumento().size());
	}
}
