package leetcode.sort.dynamic;

/**
 * 304. 二维区域和检索 - 矩阵不可变
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个二维矩阵，计算其子矩形范围内元素的总和，该子矩阵的左上角为 (row1, col1) ，右下角为 (row2, col2)。
 * <p>
 * Range Sum Query 2D
 * 上图子矩阵左上角 (row1, col1) = (2, 1) ，右下角(row2, col2) = (4, 3)，该子矩形内元素的总和为 8。
 * <p>
 * 示例:
 * 给定 matrix = [
 * [3, 0, 1, 4, 2],
 * [5, 6, 3, 2, 1],
 * [1, 2, 0, 1, 5],
 * [4, 1, 0, 1, 7],
 * [1, 0, 3, 0, 5]
 * ]
 * sumRegion(2, 1, 4, 3) -> 8
 * sumRegion(1, 1, 2, 2) -> 11
 * sumRegion(1, 2, 2, 4) -> 12
 * <p>
 * 说明:
 * 你可以假设矩阵不可变。
 * 会多次调用 sumRegion 方法。
 * 你可以假设 row1 ≤ row2 且 col1 ≤ col2。
 */
public class Solution304 {
	public static void main(String[] args) {
		int[][] matrix = new int[][]{
				{3, 0, 1, 4, 2},
				{5, 6, 3, 2, 1},
				{1, 2, 0, 1, 5},
				{4, 1, 0, 1, 7},
				{1, 0, 3, 0, 5}};
		Solution304 s = new Solution304(matrix);
		System.out.println(s.sumRegion(2, 1, 4, 3));

	}

	/**
	 * 每个点到(0,0)之间正方形之和
	 */
	int[][] sum;

	public Solution304(int[][] matrix) {
		if (null == matrix || matrix.length == 0 || null == matrix[0] || matrix[0].length == 0) {
			return;
		}
		int num = matrix.length;
		int col = matrix[0].length;
		sum = new int[num][col];
		sum[0][0] = matrix[0][0];
		for (int i = 1; i < num; i++) {
			sum[i][0] = matrix[i][0] + sum[i - 1][0];
		}
		for (int j = 1; j < col; j++) {
			sum[0][j] = matrix[0][j] + sum[0][j - 1];
		}
		for (int i = 1; i < num; ++i) {
			for (int j = 1; j < col; ++j) {
				sum[i][j] = sum[i - 1][j] + sum[i][j - 1] - sum[i - 1][j - 1] + matrix[i][j];
			}
		}
	}

	public int sumRegion(int row1, int col1, int row2, int col2) {
		if (row1 > 0 && col1 > 0) {
			return sum[row2][col2] - sum[row2][col1 - 1] - sum[row1 - 1][col2] + sum[row1 - 1][col1 - 1];
		} else if (col1 <= 0 && row1 > 0) {
			return sum[row2][col2] - sum[row1 - 1][col2];
		} else if (col1 > 0) {
			return sum[row2][col2] - sum[row2][col1 - 1];
		} else {
			return sum[row2][col2];
		}
	}
}
