package com.rnd.jasperjava;

import com.rnd.jasperjava.common.ApplicationConstant;
import com.rnd.jasperjava.common.ReportUtil;
import com.rnd.jasperjava.exception.ReportException;
import com.rnd.jasperjava.model.AdjustmentData;
import com.rnd.jasperjava.service.ReportService;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class JasperJavaApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(JasperJavaApplication.class);

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
        if (isRunningViaCmd(args)) {
            LOGGER.info("Run via command line");
            Map<String, Object> parameterMap = getParameterMap();
            reportService.generateReport(parameterMap);
            LOGGER.info("Report generated successfully via command line");
            System.exit(0);
        }
    }

    private boolean isRunningViaCmd(String[] args) {
        return args.length > 0 && ApplicationConstant.CMD.equalsIgnoreCase(args[0]);
    }

    private Map<String, Object> getParameterMap() {
        List<AdjustmentData> adjustmentDataList = ReportUtil.getDataUtilList(5);
        JRBeanCollectionDataSource dataSource = reportService.constructJavaBeans(adjustmentDataList);
        return ReportUtil.constructBasicParameter("bst", dataSource, reportOutput);
    }
}
