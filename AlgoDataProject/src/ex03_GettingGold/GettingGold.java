package ex03_GettingGold;

public class GettingGold {

	private char[][] board;
	private boolean[][] visited;

	public GettingGold(char[][] board) {
		this.board = board;
		printBoard();

		// initialiserer alle felter til false
		visited = new boolean[board.length][board[0].length];
	}

	public int gettingGold() {
		int[] startP = startPosition();

		System.out.println("Start position: " + startP[0] + ", " + startP[1]);

		return gettingGold(startP[0], startP[1], 0);
	}

	private int gettingGold(int i, int j, int gold) {
		visited[i][j] = true;
		char current = board[i][j];
		if (current == 'G') {
			gold = gold + 1;
		}
		// udforsker kun udfra felter som er sikre, dvs ikke i nær
		if (current != '#' && current != 'T' && isSafe(i, j)) {
			if (i > 1 && !visited[i - 1][j]) {
				gold = gettingGold(i - 1, j, gold);
			}
			if (i < board.length - 2 && !visited[i + 1][j]) {
				gold = gettingGold(i + 1, j, gold);
			}
			if (j > 1 && !visited[i][j - 1]) {
				gold = gettingGold(i, j - 1, gold);
			}
			if (j < board[0].length - 2 && !visited[i][j + 1]) {
				gold = gettingGold(i, j + 1, gold);
			}
		}
		return gold;
	}

	private boolean isSafe(int i, int j) {
		if (board[i - 1][j] == 'T') {
			return false;
		}
		if (board[i + 1][j] == 'T') {
			return false;
		}
		if (board[i][j - 1] == 'T') {
			return false;
		}
		if (board[i][j + 1] == 'T') {
			return false;
		}
		return true;
	}

	private int[] startPosition() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 'P') {
					return new int[] { i, j };
				}
			}
		}
		return null;
	}

	private void printBoard() {
		for (int i = 0; i < board.length; i++) {
			String line = "";
			for (int j = 0; j < board[i].length; j++) {
				line += board[i][j];
			}
			System.out.println(line);
		}
	}
}
