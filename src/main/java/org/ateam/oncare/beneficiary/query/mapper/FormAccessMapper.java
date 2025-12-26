package org.ateam.oncare.beneficiary.query.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.FormFileInfo;

@Mapper
public interface FormAccessMapper {

    /**
     * beneficiaryId 가 formId 를 볼 수 있으면 파일정보 반환, 아니면 null
     */
    FormFileInfo selectAllowedFormFileInfo(
            @Param("beneficiaryId") Long beneficiaryId,
            @Param("formId") Long formId
    );
}