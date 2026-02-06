package Baekjoon.BackTracking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class S2_10819_차이를_최대로 {
	static int[] arr, perm;
	static boolean[] isVisited;
	static int N;
	static int MAX_RES = 0;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st;

		N = Integer.parseInt(br.readLine());
		arr = new int[N];
		perm = new int[N];
		isVisited = new boolean[N];

		st = new StringTokenizer(br.readLine(), " ");
		for (int i = 0; i < N; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}

		dfs(0);
		System.out.println(MAX_RES);
	}

	private static void dfs(int index) {
		if (index == N) {
			int sum = 0;
			for (int i = 0; i < N - 1; i++) {
				sum += Math.abs(perm[i] - perm[i + 1]);
			}
			MAX_RES = Math.max(MAX_RES, sum);
			return;
		}

		for (int i = 0; i < N; i++) {
			if(isVisited[i]) continue;

			isVisited[i] = true;
			perm[index] = arr[i];
			dfs(index + 1);
			isVisited[i] = false;
		}
	}
}