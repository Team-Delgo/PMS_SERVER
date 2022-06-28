package com.pms.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pms.domain.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String phoneNo;

    public Admin getAdmin() {
        return Admin.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .phoneNo(this.phoneNo)
                .build();
    }
}
