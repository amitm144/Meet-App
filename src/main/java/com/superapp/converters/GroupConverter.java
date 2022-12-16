package com.superapp.converters;

import com.superapp.boundaries.object.ObjectIdBoundary;
import com.superapp.boundaries.user.UserBoundary;
import com.superapp.boundaries.split.GroupBoundary;
import com.superapp.data.GroupEntity;

import java.util.List;
import java.util.stream.Collectors;

public class GroupConverter {
	private UserConverter userConvertor;

	public GroupConverter() {
		UserConverter userConvertor = new UserConverter();
	}

	public GroupEntity toEntity(GroupBoundary group) {

		GroupEntity rv = new GroupEntity();
		rv.setGroupId(group.getGroupId().getInternalObjectId());
		rv.setAvatar(group.getAvatar());
		List<UserBoundary> allUsers = group.getAllUsers();
		rv.setAllUsers(allUsers
				.stream()
				.map(userConvertor::toEntity)
				.collect(Collectors.toList()));

		rv.setGroupLeader(userConvertor.toEntity(group.getGroupLeader()));
		rv.setAvatar(group.getAvatar());


		return rv;
	}


	public GroupBoundary toBoundary(GroupEntity group) {

		GroupBoundary rv = new GroupBoundary();
		rv.setAllUsers(group.getAllUsers().stream()
				.map(userConvertor::toBoundary)
				.collect(Collectors.toList()));

		ObjectIdBoundary OIdB = new ObjectIdBoundary(group.getSuperapp(),group.getGroupId());

		rv.setGroupId(OIdB);
		rv.setGroupLeader(userConvertor.toBoundary(group.getGroupLeader()));
		rv.setAvatar(group.getAvatar());
		return rv;
	}












}
