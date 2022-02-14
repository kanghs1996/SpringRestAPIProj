package com.kosmo.rest.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//모델에 대한 정보 기술
@ApiModel(value = "회원 정보", description = "아이디, 비밀번호, 이름,가입일 속성을 가진 DTO계열 클래스")
public class MemberDTO {
	//모델 속성 정보
	@ApiModelProperty(value = "아이디")
	private String username;
	@ApiModelProperty(value = "비밀번호")
	private String password;
	@ApiModelProperty(value = "이름")
	private String name;
	@ApiModelProperty(value = "가입일")
	private java.sql.Date postdate;
}
