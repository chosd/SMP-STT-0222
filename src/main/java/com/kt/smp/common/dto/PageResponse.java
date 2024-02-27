package com.kt.smp.common.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(value = AccessLevel.PROTECTED)
public class PageResponse {
	protected int pageNum;
	protected int pageSize;
	protected long total;
	protected int pages;
}
