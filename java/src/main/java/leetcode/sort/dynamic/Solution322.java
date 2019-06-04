package leetcode.sort.dynamic;

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
		
		return -1;
	}
}
