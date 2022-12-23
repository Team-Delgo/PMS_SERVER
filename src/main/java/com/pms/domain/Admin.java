package com.pms.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
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


    public Admin removePassword(){
        this.password = "";

        return this;
    }

    public Admin changePassword(String password){
        this.password = password;

        return this;
    }

    public Admin formatPhoneNo(){
        this.phoneNo = phoneNo.replaceAll("[^0-9]", "");

        return this;
    }

}
