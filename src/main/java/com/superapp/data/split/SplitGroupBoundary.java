package com.superapp.data.split;

import com.superapp.data.Group.GroupBoundary;

import java.util.ArrayList;

public class SplitGroupBoundary {

	private GroupBoundary group ;
	private ArrayList<SplitTransaction> expenses;
	private String SplitTitle;


	public SplitGroupBoundary() {
	}

	public SplitGroupBoundary(GroupBoundary group, ArrayList<SplitTransaction> expenses, String splitTitle) {
		this.group = group;
		this.expenses = expenses;
		SplitTitle = splitTitle;
	}

	public GroupBoundary getGroup() {
		return group;
	}

	public void setGroup(GroupBoundary group) {
		this.group = group;
	}

	public ArrayList<SplitTransaction> getExpenses() {
		return expenses;
	}

	public void setExpenses(ArrayList<SplitTransaction> expenses) {
		this.expenses = expenses;
	}

	public String getSplitTitle() {
		return SplitTitle;
	}

	public void setSplitTitle(String splitTitle) {
		SplitTitle = splitTitle;
	}








}
