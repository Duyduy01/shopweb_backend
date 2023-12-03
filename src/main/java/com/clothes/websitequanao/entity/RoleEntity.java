package com.clothes.websitequanao.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "role")
@NoArgsConstructor
public class RoleEntity extends BaseEnity{

  private String roleName;
  private String roleCode;

}
