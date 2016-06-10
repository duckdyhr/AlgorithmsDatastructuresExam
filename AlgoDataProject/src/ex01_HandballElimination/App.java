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
		scan.close();
		
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
		
		//Tester for hvert hold om det er elimineret
		for(int i = 0; i < teams.size(); i++){
			String candidate = teams.get(i);
			System.out.println("\nTeam " + candidate.substring(0, candidate.indexOf(" ")));
			new HandballFlowNetwork(new ArrayList<String>(teams), i);
		}
	}
}
