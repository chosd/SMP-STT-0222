/**
 * 
 */
package com.kt.smp.stt.reprocess.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.kt.smp.stt.reprocess.enums.ReprocessStatus;

/**
* @FileName : ReprocessStatusHandler.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 12.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 : mybatis handler - ReprocessStatus을 Enum으로 매핑
*/
public class ReprocessStatusHandler<E extends Enum<E>> implements TypeHandler<ReprocessStatus>{
	private Class<E> type;
	
	public ReprocessStatusHandler(Class<E> type) {
		this.type = type;
	}
	
	
	@Override
	public void setParameter(PreparedStatement ps, int i, ReprocessStatus reprocessStatus, JdbcType jdbcType)
			throws SQLException {
		ps.setInt(i, reprocessStatus.getStatusCode());
	}

	@Override
	public ReprocessStatus getResult(ResultSet rs, String s) throws SQLException {
		int statusCode = rs.getInt(s);
		return getReprocessStatus(statusCode);
	}

	@Override
	public ReprocessStatus getResult(ResultSet rs, int i) throws SQLException {
		int statusCode = rs.getInt(i);
        return getReprocessStatus(statusCode);
	}

	@Override
	public ReprocessStatus getResult(CallableStatement cs, int i) throws SQLException {
        int statusCode = cs.getInt(i);
        return getReprocessStatus(statusCode);
	}
	
    private ReprocessStatus getReprocessStatus(int statusCode) {
        try {
            ReprocessStatus[] enumConstants = (ReprocessStatus[])type.getEnumConstants();
            for (ReprocessStatus status : enumConstants) {
                if (status.getStatusCode() == statusCode) {
                    return status;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new RuntimeException("Can't make enum object '" + type + "'", exception);
        }
    }

}
