package models;

public class SimonUser {

	private String initials;
	private String score;
	
	public SimonUser() {
		
	}
	
	public SimonUser(String initials, String score) {
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
	
}
