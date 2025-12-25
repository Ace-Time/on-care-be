package org.ateam.oncare.beneficiary.command.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CareLevelExpirationCommandMapper {

    /* 연장예정 체크 변경 */
    int updateExtendsStatus(@Param("expirationId") Integer expirationId,
                            @Param("extendsStatus") String extendsStatus);

    /* 안내유무 변경 */
    int updateOutboundStatus(@Param("expirationId") Integer expirationId,
                             @Param("outboundStatus") String outboundStatus);

    /* 안내완료처리(사용자입력) */
    int insertNotice(@Param("expirationId") Integer expirationId,
                     @Param("noticeDate") String noticeDate,
                     @Param("memo") String memo,
                     @Param("empId") Integer empId);

    /* 안내이력변경 (expirationId로 소속 검증) */
    int updateNotice(@Param("expirationId") Integer expirationId,
                     @Param("noticeId") Integer noticeId,
                     @Param("noticeDate") String noticeDate,
                     @Param("memo") String memo,
                     @Param("empId") Integer empId);

    /* 안내이력삭제 (expirationId로 소속 검증) */
    int deleteNotice(@Param("expirationId") Integer expirationId,
                     @Param("noticeId") Integer noticeId);
}