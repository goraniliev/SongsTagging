package examples;

import java.io.IOException;

import dbUtilities.FillDatabase;

public class ChooseCoefficientForTanhFunction {
	
	// 1. Driver class. Here we choose the coefficient used to calculate the hotness (we use this coefficient in the tanh function).

	public static void main(String[] args) throws IOException {
		// This block was run only once, to determine the coefficient which gives best dispersion 
		// of hotness in the interval (0, 1).
		System.out.printf("Best value to use as coefficient in the tanh function is %f. "
				+ "For this value we obtain best dispersion of the values in the interval (0, 1)", 
				FillDatabase.chooseCoefficientForSigmoidalExponent("unique_tracks.txt", 1000));
		
		// Printed:
		// Best value to use as coefficient in the tanh function is 0.000100. 
		// For this value we obtain best dispersion of the values in the interval (0, 1)

//		Best value chosen for coeff is 0.000100, so we are putting this value as constant field to 
//		the class FillDatabase and we will use it in our predictions of hotness.

	}

}
