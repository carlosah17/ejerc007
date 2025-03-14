package es.santander.ascender.individualfinal.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.santander.ascender.individualfinal.model.Libros;
import es.santander.ascender.individualfinal.service.LibrosService;

@RestController
@RequestMapping("/api/libros")
public class LibrosController {
    @Autowired
    private LibrosService librosService;

    // Crear
    @PostMapping
    public ResponseEntity<Libros> createLibros(@RequestBody Libros libros) {
        libros.getGeneros().forEach(d -> d.setLibros(libros));
        Libros createdLibros = librosService.createLibros(libros);

        return new ResponseEntity<>(createdLibros, HttpStatus.CREATED);
    }

    // Listar
    @GetMapping
    public ResponseEntity<List<Libros>> getAllLibros() {
        List<Libros> libros = librosService.getAllLibros();
        return new ResponseEntity<>(libros, HttpStatus.OK);
    }

    // Listar por id
    @GetMapping("/{id}")
    public ResponseEntity<Libros> getLibbrosById(@PathVariable Long id) {
        Optional<Libros> libros = librosService.getLibrosById(id);
        if (libros.isPresent()) {
            return new ResponseEntity<>(libros.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<Libros> updateLibros(@PathVariable Long id, @RequestBody Libros librosDetails) {
               
        Libros updatedLibros = librosService.updateLibros(id, librosDetails);
        if (updatedLibros != null) {
            return new ResponseEntity<>(updatedLibros, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
