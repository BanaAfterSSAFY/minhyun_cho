package Baekjoon.Graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class G5_13023_ABCDE {
	static int N, M;
	static List<List<Integer>> graph;
	static boolean[] isVisited;
	static boolean found = false;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		N = Integer.parseInt(st.nextToken());
		M = Integer.parseInt(st.nextToken());

		graph = new ArrayList<>();
		isVisited = new boolean[N];

		for (int i = 0; i < N; i++) {
			graph.add(new ArrayList<>());
		}

		for (int i = 0; i < M; i++) {
			st = new StringTokenizer(br.readLine());

			int u = Integer.parseInt(st.nextToken());
			int v = Integer.parseInt(st.nextToken());

			graph.get(u).add(v);
			graph.get(v).add(u);
		}

		for (int i = 0; i < N; i++) {
			dfs(0, i);
			if (found) break;
		}

		System.out.println(found ? "1" : "0");
	}

	private static void dfs(int count, int target) {
		if (count == 4) {
			found = true;
			return;
		}

		isVisited[target] = true;
		for (int next : graph.get(target)) {
			if (!isVisited[next]) {
				dfs(count + 1, next);
				if (found) return;
			}
		}
		isVisited[target] = false;
	}
}