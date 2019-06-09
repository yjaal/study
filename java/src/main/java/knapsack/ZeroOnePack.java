package knapsack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 0-1背包问题
 * 有num件物品，第i物品占用空间为costs[i]，价值为values[i]，背包容量为cap
 * 如何将物品放入背包才能使得背包中物品总价值最大，物品只能放或者不放，一件物品不能
 * 放置多次
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class ZeroOnePack {

	public static void main(String[] args) {
		ZeroOnePack s = new ZeroOnePack();
		//第一个元素不算
		int[] values = new int[]{0, 3, 4, 5, 6};
		int[] costs = new int[]{0, 2, 3, 4, 5};
		int num = values.length - 1;
		int cap = 11;
		System.out.println(s.solution1(num, cap, values, costs));
	}

	/**
	 * 时间复杂度为O(num * cap), 空间复杂度为O(num * cap)
	 * 不要求装满
	 *
	 * @param num    物品的总个数
	 * @param cap    背包的总容量
	 * @param values 物品价值
	 * @param costs  物品占用（体积/重量）
	 * @return 可获得的最大价值
	 */
	public int solution(int num, int cap, int[] values, int[] costs) {
		int[][] worths = new int[num + 1][cap + 1];
		int[] flags = new int[num + 1];
		for (int i = 1; i < num + 1; i++) {
			for (int j = 1; j < cap + 1; j++) {
				if (j >= costs[i]) {
					worths[i][j] = Math.max(worths[i - 1][j], worths[i - 1][j - costs[i]] + values[i]);
				} else {
					worths[i][j] = worths[i - 1][j];
				}
			}
		}
		List<Integer> list = findBest(num, cap, values, costs, worths);
		return worths[num][cap];
	}

	/**
	 * 寻找最优组合可能不能优化，因为优化后的方式每次只保存一层i的情况，并不能保存
	 * 每个i值的情况
	 */
	public List<Integer> findBest(int num, int cap, int[] values, int[] costs, int[][] worths) {
		List<Integer> list = new ArrayList<>();
		int maxWorth = worths[num][cap];
		while (maxWorth >= 0) {
			//表示没有放入背包
			if (num > 0 && worths[num][cap] == worths[num - 1][cap]) {
				num--;
			} else {
				if (num == 0) {
					break;
				} else {
					list.add(num);
					maxWorth -= values[num];
					cap -= costs[num];
					num--;
				}
			}
		}
		return list;
	}

	/**
	 * 优化
	 * 时间复杂度为O(num * cap), 空间复杂度为O(cap)
	 * 不要求装满
	 *
	 * @param num    物品的总个数
	 * @param cap    背包的总容量
	 * @param values 物品价值
	 * @param costs  物品占用（体积/重量）
	 * @return 可获得的最大价值
	 */
	public int solution1(int num, int cap, int[] values, int[] costs) {
		int[] worths = new int[cap + 1];
		for (int i = 1; i < num + 1; i++) {
			for (int j = cap; j >= costs[i]; j--) {
				worths[j] = Math.max(worths[j], worths[j - costs[i]] + values[i]);
			}
		}
		return worths[cap];
	}

	/**
	 * 时间复杂度为O(num * cap), 空间复杂度为O(cap)
	 * 要求装满
	 *
	 * @param num    物品的总个数
	 * @param cap    背包的总容量
	 * @param values 物品价值
	 * @param costs  物品占用（体积/重量）
	 * @return 可获得的最大价值
	 */
	public int solution2(int num, int cap, int[] values, int[] costs) {
		int[] worths = new int[cap + 1];
		Arrays.fill(worths, Integer.MIN_VALUE);
		worths[0] = 0;
		for (int i = 1; i < num + 1; i++) {
			for (int j = cap; j >= costs[i]; j--) {
				worths[j] = Math.max(worths[j], worths[j - costs[i]] + values[i]);
			}
		}
		return worths[cap];
	}
}
