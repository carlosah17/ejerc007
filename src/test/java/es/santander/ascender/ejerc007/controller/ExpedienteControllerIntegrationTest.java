package es.santander.ascender.ejerc007.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.santander.ascender.ejerc007.model.Generos;
import es.santander.ascender.ejerc007.model.Libros;
import es.santander.ascender.ejerc007.repository.GenerosRepository;
import es.santander.ascender.ejerc007.repository.LibrosRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ExpedienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LibrosRepository expedienteRepository;

    @Autowired
    private GenerosRepository documentoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Libros expediente1;
    private Libros expediente2;
    private Generos documento;

    //@BeforeEach
    void setUp() {
        expedienteRepository.deleteAll();

        documento = new Generos();
        documento.setNombre("documento1");

        expediente1 = new Libros();
        expediente1.setReferencia("REF-001");
        expediente1.getDocumento().add(documento);
        documento.setExpediente(expediente1);
        
        expediente2 = new Libros();
        expediente2.setReferencia("REF-002");

        expedienteRepository.save(expediente1);
        expedienteRepository.save(expediente2);
        documentoRepository.save(documento);
    }

    @Test
    void createExpediente_ShouldReturnCreated() throws Exception {
        Libros newExpediente = new Libros();
        newExpediente.setReferencia("REF-003");

        mockMvc.perform(post("/api/expedientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newExpediente)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.referencia", is("REF-003")));
    }

    @Test
    void getAllExpedientes_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/expedientes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].referencia", is("REF-001")))
                .andExpect(jsonPath("$[1].referencia", is("REF-002")));
    }

    @Test
    void getExpedienteById_ExistingId_ShouldReturnOk() throws Exception {
        Long id = expediente1.getId();

        mockMvc.perform(get("/api/expedientes/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.referencia", is("REF-001")));
    }

    @Test
    void getExpedienteById_NonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/api/expedientes/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateExpediente_ExistingId_ShouldReturnOk() throws Exception {
        Long id = expediente1.getId();
        Libros updatedExpediente = new Libros();
        updatedExpediente.setReferencia("REF-001-UPDATED");

        mockMvc.perform(put("/api/expedientes/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedExpediente)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.referencia", is("REF-001-UPDATED")));
    }

    @Test
    void updateExpediente_NonExistingId_ShouldReturnNotFound() throws Exception {
        Libros updatedExpediente = new Libros();
        updatedExpediente.setReferencia("REF-001-UPDATED");

        mockMvc.perform(put("/api/expedientes/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedExpediente)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpediente_ExistingId_ShouldReturnNoContent() throws Exception {
        Long id = expediente1.getId();

        mockMvc.perform(delete("/api/expedientes/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/expedientes/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpediente_NonExistingId_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/api/expedientes/999"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createExpediente_ShouldCreateExpedienteWithDocumentos() throws Exception{
        Libros newExpediente = new Libros();
        newExpediente.setReferencia("REF-003");
        Generos doc1 = new Generos();
        doc1.setNombre("Doc1");
        Generos doc2 = new Generos();
        doc2.setNombre("Doc2");
        newExpediente.getDocumento().add(doc1);
        newExpediente.getDocumento().add(doc2);

        mockMvc.perform(post("/api/expedientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newExpediente)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.referencia", is("REF-003")))
                .andExpect(jsonPath("$.documento[0].nombre", is("Doc1")))
                .andExpect(jsonPath("$.documento[1].nombre", is("Doc2")));
        expedienteRepository.flush();
    }
}
