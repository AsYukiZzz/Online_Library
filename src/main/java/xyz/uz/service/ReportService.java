package xyz.uz.service;

import xyz.uz.domain.vo.PersonalDataOverviewVO;

/**
 * 报表 Service
 */

public interface ReportService {

    /**
     * 查看个人数据总览
     */
    PersonalDataOverviewVO getOverview(long id);
}
