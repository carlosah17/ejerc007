package es.santander.ascender.individualfinal.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.santander.ascender.individualfinal.model.Generos;
import es.santander.ascender.individualfinal.model.Libros;
import es.santander.ascender.individualfinal.repository.LibrosRepository;

@Service
@Transactional
public class LibrosService {
    @Autowired
    private LibrosRepository librosRepository;

    // Crear
    public Libros createLibros(Libros libros) {
        return librosRepository.save(libros);
    }

    // Listar
    @Transactional(readOnly = true)
    public List<Libros> getAllLibros() {
        return librosRepository.findAll();
    }

    // Listar por id
    @Transactional(readOnly = true)
    public Optional<Libros> getLibrosById(Long id) {
        return librosRepository.findById(id);
    }

    // Actualizar
    public Libros updateLibros(Long id, Libros librosDetails) {
        Optional<Libros> librosOptional = librosRepository.findById(id);
        if (librosOptional.isPresent()) {
            Libros libros = librosOptional.get();
            libros.setTitulo(librosDetails.getTitulo());
            libros.setAutor(librosDetails.getAutor());
            libros.setIsbm(librosDetails.getIsbm());
            List<Generos> generos = libros.getGeneros();
            
            generos.clear();
            generos.addAll(librosDetails.getGeneros());
            generos.stream().forEach(l -> l.setLibros(libros));

            return librosRepository.save(libros);
        }
        return null;
    }

}
