package leetcode.easy;

import java.util.Arrays;

/**
 * 88. 合并两个有序数组
 * <p>
 * 给定两个有序整数数组 nums1 和 nums2，将 nums2 合并到 nums1 中，使得 num1 成为一个有序数组。
 * <p>
 * 说明:
 * 初始化 nums1 和 nums2 的元素数量分别为 m 和 n。
 * 你可以假设 nums1 有足够的空间（空间大小大于或等于 m + n）来保存 nums2 中的元素。
 * <p>
 * 示例:
 * 输入:
 * nums1 = [1,2,3,0,0,0], m = 3
 * nums2 = [2,5,6],       n = 3
 * 输出: [1,2,2,3,5,6]
 */
public class Solution88 {
	public static void main(String[] args) {
		int[] nums1 = new int[]{2, 0};
		int[] nums2 = new int[]{1};
		Solution88 s = new Solution88();
		s.merge(nums1, 1, nums2, 1);
		System.out.println(Arrays.toString(nums1));
	}

	public void merge(int[] nums1, int m, int[] nums2, int n) {
		int i = 0, j = 0;
		int len = m + n;
		for (; i < n; i++) {
			while (j < m) {
				if (nums2[i] <= nums1[j]) {
					int idx = j;
					j = m;
					while (j > idx) {
						nums1[j] = nums1[j - 1];
						j--;
					}
					nums1[idx] = nums2[i];
					j = idx + 1;
					m++;
					break;
				} else {
					j++;
				}
			}
			if (j == m) {
				for (; j < len && i < n; j++, i++) {
					nums1[j] = nums2[i];
				}
			}
		}
	}

	//最佳解答
	public void merge1(int[] nums1, int m, int[] nums2, int n) {
		int i = m - 1;
		int j = n - 1;
		int index = m + n - 1;
		while (i >= 0 && j >= 0) {
			if (nums1[i] > nums2[j]) {
				nums1[index--] = nums1[i--];
			} else {
				nums1[index--] = nums2[j--];
			}
		}
		while (j >= 0) {
			nums1[index--] = nums2[j--];
		}
	}
}
