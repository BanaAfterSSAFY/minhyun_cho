package Baekjoon.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class G4_16169_수행_시간 {
	static int N;
	static int[] times, dp, degree;
	static List<List<Integer>> graph;
	static List<List<Integer>> rankGroups;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());

		times = new int[N + 1];
		dp = new int[N + 1];
		degree = new int[N + 1];
		graph = new ArrayList<>();
		rankGroups = new ArrayList<>();

		for (int i = 0; i <= N; i++) {
			graph.add(new ArrayList<>());
			rankGroups.add(new ArrayList<>());
		}

		int maxRank = 0;

		for (int i = 1; i <= N; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine());

			int rank = Integer.parseInt(st.nextToken());
			int processTime = Integer.parseInt(st.nextToken());

			times[i] = processTime;
			dp[i] = processTime;
			rankGroups.get(rank).add(i);
			maxRank = Math.max(maxRank, rank);
		}

		for (int r = 1; r < maxRank; r++) {
			for (int u : rankGroups.get(r)) {
				for (int v : rankGroups.get(r + 1)) {
					graph.get(u).add(v);
					degree[v]++;
				}
			}
		}

		solution();
	}

	private static void solution() {
		Queue<Integer> que = new ArrayDeque<>();

		for (int i = 1; i <= N; i++) {
			if (degree[i] == 0) que.offer(i);
		}

		int totalMaxTime = 0;

		while (!que.isEmpty()) {
			int curr = que.poll();

			totalMaxTime = Math.max(totalMaxTime, dp[curr]);

			for (int next : graph.get(curr)) {
				int transferTime = (curr - next) * (curr - next);

				// 다음 노드의 종료 시간 = MAX(기존 값, 현재 종료 시간 + 전송 시간 + 다음 노드 본인 작업 시간)
				dp[next] = Math.max(dp[next], dp[curr] + transferTime + times[next]);

				degree[next]--;

				if (degree[next] == 0) {
					que.offer(next);
				}
			}
		}

		System.out.println(totalMaxTime);
	}
}