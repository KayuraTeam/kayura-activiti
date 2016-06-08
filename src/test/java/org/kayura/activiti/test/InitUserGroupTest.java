package org.kayura.activiti.test;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;

public class InitUserGroupTest extends AbstractTest {

	@Test
	public void initUserGroupTest() {

		// deptLeader
		Group deptLeader = this.identityService.newGroup("deptLeader");
		deptLeader.setName("部门领导");
		deptLeader.setType("candidate");
		identityService.saveGroup(deptLeader);

		// hr
		Group hr = this.identityService.newGroup("hr");
		hr.setName("人事部领导");
		hr.setType("candidate");
		identityService.saveGroup(hr);

		User admin = this.identityService.newUser("admin");
		admin.setFirstName("xia");
		admin.setLastName("liang");
		admin.setEmail("admin@kayura.org");
		identityService.saveUser(admin);

		identityService.createMembership("admin", "deptLeader");
		identityService.createMembership("admin", "hr");

	}

}
