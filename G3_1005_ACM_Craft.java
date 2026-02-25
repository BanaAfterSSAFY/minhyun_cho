package Baekjoon.UnPackaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class G3_1005_ACM_Craft {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int T = Integer.parseInt(br.readLine());

		for (int tc = 0; tc < T; tc++) {
			StringTokenizer st = new StringTokenizer(br.readLine());
			int N = Integer.parseInt(st.nextToken());
			int M = Integer.parseInt(st.nextToken());

			List<List<Integer>> graph = new ArrayList<>();
			int[] times = new int[N + 1]; // 건설 시간 배열 [1 ~ N]
			int[] indegree = new int[N + 1];
			int[] dp = new int[N + 1];

			for (int i = 0; i <= N; i++) {
				graph.add(new ArrayList<>());
			}

			// 건설 시간 배열 저장
			st = new StringTokenizer(br.readLine());
			for (int i = 1; i <= N; i++) {
				times[i] = Integer.parseInt(st.nextToken());
				dp[i] = times[i];
			}

			for (int i = 0; i < M; i++) {
				st = new StringTokenizer(br.readLine());
				int u = Integer.parseInt(st.nextToken());
				int v = Integer.parseInt(st.nextToken());
				graph.get(u).add(v);
				indegree[v]++;
			}

			int W = Integer.parseInt(br.readLine());

			int res = topologySort(times, indegree, dp, graph, W);
			System.out.println(res);
		}
	}

	private static int topologySort(int[] times, int[] indegree, int[] dp, List<List<Integer>> graph, int W) {
		Queue<Integer> que = new ArrayDeque<>();

		for (int i = 1; i < indegree.length; i++) {
			if (indegree[i] == 0) {
				que.offer(i);
			}
		}

		while (!que.isEmpty()) {
			int curr = que.poll();

			if (curr == W) return dp[curr];

			for (int next : graph.get(curr)) {
				// 핵심 DP 점화식: 가장 오래 걸리는 이전 트리의 누적 시간을 취함
				dp[next] = Math.max(dp[next], dp[curr] + times[next]);
				indegree[next]--;

				// 선행 건물들이 모두 지어졌다면 큐에 삽입
				if (indegree[next] == 0) que.offer(next);
			}
		}

		return dp[W];
	}
}