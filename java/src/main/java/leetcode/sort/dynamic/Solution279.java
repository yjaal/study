package leetcode.sort.dynamic;

/**
 * 279. 完全平方数
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 给定正整数 n，找到若干个完全平方数（比如 1, 4, 9, 16, ...）使得它们的和等于 n。你需要让组成和的完全平方数的个数最少。
 * <p>
 * 示例 1:
 * 输入: n = 12
 * 输出: 3
 * 解释: 12 = 4 + 4 + 4.
 * <p>
 * 示例 2:
 * 输入: n = 13
 * 输出: 2
 * 解释: 13 = 4 + 9.
 */
public class Solution279 {

	/**
	 * 记住套路
	 */
	public int numSquares(int n) {
		int[] dp = new int[n + 1];
		if (n == 0) {
			return 0;
		}
		dp[0] = 0;
		for (int i = 1; i <= n; i++) {
			int min = Integer.MAX_VALUE;
			for (int j = 1; j * j <= i; j++) {
				min = Math.min(min, dp[i - j * j] + 1);
			}
			dp[i] = min;
		}
		return dp[n];
	}


	/**
	 * 最优解
	 */
	public int numSquares1(int n) {
		if (isSquare(n)) {
			return 1;
		}
		while ((n & 3) == 0) {
			//n是4的倍数
			n >>= 2;
		}
		if ((n & 7) == 7) {
			//n是8的倍数
			return 4;
		}
		int num = (int) Math.sqrt(n);
		for (int i = 0; i <= num; i++) {
			if (isSquare(n - i * i)) {
				return 2;
			}
		}
		return 3;
	}

	/**
	 * 判断一个数是否可以是另一个数的平方
	 */
	private boolean isSquare(int n) {
		int num = (int) Math.sqrt(n);
		return num * num == n;
	}
}
