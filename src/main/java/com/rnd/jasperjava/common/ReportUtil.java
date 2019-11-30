package com.rnd.jasperjava.common;

import com.rnd.jasperjava.model.AdjustmentData;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportUtil {

    private ReportUtil() {}

    public static List<AdjustmentData> getDataUtilList(int count) {
        List<AdjustmentData> adjustmentDataList = new ArrayList<>();
        AdjustmentData adjustmentData;
        for (int i = 0; i < count; i++) {
            adjustmentData = constructAdjustmentData(i);
            adjustmentDataList.add(adjustmentData);
        }
        return adjustmentDataList;
    }

    private static AdjustmentData constructAdjustmentData(int count) {
        AdjustmentData adjustmentData = new AdjustmentData();
        adjustmentData.setContainerId("1111690" + count);
        adjustmentData.setProductionDate("01 Jan 2019");
        adjustmentData.setSku("SI-909010101" + count);
        adjustmentData.setBatch("1110S" + count);
        adjustmentData.setQuantity(80);
        return adjustmentData;
    }

    public static Map<String, Object> constructBasicParameter(String reportName,
                                                              JRBeanCollectionDataSource dataSource,
                                                              String reportOutput) {
        String fileName = constructBasicFileName(reportName);
        String templateName = reportName + ".jasper";
        String pathReport = getReportPath(templateName);
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ApplicationConstant.PATH_OUTPUT, reportOutput + fileName);
        parameterMap.put(ApplicationConstant.DATASOURCE, dataSource);
        parameterMap.put(ApplicationConstant.SOURCE_LOC, "K2SP - FT. 2");
        parameterMap.put(ApplicationConstant.DESTINATION_LOC, "KWW9 - Warehouse");
        parameterMap.put(ApplicationConstant.DOC_NO, "123456789");
        parameterMap.put(ApplicationConstant.DOC_DATE, "23-10-2019");
        parameterMap.put(ApplicationConstant.USER, "Ina");
        parameterMap.put(ApplicationConstant.ACTIVITY, "THP");
        parameterMap.put(ApplicationConstant.REPORT_PATH, pathReport);
        parameterMap.put(ApplicationConstant.FILE_NAME, fileName);
        return parameterMap;
    }

    private static String constructBasicFileName(String reportName) {
        return String.format("%s_%s.pdf", reportName, new Timestamp(System.currentTimeMillis()));
    }

    private static String getReportPath(String templateName) {
        try {
            Resource resource = new ClassPathResource(templateName);
            return Paths.get(resource.getURI()).toString();
        } catch (IOException e) {
            return "";
        }
    }
}
