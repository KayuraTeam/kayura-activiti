package org.kayura.activiti.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.kayura.activiti.dao.IdentityMapper;
import org.kayura.activiti.expression.AssignmenteExpr;
import org.kayura.activiti.service.ActivitiService;
import org.kayura.activiti.vo.AssignItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivitiServiceImpl implements ActivitiService {

	@Autowired
	private IdentityMapper identityMapper;

	@Autowired
	private AssignmenteExpr assignmenteExpr;

	@Override
	public List<AssignItemVo> loadAssignUsersByIds(List<String> ids) {

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("ids", ids);
		List<User> list = identityMapper.loadUsers(args);

		List<String> idList = new ArrayList<String>(ids);
		List<AssignItemVo> items = new ArrayList<AssignItemVo>();
		for (User u : list) {
			items.add(new AssignItemVo(u.getId(), u.getLastName() + "（" + u.getFirstName() + "）", "U"));
			idList.remove(u.getId());
		}

		for (String id : idList) {
			if (assignmenteExpr.exists(id)) {
				items.add(new AssignItemVo(id, assignmenteExpr.get(id), "T"));
				idList.remove(id);
			}
		}

		for (String id : idList) {
			items.add(new AssignItemVo(id, id, "T"));
		}

		return items;
	}

	@Override
	public List<AssignItemVo> loadAssignGroupsByIds(List<String> ids) {

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("ids", ids);
		List<Group> list = identityMapper.loadGroups(args);

		List<String> idList = new ArrayList<String>(ids);
		List<AssignItemVo> items = new ArrayList<AssignItemVo>();
		for (Group g : list) {
			items.add(new AssignItemVo(g.getId(), g.getName(), g.getType()));
			idList.remove(g.getId());
		}

		for (String id : idList) {
			if (assignmenteExpr.exists(id)) {
				items.add(new AssignItemVo(id, assignmenteExpr.get(id), "T"));
				idList.remove(id);
			}
		}

		for (String id : idList) {
			items.add(new AssignItemVo(id, id, "T"));
		}

		return items;
	}

}
