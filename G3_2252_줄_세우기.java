package Baekjoon.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

// 알고리즘: 위상정렬
// https://www.acmicpc.net/problem/2252
public class G3_2252_줄_세우기 {
	static int N, M;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken()); // 학생 N명의 수
		M = Integer.parseInt(st.nextToken()); // 키 비교 수

		List<List<Integer>> graph = new ArrayList<>();
		int[] indegree = new int[N + 1];

		for (int i = 0; i <= N; i++) {
			graph.add(new ArrayList<>());
		}

		for (int tc = 0; tc < M; tc++) {
			st = new StringTokenizer(br.readLine());
			int u = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());

			graph.get(u).add(v);
			indegree[v]++;
		}
		topologySort(indegree, graph);
	}

	private static void topologySort(int[] indegree, List<List<Integer>> graph) {
		StringBuilder sb = new StringBuilder();
		Queue<Integer> q = new LinkedList<>();

		// 큐에 indegree 가 0 인 노드 담기
		for (int i = 1; i <= N; i++) {
			if (indegree[i] == 0) {
				q.offer(i);
			}
		}

		while (!q.isEmpty()) {
			int node = q.poll();
			sb.append(node).append(" ");

			for (Integer i : graph.get(node)) {
				indegree[i]--;

				if (indegree[i] == 0) {
					q.offer(i);
				}
			}
		}

		System.out.println(sb);
	}
}