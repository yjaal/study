package leetcode.sort.array;

/**
 * 63. 不同路径 II(重复)
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
 * 现在考虑网格中有障碍物。那么从左上角到右下角将会有多少条不同的路径？
 * 网格中的障碍物和空位置分别用 1 和 0 来表示。
 * <p>
 * 说明：m 和 n 的值均不超过 100。
 * <p>
 * 示例 1:
 * 输入:
 * [
 * [0,0,0],
 * [0,1,0],
 * [0,0,0]
 * ]
 * 输出: 2
 * 解释:
 * 3x3 网格的正中间有一个障碍物。
 * 从左上角到右下角一共有 2 条不同的路径：
 * 1. 向右 -> 向右 -> 向下 -> 向下
 * 2. 向下 -> 向下 -> 向右 -> 向右
 */
public class Solution63 {
	public static void main(String[] args) {
		Solution63 s = new Solution63();
		int[][] obstacleGrid = new int[][]{{0, 0}, {1, 1}, {0, 0}};
		System.out.println(s.uniquePathsWithObstacles(obstacleGrid));
	}

	public int uniquePathsWithObstacles(int[][] obstacleGrid) {
		if (obstacleGrid == null || obstacleGrid[0] == null) {
			return 0;
		}
		int m = obstacleGrid[0].length;
		int n = obstacleGrid.length;
		if (m < 0 || m > 100 || n < 0 || n > 100) {
			return 0;
		}
		int[][] res = new int[n][m];
		if (obstacleGrid[0][0] == 1) {
			return 0;
		}
		res[0][0] = 1;
		for (int num = 0; num < n; num++) {
			for (int col = 0; col < m; col++) {
				boolean flag = obstacleGrid[num][col] != 1;
				if (flag) {
					if (num == 0 && col != 0) {
						res[num][col] = res[num][col - 1];
						continue;
					}
					if (num != 0 && col == 0) {
						res[num][col] = res[num - 1][col];
						continue;
					}
					if (num != 0) {
						res[num][col] = res[num - 1][col] + res[num][col - 1];
					}
				}
			}
		}
		return res[n - 1][m - 1];
	}

	/**
	 * 最优解答
	 */
	public int uniquePathsWithObstacles1(int[][] obstacleGrid) {
		int[] dp = new int[obstacleGrid[0].length + 1];
		for (int i = 0; i < obstacleGrid[0].length; i++) {
			if (obstacleGrid[0][i] == 1) {
				break;
			}
			dp[i + 1] = 1;
		}
		for (int i = 1; i < obstacleGrid.length; i++) {
			for (int j = 0; j < obstacleGrid[0].length; j++) {
				if (obstacleGrid[i][j] == 1) {
					dp[j + 1] = 0;
				} else {
					dp[j + 1] = dp[j] + dp[j + 1];
				}
			}
		}
		return dp[obstacleGrid[0].length];
	}
}
