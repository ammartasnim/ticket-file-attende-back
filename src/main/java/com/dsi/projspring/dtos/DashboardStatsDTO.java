package com.dsi.projspring.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDTO {
    private long totalAgencies;
    private long totalAgents;
    private long todayTickets;
    private long waitingTickets;
    private long activeCounters;
}