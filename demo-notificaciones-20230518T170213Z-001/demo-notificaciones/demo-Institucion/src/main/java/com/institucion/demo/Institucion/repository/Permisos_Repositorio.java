/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.institucion.demo.Institucion.repository;

import com.institucion.demo.Institucion.entities.Permisos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Permisos_Repositorio extends JpaRepository<Permisos, Long>{
    @Query("SELECT u FROM Permisos u WHERE u.permiso = :nombre")
    public List<Permisos> findByNameList(@Param("nombre")String nombre);

    @Query("SELECT u FROM Permisos u WHERE u.permiso = :nombre")
    public Permisos findByName(@Param("nombre")String nombre);
}
