package labyrinthball.results;

import labyrinthball.util.repository.GsonRepository;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class ResultRepository extends GsonRepository<Result> {
    public ResultRepository() {
        super(Result.class);
    }

    public List<Result> findHighScores(int n) {
        return elements.stream()
                .filter(Result::isSolved)
                .sorted(Comparator.comparingInt(Result::getNumberOfMoves))
                .limit(n)
                .toList();
    }
}
