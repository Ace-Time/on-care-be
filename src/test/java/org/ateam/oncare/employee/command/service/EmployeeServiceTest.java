package org.ateam.oncare.employee.command.service;

import org.ateam.oncare.employee.command.dto.EmployeeRequestDTO;
import org.ateam.oncare.employee.command.entity.Employee;
import org.ateam.oncare.employee.command.repository.EmployeeCareerCommandRepository;
import org.ateam.oncare.employee.command.repository.EmployeeCommandRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private org.ateam.oncare.employee.command.repository.EmployeeRepository employeeRepository;
    @Mock
    private org.ateam.oncare.employee.command.repository.EmployeeCommandRepository employeeCommandRepository;
    @Mock
    private org.ateam.oncare.employee.command.repository.EmployeeCareerCommandRepository employeeCareerCommandRepository;
    @Mock
    private org.ateam.oncare.employee.command.repository.AuthorityRepository authorityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private org.modelmapper.ModelMapper modelMapper;
    @Mock
    private org.springframework.context.ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    @DisplayName("신규 직원 등록 테스트")
    void testRegisterEmployee() {
        // Given
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setName("홍길동");
        dto.setEmail("hong@test.com");
        dto.setPhone("010-0000-0000");

        when(passwordEncoder.encode(any())).thenReturn("encodedPw");

        Employee savedEmployee = new Employee();
        savedEmployee.setId(10);
        when(employeeCommandRepository.save(any(Employee.class))).thenReturn(savedEmployee);

        // When
        Integer resultId = employeeService.registerEmployee(dto);

        // Then
        assertEquals(10, resultId);
        verify(employeeCommandRepository, times(1)).save(any(Employee.class));
    }

    @Test
    @DisplayName("직원 정보 수정 테스트")
    void testUpdateEmployee() {
        // Given
        Integer empId = 10;
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setName("홍길동(수정)");
        dto.setPhone("010-9999-9999");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(empId);
        existingEmployee.setName("홍길동");

        when(employeeCommandRepository.findById(empId)).thenReturn(Optional.of(existingEmployee));

        // When
        employeeService.updateEmployee(empId, dto);

        // Then
        assertEquals("홍길동(수정)", existingEmployee.getName());
        verify(employeeCareerCommandRepository, times(1)).deleteAllByEmployeeId(empId);
    }

    @Test
    @DisplayName("직원 조회 테스트 (로그인 시)")
    void testGetEmployee() {
        // Given
        org.ateam.oncare.auth.command.dto.RequestLogin loginRequest = new org.ateam.oncare.auth.command.dto.RequestLogin();
        loginRequest.setUseremail("test@email.com");

        Employee mockEmployee = new Employee();
        mockEmployee.setName("test@email.com");

        org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO responseDTO = new org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO();
        responseDTO.setName("홍길동");

        when(employeeRepository.findByEmail("test@email.com")).thenReturn(Optional.of(mockEmployee));
        when(modelMapper.map(mockEmployee, org.ateam.oncare.employee.command.dto.ResponseLoginEmployeeDTO.class))
                .thenReturn(responseDTO);

        // When
        var result = employeeService.getEmployee(loginRequest);

        // Then
        assertEquals("홍길동", result.getName());
    }

    @Test
    @DisplayName("권한 정보 수정 및 이벤트 발행 테스트")
    void testUpdateAuthority() {
        // Given
        org.ateam.oncare.employee.command.dto.RequestAuthorityDTO requestDTO = new org.ateam.oncare.employee.command.dto.RequestAuthorityDTO(
                1L, "TEST_AUTH");
        org.ateam.oncare.employee.command.entity.Authority authority = new org.ateam.oncare.employee.command.entity.Authority();

        when(modelMapper.map(requestDTO, org.ateam.oncare.employee.command.entity.Authority.class))
                .thenReturn(authority);
        when(authorityRepository.save(authority)).thenReturn(authority);

        // When
        employeeService.updateAuthority(requestDTO);

        // Then
        verify(authorityRepository, times(1)).save(authority);
        verify(applicationEventPublisher, times(1))
                .publishEvent(any(org.ateam.oncare.global.eventType.MasterDataEvent.class));
    }

    @Test
    @DisplayName("비밀번호 변경 시 현재 비밀번호 불일치 예외 테스트")
    void testUpdateEmployee_WrongPassword() {
        // Given
        Integer empId = 11;
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setCurrentPassword("wrongPw");
        dto.setNewPassword("newPw");

        Employee existingEmployee = new Employee();
        existingEmployee.setId(empId);
        existingEmployee.setPw("encodedRealPw");

        when(employeeCommandRepository.findById(empId)).thenReturn(Optional.of(existingEmployee));
        when(passwordEncoder.matches("wrongPw", "encodedRealPw")).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> employeeService.updateEmployee(empId, dto));
    }
}
