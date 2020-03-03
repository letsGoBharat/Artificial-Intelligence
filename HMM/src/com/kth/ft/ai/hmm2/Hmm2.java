package com.kth.ft.ai.hmm2;

public class Hmm2 {
	
	public double[][] elementWiseProductForGamma0(double[][] mat, double[][] columnMatrix) {
		int rows_mat = mat.length;
		int colms_mat = mat[0].length;
		double[][] gammaMatrix = new double[rows_mat][colms_mat];
		
		for(int i=0;i<rows_mat;i++) {
			for(int j=0;j<colms_mat;j++) {
				gammaMatrix[i][j] = mat[i][j]*columnMatrix[i][j];
			}
		}
		
		return gammaMatrix;
	}
	
	public double[][] elementWiseProductForGamma(double[][] mat, double[][] columnMatrix, double emissionValue, int n, int m) {
		int rows_mat = columnMatrix.length;
		int colms_mat = columnMatrix[0].length;
		double[][] gammaMatrix = new double[rows_mat][colms_mat];
			for(int i=0;i<rows_mat;i++) {
				for(int j=0;j<colms_mat;j++) {
					gammaMatrix[i][j] = mat[m-1][j]*columnMatrix[i][j];
					gammaMatrix[i][j] = gammaMatrix[i][j]*emissionValue;
					}
				}
		return gammaMatrix;
	} 
	
	public double[][] getColumnFromMatrix(double[][] mat, int column) {
		double[][] colMatrix = new double[1][mat.length];
		
		for(int i=0;i<mat.length;i++) {
			colMatrix[0][i] = mat[i][column];
		}
		
		return colMatrix;
	}
	
	public void calculateOptimumPath(double[][] deltaMat, int[][] deltaIndexMat) {
		int rowsDelta = deltaMat.length;
		int colmDetla = deltaMat[0].length;
		int lastIndex = 0;
		double maxValue = 0;
		int optimalIndex[] = new int[rowsDelta];
		
			for(int j=0;j<colmDetla;j++) {
				if(deltaMat[rowsDelta-1][j] > maxValue) {
					maxValue = deltaMat[rowsDelta-1][j];
					lastIndex = j;
				}
			}
			optimalIndex[0] = lastIndex;
			for(int j=1;j<deltaIndexMat.length;j++) {
				optimalIndex[j] = deltaIndexMat[rowsDelta-1][optimalIndex[j-1]];
				rowsDelta--;
			}
			for(int j=optimalIndex.length-1;j>=0;j--) {
				System.out.print(optimalIndex[j]+" ");
			}
	}
}
