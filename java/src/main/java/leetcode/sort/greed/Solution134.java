package leetcode.sort.greed;

/**
 * 134. 加油站
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 在一条环路上有 N 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
 * 你有一辆油箱容量无限的的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。你从其中的一个加油站出发，开始时油箱为空。
 * 如果你可以绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
 * <p>
 * 说明: 
 * 如果题目有解，该答案即为唯一答案。
 * 输入数组均为非空数组，且长度相同。
 * 输入数组中的元素均为非负数。
 * 示例 1:
 * <p>
 * 输入:
 * gas  = [1,2,3,4,5]
 * cost = [3,4,5,1,2]
 * 输出: 3
 * 解释:
 * 从 3 号加油站(索引为 3 处)出发，可获得 4 升汽油。此时油箱有 = 0 + 4 = 4 升汽油
 * 开往 4 号加油站，此时油箱有 4 - 1 + 5 = 8 升汽油
 * 开往 0 号加油站，此时油箱有 8 - 2 + 1 = 7 升汽油
 * 开往 1 号加油站，此时油箱有 7 - 3 + 2 = 6 升汽油
 * 开往 2 号加油站，此时油箱有 6 - 4 + 3 = 5 升汽油
 * 开往 3 号加油站，你需要消耗 5 升汽油，正好足够你返回到 3 号加油站。
 * 因此，3 可为起始索引。
 * <p>
 * 示例 2:
 * 输入:
 * gas  = [2,3,4]
 * cost = [3,4,3]
 * 输出: -1
 * 解释:
 * 你不能从 0 号或 1 号加油站出发，因为没有足够的汽油可以让你行驶到下一个加油站。
 * 我们从 2 号加油站出发，可以获得 4 升汽油。 此时油箱有 = 0 + 4 = 4 升汽油
 * 开往 0 号加油站，此时油箱有 4 - 3 + 2 = 3 升汽油
 * 开往 1 号加油站，此时油箱有 3 - 3 + 3 = 3 升汽油
 * 你无法返回 2 号加油站，因为返程需要消耗 4 升汽油，但是你的油箱只有 3 升汽油。
 * 因此，无论怎样，你都不可能绕环路行驶一周。
 */
public class Solution134 {
	public static void main(String[] args) {
		Solution134 s = new Solution134();
		int[] gas = new int[]{1, 2, 3, 4, 5};
		int[] cost = new int[]{3, 4, 5, 1, 2};
		System.out.println(s.canCompleteCircuit(gas, cost));
	}

	public int canCompleteCircuit(int[] gas, int[] cost) {
		int len = gas.length;
		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += gas[i] - cost[i];
		}
		if (sum < 0) {
			return -1;
		}
		for (int start = 0; start < len; start++) {
			int i = start + 1;
			int cap = gas[start] - cost[start];
			if (cap < 0) {
				continue;
			}
			while (true) {
				if (i == len) {
					i = 0;
				}
				cap += gas[i] - cost[i];
				if (cap < 0) {
					break;
				} else {
					if (start == i++) {
						return start;
					}
				}
			}
		}
		return -1;
	}

	/**
	 * 这种方式也很巧妙
	 * 首先有gas和cost得到一个数组{-2,-2,-2,3,3}，同时如果总油耗不能
	 * 满足走完全程的话直接返回-1
	 * 后面的就可以只循环一次数组，首先要找到一个起点，同时还要向后
	 * 循环，如果某次pre+gas[i] < 0，那么就需要重新寻找起点
	 */
	public int canCompleteCircuit1(int[] gas, int[] cost) {
		int sum = 0;
		for (int i = 0; i < gas.length; i++) {
			gas[i] -= cost[i];
			sum += gas[i];
		}
		if (sum < 0) {
			return -1;
		}
		int pre = 0;
		int index = -1;
		for (int i = 0; i < gas.length; i++) {
			if (gas[i] >= 0 && index == -1) {
				index = i;
				pre = gas[i];
				continue;
			}

			if (gas[i] + pre < 0) {
				pre = 0;
				index = -1;
			} else {
				pre += gas[i];
			}

		}
		return index;

	}

	/**
	 * 最优解，这其实和上面的方法类似
	 */
	public int canCompleteCircuit2(int[] gas, int[] cost) {
		if (gas.length < cost.length) {
			return -1;
		}
		int total = 0, sum = 0, start = 0;
		for (int i = 0; i < gas.length; ++i) {
			//判断是否可能
			total += gas[i] - cost[i];
			if (sum < 0) {
				sum = gas[i] - cost[i];
				start = i;
			} else {
				sum += gas[i] - cost[i];
			}
		}
		return total < 0 ? -1 : start;
	}
}
