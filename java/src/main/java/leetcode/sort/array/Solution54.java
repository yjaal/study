package leetcode.sort.array;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 54. 螺旋矩阵
 * 给定一个包含 m x n 个元素的矩阵（m 行, n 列），请按照顺时针螺旋顺序，返回矩阵中的所有元素。
 * <p>
 * 示例 1:
 * 输入:
 * [
 * [ 1, 2, 3 ],
 * [ 4, 5, 6 ],
 * [ 7, 8, 9 ]
 * ]
 * 输出: [1,2,3,6,9,8,7,4,5]
 * <p>
 * 示例 2:
 * 输入:
 * [
 * [1, 2, 3, 4],
 * [5, 6, 7, 8],
 * [9,10,11,12]
 * ]
 * 输出: [1,2,3,4,8,12,11,10,9,5,6,7]
 */
public class Solution54 {
	public static void main(String[] args) {
		Solution54 s = new Solution54();
		int[][] nums = new int[][]{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}};
		List<Integer> list = s.spiralOrder(nums);
		list.forEach(System.out::print);
	}

	public List<Integer> spiralOrder(int[][] matrix) {
		List<Integer> res = new ArrayList<>();
		if (matrix == null || matrix.length == 0) {
			return res;
		}
		int col = matrix.length;
		int num = matrix[0].length;
		int left = 0, right = num - 1, top = 0, deep = col - 1;
		int count = num * col;
		while (true) {
			if (left > right && top > deep) {
				break;
			}
			//向右
			for (int i = left; i <= right && count != 0; i++) {
				res.add(matrix[top][i]);
				count--;
			}
			top++;
			//向下
			for (int i = top; i <= deep && count != 0; i++) {
				res.add(matrix[i][right]);
				count--;
			}
			right--;
			//向左
			for (int i = right; i >= left && count != 0; i--) {
				res.add(matrix[deep][i]);
				count--;
			}
			deep--;
			//向上
			for (int i = deep; i >= top && count != 0; i--) {
				res.add(matrix[i][left]);
				count--;
			}
			left++;
		}
		return res;
	}
}
