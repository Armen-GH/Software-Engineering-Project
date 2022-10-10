package labyrinthball.results;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;

@Builder
@Data
public class Result {

    private String name;

    private int numberOfMoves;

    private ZonedDateTime endTime;

    private String timer;

    private boolean solved;
}
