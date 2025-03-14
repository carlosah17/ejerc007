package es.santander.ascender.individualfinal.model;

import java.time.LocalDate;

import org.hibernate.validator.constraints.Length;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Prestamos {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @NotNull
    @Length(max=30)
    private String codigo;

    @NotNull
    private LocalDate fechaPrestamoInicial;

    private boolean devuelto=false;

    @ManyToOne
    private Libros libros;

    public Prestamos() {
    }
    
    public Prestamos(Long id, @NotBlank @NotNull @Length(max = 30) String codigo,
            @NotNull LocalDate fechaPrestamoInicial) {
        this.id = id;
        this.codigo = codigo;
        this.fechaPrestamoInicial = fechaPrestamoInicial;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Libros getLibros() {
        return libros;
    }

    public void setLibros(Libros libros) {
        this.libros = libros;
    }

    public LocalDate getFechaPrestamoInicial() {
        return fechaPrestamoInicial;
    }

    public void setFechaPrestamoInicial(LocalDate fechaPrestamoInicial) {
        this.fechaPrestamoInicial = fechaPrestamoInicial;
    }
   
    public boolean isDevuelto() {
        return this.devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Prestamos other = (Prestamos) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

}
