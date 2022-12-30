package superapp.boundaries;

import superapp.util.wrappers.UserIdWrapper;

public class ExpensesBoundary {
	private UserIdWrapper user;
	private double amount;

	public ExpensesBoundary() {}

	public ExpensesBoundary(UserIdWrapper user,double amount) {
		this.user = user;
		this.amount= amount;
	}

	public double getAmount() { return amount; }

	public void setAmount(double amount) { this.amount = amount; }

	public UserIdWrapper getUser() {
		return user;
	}

	public void setUser(UserIdWrapper user) {
		this.user = user;
	}
}
