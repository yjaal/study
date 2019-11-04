package leetcode.sort.dynamic;

/**
 * 整数拆分
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个正整数 n，将其拆分为至少两个正整数的和，并使这些整数的乘积最大化。 返回你可以获得的最大乘积。
 * 示例 1:
 * 输入: 2
 * 输出: 1
 * 解释: 2 = 1 + 1, 1 × 1 = 1。
 * <p>
 * 示例 2:
 * 输入: 10
 * 输出: 36
 * 解释: 10 = 3 + 3 + 4, 3 × 3 × 4 = 36。
 * 说明: 你可以假设 n 不小于 2 且不大于 58。
 */
public class Solution343 {

	public int integerBreak(int n) {
		int[] dp = new int[n + 1];
		if (n == 2) {
			return 1;
		}
		if (n == 3) {
			return 2;
		}
		dp[2] = 1;
		dp[3] = 2;
		for (int i = 4; i < n + 1; i++) {
			int tmp = i / 2;
			int c1 = tmp > dp[tmp] ? tmp : dp[tmp];
			int c2 = (i - tmp) > dp[i - tmp] ? i - tmp : dp[i - tmp];
			int c3 = (tmp - 1) > dp[tmp - 1] ? tmp - 1 : dp[tmp - 1];
			int c4 = (i - tmp + 1) > dp[i - tmp + 1] ? i - tmp + 1 : dp[i - tmp + 1];
			dp[i] = Math.max(c1 * c2, c3 * c4);
		}
		return dp[n];
	}
}
