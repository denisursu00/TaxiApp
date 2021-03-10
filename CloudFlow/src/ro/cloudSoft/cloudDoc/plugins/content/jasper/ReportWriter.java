package ro.cloudSoft.cloudDoc.plugins.content.jasper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * clasa ce inglobeaza metodele necesare pentru a exporta un raport in format PDF
 */
public class ReportWriter {

    /**
     * fisierul JRXML creat cu iReport
     */
    private InputStream jrxml;
    /**
     * parametri raportului
     */
    private Map<String, Object> parameters;
    /**
     * sursa de date de unde vor fi luate valorile
     */
    private JRDataSource dataSource;

    /**
     * aceste 2 variabile sunt intermediare
     */
    private JasperReport report;
    private JasperPrint print;

    public ReportWriter(InputStream jrxml, Map<String, Object> parameters, JRDataSource dataSource) {
        this.jrxml = jrxml;
        this.parameters = parameters;
        this.dataSource = dataSource;
    }

    /**
     * Pregateste raportul pentru export.
     * @throws net.sf.jasperreports.engine.JRException
     */
    private void prepare() throws JRException {
        this.report = JasperCompileManager.compileReport(new BufferedInputStream(this.jrxml));
        this.print = JasperFillManager.fillReport(this.report, this.parameters, this.dataSource);
    }

    /**
     * Exporta raportul ca un PDF, sub forma unui sir de caractere.
     * @return un sir de bytes ce reprezinta continutul PDF
     * @throws net.sf.jasperreports.engine.JRException
     */
    public byte[] writePdfToBytes() throws JRException {
        prepare();
        return JasperExportManager.exportReportToPdf(this.print);
    }

    /**
     * Exporta raportul ca un PDF, sub forma unui fisier.
     * @param path calea fisierului
     * (absoluta, sau doar numele fisierului - daca se doreste in directorul curent)
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void writePdfToFile(String path) throws JRException {
        prepare();
        JasperExportManager.exportReportToPdfFile(this.print, path);
    }

    /**
     * Exporta raportul ca un PDF, sub forma unui stream.
     * Acel stream poate fi un fisier sau output-ul unui servlet.
     * @param out stream-ul destinatie
     * @throws net.sf.jasperreports.engine.JRException
     */
    public void writePdfToStream(OutputStream out) throws JRException {
        prepare();
        JasperExportManager.exportReportToPdfStream(this.print, new BufferedOutputStream(out));
    }
}