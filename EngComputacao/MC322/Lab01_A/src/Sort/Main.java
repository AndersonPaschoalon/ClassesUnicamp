package Sort;

public class Main 
{
	public static void main(String[] args) {

		int quantity = 10;
		int[] vector = new int[quantity];

		// Erro 01 
		//for (int i = 0; i < vector.length - 1; i++) {
		for (int i = 0; i < vector.length; i++) {	
			vector[i] = (int) (Math.random() * 100);
		}

		sort(vector);
		// Erro 05
		// for (int i = 1; i < vector.length; i++) {
		for (int i = 0; i < vector.length; i++) {
			System.out.println(vector[i]);
		}

	}

	private static void sort(int vector[]) {

		boolean switched = true;
		int aux;
		while (switched) {
			switched = false;
			// Erro 02
			// for (int i = 0; i < vector.length + 1; i++) {
			for (int i = 0; i < vector.length - 1; i++) {
				if (vector[i] > vector[i + 1]) {
					aux = vector[i];
					vector[i] = vector[i + 1];
					// Erro 03
					//vector[i - 1] = aux;
					vector[i + 1] = aux;
					// Erro 04
					//switched = false;
					switched = true;
				}
			}
		}
	}

}