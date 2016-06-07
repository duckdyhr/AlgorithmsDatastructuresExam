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
	private int qSize = 0;
	
	public HandballFlowNetwork(ArrayList<String> input, int indexTeamToEvaluate) {
		generateFTeams(input, indexTeamToEvaluate);
		System.out.println("fTeams");
		printTable(fTeams);

		qSize = numberOfCombinations(fTeams.length);
		// Antal vertices inkl. source og sink index 0=source index 1=sink
		int vertices = 2+qSize+fTeams.length; // Brug beregning i stedet! Antal kombinationer +
							// antal hold + source + sink.
		capacities = new int[vertices][vertices];

		//Antal kampe mellem fTeams //Q-sættet...
		int fGames = insertEdges();
		//System.out.println("Capacities");
		//printTable(capacities);

		flows = new int[vertices][vertices]; // flow(i, j) = 0 i starten
		residuals = new int[vertices][vertices];

		int maxFlow = maxFlow();
		System.out.println("P: " + fGames + " MaxFlow: " + maxFlow);
		
		if(maxFlow >= fGames){
			System.out.println("Team har stadig en chance for at vinde");
		}else{
			System.out.println("Team er elimineret");
		}
	}

	// Løber capacities igennem, og optimerer flow, hvis muligt...
	//Augmented path viser en path fra source->sink. 
	public int maxFlow(){
		updateResidualNetwork();
		int [] augmentingPath = findAugmentingPath();
		while(augmentingPath != null){
			int min = Integer.MAX_VALUE;
			int current = 0;
			//1. while: Findes mindste fællesnavner for flow
			while(augmentingPath[current] != 0){
				//Mindste fællesnævner bestemmer hvor meget der kan sendes ad path.
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

	private int insertEdges() {
		// INSÆTTER EDGES
		// i < antal kombinationer/Q sættet efterfulgt af j < antal hold/F
		// sættet
		// source = capacities[0]
		// sink = capacities[capacities.length-1]
		// Edges fra source til alle i Q (kombinationer), dvs. capacities[0][Qi]
		int index = 2; // Qi's placering
		int sourceIndex=0;
		int fGames = 0;
		// Indsæt ikke begge stedet. Placering betyder retning!
		for (int i = 0; i < fTeams.length; i++) { // løber teams igennem

			int pointsToGive = 0;
			// kamp starter ved index 3 - kan ikke spille imod sig selv
			for (int j = i + qSize; j < capacities.length - qSize; j++) { 
				pointsToGive = fTeams[i][j] * 2;
				// Edges source -> Q(index)
				capacities[sourceIndex][index] = pointsToGive;
				capacities[index][sourceIndex] = pointsToGive;
				fGames += pointsToGive;
				
				// Edges Q(index) -> Fi og Fj.
				capacities[index][i + 4] = pointsToGive;
				//capacities[i + 4][index] = pointsToGive;
				capacities[index][j + 2] = pointsToGive;
				//capacities[j + 2][index] = pointsToGive;
				index++;
			}
		}
		int sinkIndex = 1;
		index = capacities.length - fTeams.length;
		// Edges Fi -> sink (indsæt i for-løkken ovenfor)
		for (int k = 0; k < fTeams.length; k++) {
			capacities[index][sinkIndex] = fTeams[k][fTeams[0].length - 1];
			capacities[sinkIndex][index] = fTeams[k][fTeams[0].length - 1];
			index++;
		}
		return fGames;
	}
	
	private int numberOfCombinations(int n){
		int fN = factorial(n); 
		int result = fN/(factorial(n-2)*factorial(2));
		return result;
	}
	public static int factorial(int f) {
	    return ((f == 0) ? 1 : f * factorial(f - 1));
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
