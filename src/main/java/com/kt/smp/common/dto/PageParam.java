
package com.kt.smp.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageParam {

	protected int page = 1;

	protected int pageSize = 10;
	
	protected int offset = 0;

	protected String orderBy;

	protected boolean doNotPaging = false;
	
	public int calOffset() {
		offset = (page - 1) * pageSize;

		return offset;
	}

}
