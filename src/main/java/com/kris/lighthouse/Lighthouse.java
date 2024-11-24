package com.kris.lighthouse;

import java.io.*;
import java.util.Scanner;

public class Lighthouse {
    private int N, M;
    private char[][] maze;
    private char[][] resultMaze;
    private int lighthouseX, lighthouseY;

    public Lighthouse() {}

    public void run(String filePath) {
        readMaze(filePath);
        initResultMaze();
        findLighthouse();
    }

    private void printMaze() {
        System.out.printf("Size: %d x %d\n", N, M);

        System.out.println("Maze: ");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(maze[i][j]);
            }
            System.out.println();
        }
    }

    private void printResultMaze() {
        System.out.printf("Size: %d x %d\n", N + 2, M + 2);

        System.out.println("Result maze: ");
        for (int i = 0; i < N + 2; i++) {
            for (int j = 0; j < M + 2; j++) {
                System.out.print(resultMaze[i][j]);
            }
            System.out.println();
        }
    }

    private void readMaze(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            N = scanner.nextInt();
            M = scanner.nextInt();
            scanner.nextLine();

            maze = new char[N][M];

            for (int i = 0; i < N; i++) {
                String line = scanner.nextLine();
                if (line.length() != M) {
                    throw new IllegalArgumentException(
                            "Line " + (i + 1) + " does not have exactly " + M + " symbols."
                    );
                }
                maze[i] = line.toCharArray();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
        } catch (Exception e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        printMaze();
    }

    private void initResultMaze() {
        resultMaze = new char[N + 2][M + 2];
        for (int i = 0; i < N + 2; i++) {
            resultMaze[i][0] = '*';
            resultMaze[i][M + 1] = '*';
        }
        for (int i = 1; i < M + 1; i++) {
            resultMaze[0][i] = '*';
            resultMaze[N + 1][i] = '*';
        }

        for (int i = 0; i < N; i++) {
            if (M >= 0)
                System.arraycopy(maze[i], 0, resultMaze[i + 1], 1, M);
        }

        printResultMaze();
    }

    private void findLighthouse() {
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {
                if (resultMaze[i][j] == 'X') {
                    lighthouseX = i;
                    lighthouseY = j;

                    System.out.println("Lighthouse: " + lighthouseY + " " + lighthouseX);
                    return;
                }
            }
        }
        System.out.println("Lighthouse not found.");
    }
}
