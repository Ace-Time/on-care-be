package org.ateam.oncare.auth.command.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLogin {
    private String useremail;
    private String password;
}
