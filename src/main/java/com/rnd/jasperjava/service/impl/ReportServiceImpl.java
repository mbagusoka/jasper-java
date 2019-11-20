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

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

    @Override
    public void generateReport(Map<String, Object> parameters) throws ReportException {
        validateParameter(parameters);
        try {
            JasperPrint jasperPrint = getReportPrint(parameters);
            if (isOutputByteArray(parameters)) {
                exportToByteArray(parameters, jasperPrint);
            } else {
                exportToPdf(parameters, jasperPrint);
            }
        } catch (JRException e) {
            LOGGER.error("JRException: ", e);
        } catch (Exception e) {
            throw new ReportException(e);
        }
    }

    private boolean isOutputByteArray(Map<String, Object> parameters) {
        return parameters.containsKey(ApplicationConstant.BYTE_ARRAY);
    }

    private JasperPrint getReportPrint(Map<String, Object> parameters) throws JRException {
        String reportPath = parameters.get(ApplicationConstant.REPORT_PATH).toString();
        parameters.remove(ApplicationConstant.REPORT_PATH);
        return JasperFillManager.fillReport(reportPath, parameters, new JREmptyDataSource());
    }

    private void exportToByteArray(Map<String, Object> parameters, JasperPrint jasperPrint) throws JRException {
        parameters.remove(ApplicationConstant.BYTE_ARRAY);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        exporter.exportReport();
        parameters.put(ApplicationConstant.RESULT, outputStream.toByteArray());
    }

    private void exportToPdf(Map<String, Object> parameters, JasperPrint jasperPrint) throws JRException {
        String pathOutput = parameters.get(ApplicationConstant.PATH_OUTPUT).toString();
        parameters.remove(ApplicationConstant.PATH_OUTPUT);
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
    }
}
