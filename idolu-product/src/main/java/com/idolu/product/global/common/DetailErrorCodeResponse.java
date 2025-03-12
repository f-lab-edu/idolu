package com.idolu.product.global.common;

import lombok.Getter;

@Getter
public class DetailErrorCodeResponse {

	private String detailCode;

	public DetailErrorCodeResponse(String detailCode) {
		this.detailCode = detailCode;
	}

	public static DetailErrorCodeResponse from(String detailCode) {
		return new DetailErrorCodeResponse(detailCode);
	}
}
