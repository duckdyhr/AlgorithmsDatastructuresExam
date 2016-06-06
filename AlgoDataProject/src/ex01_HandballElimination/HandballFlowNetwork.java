package ex01_HandballElimination;

import java.util.ArrayList;

//Vertices har en key og en value. Der sammenlignes på key.
//ikke generisk endnu, burde være FlowNetwork<K extends Comparable, V>
//Atlanta 83 8 0 1 6 1
//83 8 0 1 6 1
public class HandballFlowNetwork {
	private int[][] fTeams;
	private int[][] capacities;
	private int[][] flows;
	private int[][] residuals;

	public HandballFlowNetwork(ArrayList<String> input) {

		generateFTeams(input, 2);
		System.out.println("fTeams");
		printTable(fTeams);

		// Antal kampe mellem fTeams
		int fGames = 0;
		for (int[] team : fTeams) {
			fGames += team[1];
		}

		// Antal point F holdene fordeler imellem sig
		int fPoints = fGames * 2;
		System.out.println("\nAntal kampe i F{}: " + fGames + " Antal point i F{}: " + fPoints);

		// Antal vertices inkl. source og sink
		int vertices = 8; // Brug beregning i stedet! Antal kombinationer +
							// antal hold + source + sink.
		capacities = new int[vertices][vertices];

		insertEdges();
		System.out.println("Capacities");
		printTable(capacities);

		flows = new int[vertices][vertices]; // flow(i, j) = 0 i starten
		residuals = new int[vertices][vertices];

		System.out.println("MaxFlow: " + maxFlow());
	}

	// Løber capacities igennem, og optimerer flow, hvis muligt...
	// Mindste fællesnævner bestemmer hvor meget der kan sendes ad path.
	public int maxFlow(){
		updateResidualNetwork();
		int [] augmentingPath = findAugmentingPath();
		while(augmentingPath != null){
			int min = Integer.MAX_VALUE;
			int current = 0;
			//1. while: Findes mindste fællesnavner for flow
			while(augmentingPath[current] != 0){
				min = Math.min(min, residuals[augmentingPath[current + 1]][augmentingPath[current]]);
				current++;
			}
			current = 0;
			//2. while: 
			while(augmentingPath[current] != 0){
				int to = augmentingPath[current];
				int from = augmentingPath[current + 1];
				if(capacities[from][to] != 0){
					flows[from][to] += min;
				} else {
					flows[to][from] -= min;
				}
				current++;
			}
			updateResidualNetwork();
			augmentingPath = findAugmentingPath();

		}
		int maxFlow = 0;
		for(int i = 0; i < flows.length; i++){
			maxFlow += flows[0][i];
		}
		return maxFlow;
	}

	// Hvis capacity[x][y] = 0, kan der ikke sendes noget som helst
	// (ikke-eksisterende edge i fleste tilfælde)
	// Efter første iteration hvor flow=0, er capacities[][] == residualt[][]
	/*private void updateResidualNetwork() {
		for (int u = 0; u < capacities.length; u++) {
			for (int v = 0; v < capacities.length; v++) {
				if (capacities[u][v] != 0) {
					// outedge capacity-flow, dvs hvor meget mere kan sendes ad
					// denne edge
					residuals[u][v] = capacities[u][v] - flows[u][v];
				}
				if (flows[u][v] != 0) {
					// backedge hvor meget flow der kan sendes tilbage, er
					// præcist det der sendes out nu (rækkefølge v og u byttet
					// om)
					residuals[v][u] = flows[u][v];
				}
			}
		}
	}*/
	private void updateResidualNetwork(){
		for(int u = 0; u < capacities.length; u++){
			for(int v = 0; v < capacities.length; v++){
				if(capacities[u][v] != 0){
					residuals[u][v] = capacities[u][v] - flows[u][v];
				}
				if(flows[u][v] != 0){
					residuals[v][u] = flows[u][v];
				}
			}
		}
	}
	// Findes der en path fra source->sink i residuals? => maxFlow ikke fundet
	// endnu...
	private int[] findAugmentingPath(){
		int[] visited = new int[capacities.length];
		for(int i = 1; i < visited.length; i++){
			visited[i] = -1; //initialiseres til -1
		}
		dfs(visited, 0);
		if(visited[1] == -1){//didn't reach the sink
			return null; //maxflow fundet, da der ikke findes path			
		}		
		int[] path = new int[visited.length];
		int pathLength = 0;
		int current = 1;
		while(current != 0){
			if(visited[current] != -1){
				path[pathLength] = current;
				pathLength++;
				current = visited[current];
			}
		}
		return path;
	}

	// visited er den endelige path..
	private void dfs(int[] visited, int current) {
		for (int i = 0; i < residuals.length; i++) {
			if (residuals[current][i] != 0 && visited[i] == -1) {
				visited[i] = current;
				dfs(visited, i);
			}
		}
	}

	private void generateFTeams(ArrayList<String> input, int evaluateIndex) {
		String teamToEvaluate = input.remove(evaluateIndex);

		String[] teamArr = teamToEvaluate.split(" ");
		int points = Integer.parseInt(teamArr[1]);
		int gamesLeft = Integer.parseInt(teamArr[2]);
		int maxPoints = points + (gamesLeft * 2);

		fTeams = new int[input.size()][6];
		for (int i = 0; i < input.size(); i++) {
			String[] s = input.get(i).split(" ");
			int j = 1;
			int substraction = 1; // bruges når man skipper et hold (der som
									// evalueres)
			while (j < s.length) {
				if (j == evaluateIndex + 3) { // springer data om
												// evalueringsteamet over
					substraction = 2;
				} else {
					fTeams[i][j - substraction] = Integer.parseInt(s[j]);
				}
				j++;
			}
			int remainingPoints = maxPoints - fTeams[i][0];
			// Indsætter hvor mange point holdet må få på det sidste index i
			// arrayet
			fTeams[i][fTeams[0].length - 1] = remainingPoints;
		}
	}

	private void insertEdges() {
		// INSÆTTER EDGES
		// i < antal kombinationer/Q sættet efterfulgt af j < antal hold/F
		// sættet
		// source = capacities[0]
		// sink = capacities[capacities.length-1]
		// Edges fra source til alle i Q (kombinationer), dvs. capacities[0][Qi]
		int index = 1; // Qi's placering
		// Indsæt ikke begge stedet. Placering betyder retning!
		for (int i = 0; i < fTeams.length; i++) { // løber teams igennem

			int pointsToGive = 0;
			// kamp starter ved index 3 - kan ikke spille imod sig selv
			for (int j = i + 3; j < capacities.length - 3; j++) { 
				pointsToGive = fTeams[i][j] * 2;
				// Edges source -> Q(index)
				capacities[0][index] = pointsToGive;
				// capacities[index][0] = pointsToGive;

				// Edges Q(index) -> Fi og Fj.
				capacities[index][i + 4] = pointsToGive;
				// capacities[i + 4][index] = pointsToGive;
				capacities[index][j + 2] = pointsToGive;
				// capacities[j + 2][index] = pointsToGive;

				index++;
			}

		}
		int sinkIndex = capacities.length - 1;
		index = sinkIndex - fTeams.length;
		// Edges Fi -> sink (indsæt i for-løkken ovenfor)
		for (int k = 0; k < fTeams.length; k++) {
			capacities[index++][sinkIndex] = fTeams[k][fTeams[0].length - 1];
			// capacities[sinkIndex][index++] = fTeams[k][fTeams[0].length - 1];
		}
	}

	private void printTable(int[][] table) {
		for (int i = 0; i < table.length; i++) {
			for (int j = 0; j < table[i].length; j++) {
				System.out.printf("%3s", table[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	private void printArray(int[] arr){
		for(int i = 0; i < arr.length; i++){
			System.out.printf("%3s", arr[i] + " ");
		}
		System.out.println();
	}
}
