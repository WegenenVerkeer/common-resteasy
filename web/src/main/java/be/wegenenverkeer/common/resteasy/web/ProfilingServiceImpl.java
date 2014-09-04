/*
 * This file is part of wegenenverkeer common-resteasy.
 * Copyright (c) AWV Agentschap Wegen en Verkeer, Vlaamse Gemeenschap
 * The program is available in open source according to the Apache License, Version 2.0.
 * For full licensing details, see LICENSE.txt in the project root.
 */

package be.wegenenverkeer.common.resteasy.web;

import be.eliwan.profiling.api.GroupData;
import be.eliwan.profiling.api.ProfilingData;
import be.eliwan.profiling.service.ProfilingContainer;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.Path;

/**
 * Service which allows gathering and clearing the profiling information.
 */
@Path("/rest/profile")
@Service
public class ProfilingServiceImpl implements ProfilingService {

    private static final String LINE_FORMAT = "%60s | %10d | %10d | %10.2f%n";
    private static final String GROUP = "                                                       group";
    private static final String LINE_HEADER = GROUP + " |      count | total time |   avg time\n";
    private static final String GROUPED_LINE_FORMAT = "%60s | %10d | %10d | %10.2f | %6.2f%n";
    private static final String GROUPED_LINE_HEADER = GROUP + " |      count | total time |   avg time | %total\n";

    @Autowired
    @Qualifier("restProfilingRegistrar")
    private ProfilingContainer restProfilingContainer;

    @Autowired
    @Qualifier("jdbcMethodProfiling")
    private ProfilingContainer jdbcMethodProfilingContainer;

    @Autowired
    @Qualifier("jdbcServiceProfiling")
    private ProfilingContainer jdbcServiceProfilingContainer;

    @Autowired
    @Qualifier("gatewayMethodProfiling")
    private ProfilingContainer gatewayMethodProfilingContainer;

    @Autowired
    @Qualifier("gatewayServiceProfiling")
    private ProfilingContainer gatewayServiceProfilingContainer;

    @Override
    public String profile(boolean clear) {
        StringBuilder sb = new StringBuilder();

        sb.append("++++ REST calls:\n");
        append(sb, restProfilingContainer);
        sb.append("\n++++ JDBC calls (by method):\n");
        append(sb, jdbcMethodProfilingContainer);
        sb.append("\n++++ JDBC calls (by REST service):\n");
        append(sb, jdbcServiceProfilingContainer);
        sb.append("\n++++ gateway calls (by method):\n");
        append(sb, gatewayMethodProfilingContainer);
        sb.append("\n++++ gateway calls (by REST service):\n");
        append(sb, gatewayServiceProfilingContainer);

        sb.append("\n++++ data grouped by REST call:\n");
        appendGroupedData(sb);

        if (clear) {
            restProfilingContainer.clear();
            jdbcMethodProfilingContainer.clear();
            jdbcServiceProfilingContainer.clear();
            gatewayMethodProfilingContainer.clear();
            gatewayServiceProfilingContainer.clear();
        }

        return sb.toString();
    }

    private void append(StringBuilder sb, ProfilingContainer profilingContainer) {
        ProfilingData total = profilingContainer.getTotal();
        sb.append(String.format(LINE_FORMAT, "",
                total.getInvocationCount(), total.getTotalRunTime(), total.getAverageRunTime()));
        sb.append(LINE_HEADER);
        for (GroupData groupData : profilingContainer.getGroupData()) {
            sb.append(String.format(LINE_FORMAT, groupData.getGroup(),
                    groupData.getInvocationCount(), groupData.getTotalRunTime(), groupData.getAverageRunTime()));
        }
        sb.append("\n");
    }

    private void appendGroupedData(StringBuilder sb) {
        Map<String, ProfilingGroupData> groups = new HashMap<String, ProfilingGroupData>();
        for (GroupData groupData : restProfilingContainer.getGroupData()) {
            groups.put(groupData.getGroup(),
                    new ProfilingGroupData(groupData.getInvocationCount(), groupData.getTotalRunTime()));
        }
        for (GroupData groupData : jdbcServiceProfilingContainer.getGroupData()) {
            ProfilingGroupData pgd = groups.get(groupData.getGroup());
            if (null != pgd) {
                pgd.setJdbcInvocationCount(groupData.getInvocationCount());
                pgd.setJdbcRuntime(groupData.getTotalRunTime());
            }
        }
        for (GroupData groupData : gatewayServiceProfilingContainer.getGroupData()) {
            ProfilingGroupData pgd = groups.get(groupData.getGroup());
            if (null != pgd) {
                pgd.setGatewayInvocationCount(groupData.getInvocationCount());
                pgd.setGatewayRuntime(groupData.getTotalRunTime());
            }
        }

        ProfilingGroupData totals = new ProfilingGroupData(restProfilingContainer.getTotal().getInvocationCount(),
                restProfilingContainer.getTotal().getTotalRunTime());
        totals.setJdbcInvocationCount(jdbcServiceProfilingContainer.getTotal().getInvocationCount());
        totals.setJdbcRuntime(jdbcServiceProfilingContainer.getTotal().getTotalRunTime());
        totals.setGatewayInvocationCount(gatewayServiceProfilingContainer.getTotal().getInvocationCount());
        totals.setGatewayRuntime(gatewayServiceProfilingContainer.getTotal().getTotalRunTime());
        groups.put("*", totals);

        sb.append(GROUPED_LINE_HEADER);
        for (Map.Entry<String, ProfilingGroupData> entry : groups.entrySet()) {
            ProfilingGroupData pgd = entry.getValue();
            groupLine(sb, entry.getKey(), pgd.getRestRuntime(), pgd.getRestInvocationCount(), pgd.getRestRuntime());
            groupLine(sb, "JDBC", pgd.getRestRuntime(), pgd.getJdbcInvocationCount(), pgd.getJdbcRuntime());
            groupLine(sb, "gateway", pgd.getRestRuntime(), pgd.getGatewayInvocationCount(), pgd.getGatewayRuntime());
            sb.append('\n');
        }
    }

    private void groupLine(StringBuilder sb, String label, long totalTime, long invocationCount, long runtime) {
        double average = 0.0;
        if (invocationCount > 0) {
            average = ((double) totalTime) / invocationCount;
        }
        double percentage = 0;
        if (totalTime > 0) {
            percentage = 100.0 * runtime / totalTime;
        }

        sb.append(String.format(GROUPED_LINE_FORMAT, label, invocationCount, runtime, average, percentage));
    }

    /**
     * Container for profiling data by group.
     */
    @Data
    private static final class ProfilingGroupData {
        private long restInvocationCount;
        private long restRuntime;
        private long jdbcInvocationCount;
        private long jdbcRuntime;
        private long gatewayInvocationCount;
        private long gatewayRuntime;

        /**
         * Constructor with filled in REST profiling data.
         *
         * @param restInvocationCount REST invocation count
         * @param restRuntime REST total run time
         */
        public ProfilingGroupData(long restInvocationCount, long restRuntime) {
            this.restInvocationCount = restInvocationCount;
            this.restRuntime = restRuntime;
        }
    }

}
