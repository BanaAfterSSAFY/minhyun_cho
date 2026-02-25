package Baekjoon.UnPackaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class G3_17135_캐슬_디펜스 {
	static int N, M, D, MAX_KILL_COUNT;
	static int[][] grid;
	static int[][] map;
	static boolean[] isVisited;

	static int[] dx = {0, -1, 0};
	static int[] dy = {-1, 0, 1};

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());
		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());
		D = Integer.parseInt(st.nextToken());

		grid = new int[N][M];
		isVisited = new boolean[M];

		for (int i = 0; i < N; i++) {
			st = new StringTokenizer(br.readLine());
			for (int j = 0; j < M; j++) {
				grid[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		dfs(0, 0, new int[3]);
		System.out.println(MAX_KILL_COUNT);
	}

	private static void dfs(int count, int start, int[] archers) {
		if (count == 3) {
			bfs(archers);
			return;
		}

		for (int i = start; i < M; i++) {
			if (isVisited[i]) continue;

			archers[count] = i;
			isVisited[i] = true;
			dfs(count + 1, start + 1, archers);
			isVisited[i] = false;
		}
	}

	private static void bfs(int[] archers) {
		map = new int[N][M];
		for (int i = 0; i < N; i++) map[i] = grid[i].clone();

		int killCount = 0;

		// 적이 맵에 남아있는 동안 반복
		while (true) {
			if (!hasEnemy()) break;

			// 이번 턴에 공격받을 적의 위치 저장 (중복 제거를 위해 별도 관리)
			boolean[][] targets = new boolean[N][M];
			int turnKill = 0;

			for (int archerPos : archers) {
				// 각 궁수마다 BFS로 타겟 찾기
				int[] target = findTarget(archerPos);
				if (target != null) {
					targets[target[0]][target[1]] = true;
				}
			}

			// 화살 맞은 적 제거 및 카운트
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < M; j++) {
					if (targets[i][j]) {
						map[i][j] = 0;
						turnKill++;
					}
				}
			}
			killCount += turnKill;

			// 적 이동
			moveEnemies();
		}

		MAX_KILL_COUNT = Math.max(MAX_KILL_COUNT, killCount);
	}

	private static int[] findTarget(int archerCol) {
		Queue<int[]> q = new ArrayDeque<>();
		boolean[][] visited = new boolean[N][M];

		q.offer(new int[]{N - 1, archerCol, 1});
		visited[N - 1][archerCol] = true;

		while (!q.isEmpty()) {
			int[] curr = q.poll();
			int r = curr[0];
			int c = curr[1];
			int dist = curr[2];

			if (map[r][c] == 1) return new int[]{r, c};

			if (dist < D) {
				for (int i = 0; i < 3; i++) {
					int nr = r + dx[i];
					int nc = c + dy[i];

					if (nr >= 0 && nr < N && nc >= 0 && nc < M && !visited[nr][nc]) {
						visited[nr][nc] = true;
						q.offer(new int[]{nr, nc, dist + 1});
					}
				}
			}
		}
		return null;
	}

	private static void moveEnemies() {
		for (int i = N - 1; i > 0; i--) {
			map[i] = map[i - 1].clone();
		}
		map[0] = new int[M];
	}

	private static boolean hasEnemy() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				if (map[i][j] == 1) return true;
			}
		}
		return false;
	}
}