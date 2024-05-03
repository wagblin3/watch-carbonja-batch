package com.carbonwatch.carbonja.batch.tasklet;

import com.carbonwatch.carbonja.batch.common.BaseTasklet;
import com.carbonwatch.carbonja.batch.common.ItemReader;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.io.IOException;
import java.util.List;

public class HostTasklet extends BaseTasklet implements Tasklet {
    public HostTasklet(ItemReader itemReader) {super(itemReader);}

    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        String cluster; List<String> mzList; String dirPathMz;
        String directoryPath = "/HOST/";

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
        String metEnd = ":names";
        String metEndSum = ":sum:names";
        String split = ":splitBy(\"dt.entity.host\")";

        metVal = "host.net.bytesTx"; jsonMetricName = "_HOST_net.bytesTx.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEnd, jsonMetricName, mz, dirPathMz);

        metVal = "host.net.bytesRx"; jsonMetricName = "_HOST_net.bytesRx.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEnd, jsonMetricName, mz, dirPathMz);

        metVal = "host.mem.usage"; jsonMetricName = "_HOST_mem.usage.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEnd, jsonMetricName, mz, dirPathMz);

        metVal = "host.disk.used"; jsonMetricName = "_HOST_disk.used.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "host.disk.avail"; jsonMetricName = "_HOST_disk.avail.json"; step = "";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEndSum, jsonMetricName, mz, dirPathMz);

        metVal = "host.cpu.usage"; jsonMetricName = "_HOST_cpu.usage.json"; step = "HOST_cpu.usage";
        prepaFormat(step, cluster, metVal, "", "", "", "", split, metEnd, jsonMetricName, mz, dirPathMz);
    }

}
