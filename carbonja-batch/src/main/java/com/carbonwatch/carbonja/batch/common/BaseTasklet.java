package com.carbonwatch.carbonja.batch.common;

import com.carbonwatch.carbonja.batch.config.ApplicationProps;
import com.carbonwatch.carbonja.batch.dto.dtoMetric.RootMetric;
import com.carbonwatch.carbonja.batch.dto.dtoProperties.RootProperties;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class BaseTasklet {
    ItemReader itemReader;
    public BaseTasklet (ItemReader itemReader){
        this.itemReader = itemReader;
    }
    @Autowired
    private ApplicationProps applicationProps;
    @Autowired
    ItemWriter itemWriter;

    public List<String> getMz(String cluster) throws Exception {
        if (cluster == "One") return applicationProps.getMzone(); else return applicationProps.getMztwo();
    }

    public String prepaDirectory(String mz, String directoryPath) throws IOException {
        String tmpPath = "tmp/";
        String dirRootPath = getDirRootPath();
        StringBuilder dirPathMzBuilder = new StringBuilder();
        String dirPathMz = dirPathMzBuilder.append(tmpPath).append(dirRootPath).append(directoryPath).append(mz).toString();
        String dirPathMzClean = dirPathMz.replace(" ", "").replace(":", "");

        ///////////////////////////        A CONSERVER POUR UN USAGE DANS REPERTOIRE LOCAL        //////////////////////
        Path dirMzWithoutSpace = Paths.get(dirPathMzClean); Files.createDirectories((dirMzWithoutSpace)); ////
        ///////////////////////////        A CONSERVER POUR UN USAGE DANS REPERTOIRE LOCAL        //////////////////////
        return dirPathMzClean;
    }
    private String getDirRootPath() {
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date roundedDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        String dateStr = dateFormat.format(roundedDate);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
        String timeStr = timeFormat.format(roundedDate);
        String dirRootPath = String.format("%s_%s-%sh_%s_%s", applicationProps.getName(), dateStr, timeStr, applicationProps.getFrom(), applicationProps.getTo());
        return dirRootPath;
    }

    public void prepaFormat(String step, String cluster, String metVal, String filter, String filterOne, String filterTwo, String filterThree, String split, String metEnd, String jsonMetricName, String mz, String dirPathMz) throws IOException {
        String metric; StringBuilder metricBuilder = new StringBuilder(); String builtin="builtin:";
        String mzNameSelector; StringBuilder mzBuilder = new StringBuilder(); String mzBegin="mzName(\""; String mzEnd="\")";
        String jsonMetricPathName; StringBuilder jsonBuilder = new StringBuilder(); String slash="/";

        metric = metricBuilder.append(builtin).append(metVal)
                .append(filter).append(filterOne).append(filterTwo).append(filterThree)
                .append(split).append(metEnd).toString();
        mzNameSelector = mzBuilder.append(mzBegin).append(mz).append(mzEnd).toString();
        jsonMetricPathName = jsonBuilder.append(dirPathMz).append(slash).append(mz).append(jsonMetricName).toString();

        readWrite(step, cluster, metric, mz, mzNameSelector, jsonMetricPathName, dirPathMz);
    }
    private void readWrite(String step, String cluster, String metric, String mz, String mzNameSelector, String jsonMetricPathName, String dirPathMz) throws IOException {
        RootMetric metricObject; String slash="/";
        String entityOne;String entityTwo; String propertieFieldsOne;String propertieFieldsTwo;
        RootProperties propertiesObjectOne;
        List<RootProperties> propertiesObjectListOne = new ArrayList<>();
        List<RootProperties> propertiesObjectListTwo = new ArrayList<>();
        String jsonPropertiesNameOne; String jsonPropertiesNameTwo;
        String jsonPropertiesPathNameOne; String jsonPropertiesPathNameTwo;

        metricObject = itemReader.readMetric(cluster, metric, mzNameSelector);
        itemWriter.writeMetric(jsonMetricPathName, metricObject);

        if ( Objects.equals(step, "HOST_cpu.usage") ) {
            jsonPropertiesNameOne = "_HOST_properties.json";;
            jsonPropertiesPathNameOne = getJsonPropertiesPathName(mz, dirPathMz, slash, jsonPropertiesNameOne);
            for (int n = 0; n < metricObject.getTotalCount(); n++) {
                entityOne = metricObject.getResult().get(0).getData().get(n).getDimensionMap().getDtEntityHost();
                propertieFieldsOne="firstSeenTms,properties.cpuCores,properties.logicalCpuCores," +
                        "properties.physicalMemory,properties.memoryTotal,properties.osVersion,properties.osType," +
                        "properties.additionalSystemInfo,properties.customHostMetadata,tags";
                addProperties(cluster, entityOne, propertieFieldsOne, propertiesObjectListOne);
            }
            itemWriter.writeProperties(jsonPropertiesPathNameOne, propertiesObjectListOne);
        }

        if ( Objects.equals(step, "NS_pods") ) {
            jsonPropertiesNameOne = "_NS_properties.json";
            jsonPropertiesNameTwo = "_K8S_properties.json";
            jsonPropertiesPathNameOne = getJsonPropertiesPathName(mz, dirPathMz, slash, jsonPropertiesNameOne);
            jsonPropertiesPathNameTwo = getJsonPropertiesPathName(mz, dirPathMz, slash, jsonPropertiesNameTwo);
            for (int n=0; n < metricObject.getTotalCount(); n++) {
                entityOne = metricObject.getResult().get(0).getData().get(n).getDimensionMap().getDtEntityCloudApplicationNamespace();
                propertieFieldsOne="firstSeenTms,toRelationships.isClusterOfNamespace";
                propertiesObjectOne = addProperties(cluster, entityOne, propertieFieldsOne, propertiesObjectListOne);
                entityTwo = propertiesObjectOne.getToRelationships().getIsClusterOfNamespace().get(0).getId();
                propertieFieldsTwo="firstSeenTms,managementZones,fromRelationships.isClusterOfHost";
                addProperties(cluster, entityTwo, propertieFieldsTwo, propertiesObjectListTwo);
            }
            itemWriter.writeProperties(jsonPropertiesPathNameOne, propertiesObjectListOne);
            itemWriter.writeProperties(jsonPropertiesPathNameTwo, propertiesObjectListTwo);
        }

        if ( Objects.equals(step, "PROC_cpu.groupTotalTime") ) {
            jsonPropertiesNameOne = "_PROC_properties.json";
            jsonPropertiesPathNameOne = getJsonPropertiesPathName(mz, dirPathMz, slash, jsonPropertiesNameOne);
            for (int n=0; n < metricObject.getTotalCount(); n++) {
                entityOne = metricObject.getResult().get(0).getData().get(n).getDimensionMap().getDtEntityProcessGroup();
                propertieFieldsOne="firstSeenTms,toRelationships.isInstanceOf";
                addProperties(cluster, entityOne, propertieFieldsOne, propertiesObjectListOne);
            }
            itemWriter.writeProperties(jsonPropertiesPathNameOne, propertiesObjectListOne);
        }

        if ( Objects.equals(step, "SVS_requestCount") ) {
            jsonPropertiesNameOne = "_SVS_properties.json";
            jsonPropertiesPathNameOne = getJsonPropertiesPathName(mz, dirPathMz, slash, jsonPropertiesNameOne);
            for (int n=0; n < metricObject.getTotalCount(); n++) {
                entityOne = metricObject.getResult().get(0).getData().get(n).getDimensionMap().getDtEntityService();
                propertieFieldsOne="firstSeenTms,fromRelationships.runsOn,fromRelationships.isServiceOf";
                addProperties(cluster, entityOne, propertieFieldsOne, propertiesObjectListOne);
            }
            itemWriter.writeProperties(jsonPropertiesPathNameOne, propertiesObjectListOne);
        }

    }
    private static String getJsonPropertiesPathName(String mz, String dirPathMz, String slash, String jsonPropertiesNameOne) {
        String jsonPropertiesPathNameOne;
        StringBuilder jsonBuilder;
        jsonBuilder = new StringBuilder();
        jsonPropertiesPathNameOne = jsonBuilder.append(dirPathMz).append(slash).append(mz).append(jsonPropertiesNameOne).toString();
        return jsonPropertiesPathNameOne;
    }
    private RootProperties addProperties(String cluster, String entityOne, String propertieFieldsOne, List<RootProperties> propertiesObjectListOne) {
        RootProperties propertiesObjectOne;
        propertiesObjectOne = itemReader.readProperties(cluster, entityOne, propertieFieldsOne);
        propertiesObjectListOne.add(propertiesObjectOne);
        return propertiesObjectOne;
    }

}
