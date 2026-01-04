package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationDetailResponse;
import org.ateam.oncare.beneficiary.query.dto.response.CareLevelExpirationListResponse;
import org.ateam.oncare.beneficiary.query.dto.response.NoticeExpirationListResponse;

import java.util.List;

@Mapper
public interface CareLevelExpirationQueryMapper {

    /* 전체조회 (section + extendsStatus optional) */
    List<CareLevelExpirationListResponse.Item> selectExpirationList(
            @Param("section") Integer section,
            @Param("extendsStatus") String extendsStatus
    );

    /* 상세조회 */
    CareLevelExpirationDetailResponse selectExpirationDetail(
            @Param("expirationId") Integer expirationId
    );

    /* 안내이력조회 */
    List<NoticeExpirationListResponse.Item> selectNoticeList(
            @Param("expirationId") Integer expirationId
    );
}
