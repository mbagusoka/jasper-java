package com.rnd.jasperjava.controller;

import com.rnd.jasperjava.common.ApplicationConstant;
import com.rnd.jasperjava.common.ReportUtil;
import com.rnd.jasperjava.model.AdjustmentData;
import com.rnd.jasperjava.service.ReportService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ReportController {

    @Value("${report.output}")
    private String reportOutput;

    private ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(ApplicationConstant.REPORT_URL + "/{count}")
    public ResponseEntity<Resource> generateReport(@PathVariable int count) {
        try {
            Map<String, Object> parameterMap = getParameterMap(count);
            reportService.generateReport(parameterMap);
            return constructFileResponse(parameterMap);
        } catch (Exception e) {
            return ResponseEntity.of(Optional.empty());
        }
    }

    private ResponseEntity<Resource> constructFileResponse(Map<String, Object> parameterMap) {
        byte[] reportRaw = (byte[]) parameterMap.get(ApplicationConstant.RESULT);
        ByteArrayResource resource = new ByteArrayResource(reportRaw);
        HttpHeaders headers = new HttpHeaders();
        String fileNameContent = "attachment; filename=" + parameterMap.get(ApplicationConstant.FILE_NAME).toString();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, fileNameContent);
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    private Map<String, Object> getParameterMap(int count) {
        List<AdjustmentData> adjustmentDataList = ReportUtil.getDataUtilList(count);
        JRBeanCollectionDataSource dataSource = reportService.constructJavaBeans(adjustmentDataList);
        Map<String, Object> parameterMap = new HashMap<>(ReportUtil.constructBasicParameter("bst", dataSource, reportOutput));
        parameterMap.put(ApplicationConstant.BYTE_ARRAY, ApplicationConstant.YES);
        return parameterMap;
    }
}
