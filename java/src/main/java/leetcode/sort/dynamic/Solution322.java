package leetcode.sort.dynamic;

import java.util.Arrays;

/**
 * 322. 零钱兑换
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 给定不同面额的硬币 coins 和一个总金额 amount。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。
 * 如果没有任何一种硬币组合能组成总金额，返回 -1。
 * <p>
 * 示例 1:
 * 输入: coins = [1, 2, 5], amount = 11
 * 输出: 3
 * 解释: 11 = 5 + 5 + 1
 * <p>
 * 示例 2:
 * 输入: coins = [2], amount = 3
 * 输出: -1
 * <p>
 * 说明:
 * 你可以认为每种硬币的数量是无限的。
 */
public class Solution322 {
	public static void main(String[] args) {
		Solution322 s = new Solution322();
		System.out.println(s.coinChange(new int[]{186, 419, 83, 408}, 6249));
	}

	/**
	 * 和完全背包问题很像
	 */
	public int coinChange(int[] coins, int amount) {
		//存储硬币数量
		int dp[] = new int[amount + 1];
		Arrays.fill(dp, amount + 1);
		dp[0] = 0;

		for (int i = 1; i <= amount; i++) {
			for (int j = 0; j < coins.length && coins[j] <= i; j++) {
				dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
			}
		}
		return dp[amount] > amount ? -1 : dp[amount];
	}

	private static int maxValue = 100000000;

	private int search(int index, int amount, int[] coins) {
		if (index >= coins.length) {
			return maxValue;
		}

		if (amount == 0) {
			return 0;
		}

		if (amount < 0) {
			return maxValue;
		}
		//这里前一个递归表示某次放当前硬币，后一个递归表示某次不放当前硬币
		return Math.min(search(index, amount - coins[index], coins) + 1,
				search(index + 1, amount, coins));
	}

	public int coinChange1(int[] coins, int amount) {
		Arrays.sort(coins);
		int val = search(0, amount, coins);
		if (val < maxValue) {
			return val;
		} else {
			return -1;
		}
	}

	/**
	 * 最优解
	 */
	public int coinChange2(int[] coins, int amount) {
		Arrays.sort(coins);
		process(coins, amount, 0, coins.length - 1);
		return min == Integer.MAX_VALUE ? -1 : min;
	}

	int min = Integer.MAX_VALUE;

	private void process(int[] coins, int amount, int count, int index) {
		if (index < 0 || count + amount / coins[index] >= min) {
			return;
		}
		if (amount % coins[index] == 0) {
			min = Math.min(min, count + amount / coins[index]);
			return;
		}
		for (int i = amount / coins[index]; i >= 0; i--) {
			process(coins, amount - coins[index] * i, count + i, index - 1);
		}
	}
}
