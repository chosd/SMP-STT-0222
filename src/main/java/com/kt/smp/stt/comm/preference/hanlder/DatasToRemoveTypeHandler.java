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

import com.kt.smp.stt.comm.preference.enums.DatasToRemove;

public class DatasToRemoveTypeHandler implements TypeHandler<DatasToRemove>{

	@Override
	public void setParameter(PreparedStatement ps, int i, DatasToRemove parameter, JdbcType jdbcType)
			throws SQLException {
		ps.setString(i, parameter.getDescription());
	}

	@Override
	public DatasToRemove getResult(ResultSet rs, String columnName) throws SQLException {
		String DatasToRemoveKey = rs.getString(columnName);
		return getData(DatasToRemoveKey);
	}

	@Override
	public DatasToRemove getResult(ResultSet rs, int columnIndex) throws SQLException {
		String DatasToRemoveKey = rs.getString(columnIndex);
		return getData(DatasToRemoveKey);
	}

	@Override
	public DatasToRemove getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String DatasToRemoveKey = cs.getString(columnIndex);
		return getData(DatasToRemoveKey);
	}
	
	private DatasToRemove getData(String DatasToRemoveKey) {
		DatasToRemove data = null;
		switch (DatasToRemoveKey) {
			case "REMOVER_DATA_AM_TRAIN_DATA":
				data = DatasToRemove.AM_TRAIN_DATA;
				break;
			case "REMOVER_DATA_LM_TRAIN_MNG":
				data = DatasToRemove.LM_TRAIN_DATA;
				break;
			case "REMOVER_DATA_TRAIN_MNG":
				data = DatasToRemove.TRAIN_MNG;
				break;
			case "REMOVER_DATA_VERIFY_DATA":
				data = DatasToRemove.VERIFY_DATA;
				break;
			case "REMOVER_DATA_VERIFY_MNG":
				data = DatasToRemove.VERIFY_MNG;
				break;
			case "REMOVER_DATA_DEPLOY_MODEL":
				data = DatasToRemove.DEPLOY_MODEL;
				break;
			case "REMOVER_DATA_DEPLOY_MNG":
				data = DatasToRemove.DEPLOY_MNG;
				break;
			case "REMOVER_DATA_CALLINFO":
				data = DatasToRemove.CALLINFO;
				break;
			default:
				throw new IllegalStateException("값이 올바르지 않습니다.");
		}
		
		return data;
	}
  
}
