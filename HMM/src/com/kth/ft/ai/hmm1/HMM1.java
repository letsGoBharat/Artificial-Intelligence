package com.kth.ft.ai.hmm1;

public class HMM1 {
	
	public float[][] calculateElementWiseProduct(float[][] mat, float[][] columnMatrix) {
		int rows_alpha = mat.length;
		int colms_alpha = mat[0].length;
		float[][] alphaMatrix = new float[rows_alpha][colms_alpha];
		
		for(int i=0;i<rows_alpha;i++) {
			for(int j=0;j<colms_alpha;j++) {
				alphaMatrix[i][j] = mat[i][j]*columnMatrix[i][j];
			}
		}
		
		return alphaMatrix;
	}
	
	public float[][] getColumn(float[][] emissionMatrix, int column) {
		float[][] colMatrix = new float[1][emissionMatrix.length];
		
		for(int i=0;i<emissionMatrix.length;i++) {
			colMatrix[0][i] = emissionMatrix[i][column];
		}
		
		return colMatrix;
	}
	
	public float[][] multMatrix(float[][] alphaI, float[][] transMatrix) {
		int rows_alphaI = alphaI.length;
		int colm_alphaI = alphaI[0].length;
		int colm_transMat = transMatrix[0].length;
		
		float mulMat[][] = new float[rows_alphaI][colm_transMat];
		
		for(int i=0;i<rows_alphaI;i++) {
			for(int j=0;j<colm_alphaI;j++) {
				mulMat[i][j] = 0;
				for(int k=0;k<colm_transMat;k++) {
					mulMat[i][j] += alphaI[i][k]*transMatrix[k][j];
				}
			}
		}
		return mulMat;
	}

}
