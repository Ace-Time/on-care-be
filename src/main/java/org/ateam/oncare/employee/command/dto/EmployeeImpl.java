package org.ateam.oncare.employee.command.dto;

import lombok.Getter;
import lombok.ToString;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@ToString
@Getter
public class EmployeeImpl extends User {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private String jobName;

    public EmployeeImpl(String username, @Nullable String password,
            Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public void setDetails(Long id, String email, String name, String phone, String jobName) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.jobName = jobName;
    }
}
