package ex01_HandballElimination;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("InputHandballElimination.txt"));
		while(scan.hasNextLine()){
			System.out.println(scan.nextLine());
		}
	}
}
