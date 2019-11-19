package com.rnd.jasperjava.common;

import com.rnd.jasperjava.model.AdjustmentData;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataUtil {

    private DataUtil() {}

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

    public static Map<String, Object> constructBasicParameter(String templateName) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put(ApplicationConstant.SOURCE_LOC, "K2SP - FT. 2");
        parameterMap.put(ApplicationConstant.DESTINATION_LOC, "KWW9 - Warehouse");
        parameterMap.put(ApplicationConstant.DOC_NO, "123456789");
        parameterMap.put(ApplicationConstant.DOC_DATE, "23-10-2019");
        parameterMap.put(ApplicationConstant.USER, "Ina");
        parameterMap.put(ApplicationConstant.ACTIVITY, "THP");
        parameterMap.put(ApplicationConstant.REPORT_PATH, getReportPath(templateName));
        parameterMap.put(ApplicationConstant.ADJUSTMENT_CODE, "XXX123XXXSD4J");
        return parameterMap;
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
