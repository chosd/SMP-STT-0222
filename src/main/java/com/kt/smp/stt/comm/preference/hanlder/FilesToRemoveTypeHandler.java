/**
 * 
 */
package com.kt.smp.stt.comm.preference.hanlder;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.kt.smp.stt.comm.preference.enums.FilesToRemove;

public class FilesToRemoveTypeHandler implements TypeHandler<FilesToRemove>{

	@Override
	public void setParameter(PreparedStatement ps, int i, FilesToRemove parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.getDescription());
	}

	@Override
	public FilesToRemove getResult(ResultSet rs, String columnName) throws SQLException {
		String filesToRemoveKey = rs.getString(columnName);
		return getFile(filesToRemoveKey);
	}

	@Override
	public FilesToRemove getResult(ResultSet rs, int columnIndex) throws SQLException {
		String filesToRemoveKey = rs.getString(columnIndex);
		return getFile(filesToRemoveKey);
	}

	@Override
	public FilesToRemove getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String filesToRemoveKey = cs.getString(columnIndex);
		return getFile(filesToRemoveKey);
	}
	
	private FilesToRemove getFile(String filesToRemoveKey) {
		FilesToRemove file = null;
		switch (filesToRemoveKey) {
			case "REMOVER_FILE_AM_TRAIN_DATA":
				file = FilesToRemove.AM_TRAIN_DATA;
				break;
			case "REMOVER_FILE_DEPLOY_MODEL":
				file = FilesToRemove.DEPLOY_MODEL;
				break;
			case "REMOVER_FILE_VERIFY_DATA":
				file = FilesToRemove.VERIFY_DATA;
				break;
			case "REMOVER_FILE_CALLINFO":
				file = FilesToRemove.CALL_INFO;
				break;
			case "REMOVER_FILE_TEST":
				file = FilesToRemove.TEST;
				break;
			default:
				throw new IllegalStateException("값이 올바르지 않습니다.");
		}
		
		return file;
	}
  
}
