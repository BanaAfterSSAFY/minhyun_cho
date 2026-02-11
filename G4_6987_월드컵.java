package Baekjoon.UnPackaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class G4_6987_월드컵 {
	static int[][] matches;
	static int[][] results;

	static boolean isPossible;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		matches = new int[15][2];
		results = new int[6][3];
		// 경기 대진표 기록
		int idx = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = i + 1; j < 6; j++) {
				matches[idx][0] = i;
				matches[idx++][1] = j;
			}
		}

		for (int i = 0; i < 4; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");

			isPossible = true;
			int totalWin = 0;
			int totalDraw = 0;
			int totalLose = 0;

			for (int j = 0; j < 6; j++) {
				int win = Integer.parseInt(st.nextToken()); // 승
				int draw = Integer.parseInt(st.nextToken()); // 무
				int lose = Integer.parseInt(st.nextToken()); // 패
				results[j][0] = win;
				results[j][1] = draw;
				results[j][2] = lose;

				totalWin += win;
				totalDraw += draw;
				totalLose += lose;

				// 기본 규칙1: 승무패의 합이 5가 아니면 안됨.
				if (win + draw + lose != 5) isPossible = false;
			}

			// 기본 규칙2: 승패의 합이 동일하지 않으면 안됨.
			if (totalWin != totalLose) isPossible = false;

			// 기본 규칙3: 무승부의 합이 홀수이면 안됨.
			if (totalDraw % 2 == 1) isPossible = false;

			if (isPossible) {
				isPossible = false;
				dfs(0);
			}

			sb.append(isPossible ? '1' : '0').append(' ');
		}

		System.out.println(sb);
	}

	private static void dfs(int round) {
		if (isPossible) return;

		if (round == 15) {
			isPossible = true;
			return;
		}

		int[] teamA = results[matches[round][0]];
		int[] teamB = results[matches[round][1]];

		// [0]: win, [1]: draw, [2]: lose
		if (teamA[0] > 0 && teamB[2] > 0) {
			teamA[0]--;
			teamB[2]--;
			dfs(round + 1);
			teamA[0]++;
			teamB[2]++;
		}

		if (teamA[1] > 0 && teamB[1] > 0) {
			teamA[1]--;
			teamB[1]--;
			dfs(round + 1);
			teamA[1]++;
			teamB[1]++;
		}

		if (teamA[2] > 0 && teamB[0] > 0) {
			teamA[2]--;
			teamB[0]--;
			dfs(round + 1);
			teamA[2]++;
			teamB[0]++;
		}
	}
}