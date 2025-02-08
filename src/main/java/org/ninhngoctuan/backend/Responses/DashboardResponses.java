package org.ninhngoctuan.backend.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardResponses {
    private int totalPost;
    private int totalComment;
    private int totalLike;
    private int totalReport;
    private int totalGroup;
}
