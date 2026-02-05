package Baekjoon.BackTracking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class S3_10974_모든_순열 {
	static int[] arr, perm;
	static boolean[] isVisited;
	static int N;
	static StringBuilder sb;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		sb = new StringBuilder();
		N = Integer.parseInt(br.readLine());

		arr = new int[N];
		perm = new int[N];
		isVisited = new boolean[N];
		for (int i = 0; i < N; i++) {
			arr[i] = i + 1;
		}

		dfs(0);
		System.out.println(sb);
	}

	private static void dfs(int depth) {
		if (depth == N) {
			for(int p: perm) sb.append(p).append(' ');
			sb.append('\n');
		}

		for (int i = 0; i < N; i++) {
			if (isVisited[i]) continue;

			isVisited[i] = true;
			perm[depth] = arr[i];
			dfs(depth + 1);
			isVisited[i] = false;
		}
	}
}