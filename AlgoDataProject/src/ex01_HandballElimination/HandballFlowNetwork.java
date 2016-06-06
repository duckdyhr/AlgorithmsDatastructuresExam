package ex01_HandballElimination;

import java.util.ArrayList;

//Vertices har en key og en value. Der sammenlignes på key.
//ikke generisk endnu, burde være FlowNetwork<K extends Comparable, V>
//Atlanta 83 8 0 1 6 1
//83 8 0 1 6 1
public class HandballFlowNetwork {
	private int[][] capacities;
	private int[][] fTeams;
	public HandballFlowNetwork(ArrayList<String> input){
		//Jeg vælger der første team i input til at være det som skal evalueres...
		String teamToEvaluate = input.remove(0);
		
		String[] teamArr = teamToEvaluate.split(" ");
		int points = Integer.parseInt(teamArr[1]);
		int gamesLeft = Integer.parseInt(teamArr[2]);
		int maxPoints = points + (gamesLeft*2);

		fTeams = new int[input.size()][7];
		
		for(int i = 0; i < input.size(); i++){
			String[] s = input.get(i).split(" ");
			for(int j = 1; j < s.length; j++){
				fTeams[i][j-1] = Integer.parseInt(s[j]);
			}
			int remainingPoints = maxPoints-fTeams[i][0];
			fTeams[i][6] = remainingPoints;
		}
		printFTeams();
		
		//Antal kampe mellem F
		int fGames = 0;
		for(int[] team : fTeams){
			fGames += team[1];
		}
		//Antal point F holdene fordeler imellem sig
		int fPoints = fGames*2;
		System.out.println("Antal kampe: " + fGames + " Antal point: " + fPoints);
		
		//Antal vertices inkl. source og sink
		int vertices = 8; //Brug beregning i stedet! Antal kombinationer + antal hold + source + sink.
		capacities = new int[vertices][vertices];
		
		//indsætter edges
		//i < antal kombinationer/Q sættet efterfulgt af j < antal hold/F sættet
		//capacities[0] = source capacities[capacities.length-1] = sink
		//Fra sink capacities[0] til alle i Q, dvs. capacities[0][Qi]
		//skal udfylde edges [0][1][0][2] og [0][3]
		int index = 1;
		for(int i = 0; i < 3; i++){
			int pointsToGive = 0;
			for(int j = i+4; j < 6; j++){ //kamp index starter ved 3, men kan ikke spille imod sig selv...
				pointsToGive = fTeams[i][j];
			}
			capacities[0][index++] = pointsToGive;				
		}
		
		
		printCapacities();
	}
	
	private void printFTeams(){
		System.out.println("\nF Teams:");
		for(int i = 0; i < fTeams.length; i++){
			String team = i + ": ";
			for(int j = 0; j < fTeams[i].length; j++){
				team += fTeams[i][j] + " ";
			}
			System.out.println("\t"+team);
		}
	}
	private void printCapacities(){
		System.out.println("\nCapacities:");
		for(int i = 0; i < capacities.length; i++){
			String edges = i + ": ";
			for(int j = 0; j <capacities[i].length; j++){
				edges += capacities[i][j] + " ";
			}
			System.out.println("\t"+edges);
		}
	}
}
