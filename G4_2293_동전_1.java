package Baekjoon.DP;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// https://www.acmicpc.net/problem/2293
public class G4_2293_동전_1 {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine(), " ");

		int n = Integer.parseInt(st.nextToken()); // 동전의 수
		int k = Integer.parseInt(st.nextToken()); // 만들 합

		int[] arr = new int[n];
		int[] dp = new int[k + 1];
		dp[0] = 1;

		for (int i = 0; i < n; i++) {
			arr[i] = Integer.parseInt(br.readLine());
			for (int j = arr[i]; j < k + 1; j++) {
				dp[j] += dp[j - arr[i]];
			}
		}

		System.out.println(dp[k]);
	}
}
