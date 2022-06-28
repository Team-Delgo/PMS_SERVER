package com.pms.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int adminId;
    private String name;
    private String email;
    private String password;
    private String phoneNo;
    @CreationTimestamp
    private LocalDate registDt;
//    @JsonIgnore
//    private String roles;  // 권한

//    // ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력된 -> 그걸 파싱!!
//    @JsonIgnore
//    public List<String> getRoleList() {
//        if (this.roles.length() > 0) {
//            return Arrays.asList(this.roles.split(","));
//        }
//        return new ArrayList<>();
//    }
}
