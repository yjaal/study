package knapsack;

import java.util.Arrays;

/**
 * 多重背包问题
 * 有num件物品，第i物品占用空间为costs[i]，价值为values[i]，每种物品的数量为counts[i]，背包容量为cap
 * 如何将物品放入背包才能使得背包中物品总价值最大，物品最多可以放值counts[i]次
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class MultiplePack {

	public static void main(String[] args) {
		MultiplePack s = new MultiplePack();
		//第一个元素不算
		int[] values = new int[]{0, 3, 4, 5, 6};
		int[] costs = new int[]{0, 2, 3, 4, 5};
		int[] counts = new int[]{0, 1, 2, 3, 4};
		int num = values.length - 1;
		int cap = 8;
		System.out.println(s.solution1(num, cap, values, costs, counts));
	}

	/**
	 * 时间复杂度为O(num * cap * k), 空间复杂度为O(num * cap * k)
	 *
	 * @param num    物品的总个数
	 * @param cap    背包的总容量
	 * @param values 物品价值
	 * @param costs  物品占用（体积/重量）
	 * @param counts 物品件数
	 * @return 可获得的最大价值
	 */
	public int solution(int num, int cap, int[] values, int[] costs, int[] counts) {
		int[][] worths = new int[num + 1][cap + 1];
		int[] flags = new int[num + 1];
		for (int i = 1; i < num + 1; i++) {
			for (int j = 1; j < cap + 1; j++) {
				for (int k = 1; k <= counts[i] && k * costs[i] <= j; k++) {
					worths[i][j] = Math.max(worths[i - 1][j], worths[i - 1][j - k * costs[i]] + k * values[i]);
				}
			}
		}
		return worths[num][cap];
	}

	/**
	 * 优化
	 * 时间复杂度为O(num * cap), 空间复杂度为O(cap)
	 *
	 * @param num    物品的总个数
	 * @param cap    背包的总容量
	 * @param values 物品价值
	 * @param costs  物品占用（体积/重量）
	 * @param counts 物品件数
	 * @return 可获得的最大价值
	 */
	public int solution1(int num, int cap, int[] values, int[] costs, int[] counts) {
		int[] worths = new int[cap + 1];
		int[] times = new int[cap + 1];
		Arrays.fill(times, 1);
		for (int i = 1; i < num + 1; i++) {
			for (int j = costs[i]; j < cap + 1; j++) {
				worths[j] = Math.max(worths[j], worths[j - costs[i]] + values[i]);
			}
		}
		return worths[cap];
	}
}
