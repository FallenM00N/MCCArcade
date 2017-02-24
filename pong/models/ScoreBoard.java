package models;

import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ScoreBoard {
	private static Rectangle divider;
	private int height =  20;
	private int width = 100;
	
	public Group getScoreBoard() {
		Group scoreBoard = new Group();
		scoreBoard.getChildren().add(getPlayer1Score(3));
		return scoreBoard;
		
	}
	
	public Group getScoreDisplay(int score) {
		Group rects = new Group();
		switch (score) {
		case 0: 
			rects = getRects(1, 2, 3, 4, 5, 6, 7);
			break;
		case 1: 
			rects = getRects(3, 6);
			break;
		case 2:
			rects = getRects(1, 3, 4, 5, 7);
			break;
		case 3:
			rects = getRects(1, 3, 4, 6, 7);
			break;
		case 4:
			rects = getRects(2, 3, 4, 6);
			break;
		case 5:
			rects = getRects(1, 2, 4, 6, 7);
			break;
		case 6:
			rects = getRects(1, 2, 4, 5, 6, 7);
			break;
		case 7:
			rects = getRects(1, 3, 6);
			break;
		case 8:
			rects = getRects(1, 2, 3, 4, 5, 6, 7);
			break;
		case 9:
			rects = getRects(1, 2, 3, 4, 6, 7);
		}
		return rects;
	}
	
	private Group getRects(int...rectsID) {
		Group rects = new Group();
		for(int i = 0; i < rectsID.length; i++) {
			Rectangle rect = new Rectangle();
			switch(rectsID[i]) {
			case 1:
				rect = new Rectangle(width, height, 0, 0);
				break;
			case 2:
				rect = new Rectangle(height, width, 0, 0);
				break;
			case 3:
				rect = new Rectangle(height, width, width - height, 0);
				break;
			case 4:
				rect = new Rectangle(width, height, 0, width - height);
				break;
			case 5:
				rect = new Rectangle(width, height, 0, width);
				break;
			case 6:
				rect = new Rectangle(width, height, width - height, width);
				break;
			case 7:
				rect = new Rectangle(width, height, width - height, 0);
				break;
			}
			rect.setFill(Paint.valueOf("#fff"));
			rects.getChildren().add(rect);
		}
		return rects;
	}

	public Group getPlayer1Score(int score) {
		Group player1Score = getScoreDisplay(score);
		player1Score.setLayoutX(300);
		return player1Score;
	}
	
	public Group getPlayer2Score() {
		return null;
	}
	
	public Rectangle getDivider() {
		return divider;
	}
	public void setDivider(Rectangle divider) {
		this.divider = divider;
	}
	
	
}
