package Baekjoon.DivideAndConquer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class S1_1629_곱셈 {
	static long C;
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		long A = Integer.parseInt(st.nextToken());
		long B = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());

		System.out.println(divide(A % C, B));
	}

	private static long divide(long a, long b) {
		if (b == 0) {
			return 1 % C;
		}

		long temp = divide(a, b / 2);
		long res = (temp * temp) % C;
		if (b % 2 == 1) res  = (res * a) % C;

		return res % C;
	}
}