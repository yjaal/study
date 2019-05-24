package leetcode.sort.dynamic;

import java.util.List;

/**
 * 120. 三角形最小路径和
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个三角形，找出自顶向下的最小路径和。每一步只能移动到下一行中相邻的结点上。
 * 例如，给定三角形：
 * [
 * [2],
 * [3,4],
 * [6,5,7],
 * [4,1,8,3]
 * ]
 * 自顶向下的最小路径和为 11（即，2 + 3 + 5 + 1 = 11）。
 * <p>
 * 说明：
 * 如果你可以只使用 O(n) 的额外空间（n 为三角形的总行数）来解决这个问题，那么你的算法会很加分。
 */
public class Solution120 {
	/**
	 * [
	 * [2],
	 * [3,4],
	 * [6,5,7],
	 * [4,1,8,3]
	 * ]
	 * 变成
	 * [
	 * [2],
	 * [3,4],
	 * [7,6,10],
	 * ]
	 * 变成
	 * [
	 * [2],
	 * [9,10],
	 * ]
	 * 变成
	 * [
	 * [11],
	 * ]
	 */
	public int minimumTotal(List<List<Integer>> triangle) {
		for (int i = triangle.size() - 2; i >= 0; i--) {
			for (int j = 0; j < triangle.get(i).size(); j++) {
				int temp = triangle.get(i).get(j) + Math.min(triangle.get(i + 1).get(j), triangle.get(i + 1).get(j + 1));
				triangle.get(i).set(j, temp);
			}
		}
		return triangle.get(0).get(0);
	}

	/**
	 * 优化，其实基本思想是一样的，不过上面的更容易理解
	 */
	public int minimumTotal1(List<List<Integer>> lists) {
		int len = lists.size();
		int[] dp = new int[len + 1];
		for (int i = len - 1; i >= 0; i--) {
			List<Integer> list = lists.get(i);
			for (int j = 0; j < list.size(); j++) {
				dp[j] = Math.min(dp[j], dp[j + 1]) + list.get(j);
			}
		}
		return dp[0];
	}
}
