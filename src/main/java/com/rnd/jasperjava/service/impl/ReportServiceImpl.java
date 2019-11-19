package com.rnd.jasperjava.service.impl;

import com.rnd.jasperjava.common.ApplicationConstant;
import com.rnd.jasperjava.exception.ReportException;
import com.rnd.jasperjava.service.ReportService;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public void generateReport(Map<String, Object> parameters) throws ReportException {
        validateParameter(parameters);
        try {
            String pathOutput = parameters.get(ApplicationConstant.PATH_OUTPUT).toString();
            parameters.remove(ApplicationConstant.PATH_OUTPUT);
            String reportPath = parameters.get(ApplicationConstant.REPORT_PATH).toString();
            parameters.remove(ApplicationConstant.REPORT_PATH);
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters, new JREmptyDataSource());
            exportToPdf(pathOutput, jasperPrint);
        } catch (JRException e) {
            LOGGER.error("JRException: ", e);
        } catch (Exception e) {
            throw new ReportException(e);
        }
    }

    private void exportToPdf(String pathOutput, JasperPrint jasperPrint) throws JRException {
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pathOutput));
        exporter.exportReport();
    }

    @Override
    public <E> JRBeanCollectionDataSource constructJavaBeans(Collection<E> collection) {
        return new JRBeanCollectionDataSource(collection);
    }

    private void validateParameter(Map<String, Object> parameters) throws ReportException {
        if (!parameters.containsKey(ApplicationConstant.REPORT_PATH)) {
            throw new ReportException("Report Template is empty");
        }
        if (!parameters.containsKey(ApplicationConstant.DATASOURCE)) {
            throw new ReportException("Data source is empty");
        }
        if (!parameters.containsKey(ApplicationConstant.PATH_OUTPUT)) {
            throw new ReportException("Path output is empty");
        }
    }
}
