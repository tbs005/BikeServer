package org.ccframe.subsys.bike.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ccframe.client.components.LabelValue;
import org.ccframe.commons.base.BaseService;
import org.ccframe.commons.helper.SpringContextHelper;
import org.ccframe.subsys.bike.domain.entity.Agent;
import org.ccframe.subsys.bike.domain.entity.BikeType;
import org.ccframe.subsys.bike.repository.AgentRepository;
import org.ccframe.subsys.core.domain.code.BoolCodeEnum;
import org.ccframe.subsys.core.domain.code.UserStatCodeEnum;
import org.ccframe.subsys.core.domain.entity.Role;
import org.ccframe.subsys.core.domain.entity.User;
import org.ccframe.subsys.core.domain.entity.UserRoleRel;
import org.ccframe.subsys.core.service.ILabelValueListProvider;
import org.ccframe.subsys.core.service.RoleService;
import org.ccframe.subsys.core.service.UserRoleRelService;
import org.ccframe.subsys.core.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentService extends BaseService<Agent,java.lang.Integer, AgentRepository> implements ILabelValueListProvider {

	@Override
	@Transactional(readOnly = true)
	public List<LabelValue> getLabelValueList(String beanName, String extraParam) {
		List<LabelValue> resultList = new ArrayList<LabelValue>();
		for(Agent agent: ((AgentService)SpringContextHelper.getBean(this.getClass())).listAll()){ //需要用spring容器方法以利用缓存
			resultList.add(new LabelValue(agent.getAgentNm(), agent.getAgentId()));
		}
		return resultList;
	}
	
	/**
	 * 
		任务描述
		1）新增运营商的时候要新增一个管理员，管理员登录ID要考虑重复的情况
		2）新增运营商要添加对应的默认单车类型
		3）新增运营商的管理员要有运营商的菜单（看AXURE）
	 * @param agent
	 */
	@Transactional
	public void saveOrUpdateAgent(Agent agent) {
		agent.setIfDelete(BoolCodeEnum.NO.toCode());
		agent.setRoleId(2); // TODO 先设管理员 后期再修改
		Agent saveAgent = SpringContextHelper.getBean(this.getClass()).save(agent);
		
		Role role = new Role();
		role.setRoleNm("系统管理员");
		role.setIfTemplate("N");
		role.setOrgId(agent.getAgentId());
		Role saveRole = SpringContextHelper.getBean(RoleService.class).save(role);
		
		agent.setRoleId(saveRole.getRoleId());
		SpringContextHelper.getBean(this.getClass()).save(agent);
		
		User user = new User();
		// 登陆ID的规则：  机构ID + 00X
		user.setLoginId(getLoginId(saveAgent.getAgentId() + "001"));
		user.setUserNm(saveAgent.getAgentNm() + "管理员");
		user.setUserPsw("ee26b0dd4af7e749aa1a8ee3c10ae9923f618980772e473f8819a5d4940e0db27ac185f8a0e1d5f84f88bc887fd67b143732c304cc5fa9ad8e6f57f50028a8ff");
		user.setIfAdmin("Y");
		user.setCreateDate(new Date());
		user.setUserStatCode(UserStatCodeEnum.NORMAL.toCode());
		User saveUser = SpringContextHelper.getBean(UserService.class).save(user );
		
		UserRoleRel userRoleRel = new UserRoleRel();
		userRoleRel.setUserId(saveUser.getUserId());
		userRoleRel.setRoleId(2);
		SpringContextHelper.getBean(UserRoleRelService.class).save(userRoleRel);
		UserRoleRel userRoleRel2 = new UserRoleRel();
		userRoleRel2.setUserId(saveUser.getUserId());
		userRoleRel2.setRoleId(role.getRoleId());
		SpringContextHelper.getBean(UserRoleRelService.class).save(userRoleRel2);
		
		BikeType bikeType = new BikeType();
		bikeType.setOrgId(saveAgent.getAgentId());
		bikeType.setBikeTypeNm("标准单车");
		bikeType.setHalfhourAmmount(1.0);
		SpringContextHelper.getBean(BikeTypeService.class).save(bikeType);
	}
	
	public String getLoginId(String loginId){
		List<User> findByKey = SpringContextHelper.getBean(UserService.class).findByKey(User.LOGIN_ID, loginId);
		if (findByKey.size() == 0) {
			return loginId;
		} else {
			loginId = Integer.parseInt(loginId)+ 1 + "";
			return getLoginId(loginId);
		}
	}
}
