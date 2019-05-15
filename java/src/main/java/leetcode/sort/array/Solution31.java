package leetcode.sort.array;


import java.util.Arrays;

/**
 * 31. 下一个排列
 * <p>
 * 实现获取下一个排列的函数，算法需要将给定数字序列重新排列成字典序中下一个更大的排列。
 * 如果不存在下一个更大的排列，则将数字重新排列成最小的排列（即升序排列）。
 * 必须原地修改，只允许使用额外常数空间。
 * <p>
 * 以下是一些例子，输入位于左侧列，其相应输出位于右侧列。
 * 1,2,3 → 1,3,2
 * 3,2,1 → 1,2,3
 * 1,1,5 → 1,5,1
 * <p>
 * 比如
 * 1,2,3,4 -> 1,2,4,3 -> 1,3,2,4 -> 1,3,4,2
 */
public class Solution31 {
	public static void main(String[] args) {
		Solution31 s = new Solution31();
		int[] nums = new int[]{1, 2, 7, 4, 3, 1};
		s.nextPermutation(nums);
		System.out.println(Arrays.toString(nums));
	}

	public void nextPermutation(int[] nums) {
		int i = nums.length - 2;
		//比如1，2，7，4，3，1，则要先找到2(2比其前面的数大，即不满足倒序)
		while (i >= 0 && nums[i + 1] <= nums[i]) {
			i--;
		}
		if (i >= 0) {
			int j = nums.length - 1;
			//反着找，招到第一个比2大的数，这里是3
			//2后面是倒序，这里招到倒序中第一个比2大的数
			while (j >= 0 && nums[j] <= nums[i]) {
				j--;
			}
			swap(nums, i, j);
		}
		//如果i = -1，表明数组是一个倒序的
		reverse(nums, i + 1);
	}

	//这相当于将start到最后的元素进行翻转
	private void reverse(int[] nums, int start) {
		int i = start, j = nums.length - 1;
		while (i < j) {
			swap(nums, i, j);
			i++;
			j--;
		}
	}

	private void swap(int[] nums, int i, int j) {
		int tmp = nums[i];
		nums[i] = nums[j];
		nums[j] = tmp;
	}
}
