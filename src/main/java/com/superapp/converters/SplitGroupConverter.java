package com.superapp.converters;


import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.user.UserBoundary;
import com.superapp.boundaries.split.GroupBoundary;
import com.superapp.boundaries.split.SplitGroupBoundary;
import com.superapp.data.split.SplitGroupEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SplitGroupConverter {
	private UserConverter userConvertor;

	public SplitGroupConverter() {
		UserConverter userConvertor = new UserConverter();

	}

	public SplitGroupEntity toEntity(SplitGroupBoundary group) {
		SplitGroupEntity rv = new SplitGroupEntity();
		rv.setGroupId(group.getGroup().getGroupId().getInternalObjectId());
		rv.setSplitTitle(group.getSplitTitle());
		rv.setExpenses(group.getExpenses());
		return rv;
	}

	public SplitGroupBoundary toBoundary(SplitGroupEntity group) {
		SplitGroupBoundary rv = new SplitGroupBoundary();
		rv.setGroup(convertToGroupBoundary(group));
		rv.setSplitTitle(group.getSplitTitle());
		rv.setExpenses(group.getExpenses());

		return rv;
	}

	public GroupBoundary convertToGroupBoundary(SplitGroupEntity group){
		ObjectIdBoundary groupId = new ObjectIdBoundary(group.getSuperapp(),group.getGroupId());
		UserBoundary leader = userConvertor.toBoundary(group.getGroupLeader());
		List<UserBoundary> allUsers = group.getAllUsers()
				.stream()
				.map(userConvertor::toBoundary)
				.collect(Collectors.toList());
		return new GroupBoundary(groupId,leader,allUsers,group.getAvatar());



	}



}
