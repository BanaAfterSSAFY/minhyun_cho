package Baekjoon.UnPackaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class G4_9663_NQueen {
	static int resCnt = 0;
	static int[] grid;

	static int N;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		grid = new int[N];

		backTracking(0);
		System.out.println(resCnt);
	}

	private static void backTracking(int depth) {
		if (depth == N) { // 모든 퀸을 다 놓은 경우
			resCnt++;
			return;
		}

		for (int i = 0; i < N; i++) {
			grid[depth] = i;
			if (possible(depth)) backTracking(depth + 1);
		}
	}

	private static boolean possible(int col) {
		for (int i = 0; i < col; i++) {
			// 같은 열에 있는지 확인
			if (grid[col] == grid[i]) return false;

			// 대각선에 있는지 확인, (행 차이 절대값 == 열 차이 절대값)이면 대각선
			else if (Math.abs(col - i) == Math.abs(grid[col] - grid[i])) return false;
		}
		return true;
	}
}