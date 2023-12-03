package com.clothes.websitequanao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "img_child")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ImgEntity extends BaseEnity{

  private String img;
  private Long productId;

}
