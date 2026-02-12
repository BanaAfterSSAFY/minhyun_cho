package Baekjoon.DivideAndConquer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class S2_2630_색종이_만들기 {
	static int[][] grid;
	static int blue, white;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(br.readLine());
		grid = new int[N][N];
		blue = white = 0;

		for (int i = 0; i < N; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			for (int j = 0; j < N; j++) {
				grid[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		divider(0, 0, N, N);
		System.out.println(white);
		System.out.println(blue);
	}

	private static int checker(int startX, int startY, int endX, int endY) {
		int startColor = grid[startX][startY];

		for (int i = startX; i < endX; i++) {
			for (int j = startY; j < endY; j++) {
				if(startColor != grid[i][j]) return -1;
			}
		}

		return startColor;
	}

	private static void divider(int startX, int startY, int endX, int endY) {
		int res = checker(startX, startY, endX, endY);
		if (res == 1) {
			blue++;
		} else if (res == 0) {
			white++;
		} else {
			int midX = startX + (endX - startX) / 2;
			int midY = startY + (endY - startY) / 2;
			divider(startX, startY, midX, midY);
			divider(startX, midY, midX, endY);
			divider(midX, startY, endX, midY);
			divider(midX, midY, endX, endY);
		}
	}
}
