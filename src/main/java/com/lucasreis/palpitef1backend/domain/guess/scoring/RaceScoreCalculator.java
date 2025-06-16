package com.lucasreis.palpitef1backend.domain.guess.scoring;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaceScoreCalculator {
    
    private final List<Long> realRaceResult;
    private final List<Long> guessRace;
    private final Map<Integer, Integer> diffGrids;
    
    public RaceScoreCalculator(List<Long> realRaceResult, List<Long> guessRace) {
        this.realRaceResult = realRaceResult;

        this.guessRace = guessRace;
        this.diffGrids = differencesBetwenGrids();
    }
    
    private Map<Integer, Integer> differencesBetwenGrids() {
        List<Long> guessDrivers = normalizeDriverList();
        return generateGridDifferences(guessDrivers);
    }
    
    private List<Long> normalizeDriverList() {
        return guessRace.stream().map(driver -> driver).toList();
    }
    
    private Map<Integer, Integer> generateGridDifferences(List<Long> guessDrivers) {
        Map<Integer, Integer> diff = new HashMap<>();
        
        for (int index = 0; index < realRaceResult.size(); index++) {
            Long pilotId = realRaceResult.get(index);
            int guessPosition = guessDrivers.indexOf(pilotId);
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
        totalScore = totalScore.add(calculateThirteenth());
        totalScore = totalScore.add(calculateFourteenth());
        
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
        positionScores.put(12, calculateThirteenth());
        positionScores.put(13, calculateFourteenth());
        
        return positionScores;
    }
    
    private BigDecimal calculateFirst() {
        BigDecimal[] scores = {
            new BigDecimal("25"), 
            new BigDecimal("21.25"), 
            new BigDecimal("18.062"), 
            new BigDecimal("12.282"), 
            new BigDecimal("10.44")
        };
        
        Integer position = diffGrids.get(0);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateSecond() {
        BigDecimal[] scores = {
            new BigDecimal("21.25"), 
            new BigDecimal("25"), 
            new BigDecimal("21.25"), 
            new BigDecimal("14.45"), 
            new BigDecimal("12.282"), 
            new BigDecimal("10.44")
        };
        
        Integer position = diffGrids.get(1);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateThird() {
        BigDecimal[] scores = {
            new BigDecimal("18.062"), 
            new BigDecimal("21.25"), 
            new BigDecimal("25"), 
            new BigDecimal("17"), 
            new BigDecimal("14.45"), 
            new BigDecimal("12.282"), 
            new BigDecimal("7.83")
        };
        
        Integer position = diffGrids.get(2);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateFourth() {
        BigDecimal[] scores = {
            new BigDecimal("15.353"), 
            new BigDecimal("18.062"), 
            new BigDecimal("21.25"), 
            new BigDecimal("20"), 
            new BigDecimal("17"), 
            new BigDecimal("14.45"), 
            new BigDecimal("9.212"), 
            new BigDecimal("7.83")
        };
        
        Integer position = diffGrids.get(3);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateFifth() {
        BigDecimal[] scores = {
            new BigDecimal("13.05"), 
            new BigDecimal("15.353"), 
            new BigDecimal("18.062"), 
            new BigDecimal("17"), 
            new BigDecimal("20"), 
            new BigDecimal("17"), 
            new BigDecimal("10.837"), 
            new BigDecimal("9.212"), 
            new BigDecimal("7.83")
        };
        
        Integer position = diffGrids.get(4);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateSixth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            new BigDecimal("13.05"), 
            new BigDecimal("15.353"), 
            new BigDecimal("14.45"), 
            new BigDecimal("17"), 
            new BigDecimal("20"), 
            new BigDecimal("12.75"), 
            new BigDecimal("10.837"), 
            new BigDecimal("9.212"), 
            new BigDecimal("7.83")
        };
        
        Integer position = diffGrids.get(5);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateSeventh() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("13.05"), 
            new BigDecimal("12.282"), 
            new BigDecimal("14.45"), 
            new BigDecimal("17"), 
            new BigDecimal("15"), 
            new BigDecimal("12.75"), 
            new BigDecimal("10.837"), 
            new BigDecimal("9.212")
        };
        
        Integer position = diffGrids.get(6);
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
            new BigDecimal("10.44"), 
            new BigDecimal("12.282"), 
            new BigDecimal("14.45"), 
            new BigDecimal("12.75"), 
            new BigDecimal("15"), 
            new BigDecimal("12.75"), 
            new BigDecimal("10.837")
        };
        
        Integer position = diffGrids.get(7);
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
            new BigDecimal("10.44"), 
            new BigDecimal("12.282"), 
            new BigDecimal("10.837"), 
            new BigDecimal("12.75"), 
            new BigDecimal("15"), 
            new BigDecimal("12.75")
        };
        
        Integer position = diffGrids.get(8);
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
            new BigDecimal("10.44"), 
            new BigDecimal("9.212"), 
            new BigDecimal("10.837"), 
            new BigDecimal("12.75"), 
            new BigDecimal("15")
        };
        
        Integer position = diffGrids.get(9);
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
            new BigDecimal("7.83"), 
            new BigDecimal("9.212"), 
            new BigDecimal("10.837"), 
            new BigDecimal("12.75")
        };
        
        Integer position = diffGrids.get(10);
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
            new BigDecimal("7.83"), 
            new BigDecimal("9.212"), 
            new BigDecimal("10.837")
        };
        
        Integer position = diffGrids.get(11);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateThirteenth() {
        BigDecimal[] scores = {
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            BigDecimal.ZERO, 
            new BigDecimal("7.83"), 
            new BigDecimal("9.212")
        };
        
        Integer position = diffGrids.get(12);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
    
    private BigDecimal calculateFourteenth() {
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
            new BigDecimal("7.83")
        };
        
        Integer position = diffGrids.get(13);
        if (position != null && position >= 0 && position < scores.length) {
            return scores[position];
        }
        return BigDecimal.ZERO;
    }
} 
