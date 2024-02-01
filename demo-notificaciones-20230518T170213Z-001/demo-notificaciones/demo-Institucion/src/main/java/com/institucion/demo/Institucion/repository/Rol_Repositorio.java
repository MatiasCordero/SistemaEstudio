
package com.institucion.demo.Institucion.repository;

import com.institucion.demo.Institucion.entities.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Rol_Repositorio extends JpaRepository<Rol, Long>{
    @Query("SELECT u FROM Rol u WHERE u.nombre = :nombre")
    public Rol findByNombre(@Param("nombre")String nombre);
}
