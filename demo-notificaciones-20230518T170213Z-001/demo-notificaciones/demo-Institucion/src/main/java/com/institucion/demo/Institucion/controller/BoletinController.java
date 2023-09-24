package com.institucion.demo.Institucion.controller;

import com.institucion.demo.Institucion.Util.BoletinReportGenerator;
import com.institucion.demo.Institucion.entities.Recibos_PDF;
import com.institucion.demo.Institucion.services.BoletinPDFService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Controller
@RequestMapping("/recibos")
public class BoletinController {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private BoletinPDFService boletinPDFService;

    private final ServletContext servletContext;
    private final ResourceLoader resourceLoader;
    @Autowired
    public BoletinController(DataSource dataSource, ServletContext servletContext, ResourceLoader resourceLoader) {
        this.dataSource = dataSource;
        this.servletContext = servletContext;
        this.resourceLoader = resourceLoader;
    }

    @GetMapping("/export-pdf")
    public ResponseEntity<String> exportPdf() throws JRException, SQLException, IOException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            String consultaSQL = "SELECT id, nro_recibo, cuil, codigo_unico, aux_centro_liq FROM auditoria_recibo_online WHERE pdf = 'N'";

            PreparedStatement preparedStatement = connection.prepareStatement(consultaSQL);
            ResultSet resultSet = preparedStatement.executeQuery();

            Map<String, Object> params = new HashMap<>();
            if (resultSet.next()) {
                do {
                    int nroRecibo = resultSet.getInt("nro_recibo");
                    String cuil = resultSet.getString("cuil");
                    String codigoUnico = resultSet.getString("codigo_unico");
                    String auxCentroLiq = resultSet.getString("aux_centro_liq");
                    Long auditoriaId = resultSet.getLong("id");

                    System.out.println(nroRecibo);
                    System.out.println(cuil);
                    System.out.println(codigoUnico);
                    System.out.println(auxCentroLiq);

                    params.put("p_nro_recibo", nroRecibo);
                    params.put("p_cuil", cuil);
                    params.put("p_url_valid", "1234");
                    params.put("p_codigo_unico", codigoUnico);
                    params.put("p_centro_liquidacion", auxCentroLiq);

                    Resource resource = resourceLoader.getResource("classpath:ReciboSueldoPDFCloud.jrxml");
                    InputStream inputStream = resource.getInputStream();
                    // Generar el reporte en JasperPrint
                    JasperPrint jasperPrint = BoletinReportGenerator.getReport(connection, inputStream, params);

                    // Convertir el JasperPrint a bytes
                    byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

                    // Crear una instancia de Recibos_PDF y guardar el PDF en la base de datos
                    Recibos_PDF recibosPdf = new Recibos_PDF();
                    recibosPdf.setBoletin_PDF(pdfBytes);
                    recibosPdf.setAuditoria_id(auditoriaId);

                    boletinPDFService.guardarBoletinPDF(recibosPdf); // Guardar el boletín PDF en la base de datos
                } while (resultSet.next()); // Continúa procesando las filas restantes si las hay
            }
        } catch (SQLException | IOException | JRException e) {
            System.out.println(e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("mensaje");
    }


}
