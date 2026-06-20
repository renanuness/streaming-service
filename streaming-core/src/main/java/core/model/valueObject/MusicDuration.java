package core.model.valueObject;

import shared.exception.BusinessRuleException;

public record MusicDuration(int duration) {
    //private int inSeconds;
    private static final int MIN_DURATION = 1;
    private static final int MAX_DURATION = 3600;

    public MusicDuration{
        if(duration < MIN_DURATION || duration > MAX_DURATION){
            throw  new BusinessRuleException(String.format("Tempo de duração inválida. Uma música deve ter entre %d segundos e %d segundos",
                    MIN_DURATION, MAX_DURATION));
        }

    }

    public String toMinutes(){
        int minutes = duration/60;
        int seconds = duration%60;

        return "%d:%d".formatted(minutes, seconds);
    }
}
