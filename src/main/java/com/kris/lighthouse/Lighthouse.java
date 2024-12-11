package com.kris.lighthouse;

import java.io.*;
import java.util.Scanner;

public class Lighthouse {
    private int N, M;
    private char[][] maze;
    private char[][] resultMaze;
    private final Block lighthouse;

    public Lighthouse() {
        lighthouse = new Block();
    }

    public void run(String filePath) {
        readMaze(filePath);
        initResultMaze();
        findLighthouse();
        turnOnRays();
        printResultMaze();
        writeMaze();
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
        System.out.println();
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
        System.out.println();
    }

    private void readMaze(String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            N = scanner.nextInt();
            M = scanner.nextInt();
//            System.out.println("\n\nAAAAAAAAAAAAAAAAAA" + N);
//            System.out.println(M);
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
                    lighthouse.setX(j).setY(i);

                    System.out.println("Lighthouse: " + lighthouse.y() + " " + lighthouse.x());
                    return;
                }
            }
        }
        System.out.println("Lighthouse not found.");
    }

    private boolean checkOppRay(Block currentBlock, Ray ray, int reflectCase) {
        if(reflectCase == 0) {
            return resultMaze[currentBlock.y()][currentBlock.x()] == ray.getOppositeRay();
        }
        return resultMaze[currentBlock.y()][currentBlock.x()] == ray.getRay();
    }

    private void cross(Block currentBlock) {
        resultMaze[currentBlock.y()][currentBlock.x()] = Ray.crossRays;
    }

    private void turnOnRays() {
        Block currentBlock = new Block();
        Ray ray = new Ray();

        resultMaze[lighthouse.y()][lighthouse.x()] = 'O';

        for (int i = 1; i <= 4; i++) {
            currentBlock.setX(lighthouse.x()).setY(lighthouse.y());
            ray.setSide(i);

            int count = 0;

            while (count < 2 * N * M && !ray.stop(resultMaze[currentBlock.y() + ray.getLeft().y()][currentBlock.x() + ray.getLeft().x()],
                    resultMaze[currentBlock.y() + ray.getRight().y()][currentBlock.x() + ray.getRight().x()],
                    resultMaze[currentBlock.y() + ray.getDiagonal().y()][currentBlock.x() + ray.getDiagonal().x()])) {
//                System.out.println("BLAAAAAA " + i + " : " + count + " / " + (2 * N * M));
//                    printResultMaze();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                int reflectCase = ray.reflect(resultMaze[currentBlock.y() + ray.getLeft().y()][currentBlock.x() + ray.getLeft().x()],
                        resultMaze[currentBlock.y() + ray.getRight().y()][currentBlock.x() + ray.getRight().x()],
                        resultMaze[currentBlock.y() + ray.getDiagonal().y()][currentBlock.x() + ray.getDiagonal().x()]);

                if (reflectCase == 0) {
                    currentBlock.setX(currentBlock.x() + ray.getDiagonal().x())
                            .setY(currentBlock.y() + ray.getDiagonal().y());
                    if (checkOppRay(currentBlock, ray, reflectCase)) {
                        cross(currentBlock);
                    }
                    else if (resultMaze[currentBlock.y()][currentBlock.x()] == '.') {
                        resultMaze[currentBlock.y()][currentBlock.x()] = ray.getRay();
                    }
                }
                else if (reflectCase == 1) {
                    currentBlock.setX(currentBlock.x() + ray.getRight().x())
                            .setY(currentBlock.y() + ray.getRight().y());
                    if (checkOppRay(currentBlock, ray, reflectCase)) {
                        cross(currentBlock);
                    }
                    else if (resultMaze[currentBlock.y()][currentBlock.x()] == '.') {
                        resultMaze[currentBlock.y()][currentBlock.x()] = ray.getOppositeRay();
                    }
                }
                else if (reflectCase == 2) {
                    currentBlock.setX(currentBlock.x() + ray.getLeft().x())
                            .setY(currentBlock.y() + ray.getLeft().y());
                    if (checkOppRay(currentBlock, ray, reflectCase)) {
                        cross(currentBlock);
                    }
                    else if (resultMaze[currentBlock.y()][currentBlock.x()] == '.') {
                        resultMaze[currentBlock.y()][currentBlock.x()] = ray.getOppositeRay();
                    }
                }
                ray.changeSide(reflectCase);

                count++;

            }
        }
    }

    private void writeMaze() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/mazeRayed.txt"));

            for (int i = 0; i < N + 2; i++) {
                for (int j = 0; j < M + 2; j++) {
                    bw.write(resultMaze[i][j]);
                }
                bw.newLine();
            }
            bw.flush();
        } catch (IOException e) {
            System.out.println("Error writing maze.");
        }
    }
}
