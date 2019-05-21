package leetcode.sort.array;

/**
 * 62. 不同路径(重复)
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * * 一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为“Start” ）。
 * 机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为“Finish”）。
 * 问总共有多少条不同的路径？
 * <p>
 * 例如，上图是一个7 x 3 的网格。有多少可能的路径？
 * 说明：m 和 n 的值均不超过 100。
 * <p>
 * 示例 1:
 * 输入: m = 3, n = 2
 * 输出: 3
 * 解释:
 * 从左上角开始，总共有 3 条路径可以到达右下角。
 * 1. 向右 -> 向右 -> 向下
 * 2. 向右 -> 向下 -> 向右
 * 3. 向下 -> 向右 -> 向右
 * <p>
 * 示例 2:
 * 输入: m = 7, n = 3
 * 输出: 28
 */
public class Solution62 {

	public static void main(String[] args) {
		Solution62 s = new Solution62();
		System.out.println(s.uniquePaths(7, 3));
	}

	/**
	 * @param m 列 col
	 * @param n 行 num
	 */
	public int uniquePaths(int m, int n) {
		if (m < 0 || m > 100 || n < 0 || n > 100) {
			return 0;
		}
		if (m == 1 || n == 1) {
			return 1;
		}
		int[][] res = new int[n][m];
		res[0][0] = 1;
		for (int num = 0; num < n; num++) {
			for (int col = 0; col < m; col++) {
				if (num == 0 || col == 0) {
					res[num][col] = 1;
					continue;
				}
				res[num][col] = res[num - 1][col] + res[num][col - 1];
			}
		}
		return res[n - 1][m - 1];
	}

	/**
	 * 动态规划优化
	 * 这里之所以定义的结果数据是一个一维数组，是因为
	 * 其实向前走只有两种方式，就是向右和向下
	 * 其实下面的path中每个元素值是多次计算出来的结果
	 * 所以一维数组也可以
	 */
	public int uniquePaths1(int m, int n) {
		// 动态规划，每一行中的某个方格的步数等于其上方和左方方格的步数的和
		int[] path = new int[n];
		for (int i = 0; i < n; i++) {
			path[i] = 1;
		}
		for (int i = 1; i < m; i++) {
			for (int j = 0; j < n; j++) {
				path[j] = j == 0 ? path[j] : path[j - 1] + path[j];
			}
		}
		return path[n - 1];
	}

	/**
	 * 最优解
	 * 排列组合res = C(m-1, m+n-2)
	 */
	public int uniquePaths2(int m, int n) {
		double res = 1.0;
		int min = Math.min(m, n);
		for (int i = 1; i < min; i++) {
			res *= m + n - 1 - i;
			res /= i;
		}
		return (int) res;
	}
}
