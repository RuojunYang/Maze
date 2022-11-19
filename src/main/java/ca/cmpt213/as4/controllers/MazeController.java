package ca.cmpt213.as4.controllers;

import ca.cmpt213.as4.model.Direction;
import ca.cmpt213.as4.model.GameLogic;
import ca.cmpt213.as4.model.Maze;
import ca.cmpt213.as4.restapi.ApiBoardWrapper;
import ca.cmpt213.as4.restapi.ApiGameWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MazeController {
    private List<ApiGameWrapper> apiGameWrappers = new ArrayList<>();
    private List<GameLogic> gameLogics = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong(-1);

    @GetMapping("/api/about")
    public String getHelloMessage() {
        return "Ruojun Yang";
    }

    @GetMapping("/api/games")
    public List<ApiGameWrapper> getGames() {
        return apiGameWrappers;
    }

    // CREATED for success create new game
    @PostMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper newGame() {
        // create gameLogic and add to list
        GameLogic gameLogic = new GameLogic(new Maze(15, 20, 5));
        gameLogics.add(gameLogic);
        // create apiGameWrapper based on the game logic and ID number
        // add to list
        ApiGameWrapper apiGameWrapper = ApiGameWrapper.makeFromGame(gameLogic, (int) nextId.incrementAndGet());
        apiGameWrappers.add(apiGameWrapper);
        return apiGameWrapper;
    }

    @GetMapping("/api/games/{id}")
    public ApiGameWrapper loadGame(@PathVariable("id") long id) throws IllegalAccessException {
        // find game
        for (ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if (apiGameWrapper.gameNumber == id) {
                return apiGameWrapper;
            }
        }
        throw new IllegalAccessException();
    }

    @GetMapping("/api/games/{id}/board")
    public ApiBoardWrapper loadGameBoard(@PathVariable("id") long id) throws CloneNotSupportedException, IllegalAccessException {
        // find game and create game board
        for (ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if (apiGameWrapper.gameNumber == id) {
                return ApiBoardWrapper.makeFromGame(gameLogics.get(apiGameWrapper.gameNumber));
            }
        }
        throw new IllegalAccessException();
    }

    // ACCEPTED for success move
    // IllegalArgumentException is for valid move but hit wall
    // IllegalStateException is for invalid instruction
    // IllegalAccessException is for not find the game
    @PostMapping("/api/games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendMove(@PathVariable("id") long id, @RequestBody String move) throws IllegalAccessException {
        for (ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            if (apiGameWrapper.gameNumber == id) {
                switch (move) {
                    // if player move is valid, check if there is wall
                    case "MOVE_UP":
                        if (gameLogics.get(apiGameWrapper.gameNumber).playerMoveCheck(Direction.up)) {
                            gameLogics.get(apiGameWrapper.gameNumber).playerMove(Direction.up);
                        } else {
                            throw new IllegalArgumentException();
                        }
                        break;
                    case "MOVE_DOWN":
                        if (gameLogics.get(apiGameWrapper.gameNumber).playerMoveCheck(Direction.down)) {
                            gameLogics.get(apiGameWrapper.gameNumber).playerMove(Direction.down);
                        } else {
                            throw new IllegalArgumentException();
                        }
                        break;
                    case "MOVE_LEFT":
                        if (gameLogics.get(apiGameWrapper.gameNumber).playerMoveCheck(Direction.left)) {
                            gameLogics.get(apiGameWrapper.gameNumber).playerMove(Direction.left);
                        } else {
                            throw new IllegalArgumentException();
                        }
                        break;
                    case "MOVE_RIGHT":
                        if (gameLogics.get(apiGameWrapper.gameNumber).playerMoveCheck(Direction.right)) {
                            gameLogics.get(apiGameWrapper.gameNumber).playerMove(Direction.right);
                        } else {
                            throw new IllegalArgumentException();
                        }
                        break;
                    case "MOVE_CATS":
                        // after cat move check the game win or lose
                        gameLogics.get(apiGameWrapper.gameNumber).catsMove();
                        if (gameLogics.get(apiGameWrapper.gameNumber).gameLoseCheck()) {
                            apiGameWrapper.isGameLost = true;
                            return;
                        } else if (gameLogics.get(apiGameWrapper.gameNumber).gameWiningCheck()) {
                            apiGameWrapper.isGameWon = true;
                            return;
                        }
                        return;
                    default:
                        throw new IllegalStateException();
                }
                // if player eat the cheese the game logic will increment the current goal
                // and it will create a new cheese automatically
                // just get it and update it for apiGameWrapper
                apiGameWrapper.numCheeseFound = gameLogics.get(apiGameWrapper.gameNumber).getCurrentGoalMade();
                // after player move check the game win or lose
                if (gameLogics.get(apiGameWrapper.gameNumber).gameLoseCheck()) {
                    apiGameWrapper.isGameLost = true;
                    return;
                } else if (gameLogics.get(apiGameWrapper.gameNumber).gameWiningCheck()) {
                    apiGameWrapper.isGameWon = true;
                    return;
                }
                return;
            }
        }
        throw new IllegalAccessException();
    }

    // ACCEPTED for success cheat
    @PostMapping("/api/games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendCheats(@PathVariable("id") long id, @RequestBody String cheat) throws IllegalAccessException {
        for (ApiGameWrapper apiGameWrapper : apiGameWrappers) {
            // find the game
            if (apiGameWrapper.gameNumber == id) {
                // one cheese cheat
                if (cheat.equals("1_CHEESE")) {
                    gameLogics.get(apiGameWrapper.gameNumber).setGoalToOne();
                    apiGameWrapper.numCheeseGoal = 1;
                    return;
                }
                // show map cheat
                else if (cheat.equals("SHOW_ALL")) {
                    gameLogics.get(apiGameWrapper.gameNumber).showAllMap();
                    return;
                }
                // IllegalStateException is for invalid instruction
                else {
                    throw new IllegalStateException();
                }
            }
        }
        // IllegalAccessException is for not find the game
        throw new IllegalAccessException();
    }

    // Create Exception Handle
    // illegal move
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
            reason = "Cannot Move into wall.")
    @ExceptionHandler(IllegalArgumentException.class)
    public void IllegalMoveHandler() {
        // Nothing to do
    }

    // illegal game id
    @ResponseStatus(value = HttpStatus.NOT_FOUND,
            reason = "Game does not exist")
    @ExceptionHandler(IllegalAccessException.class)
    public void IllegalAccessHandler() {
    }

    // illegal input
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,
            reason = "Invalid instruction")
    @ExceptionHandler(IllegalStateException.class)
    public void IllegalInput() {
    }
}
