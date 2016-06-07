package ex03_GettingGold;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("Input2GettingGold.txt"));
		String[] dimensions = scan.nextLine().split(" ");
		char[][] board = new char[Integer.parseInt(dimensions[0])][Integer.parseInt(dimensions[1])];
		
		String line = "";
		int index = 0;
		while(scan.hasNextLine()){
			line = scan.nextLine();
			//System.out.println(line);
			board[index++] = line.toCharArray();
		}
		scan.close();
		GettingGold gg = new GettingGold(board);
		System.out.println("Gold collected safely: " + gg.gettingGold());
	}
}
