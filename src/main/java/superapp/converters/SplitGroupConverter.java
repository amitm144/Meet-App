package superapp.converters;


import superapp.boundaries.object.SuperAppObjectIdBoundary;
import superapp.boundaries.user.UserBoundary;
import superapp.boundaries.split.GroupBoundary;
import superapp.data.split.SplitGroup;

import java.util.List;
import java.util.stream.Collectors;

public class SplitGroupConverter {
	private UserConverter userConvertor;

	public SplitGroupConverter() {
		UserConverter userConvertor = new UserConverter();

	}

	public SplitGroup toEntity(GroupBoundary group) {
		SplitGroup rv = new SplitGroup();
		rv.setGroupId(group.getGroupId().getInternalObjectId());
		rv.setSplitTitle(group.getSplitTitle());
		rv.setExpenses(group.getExpenses());
		return rv;
	}

	public GroupBoundary toBoundary(SplitGroup group) {
		GroupBoundary rv = new GroupBoundary();
		rv.setGroup(convertToGroupBoundary(group));
		rv.setSplitTitle(group.getSplitTitle());
		rv.setExpenses(group.getExpenses());

		return rv;
	}

	public GroupBoundary convertToGroupBoundary(SplitGroup group){
		SuperAppObjectIdBoundary groupId = new SuperAppObjectIdBoundary(group.getSuperapp(),group.getGroupId());
		UserBoundary leader = userConvertor.toBoundary(group.getGroupLeader());
		List<UserBoundary> allUsers = group.getAllUsers()
				.stream()
				.map(userConvertor::toBoundary)
				.collect(Collectors.toList());
		return new GroupBoundary(groupId,leader,allUsers,group.getAvatar());



	}



}
