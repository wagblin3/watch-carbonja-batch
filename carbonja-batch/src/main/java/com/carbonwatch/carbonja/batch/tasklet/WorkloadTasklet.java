package com.carbonwatch.carbonja.batch.tasklet;

import com.carbonwatch.carbonja.batch.common.BaseTasklet;
import com.carbonwatch.carbonja.batch.common.ItemReader;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.util.List;

public class WorkloadTasklet  extends BaseTasklet implements Tasklet {
    public WorkloadTasklet(ItemReader itemReader) {super(itemReader);}

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        String cluster; List<String> mzList; String dirPathMz;
        String directoryPath = "/WL/";

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
        String metEndSum = ":sum:names";
        String splitA = ":splitBy(\"dt.entity.cloud_application\",\"dt.entity.cloud_application_namespace\",\"dt.entity.kubernetes_cluster\")";
        String splitB = ":parents:parents:splitBy(\"dt.entity.cloud_application\")";

        metVal = "kubernetes.workload.requests_cpu"; jsonMetricName = "_WL_requests_cpu.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", splitA, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "containers.cpu.usageMilliCores"; jsonMetricName = "_WL_cpu.usageMilliCores.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", splitB, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "kubernetes.workload.requests_memory"; jsonMetricName = "_WL_requests_memory.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", splitA, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "containers.memory.residentSetBytes"; jsonMetricName = "_WL_memory.residentSetBytes.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", splitB, metEndSum, jsonMetricName, mz, dirPathMz);
    }

}
