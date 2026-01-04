package org.ateam.oncare.customer.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ateam.oncare.customer.query.dto.CustomerManageDTO;

import java.util.List;

@Mapper
public interface CustomerManageMapper {
    CustomerManageDTO.CustomerManageDetail selectCustomerManageDetail(Long beneficiaryId);

    Integer selectCareWorkerId(Long beneficiaryId);

    List<CustomerManageDTO.BeneficiaryListItem> selectBeneficiaryList(CustomerManageDTO.SearchCondition condition);

    long countBeneficiaryList(CustomerManageDTO.SearchCondition condition);

    CustomerManageDTO.CategoryCount selectCategoryCounts();

    CustomerManageDTO.CounselSummary selectLatestComplaint(Long beneficiaryId);

    CustomerManageDTO.CounselSummary selectLatestTermination(Long beneficiaryId);

    CustomerManageDTO.CounselSummary selectLatestRentalCounsel(Long beneficiaryId);
}
