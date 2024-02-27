/**
 * 
 */
package com.kt.smp.stt.callinfo.handler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.kt.smp.stt.callinfo.enums.CallStatus;

/**
* @FileName : CallStatusHandler.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 12.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 : mybatis handler - CallStatus을 Enum으로 매핑
*/
public class CallStatusHandler<E extends Enum<E>> implements TypeHandler<CallStatus>{
	private Class<E> type;
	
	public CallStatusHandler(Class<E> type) {
		this.type = type;
	}
	
	
	@Override
	public void setParameter(PreparedStatement ps, int i, CallStatus callStatus, JdbcType jdbcType)
			throws SQLException {
		ps.setInt(i, callStatus.getStatusCode());
	}

	@Override
	public CallStatus getResult(ResultSet rs, String s) throws SQLException {
		int statusCode = rs.getInt(s);
		return getCallStatus(statusCode);
	}

	@Override
	public CallStatus getResult(ResultSet rs, int i) throws SQLException {
		int statusCode = rs.getInt(i);
        return getCallStatus(statusCode);
	}

	@Override
	public CallStatus getResult(CallableStatement cs, int i) throws SQLException {
        int statusCode = cs.getInt(i);
        return getCallStatus(statusCode);
	}
	
    private CallStatus getCallStatus(int statusCode) {
        try {
            CallStatus[] enumConstants = (CallStatus[])type.getEnumConstants();
            for (CallStatus status : enumConstants) {
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
