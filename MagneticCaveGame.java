package com.example.demo;

import java.util.Scanner;

// “We certify that this submission is the original work of members of the group and meets
//the Faculty's Expectations of Originality”, 1200430 salwa fayyad ,1200905 sondos ashraf
//20.06.2023.


public class MagneticCaveGame {
    private static final int BOARD_SIZE = 8;
    private static final char EMPTY_CELL = '_';
    private static final char PLAYER_1_BRICK = '■';
    private static final char PLAYER_2_BRICK = '□';
    private static int test=0;

    private char[][] board;
    private Player player1;
    private Player player2;
    private Player currentPlayer;

    public MagneticCaveGame() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        initializeBoard();

        player1 = new Player(PLAYER_1_BRICK);
        player2 = new Player(PLAYER_2_BRICK);
        currentPlayer = player1;
    }

    private void initializeBoard() {

        for (int row = 0; row < BOARD_SIZE; row++) {

            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col]=EMPTY_CELL;
            }

        }
    }

    private void displayBoard() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int row = 0; row < BOARD_SIZE; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < BOARD_SIZE; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private boolean isMoveValid(int row, int col) {
        // Check if the cell is within the board boundaries
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }

        // Check if the cell is empty
        if (board[row][col] != EMPTY_CELL) {
            return false;
        }
        if(col>0) {
            if (board[row][col] == EMPTY_CELL && board[row][col - 1] != EMPTY_CELL) {
                return true;
            }
            if(col<7 &&board[row][col + 1] != EMPTY_CELL ){
                return true;
            }
            if(col == 7 &&board[row][col] == EMPTY_CELL){
                return true;
            }

            else
                return false;
        }
        if (board[row][col] == EMPTY_CELL) {
            return true;
        }


        // Check if the cell is adjacent to an existing brick or the wall
        boolean adjacentToBrick = false;

        if (col > 0 && board[row][col - 1] == currentPlayer.getBrick()) {
            adjacentToBrick = true;
        }

        if (col < BOARD_SIZE - 1 && board[row][col + 1] == currentPlayer.getBrick()) {
            adjacentToBrick = true;
        }

        if (row > 0 && board[row - 1][col] == currentPlayer.getBrick()) {
            adjacentToBrick = true;
        }

        if (row < BOARD_SIZE - 1 && board[row + 1][col] == currentPlayer.getBrick()) {
            adjacentToBrick = true;
        }

        return adjacentToBrick;
    }

    private boolean hasWon(int row, int col) {
        // Check horizontal
        switchPlayers();
        int count = 0;
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                if (board[r][c] == currentPlayer.getBrick()) {
                    count++;
                    if (count == 5) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        // Check vertical
        count = 0;
        for (int c = 0; c < BOARD_SIZE; c++) {
            for (int r = 0; r < BOARD_SIZE; r++) {
                if (board[r][c] == currentPlayer.getBrick()) {
                    count++;
                    if (count == 5) {
                        return true;
                    }
                } else {
                    count = 0;
                }
            }
        }

        // Check diagonal from top-left to bottom-right
        count = 0; //
        int rr=0,cc=1;
        while (rr<BOARD_SIZE && cc<BOARD_SIZE) {
            if (board[rr][cc] == currentPlayer.getBrick()) {
                count++;
                if (count == 5) {
                    return true;
                }
            } else {
                count = 0;
            }
            rr++;
            cc++;
        }
        cc=BOARD_SIZE-1; rr=0;
        while (rr<BOARD_SIZE && cc<BOARD_SIZE) {
            if (board[rr][cc] == currentPlayer.getBrick()) {
                count++;
                if (count == 5) {
                    return true;
                }
            } else {
                count = 0;
            }
            rr++;
            cc--;
        }

        int startRow = row - Math.min(row, col);//
        int startCol = col - Math.min(row, col);//
        for (int r = startRow, c = startCol; r < BOARD_SIZE && c < BOARD_SIZE; r++, c++) {
            if (board[r][c] == currentPlayer.getBrick()) {
                count++;
                if (count == 5) {
                    return true;
                }
            } else {
                count = 0;
            }
        }

        // Check diagonal from top-right to bottom-left
        count = 0;
        startRow = row - Math.min(row, BOARD_SIZE - col - 1);
        startCol = col + Math.max(row, BOARD_SIZE - col - 1);
        for (int r = startRow, c = startCol; r < BOARD_SIZE && c >= 0; r++, c--) {
            if (board[r][c] == currentPlayer.getBrick()) {
                count++;
                if (count == 5) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        switchPlayers();
        return false;
    }

    private void makeMove(int row, int col) {
        board[row][col] = currentPlayer.getBrick();
    }

    private boolean isBoardFull() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isGameOver(int row, int col) {
        if (hasWon(row, col)) {
            System.out.println("Player " + currentPlayer.getBrick() + " wins!");
            return true;
        }

        if (isBoardFull()) {
            System.out.println("It's a tie!");
            return true;
        }

        return false;
    }

    private void switchPlayers() {
        if (currentPlayer == player1) {
            currentPlayer = player2;
        } else {
            currentPlayer = player1;
        }
    }


    private void makeAIMove() {
        int[] bestMove = minimax(4, Integer.MIN_VALUE, Integer.MAX_VALUE, true);//the AI will look ahead two moves
        int row = bestMove[1];
        int col = bestMove[2];
        System.out.println("Player " + currentPlayer.getBrick() + " (AI) chooses row " + row + " and column " + col+"\n");
        makeMove(row, col);
    }
    private void undoMove(int row, int col) {
        board[row][col] = EMPTY_CELL; // Reset the cell to its empty state
        // Other necessary actions to undo the move
    }


    private int[] minimax(int depth, int alpha, int beta, boolean maximizingPlayer) {
        int[] bestMove = new int[3];

        if (depth == 0 || isGameOver(0, 0)) {//the function evaluates the current state of the board
            bestMove[0] = evaluateBoard(); // first index in bestmove = evaluateBoard
            return bestMove;
        }

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    if (isMoveValid(row, col)) {
                        makeMove(row, col);
                        int eval = minimax(depth - 1, alpha, beta, false)[0];
                        undoMove(row,col);
                        if (eval > maxEval) {
                            maxEval = eval;
                            bestMove[0] = eval;
                            bestMove[1] = row;
                            bestMove[2] = col;
                        }
                        alpha = Math.max(alpha, eval);
                        if (beta <= alpha) {
                            break;
                        }
                    }
                }
            }
            return bestMove;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (int row = 0; row < BOARD_SIZE; row++) {
                for (int col = 0; col < BOARD_SIZE; col++) {
                    if (isMoveValid(row, col)) {
                        makeMove(row, col);
                        int eval = minimax(depth - 1, alpha, beta, true)[0];
                        undoMove(row,col);
                        if (eval < minEval) {
                            minEval = eval;//Update the minimum evaluation value encountered so far.
                            bestMove[0] = eval;//best move for the minimizing player
                            bestMove[1] = row;
                            bestMove[2] = col;
                        }
                        beta = Math.min(beta, eval);
                        if (beta <= alpha) {//pruning
                            break;
                        }
                    }
                }
            }
            return bestMove;
        }
    }

    private int evaluateBoard() {
        int score = 0;
        score += evaluateRows();
        score += evaluateColumns();
        score += evaluateDiagonals();
        return score;
    }

    private int evaluateRows() {
        int score = 0;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col <= BOARD_SIZE - 5; col++) {
                score += evaluateLine(row, col, 0, 1);
            }
        }
        return score;
    }

    private int evaluateColumns() {
        int score = 0;
        for (int col = 0; col < BOARD_SIZE; col++) {
            for (int row = 0; row <= BOARD_SIZE - 5; row++) {
                score += evaluateLine(row, col, 1, 0);
            }
        }
        return score;
    }


    private int evaluateDiagonals() {
        int score = 0;
        for (int row = 0; row <= BOARD_SIZE - 5; row++) {
            for (int col = 0; col <= BOARD_SIZE - 5; col++) {
                score += evaluateLine(row, col, 1, 1); // Top left to bottom right
                score += evaluateLine(row, col + 4, 1, -1); // Top right to bottom left

            }
        }
        return score;
    }


    private int evaluateLine(int row, int col, int rowStep, int colStep) {
        int score = 0;
        int player1Count = 0;
        int player2Count = 0;

        for (int i = 0; i < 5; i++) {
            if (board[row][col] == player1.getBrick()) {
                player1Count++;
            } else if (board[row][col] == player2.getBrick()) {
                player2Count++;
            }
            row += rowStep;
            col += colStep;
        }

        if (test==0) {
            if (player1Count == 0 && player2Count == 0) {
                return 0;
            } else if (player1Count > 0) {
                score -= Math.pow(10, player1Count - 1); // Decrease score for the player's bricks
            } else if (player2Count > 0) {
                score += Math.pow(10, player2Count - 1); // Increase score for the computer's bricks
            }
        }
        if(test==1){
            if (player1Count == 0 && player2Count == 0) {
                return 0;
            } else if (player1Count > 0) {
                score += Math.pow(10, player1Count - 1); // Decrease score for the player's bricks
            } else if (player2Count > 0) {
                score -= Math.pow(10, player2Count - 1); // Increase score for the computer's bricks
            }
        }
        return score;

    }


    private class Player {
        private char brick;

        public Player(char brick) {
            this.brick = brick;
        }

        public char getBrick() {
            return brick;
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ღ WELCOME TO MAGNATIC CAVE GAME ღ \nChoose (1)To play with computer AI \n       (2) Play with Second player");
        int ch=scanner.nextInt();

        if (ch==1){//play with computer AI
            System.out.println("Choose (1) to start with AI Computer and (2) To start with Player ☺");
            int ch1=scanner.nextInt();
            if(ch1==2){ //playing with AI starting with player
                do {
                    displayBoard();
                    if (currentPlayer == player1) {
                        System.out.print("Player " + currentPlayer.getBrick() + " (Manual) - Enter row (0-7): ");
                        int row = scanner.nextInt();
                        System.out.print("Player " + currentPlayer.getBrick() + " (Manual) - Enter column (0-7): ");
                        int col = scanner.nextInt();
                        System.out.println();
                        if (isMoveValid(row, col)) {
                            makeMove(row, col);
                            switchPlayers();
                        } else {
                            System.out.println("Invalid move. Try again.");
                        }
                    } else {
                        if(ch==1){
                            System.out.println("Player " + currentPlayer.getBrick() + " (AI) is making a move...");
                            makeAIMove();
                            switchPlayers();
                        }
                        else{
                            System.out.print("\n\nPlayer " + currentPlayer.getBrick() + " (Manual) - Enter row (0-7): ");
                            int row = scanner.nextInt();
                            System.out.print("\nPlayer " + currentPlayer.getBrick() + " (Manual) - Enter column (0-7): ");
                            int col = scanner.nextInt();
                            System.out.println();
                            if (isMoveValid(row, col)) {
                                makeMove(row, col);
                                switchPlayers();
                            } else {
                                System.out.println("Invalid move. Try again.");
                            }
                        }
                    }
                }while (!isGameOver(0, 0));

                displayBoard();
                scanner.close();
            }
            if(ch1==1){ // playing with AI starting with AI
                do {

                    displayBoard();
                    currentPlayer = player2;
                    System.out.println("Player " + currentPlayer.getBrick() + " (AI) is making a move...");
                    makeAIMove();
                    displayBoard();
                    switchPlayers();
                    if(!isGameOver(0, 0)){
                        while(currentPlayer==player1) {
                            System.out.println("Player " + currentPlayer.getBrick() + " (Manual) - Enter row (0-7): ");
                            int row = scanner.nextInt();
                            System.out.println("Player " + currentPlayer.getBrick() + " (Manual) - Enter column (0-7): ");
                            int col = scanner.nextInt();
                            System.out.println();
                            if (isMoveValid(row, col)) {
                                makeMove(row, col);
                                switchPlayers();
                                break;
                            } else {
                                System.out.println("Invalid move. Try again.");
                            }
                        }
                    }
                    else
                        break;

                }
                while (!isGameOver(0, 0));
                displayBoard();
                scanner.close();
            }

        }
        if (ch==2){ // Two players paly together
            do {
                displayBoard();

                if (currentPlayer == player1) {
                    System.out.print("Player " + currentPlayer.getBrick() + " (Manual) - Enter row (0-7): ");
                    int row = scanner.nextInt();
                    System.out.print("Player " + currentPlayer.getBrick() + " (Manual) - Enter column (0-7): ");
                    int col = scanner.nextInt();
                    System.out.println();
                    if (isMoveValid(row, col)) {
                        makeMove(row, col);
                        switchPlayers();
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                }
                else{
                    System.out.print("Player " + currentPlayer.getBrick() + " (Manual) - Enter row (0-7): ");
                    int row = scanner.nextInt();
                    System.out.print("Player " + currentPlayer.getBrick() + " (Manual) - Enter column (0-7): ");
                    int col = scanner.nextInt();
                    System.out.println();
                    if (isMoveValid(row, col)) {
                        makeMove(row, col);
                        switchPlayers();
                    } else {
                        System.out.println("Invalid move. Try again.");
                    }
                }

            }while (!isGameOver(0, 0));

            displayBoard();
            scanner.close();
        }

    }

    public static void main(String[] args) {
        MagneticCaveGame game = new MagneticCaveGame();
        game.play();
    }
}