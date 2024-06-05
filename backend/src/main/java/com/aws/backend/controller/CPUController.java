package com.aws.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cpu")
@Slf4j
public class CPUController {

    @GetMapping("/load")
    public String loadCpu() {
        log.info("CPU load request received. Generating CPU load...");
        int size = 1000;
        double[][] matrixA = generateRandomMatrix(size, size);
        double[][] matrixB = generateRandomMatrix(size, size);
        multiplyMatrices(matrixA, matrixB, size);
        int threadsInUse = Thread.activeCount();
        return "CPU load generated. Threads in use: " + threadsInUse;
    }

    private double[][] generateRandomMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Math.random();
            }
        }
        return matrix;
    }

    private void multiplyMatrices(double[][] firstMatrix, double[][] secondMatrix, int size) {
        var result = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
    }
}
