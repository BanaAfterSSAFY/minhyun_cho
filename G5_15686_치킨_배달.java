package Baekjoon.BFS_DFS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class G5_15686_치킨_배달 {
	static int N, M;
	static int[][] grid;
	static int chicken = 0;
	static int answer = Integer.MAX_VALUE;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		grid = new int[N][N];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for (int j = 0; j < N; j++) {
				int num = Integer.parseInt(st.nextToken());
				grid[i][j] = num;
				if (num == 2) chicken++;
			}
		}

		// 제거해야 할 치킨집 개수 계산
		// 0, 0 좌표부터 탐색 시작
		dfs(0, 0, chicken - M);

		System.out.println(answer);
	}

	// startR, startC를 추가하여 중복 탐색 방지 (조합 최적화)
	private static void dfs(int startR, int startC, int count) {
		if (count == 0) {
			int[][] langeCnt = new int[N][N];
			for (int i = 0; i < N; i++) {
				Arrays.fill(langeCnt[i], Integer.MAX_VALUE);
			}

			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					if (grid[i][j] == 2) {
						langeChecker(langeCnt, new int[]{i, j});
					}
				}
			}

			int sum = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					// grid[i][j]가 1인 집의 치킨 거리만 합산
					if (grid[i][j] == 1) {
						sum += langeCnt[i][j];
					}
				}
			}

			answer = Math.min(answer, sum);
			return;
		}

		// 현재 위치부터 탐색하여 이전에 확인한 칸을 다시 보지 않음
		for (int i = startR; i < N; i++) {
			// 행이 바뀌면 열 인덱스를 0으로 초기화, 아니면 startC부터 시작
			for (int j = (i == startR ? startC : 0); j < N; j++) {
				if (grid[i][j] == 2) {
					grid[i][j] = 0; // 치킨집 제거 (선택 해제)

					// 다음 칸부터 탐색하도록 좌표 전달
					int nextJ = j + 1;
					int nextI = i;
					if (nextJ == N) {
						nextI++;
						nextJ = 0;
					}
					dfs(nextI, nextJ, count - 1);

					grid[i][j] = 2; // 복구 (백트래킹)
				}
			}
		}
	}

	private static void langeChecker(int[][] langeCnt, int[] target) {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (grid[i][j] == 1) {
					int dist = Math.abs(i - target[0]) + Math.abs(j - target[1]);
					langeCnt[i][j] = Math.min(langeCnt[i][j], dist);
				}
			}
		}
	}
}