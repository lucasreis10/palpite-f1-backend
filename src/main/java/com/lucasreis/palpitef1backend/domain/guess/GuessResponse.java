package com.lucasreis.palpitef1backend.domain.guess;

import com.lucasreis.palpitef1backend.domain.pilot.PilotResponse;
import com.lucasreis.palpitef1backend.domain.user.UserSummary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuessResponse {
    
    private Long id;
    private UserSummary user;
    private Long grandPrixId;
    private String grandPrixName;
    private Integer season;
    private Integer round;
    private GuessType guessType;
    private List<PilotResponse> pilots; // Palpite do usuário
    private List<PilotResponse> realResultPilots; // Resultado real (se disponível)
    private BigDecimal score;
    private Boolean calculated;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static GuessResponse fromGuess(Guess guess, List<PilotResponse> pilots, List<PilotResponse> realResultPilots) {
        return GuessResponse.builder()
                .id(guess.getId())
                .user(UserSummary.fromUser(guess.getUser()))
                .grandPrixId(guess.getGrandPrix().getId())
                .grandPrixName(guess.getGrandPrix().getName())
                .season(guess.getGrandPrix().getSeason())
                .round(guess.getGrandPrix().getRound())
                .guessType(guess.getGuessType())
                .pilots(pilots)
                .realResultPilots(realResultPilots)
                .score(guess.getScore())
                .calculated(guess.getCalculated())
                .active(guess.getActive())
                .createdAt(guess.getCreatedAt())
                .updatedAt(guess.getUpdatedAt())
                .build();
    }
} 