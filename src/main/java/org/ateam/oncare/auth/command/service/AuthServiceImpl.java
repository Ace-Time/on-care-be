package org.ateam.oncare.auth.command.service;

import jakarta.servlet.ServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ateam.oncare.auth.command.dto.ResponseLoginEmployeeDTO;
import org.ateam.oncare.auth.command.dto.RequestLogin;
import org.ateam.oncare.auth.command.dto.ResponseToken;
import org.ateam.oncare.auth.command.mapper.EmployeeMapper;
import org.ateam.oncare.employee.command.service.EmployeeService;
import org.ateam.oncare.global.emun.MasterInternalType;
import org.ateam.oncare.global.eventType.MasterDataEvent;
import org.ateam.oncare.auth.security.JwtTokenProvider;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final EmployeeService employeeService;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider tokenProvider;
    private final EmployeeMapper employeeOfLoginMapper;
    private final TokenService tokenService;

    /**
     * modelMapper와 mapStruct 차이 비교를 위한 테스트 코드 추 후 삭제 예정
     * @param loginRequest
     * @return
     */
    @Override
    public ResponseLoginEmployeeDTO mapStructTest(RequestLogin loginRequest) {

        var employee = employeeService.getEmployee(loginRequest);

        Long s_modelMapper =  System.currentTimeMillis();
        ResponseLoginEmployeeDTO modelMapperOfEmployee = modelMapper.map(employee, ResponseLoginEmployeeDTO.class);
        Long useTime_ModelMapper =  System.currentTimeMillis() - s_modelMapper;
        log.debug("modelMapper 소요시간 :{}, data : {}",useTime_ModelMapper, modelMapperOfEmployee);

        Long s_mapStruct =  System.currentTimeMillis();
        ResponseLoginEmployeeDTO mapStructOfEmployee = employeeOfLoginMapper.toLoginDTO(employee);
        Long useTime_s_mapStruct =  System.currentTimeMillis() - s_mapStruct;
        log.debug("mapStruct 소요시간 :{}, data : {}",useTime_s_mapStruct,mapStructOfEmployee);

        return mapStructOfEmployee;
    }

    @Override
    public @Nullable ResponseToken refreshToken(String refreshToken, ServletRequest request) {
        ResponseToken responseToken =
                tokenService.verifyByRefreshToken(refreshToken,request);



        return null;
    }


    @Override
    public ResponseToken login(RequestLogin loginRequest, String clientIp) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUseremail(),
                        loginRequest.getPassword()
                )
        );

        ResponseToken responseToken =
                tokenService.generateToken(clientIp,authentication, loginRequest.getUseremail());

        return responseToken;
    }

    @Override
    @Cacheable(value="masterData" , key="'m_authorities'")
    public Map<Long, String> getAuthorities() {
        Map<Long, String> getAuthorities = employeeService.getAuthorityMasters();
        log.debug("getAuthorities: getAuthorities={}", getAuthorities);
        return getAuthorities;
    }

    /**
     * 
     * @param event : evnet 타입
     * 커밋이 완료된 후 m_authorities 삭제
     */
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @CacheEvict(value="masterData" , key="'m_authorities'")
    public void updateAuthorityEvent(MasterDataEvent event) {
        if(event.getType() == MasterInternalType.AUTHORITY) {
            log.debug("updateAuthorityEvent: eventType=AUTHORITY 실행");
        }
    }


}
