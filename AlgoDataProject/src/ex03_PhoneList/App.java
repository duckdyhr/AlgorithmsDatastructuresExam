package ex03_PhoneList;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class App {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner scan = new Scanner(new File("InputPhoneList.txt"));
		int numCases = Integer.parseInt(scan.nextLine());
		ArrayList<String> pNumbers;
		PhoneListValidation validator = new PhoneListValidation();
		
		for(int i = 0; i < numCases; i++){
			int numNumbers = Integer.parseInt(scan.nextLine());
			pNumbers = new ArrayList<String>();
			for(int j = 0; j < numNumbers; j++){
				pNumbers.add(scan.nextLine());
			}
			System.out.println("Testcase "+ i);
			//System.out.println(Arrays.toString(pNumbers.toArray()));
			boolean result = validator.isValidPhonelist(new ArrayList<String>(pNumbers));
			System.out.println("isValid: " + result);
		}
		scan.close();
	}
}
