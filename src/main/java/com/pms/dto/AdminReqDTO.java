package com.pms.dto;

import com.pms.domain.Admin;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminReqDTO {
    @NotBlank private String name;
    @NotBlank private String email;
    @NotBlank private String password;
    @NotBlank private String phoneNo;

    public Admin toEntity() {
        return Admin.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .phoneNo(this.phoneNo)
                .build();
    }
}
