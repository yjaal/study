package leetcode.easy;

/**
 * 53. 最大子序和
 * <p>
 * 给定一个整数数组 nums ，找到一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * <p>
 * 示例:
 * <p>
 * 输入: [-2,1,-3,4,-1,2,1,-5,4],
 * 输出: 6
 * 解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。
 * 进阶:
 * <p>
 * 如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的分治法求解。
 */
public class Solution53 {

	public static void main(String[] args) {
		Solution53 s = new Solution53();
		int[] nums = new int[]{-2, -1};
		int res = s.maxSubArray4(nums);
		System.out.println("res = " + res);
	}

	//前三种方法都采用了分治策略，时间复杂度为O(n)
	public int maxSubArray1(int[] nums) {
		int len = nums.length;
		int[] dp = new int[len + 1];
		dp[0] = nums[0];
		int sum = nums[0];
		for (int i = 1; i < len; i++) {
			dp[i] = Math.max(nums[i] + dp[i - 1], nums[i]);
			sum = Math.max(sum, dp[i]);
		}
		return sum;
	}

	public int maxSubArray2(int[] nums) {
		int len = nums.length;
		int sum = nums[0];
		int tmp = 0;
		for (int num : nums) {
			tmp += num;
			if (tmp > sum) {
				sum = tmp;
			}
			if (tmp <= 0) {
				tmp = 0;
			}
		}
		return sum;
	}

	//和上面的其实一样
	public int maxSubArray3(int[] nums) {
		int sum = nums[0];
		int tmp = 0;
		for (int num : nums) {
			tmp = tmp + num < 0 ? 0 : tmp + num;
			sum = Math.max(tmp, sum);
		}
		return sum;
	}

	public int maxSubArray4(int[] nums) {
		return maxSumRec(nums, 0, nums.length - 1);
	}

	//这是二分法解决，时间复杂度为O(nlgn)
	public int maxSumRec(int[] a, int left, int right) {
		//递归中的基本情况
		if (left == right) {
			return a[left];
		}
		int center = (left + right) / 2;
		//最大子序列在左侧
		int maxLeftSum = maxSumRec(a, left, center);//-2
		//最大子序列在右侧
		int maxRightSum = maxSumRec(a, center + 1, right);//-1
		//最大子序列在中间（左边靠近中间的最大子序列+右边靠近中间的最大子序列）
		//注意：下面的循环都必须从中间开始，不能反，因为要保证连续
		int maxLeftBorderSum = a[center], leftBorderSum = 0;
		for (int i = center; i >= left; i--) {
			leftBorderSum += a[i];//注意这个值是一直连续的加
			if (leftBorderSum > maxLeftBorderSum) maxLeftBorderSum = leftBorderSum;
		}
		int maxRightBorderSum = a[center + 1], rightBorderSum = 0;
		for (int i = center + 1; i <= right; i++) {
			rightBorderSum += a[i];//注意这个值是一直连续的加
			if (rightBorderSum > maxRightBorderSum) maxRightBorderSum = rightBorderSum;
		}
		return Math.max(Math.max(maxLeftSum, maxRightSum), maxLeftBorderSum + maxRightBorderSum);
	}

}

