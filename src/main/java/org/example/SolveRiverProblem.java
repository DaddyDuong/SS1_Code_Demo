package org.example;

import org.moeaframework.algorithm.NSGAII;
import org.moeaframework.problem.Problem;

public class SolveRiverProblem {

    public static void main(String[] args) {
        // Khởi tạo bài toán đa mục tiêu
        Problem problem = new RiverProblem();

        // Khởi tạo thuật toán NSGA-II
        NSGAII algorithm = new NSGAII(problem);

        // Chạy thuật toán với 10000 lần lặp
        algorithm.run(10000);

        // Hiển thị kết quả Pareto front
        algorithm.getResult().display();
    }
}
