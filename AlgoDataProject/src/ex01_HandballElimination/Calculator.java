package ex01_HandballElimination;

public class Calculator {
	//2! = 2
	//3! = 6
	public static void main(String[] args) {
		int n = 3;
		int fN = factorial(n); 
		int result = fN/(factorial(n-2)*factorial(2));
		System.out.println(n + " hold kombinationer: " + result);
	}
	public static int factorial(int f) {
	    return ((f == 0) ? 1 : f * factorial(f - 1));
	}  
}
