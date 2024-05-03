package com.carbonwatch.carbonja.batch.tasklet;

import com.carbonwatch.carbonja.batch.common.BaseTasklet;
import com.carbonwatch.carbonja.batch.common.ItemReader;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.util.List;

public class ServiceTasklet extends BaseTasklet implements Tasklet {
    public ServiceTasklet(ItemReader itemReader) {super(itemReader);}

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        String cluster; List<String> mzList; String dirPathMz;
        String directoryPath = "/SVS/";

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

    //////////////////////////////// getMetric : metric list to collect ////////////////////////////////////////////////////
    private void getMetric(String cluster, String mz, String dirPathMz) throws IOException {
        String step; String metVal; String jsonMetricName;
        String metEndSum = ":sum:names";
        String split = ":splitBy(\"dt.entity.service\")";

        metVal = "service.cpu.time"; jsonMetricName = "_SVS_cpu.time.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "service.requestCount.total"; jsonMetricName = "_SVS_requestCount.json"; step = "SVS_requestCount";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEndSum, jsonMetricName, mz, dirPathMz);
    }

}
