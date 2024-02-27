package com.kt.smp.common.mapper;

import com.kt.smp.common.domain.AttachVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @title  첨부 매퍼
 * @author Brian
 * @since  2022. 01. 29.
 * @see
 * <pre>
 * </pre>
 */
@Mapper
public interface AttachMapper {
	
	/**
	 * @title 첨부시퀀스를 가져온다.
	 * @return String
	 */
	String getAtSeq();
	
	/**
	 * @title 첨부 리스트
	 * @param vo
	 * @return List<AttachVO>
	 */
	List<AttachVO> list(AttachVO vo);
	
	/**
	 * @title 상세
	 * @param vo
	 * @return AttachVO
	 */
	AttachVO detail(AttachVO vo);
	
	/**
	 * @title 등록
	 * @param vo
	 * @return int
	 */
	int insert(AttachVO vo);
	
	/**
	 * @title 수정
	 * @param vo
	 * @return int
	 */
	int update(AttachVO vo);
	
	/**
	 * @title 삭제
	 * @param vo
	 * @return int
	 */
	int delete(AttachVO vo);

}
