package org.ninhngoctuan.backend.service.impl;

import org.ninhngoctuan.backend.Responses.DashboardResponses;
import org.ninhngoctuan.backend.repository.*;
import org.ninhngoctuan.backend.service.DashboardService;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceIMPL implements DashboardService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final LikeRepository likeRepository;
    private final GroupRepository groupRepository;

    public DashboardServiceIMPL(PostRepository postRepository, CommentRepository commentRepository, ReportRepository reportRepository, LikeRepository likeRepository, GroupRepository groupRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.reportRepository = reportRepository;
        this.likeRepository = likeRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public DashboardResponses getDashboard() {
        int totalPost = (int) postRepository.count();
        int totalComment = (int) commentRepository.count();
        int totalReport = (int) reportRepository.count();
        int totalLike = (int) likeRepository.count();
        int totalGroup = (int) groupRepository.count();
        DashboardResponses dashboard = new DashboardResponses();
        dashboard.setTotalComment(totalComment);
        dashboard.setTotalPost(totalPost);
        dashboard.setTotalReport(totalReport);
        dashboard.setTotalLike(totalLike);
        dashboard.setTotalGroup(totalGroup);
        return dashboard;
    }
}
