package Baekjoon.BFS_DFS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// https://www.acmicpc.net/problem/15683s
public class G3_15683_감시 {
	static int N, M;
	static int[][] grid;
	static int[][] visited;
	static int minCount = Integer.MAX_VALUE;
	static int cctvCount = 0;

	static int[] dx = {-1, 0, 1, 0};
	static int[] dy = {0, 1, 0, -1};

	static int[][][] cctvDirs = {
		{}, // 0번 인덱스 생략
		{{0}, {1}, {2}, {3}},             // 1번: 한쪽만 (4방향)
		{{0, 2}, {1, 3}},                 // 2번: 직선 (2방향)
		{{0, 1}, {1, 2}, {2, 3}, {3, 0}}, // 3번: 직각 (4방향)
		{{0, 1, 2}, {1, 2, 3}, {2, 3, 0}, {3, 0, 1}}, // 4번: 세 방향 (4방향)
		{{0, 1, 2, 3}}                    // 5번: 전 방향 (1방향)
	};

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		grid = new int[N][M];
		visited = new int[N][M];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for (int j = 0; j < M; j++) {
				int num = Integer.parseInt(st.nextToken());
				grid[i][j] = num;
				if((num > 0) && (num < 6)) cctvCount++;
			}
		}

		dfs(0, 0, cctvCount);
		System.out.println(minCount);
	}

	private static void dfs(int startR, int startC, int count) {
		if (count == 0) {
			int blindSpot = 0;
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					// 빈칸(0)이면서 감시되지 않은(visited == 0) 곳 탐색
					if (grid[i][j] == 0 && visited[i][j] == 0) {
						blindSpot++;
					}
				}
			}
			minCount = Math.min(minCount, blindSpot);
			return;
		}

		for (int i = startR; i < N; i++) {
			for(int j = (i == startR ? startC : 0); j < M; j++) {
				// CCTV를 만나면
				if(grid[i][j] >= 1 && grid[i][j] <= 5) {
					int type = grid[i][j];
					// 해당 CCTV의 가능한 모든 회전 방향 시도
					for (int k = 0; k < cctvDirs[type].length; k++) {
						for (int dir : cctvDirs[type][k]) {
							int nx = i + dx[dir];
							int ny = j + dy[dir];
							while (nx >= 0 && nx < N && ny >= 0 && ny < M && grid[nx][ny] != 6) {
								if (grid[nx][ny] == 0) visited[nx][ny]++;
								nx += dx[dir];
								ny += dy[dir];
							}
						}

						if (j + 1 < M) dfs(i, j + 1, count - 1);
						else dfs(i + 1, 0, count - 1);

						for (int dir : cctvDirs[type][k]) {
							int nx = i + dx[dir];
							int ny = j + dy[dir];
							while (nx >= 0 && nx < N && ny >= 0 && ny < M && grid[nx][ny] != 6) {
								if (grid[nx][ny] == 0) visited[nx][ny]--;
								nx += dx[dir];
								ny += dy[dir];
							}
						}
					}
					return;
				}
			}
		}
	}
}