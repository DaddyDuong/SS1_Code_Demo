package org.example;

import org.moeaframework.core.Solution;
import org.moeaframework.core.objective.Maximize;
import org.moeaframework.core.variable.BinaryIntegerVariable;
import org.moeaframework.problem.AbstractProblem;

/**
 * Bài toán phân bổ nước sông 2 mục tiêu:
 * - Mục tiêu 1: Tối đa hóa sức khỏe sông (nước còn lại)
 * - Mục tiêu 2: Tối đa hóa tổng lợi ích kinh tế
 */
public class RiverProblem extends AbstractProblem {

    // 4 chiến lược khác nhau với tỷ lệ sử dụng nước khác nhau
    private static final double[] USAGE_RATES = {0.10, 0.25, 0.45, 0.60};
    
    // Lượng nước ban đầu của sông
    private static final double INITIAL_WATER_FLOW = 100.0;

    /**
     * Constructor: Khởi tạo bài toán với 3 biến quyết định và 2 mục tiêu.
     */
    public RiverProblem() {
        super(3, 2); // 3 variables, 2 objectives
    }

    @Override
    public void evaluate(Solution solution) {
        // === LẤY GIÁ TRỊ TỪ BIẾN QUYẾT ĐỊNH CỦA SOLUTION ===
        int choiceA = BinaryIntegerVariable.getInt(solution.getVariable(0));
        int choiceB = BinaryIntegerVariable.getInt(solution.getVariable(1));
        int choiceC = BinaryIntegerVariable.getInt(solution.getVariable(2));

        // === TÍNH TOÁN DÒNG CHẢY NƯỚC TUẦN TỰ ===
        
        // Quốc gia A (thượng lưu)
        double waterAvailableForA = INITIAL_WATER_FLOW;
        double waterTakenByA = waterAvailableForA * USAGE_RATES[choiceA];

        // Quốc gia B (trung lưu)
        double waterAvailableForB = waterAvailableForA - waterTakenByA;
        double waterTakenByB = waterAvailableForB * USAGE_RATES[choiceB];

        // Quốc gia C (hạ lưu)
        double waterAvailableForC = waterAvailableForB - waterTakenByB;
        double waterTakenByC = waterAvailableForC * USAGE_RATES[choiceC];

        // === TÍNH TOÁN 2 MỤC TIÊU ===
        // Mục tiêu 1: Sức khỏe sông - Tính bằng lượng nước còn lại sau khi các quốc gia khai thác
        double riverHealth = waterAvailableForC - waterTakenByC;  
        
        // Mục tiêu 2: Lợi ích kinh tế 
        // Mỗi quốc gia có trình độ khai thác kinh tế từ nguồn nước khác nhau
        // Do đó, lợi ích kinh tế sẽ được tính theo hệ số khác nhau
        double payoffA = waterTakenByA * 2.0;
        double payoffB = waterTakenByB * 1.2;
        double payoffC = waterTakenByC * 1.6;                    
        double totalEconomicBenefit = payoffA + payoffB + payoffC; 

        solution.setObjectiveValue(0, riverHealth);        
        solution.setObjectiveValue(1, totalEconomicBenefit);
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(3, 2);

        solution.setVariable(0, new BinaryIntegerVariable(0, 3)); // Quốc gia A
        solution.setVariable(1, new BinaryIntegerVariable(0, 3)); // Quốc gia B
        solution.setVariable(2, new BinaryIntegerVariable(0, 3)); // Quốc gia C

        solution.setObjective(0, new Maximize());
        solution.setObjective(1, new Maximize());

        return solution;
    }
}
