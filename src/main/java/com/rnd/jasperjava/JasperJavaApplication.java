package com.rnd.jasperjava;

import com.rnd.jasperjava.common.ApplicationConstant;
import com.rnd.jasperjava.common.DataUtil;
import com.rnd.jasperjava.exception.ReportException;
import com.rnd.jasperjava.model.AdjustmentData;
import com.rnd.jasperjava.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class JasperJavaApplication implements CommandLineRunner {

    @Value("${report.output}")
    private String reportOutput;

    private ReportService reportService;

    @Autowired
    public JasperJavaApplication(ReportService reportService) {
        this.reportService = reportService;
    }

    public static void main(String[] args) {
        SpringApplication.run(JasperJavaApplication.class, args);
    }

    @Override
    public void run(String... args) throws ReportException {
        Map<String, Object> parameterMap = getParameterMap();
        reportService.generateReport(parameterMap);
    }

    private Map<String, Object> getParameterMap() {
        List<AdjustmentData> adjustmentDataList = DataUtil.getDataUtilList(5);
        String fileName = "bast.pdf";
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ApplicationConstant.PATH_OUTPUT, reportOutput + fileName);
        parameterMap.put(ApplicationConstant.DATASOURCE, reportService.constructJavaBeans(adjustmentDataList));
        parameterMap.putAll(DataUtil.constructBasicParameter("bast.jasper"));
        return parameterMap;
    }
}
