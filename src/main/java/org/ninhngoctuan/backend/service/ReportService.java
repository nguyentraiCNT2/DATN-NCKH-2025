package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.ReportDTO;

import java.util.List;

public interface ReportService {
    boolean sendReport(ReportDTO reportDTO);
    List<ReportDTO> getAllReports();

}
