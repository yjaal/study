package leetcode.sort.array;

/**
 * 64. 最小路径和(重复)
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个包含非负整数的 m x n 网格，请找出一条从左上角到右下角的路径，使得路径上的数字总和为最小。
 * <p>
 * 说明：每次只能向下或者向右移动一步。
 * <p>
 * 示例:
 * <p>
 * 输入:
 * [
 * [1,3,1],
 * [1,5,1],
 * [4,2,1]
 * ]
 * 输出: 7
 * 解释: 因为路径 1→3→1→1→1 的总和最小。
 */
public class Solution64 {

	/**
	 * 使用动态规划的思想解决本题：
	 * 关键是求出递推关系式。对于本题，从原点到达（i, j）的最小路径
	 * 等于 ：原点到达（i-1, j）最小路径与到达（i, j-1）最小路径中
	 * 的最小值。即 dp[i][j] = Math.min(grid[i - 1][j], grid[i][j - 1]) + grid[i][j]。
	 */
	public int minPathSum(int[][] grid) {
		for (int i = 1; i < grid.length; i++) {
			grid[i][0] += grid[i - 1][0];
		}
		for (int j = 1; j < grid[0].length; j++) {
			grid[0][j] += grid[0][j - 1];
		}

		for (int i = 1; i < grid.length; i++) {
			for (int j = 1; j < grid[i].length; j++) {
				grid[i][j] = Math.min(grid[i - 1][j], grid[i][j - 1]) + grid[i][j];
			}
		}
		return grid[grid.length - 1][grid[0].length - 1];
	}

	/**
	 * 动态规划优化(和62、63类似)
	 */
	public int minPathSum1(int[][] grid) {
		int[] path = new int[grid[0].length];
		path[0] = grid[0][0];
		for (int i = 1; i < path.length; i++) {
			path[i] = path[i - 1] + grid[0][i];
		}
		for (int i = 1; i < grid.length; i++) {
			for (int j = 0; j < path.length; j++) {
				path[j] = j == 0 ? path[j] + grid[i][j] : Math.min(path[j - 1], path[j]) + grid[i][j];
			}
		}
		return path[path.length - 1];
	}

}
