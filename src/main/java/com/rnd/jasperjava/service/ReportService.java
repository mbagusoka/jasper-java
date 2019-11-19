package com.rnd.jasperjava.service;

import com.rnd.jasperjava.exception.ReportException;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public interface ReportService {

    void generateReport(Map<String, Object> parameters) throws ReportException;

    <E> JRBeanCollectionDataSource constructJavaBeans(Collection<E> collection);
}
