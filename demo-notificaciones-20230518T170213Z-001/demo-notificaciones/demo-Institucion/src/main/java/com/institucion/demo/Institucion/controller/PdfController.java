package com.institucion.demo.Institucion.controller;
import com.institucion.demo.Institucion.entities.Recibos_PDF;
import com.institucion.demo.Institucion.repository.BoletinesPDF_Reporitorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayInputStream;

@Controller
public class PdfController {
    @Autowired
    BoletinesPDF_Reporitorio boletinesPDFReporitorio;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/pdf/{id}")
    public ResponseEntity<Resource> showPDF(@PathVariable(required = true) Long id) {
        try {
            Recibos_PDF boletin = boletinesPDFReporitorio.findById(id).get();
            if (boletin != null) {
                byte[] pdfBytes = boletin.getBoletin_PDF();
                if (pdfBytes != null && pdfBytes.length > 0) {
                    InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(pdfBytes));

                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                            .body(resource);
                }
            }

            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
