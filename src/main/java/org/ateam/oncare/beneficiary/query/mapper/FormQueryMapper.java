package org.ateam.oncare.beneficiary.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ateam.oncare.beneficiary.query.dto.FormListItem;

import java.util.List;

@Mapper
public interface FormQueryMapper {

    List<FormListItem> selectFormsForBeneficiary(@Param("beneficiaryId") Long beneficiaryId);
}