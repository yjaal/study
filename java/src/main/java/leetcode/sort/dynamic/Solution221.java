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

	public static void main(String[] args) {
		Solution221 s = new Solution221();
		char[][] matrix = new char[][]{
				{'1', '0', '1', '0', '0'},
				{'1', '0', '1', '1', '1'},
				{'1', '1', '1', '1', '1'},
				{'1', '0', '0', '1', '0'}};
		System.out.println(s.maximalSquare(matrix));
	}

	public int maximalSquare(char[][] matrix) {
		if (null == matrix || matrix.length == 0 || null == matrix[0] || matrix[0].length == 0) {
			return 0;
		}
		int num = matrix.length;
		int col = matrix[0].length;
		int[][] dp = new int[num][col];
		int max = 0;
		for (int i = 0; i < num; i++) {
			if (matrix[i][0] == '1') {
				dp[i][0] = 1;
				max = 1;
			}
		}
		for (int j = 0; j < col; j++) {
			if (matrix[0][j] == '1') {
				dp[0][j] = 1;
				max = 1;
			}
		}
		for (int i = 1; i < num; i++) {
			for (int j = 1; j < col; j++) {
				if (matrix[i][j] == '1') {
					dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
					max = Math.max(max, dp[i][j]);
				}
			}
		}
		return max * max;
	}
}
