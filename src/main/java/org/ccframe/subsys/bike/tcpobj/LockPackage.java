package org.ccframe.subsys.bike.tcpobj;

import java.util.HashMap;
import java.util.Map;

import org.ccframe.subsys.bike.domain.code.BykeTypeCodeEnum;

/**
 * 智能锁通讯包协议：
 * 
 * 起始标志单元:协议类型:协议版本号:单车设备号:单车类别:命令单元:数据单元:CRC 校验单元:结束标志单元
 * 
 * 本协议对象为去掉起始结尾标识和CRC校验部分。
 * 
 * @author JIM
 */
public class LockPackage {

	private byte type;
	
	private byte version = 0x01;
	
	private long lockId;
	
	private BykeTypeCodeEnum bykeTypeCodeEnum;
	
	private CommandFlagEnum CommandFlagEnum;
	
	private AnswerFlagEnum answerFlagEnum;
	
	private Map<DataBlockTypeEnum, Object> dataBlockMap;

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getVersion() {
		return version;
	}

	public void setVersion(byte version) {
		this.version = version;
	}

	public long getLockId() {
		return lockId;
	}

	public void setLockId(long lockId) {
		this.lockId = lockId;
	}

	public BykeTypeCodeEnum getBykeTypeCodeEnum() {
		return bykeTypeCodeEnum;
	}

	public void setBykeTypeEnum(BykeTypeCodeEnum bykeTypeCodeEnum) {
		this.bykeTypeCodeEnum = bykeTypeCodeEnum;
	}

	public CommandFlagEnum getCommandFlagEnum() {
		return CommandFlagEnum;
	}

	public void setCommandFlagEnum(CommandFlagEnum commandFlagEnum) {
		CommandFlagEnum = commandFlagEnum;
	}

	public AnswerFlagEnum getAnswerFlagEnum() {
		return answerFlagEnum;
	}

	public void setAnswerFlagEnum(AnswerFlagEnum answerFlagEnum) {
		this.answerFlagEnum = answerFlagEnum;
	}

	public Map<DataBlockTypeEnum, Object> getDataBlockMap() {
		return dataBlockMap;
	}

	public void setDataBlockMap(Map<DataBlockTypeEnum, Object> dataBlockMap) {
		this.dataBlockMap = dataBlockMap;
	}

}