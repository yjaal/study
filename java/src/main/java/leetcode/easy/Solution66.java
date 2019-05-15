package leetcode.easy;

import java.util.Arrays;

/**
 * 66. 加一
 * <p>
 * 给定一个由整数组成的非空数组所表示的非负整数，在该数的基础上加一。
 * <p>
 * 最高位数字存放在数组的首位， 数组中每个元素只存储一个数字。
 * <p>
 * 你可以假设除了整数 0 之外，这个整数不会以零开头。
 * <p>
 * 示例 1:
 * <p>
 * 输入: [1,2,3]
 * 输出: [1,2,4]
 * 解释: 输入数组表示数字 123。
 * 示例 2:
 * <p>
 * 输入: [4,3,2,1]
 * 输出: [4,3,2,2]
 * 解释: 输入数组表示数字 4321。
 */
public class Solution66 {
	public static void main(String[] args) {
		Solution66 s = new Solution66();
		int[] nums = new int[]{9, 9, 9, 9};
		int[] res = s.plusOne(nums);
		System.out.println(Arrays.toString(res));
	}

	public int[] plusOne(int[] digits) {
		if (digits[0] == 0) {
			return new int[]{1};
		}
		int len = digits.length;
		int tmp = 1;
		for (int i = len - 1; i >= 0; i--) {
			tmp += digits[i];
			if (tmp == 10) {
				digits[i] = 0;
				tmp = 1;
			} else {
				digits[i] += 1;
				tmp = 0;
				break;
			}
		}
		if (tmp == 1) {
			int[] res = new int[len + 1];
			res[0] = tmp;
			System.arraycopy(digits, 0, res, 1, len);
			return res;
		} else {
			return digits;
		}
	}
}
