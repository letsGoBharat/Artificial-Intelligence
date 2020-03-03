package com.kth.ft.ai.hmm0;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Hmm0 obj;
		Hmm0 obj1;
		
		int rows, columns;
		float[][] transitionMatrix;
		float[][] emissionMatrix;
		float[][] pi;
		float[][] initialTransition;
		float[][] initialEmission;
		
		Scanner scan = new Scanner(System.in);
		
		rows = scan.nextInt();
		columns = scan.nextInt();
		
		transitionMatrix = new float[rows][columns];
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				transitionMatrix[i][j] = scan.nextFloat();
			}
			
		}
		
		rows = scan.nextInt();
		columns = scan.nextInt();
		
		emissionMatrix = new float[rows][columns];
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				emissionMatrix[i][j] = scan.nextFloat();
			}
			
		}
		
		rows = scan.nextInt();
		columns = scan.nextInt();
		
		pi = new float[rows][columns];
		
		for(int i=0; i<rows; i++) {
			for(int j=0; j<columns; j++) {
				pi[i][j] = scan.nextFloat();
			}
		}
		
		scan.close();
		
		initialTransition = new float[pi.length][transitionMatrix[0].length];
		
		obj = new Hmm0();
		float[][] initialTransitionMatrix = obj.calculateInitialTransition(initialTransition, pi, transitionMatrix);
		
		initialEmission = new float[initialTransitionMatrix.length][emissionMatrix[0].length];
		
		obj1 = new Hmm0();
		float[][] initialEmissionMatrix = obj1.calculateInitialEmission(initialEmission, initialTransitionMatrix, emissionMatrix);
		
		System.out.print(initialEmissionMatrix.length + " " + initialEmissionMatrix[0].length + " ");
		
		for(int i=0; i<initialEmissionMatrix.length;i++) {
			for(int j=0; j<initialEmissionMatrix[0].length; j++) {
				System.out.print(initialEmissionMatrix[i][j] + " ");
			}
		}

	}

}
