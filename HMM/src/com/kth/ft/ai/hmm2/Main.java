package com.kth.ft.ai.hmm2;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		Hmm2 hmm2;
		
		double[][] transitionMatrix;
		double[][] emissionMatrix;
		double[][] pi;
		double[][] maxProbabilityGammaMatrix;
		double[][] deltaMatrix;
		int[][] deltaIndex;
		
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
		
		hmm2 = new Hmm2();
		
		deltaMatrix = new double[emissionCount][columnsPi];
		deltaIndex = new int[emissionCount][columnsPi];
		
		//veterbi starts
		
		maxProbabilityGammaMatrix = hmm2.elementWiseProductForGamma0(pi, hmm2.getColumnFromMatrix(emissionMatrix, emissionSeq[0]));
		for(int i=0;i<columnsPi;i++) {
			deltaMatrix[0][i] = maxProbabilityGammaMatrix[0][i];
			deltaIndex[0][i] = i;
		}
			
				for(int i=1;i<emissionCount;i++) {
					for(int j=0;j<columnsA;j++) {
						double maxValue = 0;
						int maxIndex = 0;
						maxProbabilityGammaMatrix = hmm2.elementWiseProductForGamma(deltaMatrix, hmm2.getColumnFromMatrix(transitionMatrix, j),
								emissionMatrix[j][emissionSeq[i]], j, i);
						for(int k=0;k<maxProbabilityGammaMatrix[0].length;k++) {
							if(maxProbabilityGammaMatrix[0][k] > maxValue) {
								maxValue = maxProbabilityGammaMatrix[0][k];
								maxIndex = k;
							}
						}
						deltaMatrix[i][j] = maxValue;
						deltaIndex[i][j] = maxIndex;
					}
				}
				hmm2.calculateOptimumPath(deltaMatrix, deltaIndex);
	}

}
