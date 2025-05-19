package com.idolu.user.global.common;

import lombok.Getter;

@Getter
public class DetailErrorCodeResponse {

	private String errorCode;

	public DetailErrorCodeResponse(String errorCode) {
		this.errorCode = errorCode;
	}

	public static DetailErrorCodeResponse from(String detailCode) {
		return new DetailErrorCodeResponse(detailCode);
	}
}
