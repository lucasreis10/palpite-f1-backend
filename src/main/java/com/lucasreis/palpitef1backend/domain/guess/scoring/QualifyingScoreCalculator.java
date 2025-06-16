package com.lucasreis.palpitef1backend.domain.guess.scoring;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QualifyingScoreCalculator {
    
    private final List<Long> realQualifyResult;
    private final List<Long> guessQualify;
    private final Map<Integer, Integer> diffGrids;
    
    public QualifyingScoreCalculator(List<Long> realGrid, List<Long> guessGrid) {
        this.realQualifyResult = realGrid;
        this.guessQualify = guessGrid;
        this.diffGrids = checkDifferencesWithRealGridAndGuessGrid();
    }
    
    private Map<Integer, Integer> checkDifferencesWithRealGridAndGuessGrid() {
        Map<Integer, Integer> diff = new HashMap<>();
        
        for (int index = 0; index < realQualifyResult.size(); index++) {
            Long pilotId = realQualifyResult.get(index);
            int guessPosition = guessQualify.indexOf(pilotId);
            diff.put(index, guessPosition);
        }
        
        return diff;
    }
    
    public BigDecimal calculate() {
        BigDecimal totalScore = BigDecimal.ZERO;
        
        totalScore = totalScore.add(calculateFirst());
        totalScore = totalScore.add(calculateSecond());
        totalScore = totalScore.add(calculateThird());
        totalScore = totalScore.add(calculateFourth());
        totalScore = totalScore.add(calculateFifth());
        totalScore = totalScore.add(calculateSixth());
        totalScore = totalScore.add(calculateSeventh());
        totalScore = totalScore.add(calculateEighth());
        totalScore = totalScore.add(calculateNinth());
        totalScore = totalScore.add(calculateTenth());
        totalScore = totalScore.add(calculateEleventh());
        totalScore = totalScore.add(calculateTwelfth());
        
        return totalScore.setScale(3, RoundingMode.HALF_UP);
    }
    
    // Método para calcular pontuações individuais por posição
    public Map<Integer, BigDecimal> calculateByPosition() {
        Map<Integer, BigDecimal> positionScores = new HashMap<>();
        
        positionScores.put(0, calculateFirst());
        positionScores.put(1, calculateSecond());
        positionScores.put(2, calculateThird());
        positionScores.put(3, calculateFourth());
        positionScores.put(4, calculateFifth());
        positionScores.put(5, calculateSixth());
        positionScores.put(6, calculateSeventh());
        positionScores.put(7, calculateEighth());
        positionScores.put(8, calculateNinth());
        positionScores.put(9, calculateTenth());
        positionScores.put(10, calculateEleventh());
        positionScores.put(11, calculateTwelfth());
        
        return positionScores;
    }
    
    private BigDecimal calculateFirst() {
        BigDecimal[] scores = {
            new BigDecimal("5.0"), 
            new BigDecimal("4.25"), 
            new BigDecimal("3.612")
        };
        
        Integer position = diffGrids.get(0); // GridPosition.FIRST = 0
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateSecond() {
        BigDecimal[] scores = {
            new BigDecimal("4.25"), 
            new BigDecimal("5.0"), 
            new BigDecimal("4.25"), 
            new BigDecimal("2.89")
        };
        
        Integer position = diffGrids.get(1); // GridPosition.SECOND = 1
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateThird() {
        BigDecimal[] scores = {
            new BigDecimal("3.612"), 
            new BigDecimal("4.25"), 
            new BigDecimal("5.0"), 
            new BigDecimal("3.4"), 
            new BigDecimal("2.89")
        };
        
        Integer position = diffGrids.get(2); // GridPosition.THIRD = 2
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateFourth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            new BigDecimal("3.612"), 
            new BigDecimal("4.25"), 
            new BigDecimal("4.0"), 
            new BigDecimal("3.4"), 
            new BigDecimal("2.89")
        };
        
        Integer position = diffGrids.get(3); // GridPosition.FOURTH = 3
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateFifth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("3.612"), 
            new BigDecimal("3.4"), 
            new BigDecimal("4.0"), 
            new BigDecimal("3.4"), 
            new BigDecimal("2.167")
        };
        
        Integer position = diffGrids.get(4); // GridPosition.FIFTH = 4
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateSixth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("2.89"), 
            new BigDecimal("3.4"), 
            new BigDecimal("4.0"), 
            new BigDecimal("2.55"), 
            new BigDecimal("2.167")
        };
        
        Integer position = diffGrids.get(5); // GridPosition.SIXTH = 5
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateSeventh() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("2.89"), 
            new BigDecimal("3.4"), 
            new BigDecimal("3.0"), 
            new BigDecimal("2.55"), 
            new BigDecimal("2.167")
        };
        
        Integer position = diffGrids.get(6); // GridPosition.SEVENTH = 6
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateEighth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("2.89"), 
            new BigDecimal("2.55"), 
            new BigDecimal("3.0"), 
            new BigDecimal("2.55")
        };
        
        Integer position = diffGrids.get(7); // GridPosition.EIGHTH = 7
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateNinth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("2.167"), 
            new BigDecimal("2.55"), 
            new BigDecimal("3.0"), 
            new BigDecimal("2.55")
        };
        
        Integer position = diffGrids.get(8); // GridPosition.NINTH = 8
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateTenth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("2.167"), 
            new BigDecimal("2.55"), 
            new BigDecimal("3.0")
        };
        
        Integer position = diffGrids.get(9); // GridPosition.TENTH = 9
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateEleventh() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("2.167"), 
            new BigDecimal("2.55")
        };
        
        Integer position = diffGrids.get(10); // GridPosition.ELEVENTH = 10
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateTwelfth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("2.167")
        };
        
        Integer position = diffGrids.get(11); // GridPosition.TWELFTH = 11
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
} 