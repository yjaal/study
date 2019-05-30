package leetcode.sort.dynamic;

/**
 * 221. 最大正方形
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 在一个由 0 和 1 组成的二维矩阵内，找到只包含 1 的最大正方形，并返回其面积。
 * <p>
 * 示例:
 * <p>
 * 输入:
 * <p>
 * 1 0 1 0 0
 * 1 0 1 1 1
 * 1 1 1 1 1
 * 1 0 0 1 0
 * <p>
 * 输出: 4
 */
public class Solution221 {

	public int maximalSquare(char[][] matrix) {
		int num = matrix.length;
		int col = matrix[0].length;
		int[][] dp = new int[num][col];
		for(int i = 0; i < num; i++) {
			dp[i][0] = matrix[i][0] == 0 ? 0 : 1;
		}
		for(int j = 0; j < col; j++) {
			dp[0][j] = matrix[0][j] == 0 ? 0 : 1;
		}
		for(int i = 1; i < num; i++) {
			for(int j = 1;j < col; j++) {
				if (matrix[i][j] == 1) {
					dp[i][j] = Math.min(dp[i- 1][j], dp[i][j - 1]);
				}
			}
		}
		return 0;
	}
}
