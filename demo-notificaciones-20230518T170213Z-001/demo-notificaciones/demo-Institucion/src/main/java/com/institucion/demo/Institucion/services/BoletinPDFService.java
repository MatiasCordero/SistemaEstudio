package com.institucion.demo.Institucion.services;

import com.institucion.demo.Institucion.entities.Recibos_PDF;
import com.institucion.demo.Institucion.repository.BoletinesPDF_Reporitorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BoletinPDFService {
    @Autowired
    private BoletinesPDF_Reporitorio boletinesPDFReporitorio;

    public void guardarBoletinPDF(Recibos_PDF recibosPdf) {

            boletinesPDFReporitorio.save(recibosPdf);
    }

    public void actualizarBoletinPDF(Recibos_PDF recibosPdf) {
        boletinesPDFReporitorio.save(recibosPdf);
    }

    public void eliminarBoletinPDF(Long id) {
        boletinesPDFReporitorio.deleteById(id);
    }
}

