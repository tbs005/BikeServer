package org.ccframe.subsys.bike.socket.controller;

import java.util.Map;

import org.ccframe.subsys.bike.socket.commons.ISocketController;
import org.ccframe.subsys.bike.socket.tcpobj.CommandFlagEnum;
import org.ccframe.subsys.bike.socket.tcpobj.DataBlockTypeEnum;
import org.springframework.stereotype.Component;

@Component
public class MacConnectController implements ISocketController {

	@Override
	public CommandFlagEnum getCommand() {
		return CommandFlagEnum.MAC_INFO;
	}

	@Override
	public Map<DataBlockTypeEnum, Object> execute(long lockerHardwareCode, Map<DataBlockTypeEnum, Object> requestDataMap) {
		//TODO 保存锁的状态和电量
		System.out.println("电量="+requestDataMap.get(DataBlockTypeEnum.LOCK_BATTERY));
		System.out.println("MAC="+requestDataMap.get(DataBlockTypeEnum.LOCK_MAC));
		System.out.println("时间="+requestDataMap.get(DataBlockTypeEnum.SYS_TIME));
		System.out.println("GPS="+requestDataMap.get(DataBlockTypeEnum.GPS_INFO));
		//长连接事件，数据原封不动丢回去完事
		return requestDataMap;
	}

}