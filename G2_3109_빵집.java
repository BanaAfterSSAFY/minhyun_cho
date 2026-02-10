package Baekjoon.BFS_DFS;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class G2_3109_빵집 {
	static char[][] grid;
	static int[] dirR = {-1, 0, 1};
	static int R, C;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());

		grid = new char[R][C];

		// 그리드 정보 입력
		for (int i = 0; i < R; i++) {
			grid[i] = br.readLine().toCharArray();
		}

		int res = 0;

		for (int i = 0; i < R; i++) {
			if(dfs(i, 0)) res += 1;
		}

		System.out.println(res);
	}

	private static boolean dfs(int x, int y) {
		grid[x][y] = 'x';
		// 기저 조건: 끝에 닿는 경우
		if (y == C - 1) return true;

		// 3방향 탐색 { 오른쪽 위, 오른쪽, 오른쪽 아래 }
		for (int d = 0; d < 3; d++) {
			int dx = x + dirR[d];
			int dy = y + 1;

			if (dx >= 0 && dx < R && dy < C && grid[dx][dy] == '.') {
				if (dfs(dx, dy)) return true;
			}
		}
		return false;
	}
}