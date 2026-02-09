package Baekjoon.UnPackaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// https://www.acmicpc.net/problem/2961
public class S2_2961_도영이가_만든_맛있는_음식 {
	static int minDiff = Integer.MAX_VALUE;
	static int[] sArr;
	static int[] bArr;
	static int N;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		N = Integer.parseInt(br.readLine());
		sArr = new int[N];
		bArr = new int[N];

		for (int i = 0; i < N; i++) {
			StringTokenizer st = new StringTokenizer(br.readLine(), " ");
			sArr[i] = Integer.parseInt(st.nextToken());
			bArr[i] = Integer.parseInt(st.nextToken());
		}

		dfs(0, 1, 0, 0);
		System.out.println(minDiff);
	}

	private static void dfs(int count, int currSourScore, int currBitterScore, int selectCount) {
		if (count == N) {
			if(selectCount > 0) minDiff = Math.min(minDiff, Math.abs(currSourScore - currBitterScore));
			return;
		}

		dfs(count + 1, currSourScore * sArr[count], currBitterScore + bArr[count], selectCount + 1);

		dfs(count + 1, currSourScore, currBitterScore, selectCount);
	}
}