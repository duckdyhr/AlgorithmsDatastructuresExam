package ex03_PhoneList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class PhoneListValidation {
	private LengthComparator comparator;
	public PhoneListValidation() {
		comparator = new LengthComparator();
	}
	
	public boolean isValidPhonelist(ArrayList<String> list){
		Collections.sort(list, comparator);
		System.out.println("Sorted: " + Arrays.toString(list.toArray()));
		//stop ved index af første af længste phone number...
		int until = firstIndexLongestNum(list);
		System.out.println("until: " + until);
		for(int i = 0; i < until; i++){
			String num1 = list.get(i);
			//denne løkke skal køre hele arraylisten igennem
			for(int j = i+1; j < list.size(); j++){
				String num2 = list.get(j);
				if(num1.equals(num2.substring(0, num1.length()))){
					return false;
				}
			}
		}
		return true;
	}
	
	private int firstIndexLongestNum(ArrayList<String> list){
		int index = list.size()-1;
		int longest = list.get(index).length();
		boolean found = false;
		while(!found && index > 0){
			if(list.get(index-1).length() < longest){
				found = true;
			}else{
				index--;				
			}
		}
		return index;
	}
	class LengthComparator implements Comparator<String>{
		@Override
		public int compare(String s1, String s2) {
			return s1.length() - s2.length();
		}
	}
}
