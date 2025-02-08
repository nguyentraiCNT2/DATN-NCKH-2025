package org.ninhngoctuan.backend.controller.Admin;

import org.ninhngoctuan.backend.Responses.DashboardResponses;
import org.ninhngoctuan.backend.dto.GroupDTO;
import org.ninhngoctuan.backend.service.DashboardService;
import org.ninhngoctuan.backend.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {
    private final DashboardService dashboardService;
    private final GroupService groupService;

    public AdminDashboardController(DashboardService dashboardService, GroupService groupService) {
        this.dashboardService = dashboardService;
        this.groupService = groupService;
    }

    @GetMapping("/total")
    public ResponseEntity<?> dashboard() {
        try {
            DashboardResponses dashboardResponses = dashboardService.getDashboard();
            return ResponseEntity.ok(dashboardResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/group/getall")
    public ResponseEntity<?> getGroupsByUserId() {
        try {
            List<GroupDTO> groupDTOList = groupService.getAdminAllGroup();
            return ResponseEntity.ok(groupDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
