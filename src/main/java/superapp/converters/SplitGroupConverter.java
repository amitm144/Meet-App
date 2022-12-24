package superapp.converters;


import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.split.GroupBoundary;
import superapp.data.group.SplitGroupEntity;

import java.util.List;
import java.util.stream.Collectors;

public class SplitGroupConverter {
	private UserConverter userConvertor;

	public SplitGroupConverter() {
		UserConverter userConvertor = new UserConverter();

	}

	public SplitGroupEntity toEntity(GroupBoundary group) {
		SplitGroupEntity rv = new SplitGroupEntity();
		rv.setGroupId(group.getGroupId().getInternalObjectId());
		rv.setSplitTitle(group.getSplitTitle());
		rv.setExpenses(group.getExpenses());
		return rv;
	}

	public GroupBoundary toBoundary(SplitGroupEntity group) {
		GroupBoundary rv = new GroupBoundary();
		rv.setGroup(convertToGroupBoundary(group));
		rv.setSplitTitle(group.getSplitTitle());
		rv.setExpenses(group.getExpenses());

		return rv;
	}

	public GroupBoundary convertToGroupBoundary(SplitGroupEntity group){
		SuperAppObjectIdBoundary groupId = new SuperAppObjectIdBoundary(group.getSuperapp(),group.getGroupId());
		UserBoundary leader = userConvertor.toBoundary(group.getGroupLeader());
		List<UserBoundary> allUsers = group.getAllUsers()
				.stream()
				.map(userConvertor::toBoundary)
				.collect(Collectors.toList());
		return new GroupBoundary(groupId,leader,allUsers,group.getAvatar());



	}



}
