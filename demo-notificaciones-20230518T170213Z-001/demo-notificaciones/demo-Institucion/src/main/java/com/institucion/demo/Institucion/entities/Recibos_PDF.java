package com.institucion.demo.Institucion.entities;

import javax.persistence.*;

@Entity
public class Recibos_PDF {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long auditoria_id;

    @Lob
    private byte[] Boletin_PDF;

    public Recibos_PDF() {
    }

    public Long getAuditoria_id() {
        return auditoria_id;
    }

    public void setAuditoria_id(Long auditoria_id) {
        this.auditoria_id = auditoria_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getBoletin_PDF() {
        return Boletin_PDF;
    }

    public void setBoletin_PDF(byte[] boletin_PDF) {
        Boletin_PDF = boletin_PDF;
    }
}
