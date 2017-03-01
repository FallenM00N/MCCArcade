package models;

public class User implements Comparable<User>{

	private String initials = "AAA";
	private String score = "0000000000";
	
	public User() {
		
	}
	
	public User(String initials, String score) {
		setInitials(initials);
		setScore(score);
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public int compareTo(User user) {
		int diff = 0;
		diff = Integer.parseInt(getScore()) - Integer.parseInt(user.getScore());
		return diff;
	}
}
