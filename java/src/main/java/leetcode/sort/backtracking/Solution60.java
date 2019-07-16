package leetcode.sort.backtracking;

import java.util.Arrays;

/**
 * 60. 第k个排列
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * 给出集合 [1,2,3,…,n]，其所有元素共有 n! 种排列。
 * 按大小顺序列出所有排列情况，并一一标记，当 n = 3 时, 所有排列如下：
 * "123"
 * "132"
 * "213"
 * "231"
 * "312"
 * "321"
 * 给定 n 和 k，返回第 k 个排列。
 * <p>
 * 说明：
 * 给定 n 的范围是 [1, 9]。
 * 给定 k 的范围是[1,  n!]。
 * <p>
 * 示例 1:
 * 输入: n = 3, k = 3
 * 输出: "213"
 * <p>
 * 示例 2:
 * 输入: n = 4, k = 9
 * 输出: "2314"
 */
public class Solution60 {

	public String getPermutation(int n, int k) {
		String res = "";
		return "";
	}

	public static void main(String[] args) {
		Solution60 solution60 = new Solution60();
		int[] arr = new int[]{1, 2, 6, 3, 5, 9};
		solution60.sort(arr);
		System.out.println(Arrays.toString(arr));
	}

	public void sort(int[] arr) {
		helper(arr, 0, arr.length - 1);
	}

	private void helper(int[] arr, int start, int end) {
		if (start >= end) {
			return;
		}
		int i = start, j = end, middle = start + (end - start) / 2;
		while (i <= j) {
			while (arr[i] < arr[middle]) {
				i++;
			}
			while (arr[j] > arr[middle]) {
				j--;
			}
			if (i < j) {
				int tmp = arr[i];
				arr[i] = arr[j];
				arr[j] = tmp;
				i++;
				j--;
			}
			if (i == j) {
				i++;
			}
		}
		helper(arr, start, j);
		helper(arr, i, end);
	}


}
