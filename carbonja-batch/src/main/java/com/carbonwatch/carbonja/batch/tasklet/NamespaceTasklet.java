package com.carbonwatch.carbonja.batch.tasklet;

import com.carbonwatch.carbonja.batch.common.BaseTasklet;
import com.carbonwatch.carbonja.batch.common.ItemReader;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.util.List;

public class NamespaceTasklet extends BaseTasklet implements Tasklet {
    public NamespaceTasklet(ItemReader itemReader) {super(itemReader);}

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        String cluster; List<String> mzList; String dirPathMz;
        String directoryPath = "/NS/";

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
        String filter = ":filter(and(or(";
        String filterOne = "not(existsKey(pod_status))";
        String filterTwo = ",ne(pod_status,Terminating))";
        String filterThree = ",eq(pod_phase,Running)))";
        String split = ":splitBy(\"dt.entity.cloud_application_namespace\")";

        metVal = "kubernetes.workload.requests_cpu"; jsonMetricName = "_NS_workload.requests_cpu.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "kubernetes.workload.cpu_usage"; jsonMetricName = "_NS_workload.cpu_usage.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "kubernetes.workload.requests_memory"; jsonMetricName = "_NS_workload.requests_memory.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "kubernetes.workload.memory_working_set"; jsonMetricName = "_NS_workload.memory_working_set.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "kubernetes.pods"; jsonMetricName = "_NS_pods.json"; step = "NS_pods";
        prepaFormat(step, cluster, metVal, filter, filterOne, filterTwo, filterThree, split, metEndSum, jsonMetricName, mz, dirPathMz);
    }

}
