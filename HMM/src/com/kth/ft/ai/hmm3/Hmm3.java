package com.kth.ft.ai.hmm3;

public class Hmm3 {
	
	double[] scaleFactor;
	double[][] alphamatrix;
	double[][] betaMatrix;
	double[][] gammaMatrix;
	double[][][] diGammaMatrix;
	
	public void alphaPass(double[][] mat, double[][] emissionMatrix, int[] emissionSeq, double[][] transitionMatrix) {
		int rows_alpha = emissionSeq.length;
		int colms_alpha = mat[0].length;
		scaleFactor = new double[emissionSeq.length];
		alphamatrix = new double[rows_alpha][colms_alpha];
		
		for(int i=0;i<colms_alpha;i++) {
			alphamatrix[0][i] = mat[0][i]*emissionMatrix[i][emissionSeq[0]];
			scaleFactor[0] += alphamatrix[0][i];
		}
		scaleFactor[0] = 1/scaleFactor[0];
		for(int i=0;i<colms_alpha;i++) {
			alphamatrix[0][i] = scaleFactor[0]*alphamatrix[0][i];
		}
		
		for(int t=1;t<rows_alpha;t++) {
			scaleFactor[t] = 0;
			for(int i=0;i<colms_alpha;i++) {
				double alphaAtI = 0;
				for(int j=0;j<colms_alpha;j++) {
					alphaAtI += (alphamatrix[t-1][j]*transitionMatrix[j][i]);
				}
				alphamatrix[t][i] = alphaAtI*emissionMatrix[i][emissionSeq[t]];
				scaleFactor[t]+=alphamatrix[t][i];
			}
			scaleFactor[t] = 1/scaleFactor[t];
			for(int i=0;i<colms_alpha;i++) {
				alphamatrix[t][i] = scaleFactor[t]*alphamatrix[t][i];
			}
		}
	}
	
	public void betaPass(double[][] emissionMatrix, int[] emissionSeq, double[][] transitionMatrix) {
		int rows_beta = emissionSeq.length;
		int colms_beta = transitionMatrix.length;
		betaMatrix = new double[rows_beta][colms_beta];
		for(int i=0;i<colms_beta;i++) {
			betaMatrix[rows_beta-1][i] = scaleFactor[rows_beta-1];
		}
		for(int t=rows_beta-2;t>0;t--) {
			for(int i=0;i<colms_beta;i++) {
				double betaAtT = 0;
				for(int j=0;j<colms_beta;j++) {
					betaAtT += transitionMatrix[i][j]*emissionMatrix[j][emissionSeq[t+1]]*betaMatrix[t+1][j];
				}
				betaMatrix[t][i] = scaleFactor[t]*betaAtT;
			}
		}
	}
	
	public void calculateGammaDiGamma(double[][] emissionMatrix, int[] emissionSeq, double[][] transitionMatrix) {
		int rows_gamma = emissionSeq.length;
		int colms_gamma = transitionMatrix.length;
		diGammaMatrix = new double[rows_gamma][colms_gamma][colms_gamma];
		gammaMatrix = new double[rows_gamma][colms_gamma];
		
		for(int t=0;t<rows_gamma-1;t++) {
			for(int i=0;i<colms_gamma;i++) {
				gammaMatrix[t][i] = 0;
				for(int j=0;j<colms_gamma;j++) {
					diGammaMatrix[t][i][j] = alphamatrix[t][i]*transitionMatrix[i][j]*emissionMatrix[j][emissionSeq[t+1]]*betaMatrix[t+1][j];
					gammaMatrix[t][i] += diGammaMatrix[t][i][j];
				}
			}
		}
		for(int i=0;i<colms_gamma;i++) {
			gammaMatrix[rows_gamma-1][i] = alphamatrix[rows_gamma-1][i];
		}
		
	}
	
	public double[][] estimatePi(double[][] mat) {
		int colms_pi = mat[0].length;
		for(int i=0;i<colms_pi;i++) {
			mat[0][i] = gammaMatrix[0][i];
		}
		return mat;
	}
	
	public double[][] estimateA(double[][] transMatrix, int[] emissionSeq) {
		int stateSize = transMatrix.length;
		int emissionSize = emissionSeq.length;
		for(int i=0;i<stateSize;i++) {
			double denom = 0;
			for(int t=0;t<emissionSize-1;t++) {
				denom += gammaMatrix[t][i];
			}
			for(int j=0;j<stateSize;j++) {
				double numer = 0;
				for(int t=0;t<emissionSize-1;t++) {
					numer += diGammaMatrix[t][i][j];
				}
				transMatrix[i][j] = numer/denom;
			}
		}
		return transMatrix;
	}
	
	public double[][] estimateB(double[][] emissionMat, double[][] transMat, int[] emissionSeq) {
		int stateSize = transMat.length;
		int emissionSize = emissionSeq.length;
		int obsSize = emissionMat[0].length;
		for(int i=0;i<stateSize;i++) {
			double denom = 0;
			for(int t=0;t<emissionSize;t++) {
				denom += gammaMatrix[t][i];
			}
			for(int j=0;j<obsSize;j++) {
				double numer = 0;
				for(int t=0;t<emissionSize;t++) {
					if(emissionSeq[t] == j) {
						numer += gammaMatrix[t][i];
					}
				}
				emissionMat[i][j] = numer/denom;
			}
		}
		return emissionMat;
	}
	
	public double calculateLogProbEmissionSeq(int[] emissionSeq) {
		int emissionSize = emissionSeq.length;
		double logProb = 0;
		for(int i=0;i<emissionSize;i++) {
			logProb += Math.log(scaleFactor[i]);
		}
		logProb = -logProb;
		return logProb;
	}
	
	public void printMat(double[][] matrix) {
		System.out.print(matrix.length);
		System.out.print(" ");
		System.out.println(matrix[0].length);
		for (double[] row : matrix) {

            for (double cell : row) {

                System.out.print(" ");
                //System.out.print(round(cell, 6));
                System.out.print(cell);
            }

        }
	}
	
	/*public double round(double inputValue, int decimalPlaces) {
        long factor = (long) Math.pow(10, decimalPlaces);
        inputValue = inputValue * factor;
        long tmp = Math.round(inputValue);
        return (double) tmp / factor;
    }*/
}
