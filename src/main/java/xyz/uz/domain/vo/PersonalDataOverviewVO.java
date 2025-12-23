package xyz.uz.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalDataOverviewVO {

    /**
     * 累计借阅次数
     */
    private String borrowCount;

    /**
     * 按时归还次数
     */
    private String returnOnTimeCount;

    /**
     * 当前逾期本书
     */
    private String overdueCount;

    /**
     * 阅读分类偏好
     */
    private List<Map<String,Object>> categoryCount;
}
