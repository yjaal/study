package leetcode.sort.array;

import java.util.Arrays;

/**
 * 59. 螺旋矩阵 II(×)
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个正整数 n，生成一个包含 1 到 n2 所有元素，且元素按顺时针顺序螺旋排列的正方形矩阵。
 * <p>
 * 示例:
 * 输入: 3
 * 输出:
 * [
 * [ 1, 2, 3 ],
 * [ 8, 9, 4 ],
 * [ 7, 6, 5 ]
 * ]
 */
public class Solution59 {
	public static void main(String[] args) {
		Solution59 s = new Solution59();
		int[][] res = s.generateMatrix(4);
		for (int[] nums : res) {
			System.out.println(Arrays.toString(nums));
		}
	}

	public int[][] generateMatrix(int n) {
		if (n <= 1) {
			return new int[][]{{n}};
		}
		int left = 0, right = n, high = 0, deep = n;
		int[][] res = new int[n][n];
		int i = 0, j = 0;
		int count = 1;
		int last = n * n;
		while (count <= last) {
			//从左向右
			while (count <= last && i >= left && i < right) {
				res[j][i] = count++;
				i++;
			}
			high++;
			j = high;
			i = right - 1;
			//从上到下
			while (count <= last && j >= high && j < deep) {
				res[j][i] = count++;
				j++;
			}
			right--;
			i = right - 1;
			j = deep - 1;
			//从右向左
			while (count <= last && i >= left && i < right) {
				res[j][i] = count++;
				i--;
			}
			deep--;
			j = deep - 1;
			i = left;
			//从下到上
			while (count <= last && j >= high && j < deep) {
				res[j][i] = count++;
				j--;
			}
			left++;
			i = left;
			j = high;
		}
		return res;
	}
}
