package com.kt.smp.common.util;

import java.util.HashMap;
import java.util.Map;

public class PagingUtil {

	public static Map<String, Object> getPage(int page, int total, int pageSize) {
		Map<String, Object> pagination = new HashMap<String, Object>();

		int totalPage;
		int check = 0;
		int startPage;
		int endPage;
		if (total % pageSize == 0) {
			totalPage = total / pageSize;
		} else {
			totalPage = (total / pageSize) + 1;
		}

		if (page != 1) {
			check = (page - 1) / pageSize;
		}
		if (check > 0) {
			startPage = (check * pageSize) + 1;
		} else {
			startPage = 1;
		}

//		if (totalPage >= ((startPage - 1) + pageSize)) {
//			endPage = (startPage - 1) + pageSize;
//		} else 
		if (totalPage == 0) {
			endPage = 1;
			totalPage = 1;
		} else {
			endPage = totalPage;
		}

		pagination.put("startPage", String.valueOf(startPage));
		pagination.put("endPage", String.valueOf(endPage));
		pagination.put("currentPage", String.valueOf(page));
		pagination.put("totalPage", String.valueOf(totalPage));
		pagination.put("total", String.valueOf(total));

		return pagination;
	}

}
