package com.institucion.demo.Institucion.repository;

import com.institucion.demo.Institucion.entities.Recibos_PDF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletinesPDF_Reporitorio extends JpaRepository<Recibos_PDF, Long> {
}
