package Baekjoon.BFS_DFS;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

// https://www.acmicpc.net/problem/16928
public class G5_16928_뱀과_사다리_게임 {

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		int n = Integer.parseInt(st.nextToken()); // 사다리 수
		int m = Integer.parseInt(st.nextToken()); // 뱀 수

		int[] arr = new int[101];
		boolean[] isVisited = new boolean[101];

		for (int i = 0; i < n + m; i++) {
			st = new StringTokenizer(br.readLine(), " ");
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			arr[x] = y;
		}

		bfs(arr, isVisited, 1);
	}

	private static void bfs(int[] arr, boolean[] isVisited, int start) {
		Queue<int[]> que = new ArrayDeque<>();
		que.offer(new int[] {start, 0}); // {말의 칸(현재 서있는 칸), 주사위 카운트(0부터 증가)}
		isVisited[start] = true;

		while (!que.isEmpty()) {
			int[] curr = que.poll();

			if(curr[0] == 100) {
				System.out.println(curr[1]);
				return;
			}

			for (int i = 1; i < 7; i++) {
				int next = curr[0] + i;
				if(next <= 100) {
					if(arr[next] != 0) next = arr[next];
					if(!isVisited[next]) {
						que.offer(new int[] {next, curr[1] + 1});
						isVisited[next] = true;
					}
				}
			}
		}
	}
}
