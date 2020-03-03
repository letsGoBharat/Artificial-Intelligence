package com.kth.ft.ai.hmm3;

import java.util.Scanner;

public class Main {
	
	private static int maxItrs = 50000;
	private static double oldLogProb = Double.NEGATIVE_INFINITY;
	private static double logProb = Double.NEGATIVE_INFINITY;

	public static void main(String[] args) {
		
		Hmm3 hmm3;
		
		double[][] transitionMatrix;
		double[][] emissionMatrix;
		double[][] pi;
		
		int rowsA, columnsA, rowsB, columnsB, rowsPi, columnsPi, emissionCount;
		
		int[] emissionSeq;
		
		Scanner scan = new Scanner(System.in);
		
		rowsA = scan.nextInt();
		columnsA = scan.nextInt();
		
		transitionMatrix = new double[rowsA][columnsA];
		
		for(int i=0; i<rowsA; i++) {
			for(int j=0; j<columnsA; j++) {
				transitionMatrix[i][j] = scan.nextDouble();
			}
			
		}
		
		rowsB = scan.nextInt();
		columnsB = scan.nextInt();
		
		emissionMatrix = new double[rowsB][columnsB];
		
		for(int i=0; i<rowsB; i++) {
			for(int j=0; j<columnsB; j++) {
				emissionMatrix[i][j] = scan.nextDouble();
			}
			
		}
		
		rowsPi = scan.nextInt();
		columnsPi = scan.nextInt();
		
		pi = new double[rowsPi][columnsPi];
		
		for(int i=0; i<rowsPi; i++) {
			for(int j=0; j<columnsPi; j++) {
				pi[i][j] = scan.nextDouble();
			}
		}
		
		emissionCount = scan.nextInt();
		
		emissionSeq = new int[emissionCount];
		
		for(int i=0; i<emissionCount; i++) {
			emissionSeq[i] = scan.nextInt();
		}
		
		scan.close();
		
		hmm3 = new Hmm3();
		
		int iters = 0;
		do {
			oldLogProb = logProb;
			hmm3.alphaPass(pi, emissionMatrix, emissionSeq, transitionMatrix);
			hmm3.betaPass(emissionMatrix, emissionSeq, transitionMatrix);
			hmm3.calculateGammaDiGamma(emissionMatrix, emissionSeq, transitionMatrix);
			pi = hmm3.estimatePi(pi);
			transitionMatrix = hmm3.estimateA(transitionMatrix, emissionSeq);
			emissionMatrix = hmm3.estimateB(emissionMatrix, transitionMatrix, emissionSeq);
			logProb = hmm3.calculateLogProbEmissionSeq(emissionSeq);
			iters++;
		}while(iters<maxItrs && logProb>oldLogProb);
			hmm3.printMat(pi);
			System.out.println("");
			hmm3.printMat(transitionMatrix);
			System.out.println("");
			hmm3.printMat(emissionMatrix);
			System.out.println(" " + "Iterations " + iters);
			System.out.println("");
			System.out.println(logProb);
		}
}
