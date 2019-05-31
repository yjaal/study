package leetcode.sort.dynamic;

/**
 * 264. 丑数 II
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 编写一个程序，找出第 n 个丑数。
 * 丑数就是只包含质因数 2, 3, 5 的正整数。
 * <p>
 * 示例:
 * 输入: n = 10
 * 输出: 12
 * 解释: 1, 2, 3, 4, 5, 6, 8, 9, 10, 12 是前 10 个丑数。
 * <p>
 * 说明:
 * 1 是丑数。
 * n 不超过1690。
 */
public class Solution264 {

	public static void main(String[] args) {
		Solution264 s = new Solution264();
		System.out.println(s.nthUglyNumber(10));
	}

	public int nthUglyNumber(int n) {
		int[] idx = new int[3];
		int[] dp = new int[n];
		dp[0] = 1;
		int value2 = 0, value3 = 0, value5 = 0, min = 0;
		for (int i = 1; i < n; i++) {
			value2 = dp[idx[0]] * 2;
			value3 = dp[idx[1]] * 3;
			value5 = dp[idx[2]] * 5;
			min = Math.min(value2, Math.min(value3, value5));
			if (min == value2) {
				idx[0]++;
			}
			if (min == value3) {
				idx[1]++;
			}
			if (min == value5) {
				idx[2]++;
			}
			dp[i] = min;
		}
		return dp[n - 1];
	}

	public int nthUglyNumber1(int n) {
		int[] ugly = new int[n];
		ugly[0] = 1;
		int i2 = 0, i3 = 0, i5 = 0;
		int factor2 = 2, factor3 = 3, factor5 = 5;
		for (int i = 1; i < n; i++) {
			int min = Math.min(Math.min(factor2, factor3), factor5);
			ugly[i] = min;
			if (min == factor2) {
				factor2 = 2 * ugly[++i2];
			}
			if (min == factor3) {
				factor3 = 3 * ugly[++i3];
			}
			if (min == factor5) {
				factor5 = 5 * ugly[++i5];
			}
		}
		return ugly[n - 1];
	}
}
