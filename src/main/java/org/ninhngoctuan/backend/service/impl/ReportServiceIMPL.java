package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.dto.ReportDTO;
import org.ninhngoctuan.backend.entity.ReportEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.ReportRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.ninhngoctuan.backend.service.ReportService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceIMPL implements ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public ReportServiceIMPL(ReportRepository reportRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean sendReport(ReportDTO reportDTO) {
        try {
            String authEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(authEmail).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
            UserEntity reportUser = userRepository.findByUserId(reportDTO.getUser().getUserId()).orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng được báo cáo"));
            ReportEntity reportEntity = new ReportEntity();
            reportEntity.setReason(reportDTO.getReason());
            reportEntity.setReportedBy(user);
            reportEntity.setUser(reportUser);
            reportEntity.setCreatedAt(Timestamp.from(Instant.now()));
            reportRepository.save(reportEntity);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<ReportDTO> getAllReports() {
        try {
            List<ReportEntity> reportEntities = reportRepository.findAll();
            List<ReportDTO> reportDTOS = new ArrayList<>();
            reportEntities.forEach(reportEntity -> reportDTOS.add(modelMapper.map(reportEntity, ReportDTO.class)));
            return reportDTOS;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
