package com.institucion.demo.Institucion.Util;

import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;

@Service
public class BoletinReportGenerator {

    public static void exportToPdf(Connection c, InputStream reportName, String nomSalida, Map param) throws JRException {
        JasperExportManager.exportReportToPdfFile(getReport(c, reportName, param), nomSalida);
    }

    public static JasperPrint getReport(Connection c, InputStream reportName, Map param) throws JRException {

        JasperPrint report = JasperFillManager.fillReport(JasperCompileManager.compileReport(reportName),
                param, c);

        return report;
    }

}
