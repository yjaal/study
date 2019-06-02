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

	public int coinChange(int[] coins, int amount) {
		if (amount == 0) {
			return 0;
		}
		if (null == coins || coins.length == 0) {
			return -1;
		}
		Arrays.sort(coins);
		if (amount < coins[0]) {
			return -1;
		}
		int len = coins.length;
		for (int i = len - 1; i >= 0; i--) {
			int surplus = amount;
			if (surplus % coins[i] == 0) {
				return surplus / coins[i];
			}
			int min = surplus / coins[i];
			surplus %= coins[i];
			for (int j = i - 1; j >= 0; j--) {
				if (surplus >= coins[j]) {
					min += surplus / coins[j];
					surplus %= coins[j];
				}
			}
			if (surplus == 0) {
				return min;
			}
		}
		return -1;
	}
}
