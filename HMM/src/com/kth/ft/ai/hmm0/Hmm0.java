package com.kth.ft.ai.hmm0;

public class Hmm0 {
	
	public float[][] calculateInitialTransition(float[][] initialTransition, float[][] pi, float[][] transitionMatrix) {
		for(int i=0; i<initialTransition.length; i++) {
			for(int j=0; j<initialTransition[0].length; j++) {
				initialTransition[i][j]=0;
				for(int k=0; k<pi[0].length; k++) {
					initialTransition[i][j] += pi[i][k]*transitionMatrix[k][j];
				}
			}
		}
		return initialTransition;
	}
	
	public float[][] calculateInitialEmission(float[][] initialEmission, float[][] initialTransitionMatrix, float[][] emissionMatrix) {
		for(int i=0; i<initialEmission.length; i++) {
			for(int j=0; j<initialEmission[0].length; j++) {
				initialEmission[i][j]=0;
				for(int k=0; k<initialTransitionMatrix[0].length; k++) {
					initialEmission[i][j] += initialTransitionMatrix[i][k]*emissionMatrix[k][j];
				}
			}
			
		}
		
		return initialEmission;
		
	}

}
