package Baekjoon.DivideAndConquer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class S1_1992_쿼드트리 {
	static int[][] grid;
	static StringBuilder sb;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		int N = Integer.parseInt(br.readLine());
		grid = new int[N][N];

		for (int i = 0; i < N; i++) {
			char[] str = br.readLine().toCharArray();
			for (int j = 0; j < N; j++) {
				grid[i][j] = Integer.parseInt(String.valueOf(str[j]));
			}
		}

		divider(0,0, N, N);
		System.out.println(sb);
	}

	private static int checker(int startX, int startY, int endX, int endY) {
		int startColor = grid[startX][startY];

		for (int i = startX; i < endX; i++) {
			for (int j = startY; j < endY; j++) {
				if (startColor != grid[i][j]) return -1;
			}
		}

		return startColor;
	}

	private static void divider(int startX, int startY, int endX, int endY) {
		int res = checker(startX, startY, endX, endY);
		if (res == 1) { // 모두 1인 경우
			sb.append('1');
		} else if (res == 0) {
			sb.append('0');
		} else  { // 일치하지 않는 숫자가 있는 경우
			sb.append('(');
			int midX = startX + ((endX - startX) / 2);
			int midY = startY + ((endY - startY) / 2);
			divider(startX, startY, midX, midY);
			divider(startX, midY, midX, endY);
			divider(midX, startY, endX, midY);
			divider(midX, midY, endX, endY);
			sb.append(')');
		}
	}
}