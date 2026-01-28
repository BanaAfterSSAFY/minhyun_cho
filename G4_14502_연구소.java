package Baekjoon.BFS_DFS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class G4_14502_연구소 {
	static int N, M, ANSWER = 0;
	static int[][] grid;
	static int[][] copyGrid;

	static int[] dx = {0, 0, 1, -1};
	static int[] dy = {1, -1, 0, 0};

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		grid = new int[N][M];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			for (int j = 0; j < M; j++) {
				grid[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		dfs(0);

		System.out.println(ANSWER);
	}

	private static void dfs(int wallCnt) {
		if (wallCnt == 3) {
			bfs();
			return;
		}

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if(grid[i][j] == 0) {
					grid[i][j] = 1;
					dfs(wallCnt + 1);
					grid[i][j] = 0;
				}
			}
		}
	}

	// 바이러스 전파
	private static void bfs() {
		Queue<int[]> que = new ArrayDeque<>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if(grid[i][j] == 2) {
					que.offer(new int[] {i, j});
				}
			}
		}

		copyGrid = new int[N][M];

		// 값 복사
		for (int i = 0; i < N; i++) {
			copyGrid[i] = grid[i].clone();
		}

		while (!que.isEmpty()) {
			int[] curr = que.poll();
			for (int d = 0; d < 4; d++) {
				int nx = curr[0] + dx[d];
				int ny = curr[1] + dy[d];

				if(nx < 0 || nx >= copyGrid.length || ny < 0 || ny >= copyGrid[0].length) continue;
				if(copyGrid[nx][ny] != 0) continue;

				copyGrid[nx][ny] = 2;
				que.offer(new int[] {nx, ny});
			}
		}

		int safeZone = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if(copyGrid[i][j] == 0) safeZone++;
			}
		}

		ANSWER = Math.max(ANSWER, safeZone);
	}
}
