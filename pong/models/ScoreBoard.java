package models;

import game.PongEngine;
import javafx.scene.Group;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class ScoreBoard {
	
	private int small = 15;
	private int big = 50;
	private static final int HEIGHT = 160;

	public static int getHeight() {
		return HEIGHT;
	}
	
	public Group getScoreBoard() {
		Group scoreBoard = new Group();
		scoreBoard.getChildren().add(getPlayer1Score(2));
		scoreBoard.getChildren().add(getDivider());
		scoreBoard.getChildren().add(getPlayer2Score(2));
		scoreBoard.getChildren().add(getHR());
		return scoreBoard;

	}

	public Group getScoreDisplay(int score) {
		Group rects = new Group();
		switch (score) {
		case 0:
			rects = getRects(1, 2, 3, 5, 6, 7);
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

	private Group getRects(int... rectsID) {
		Group rects = new Group();
		for (int i = 0; i < rectsID.length; i++) {
			Rectangle rect = new Rectangle();
			switch (rectsID[i]) {
			case 1:
				rect = new Rectangle(0, 0, big, small);
				break;
			case 2:
				rect = new Rectangle(0, 0, small, big);
				break;
			case 3:
				rect = new Rectangle(big - small, 0, small, big);
				break;
			case 4:
				rect = new Rectangle(0, big, big, small);
				break;
			case 5:
				rect = new Rectangle(0, big, small, big);
				break;
			case 6:
				rect = new Rectangle(big - small, big, small, big);
				break;
			case 7:
				rect = new Rectangle(0, big + big, big, small);
				break;
			}
			rect.setFill(Paint.valueOf("#fff"));
			rects.getChildren().add(rect);
		}
		return rects;
	}

	public Group getPlayer1Score(int score) {
		Group player1Score = getScoreDisplay(score);
		player1Score.setLayoutX(PongEngine.getWidth() / 2 - 80);
		player1Score.setLayoutY(20);
		return player1Score;
	}

	public Group getPlayer2Score(int score) {
		Group player2Score = getScoreDisplay(score);
		player2Score.setLayoutX(PongEngine.getWidth() / 2 + 30);
		player2Score.setLayoutY(20);
		return player2Score;
	}

	public Rectangle getDivider() {
		Rectangle divider = new Rectangle(25, 15);
		divider.setFill(Paint.valueOf("#fff"));
		divider.setX(PongEngine.getWidth() / 2 - 15);
		divider.setY(70);
		return divider;
	}

	private Rectangle getHR() {
		Rectangle line = new Rectangle(0, HEIGHT, PongEngine.getWidth(), 10);
		line.setFill(Paint.valueOf("#fff"));
		return line;
	}

}
