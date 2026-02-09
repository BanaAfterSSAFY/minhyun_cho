package Baekjoon.UnPackaged;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

// https://www.acmicpc.net/problem/10972R
public class S3_10972_다음_순열 {
	static int[] arr;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(br.readLine());

		StringTokenizer st = new StringTokenizer(br.readLine(), " ");
		arr = new int[N];

		for (int i = 0; i < N; i++) {
			arr[i] = Integer.parseInt(st.nextToken());
		}

		if (nextPermutation(arr)) {
			StringBuilder sb = new StringBuilder();
			for (int num : arr) {
				sb.append(num).append(" ");
			}
			System.out.println(sb);
		} else {
			System.out.println("-1");
		}
	}

	private static boolean nextPermutation(int[] arr) {
		int n = arr.length;

		int i = n - 1;
		while (i > 0 && arr[i - 1] >= arr[i]) {
			i--;
		}

		if (i == 0) return false;

		int j = n - 1;
		while (arr[i - 1] >= arr[j]) {
			j--;
		}

		swap(arr, i - 1, j);

		int k = n - 1;
		while (i < k) {
			swap(arr, i++, k--);
		}

		return true;
	}

	private static void swap(int[] arr, int a, int b) {
		int temp = arr[a];
		arr[a] = arr[b];
		arr[b] = temp;
	}
}