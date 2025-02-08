package org.ninhngoctuan.backend.controller;

import org.ninhngoctuan.backend.dto.ReportDTO;
import org.ninhngoctuan.backend.service.ReportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllReports() {
        try {
            List<ReportDTO>  reportDTOList = reportService.getAllReports();
            return new ResponseEntity<>(reportDTOList, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PostMapping("/send")
    public ResponseEntity<?> sendReport(@RequestBody ReportDTO reportDTO) {
        try {
           boolean report =  reportService.sendReport(reportDTO);
            return new ResponseEntity<>(report,HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
