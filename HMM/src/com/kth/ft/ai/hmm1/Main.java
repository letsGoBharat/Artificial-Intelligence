package com.kth.ft.ai.hmm1;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		HMM1 hmm1;
		
		float[][] transitionMatrix;
		float[][] emissionMatrix;
		float[][] pi;
		float[][] alphaMatrix;
		
		int rowsA, columnsA, rowsB, columnsB, rowsPi, columnsPi, emissionCount;
		
		int[] emissionSeq;
		
		Scanner scan = new Scanner(System.in);
		
		rowsA = scan.nextInt();
		columnsA = scan.nextInt();
		
		transitionMatrix = new float[rowsA][columnsA];
		
		for(int i=0; i<rowsA; i++) {
			for(int j=0; j<columnsA; j++) {
				transitionMatrix[i][j] = scan.nextFloat();
			}
			
		}
		
		rowsB = scan.nextInt();
		columnsB = scan.nextInt();
		
		emissionMatrix = new float[rowsB][columnsB];
		
		for(int i=0; i<rowsB; i++) {
			for(int j=0; j<columnsB; j++) {
				emissionMatrix[i][j] = scan.nextFloat();
			}
			
		}
		
		rowsPi = scan.nextInt();
		columnsPi = scan.nextInt();
		
		pi = new float[rowsPi][columnsPi];
		
		for(int i=0; i<rowsPi; i++) {
			for(int j=0; j<columnsPi; j++) {
				pi[i][j] = scan.nextFloat();
			}
		}
		
		emissionCount = scan.nextInt();
		
		emissionSeq = new int[emissionCount];
		
		for(int i=0; i<emissionCount; i++) {
			emissionSeq[i] = scan.nextInt();
		}
		
		scan.close();
		
		hmm1 = new HMM1();
		//alphaPass starts
		alphaMatrix = hmm1.calculateElementWiseProduct(pi, hmm1.getColumn(emissionMatrix,emissionSeq[0]));
		
		for(int i=1;i<emissionCount;i++) {
			alphaMatrix = hmm1.calculateElementWiseProduct(hmm1.multMatrix(alphaMatrix, transitionMatrix), hmm1.getColumn(emissionMatrix,emissionSeq[i]));
		}
		
		float max = 0;
		for(int n=0;n<alphaMatrix[0].length;n++) {
			max += alphaMatrix[0][n];
		}
		System.out.println(max);
	}

}
