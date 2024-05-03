package com.carbonwatch.carbonja.batch.tasklet;

import com.carbonwatch.carbonja.batch.common.BaseTasklet;
import com.carbonwatch.carbonja.batch.common.ItemReader;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.util.List;

public class ProcessTasklet extends BaseTasklet implements Tasklet {
    public ProcessTasklet(ItemReader itemReader) {super(itemReader);}

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        String cluster; List<String> mzList; String dirPathMz;
        String directoryPath = "/PROC/";

        cluster = "One"; mzList = getMz(cluster);
        // mzList.stream().forEach(mz -> prepaGetMetric(mz, directoryPath));
        for (String mz : mzList) { dirPathMz = prepaDirectory(mz, directoryPath);
            getMetric(cluster, mz, dirPathMz); }

//        cluster = "Two"; mzList = getMz(cluster);
//        // mzList.stream().forEach(mz -> prepaGetMetric(mz, directoryPath));
//        for (String mz : mzList) { dirPathMz = prepaDirectory(mz, directoryPath);
//                                    getMetric(cluster, mz, dirPathMz); }

        return RepeatStatus.FINISHED;
    }

    private void getMetric(String cluster, String mz, String dirPathMz) throws IOException {
        String step; String metVal; String jsonMetricName;
        String metEndParents = ":parents:names";
        String filter = ":filter(or(";
        String filterAOne = "in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),softwareTechnologies(~\"WEBSPHERE_LIBERTY~\")\"))";
        String filterATwo = ",in(\"dt.entity.process_group_instance\",entitySelector(\"type(~\"PROCESS_GROUP_INSTANCE~\"),softwareTechnologies(~\"TOMCAT~\")\"))))";
        String splitA = ":splitBy(\"dt.entity.process_group_instance\")";
        String filterBOne = "in(\"dt.entity.process_group\",entitySelector(\"type(~\"PROCESS_GROUP~\"),softwareTechnologies(~\"WEBSPHERE_LIBERTY~\")\"))";
        String filterBTwo = ",in(\"dt.entity.process_group\",entitySelector(\"type(~\"PROCESS_GROUP~\"),softwareTechnologies(~\"TOMCAT~\")\"))))";
        String splitB = ":splitBy(\"dt.entity.process_group\")";

        metVal = "tech.generic.cpu.usage"; jsonMetricName = "_PROC_cpu.usage.json"; step = "";
        prepaFormat(step, cluster, metVal, filter, filterAOne, filterATwo, "", splitA, metEndParents, jsonMetricName, mz, dirPathMz);

        metVal = "tech.generic.cpu.groupTotalTime"; jsonMetricName = "_PROC_cpu.groupTotalTime.json"; step = "PROC_cpu.groupTotalTime";
        prepaFormat(step, cluster, metVal, filter, filterBOne, filterBTwo, "", splitB, metEndParents, jsonMetricName, mz, dirPathMz);

        metVal = "tech.generic.mem.workingSetSize"; jsonMetricName = "_PROC_mem.workingSetSize.json"; step = "";
        prepaFormat(step, cluster, metVal, filter, filterAOne, filterATwo, "", splitA, metEndParents, jsonMetricName, mz, dirPathMz);

    }

}
