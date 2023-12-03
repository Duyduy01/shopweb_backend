package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements Cloneable{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id",columnDefinition = "bigint",updatable = false)
  private Long id;
  private String userName;
  private String password;
  private String type;
  private String fullName;
  private Integer gender;
  private LocalDate birthday;
  private String avatar;
  private String email;
  private String phone;
  private String address;
  private String ward;
  private String district;
  private String city;
  private int status;
  private String userType;

  @Transient
  private boolean boolActive;

  @CreatedDate
  private LocalDateTime createdDate;

  private LocalDateTime modifiedDate;

  @CreatedBy
  private String createdBy;

  private String modifiedBy;

  @Override
  public UserEntity clone() {
    UserEntity user = null;
    try {
      user = (UserEntity) super.clone();
    } catch (CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return user;
  }

//  public String get2FaType(){
//    if (this.getApp2FaEnabled() == ON) return APP;
//    if (this.getSms2FaEnabled() == ON) return SMS;
//    if (this.getEmail2FaEnabled() == ON) return EMAIL;
//    return NONE;
//  }

}
