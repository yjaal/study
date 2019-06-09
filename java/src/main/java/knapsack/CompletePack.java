package knapsack;

/**
 * 完全背包问题
 * 有num件物品，第i物品占用空间为costs[i]，价值为values[i]，背包容量为cap
 * 如何将物品放入背包才能使得背包中物品总价值最大，物品可以放多次
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class CompletePack {

	public static void main(String[] args) {
		CompletePack s = new CompletePack();
		//第一个元素不算
		int[] values = new int[]{0, 3, 4, 5, 6};
		int[] costs = new int[]{0, 2, 3, 4, 5};
		int num = values.length - 1;
		int cap = 13;
		System.out.println(s.solution(num, cap, values, costs));
	}

	/**
	 * 时间复杂度为O(num * cap * k), 空间复杂度为O(num * cap * k)
	 *
	 * @param num    物品的总个数
	 * @param cap    背包的总容量
	 * @param values 物品价值
	 * @param costs  物品占用（体积/重量）
	 * @return 可获得的最大价值
	 */
	public int solution(int num, int cap, int[] values, int[] costs) {
		int[][] worths = new int[num + 1][cap + 1];
		for (int i = 1; i < num + 1; i++) {
			for (int j = 1; j < cap + 1; j++) {
				for (int k = 1; k * costs[i] <= j; k++) {
					worths[i][j] = Math.max(worths[i][j], worths[i - 1][j - k * costs[i]] + k * values[i]);
				}
			}
		}
		int maxRes = Integer.MIN_VALUE;
		for (int i = 1; i < num + 1; i++) {
			maxRes = Math.max(maxRes, worths[i][cap]);
		}
		return maxRes;
	}

	/**
	 * 优化
	 * 时间复杂度为O(num * cap), 空间复杂度为O(cap)
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
			for (int j = costs[i]; j < cap + 1; j++) {
				worths[j] = Math.max(worths[j], worths[j - costs[i]] + values[i]);
			}
		}
		return worths[cap];
	}
}
