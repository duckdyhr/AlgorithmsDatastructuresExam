package ex01_HandballElimination;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("InputHandballElimination.txt"));
		String input = "";
		while(scan.hasNextLine()){
			input += scan.nextLine();
		}
		System.out.println(input);
		ArrayList<String> teams = new ArrayList<>();
		String team = "";
		for(char c : input.toCharArray()){
			if(c == '*'){
				break;
			}
			if(c == '-'){
				teams.add(team);
				team = "";
			}else{
				team += c;				
			}
			
		}
		System.out.println("ArrayList:");
		for(String s : teams){
			System.out.println(s);
		}
		HandballFlowNetwork graph = new HandballFlowNetwork(teams);
		
	}
}
