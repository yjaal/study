package leetcode.sort.dynamic;

/**
 * 300. 最长上升子序列
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 * <p>
 * 给定一个无序的整数数组，找到其中最长上升子序列的长度。
 * <p>
 * 示例:
 * 输入: [10,9,2,5,3,7,101,18]
 * 输出: 4
 * 解释: 最长的上升子序列是 [2,3,7,101]，它的长度是 4。
 * <p>
 * 说明:
 * 可能会有多种最长上升子序列的组合，你只需要输出对应的长度即可。
 * 你算法的时间复杂度应该为 O(n2) 。
 * <p>
 * 进阶: 你能将算法的时间复杂度降低到 O(n log n) 吗?
 */
public class Solution300 {

	public static void main(String[] args) {
		Solution300 s = new Solution300();
		System.out.println(s.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
	}

	/**
	 * 复杂度为O(n^2)
	 */
	public int lengthOfLIS(int[] nums) {
		if (null == nums || nums.length == 0) {
			return 0;
		}
		int len = nums.length;
		int[] dp = new int[len];
		int max = dp[0];
		for (int i = 1; i < len; i++) {
			for (int j = i - 1; j >= 0; j--) {
				//后大于前
				if (nums[i] > nums[j]) {
					dp[i] = Math.max(dp[i], dp[j] + 1);
				}
			}
			max = Math.max(max, dp[i]);
		}
		return max + 1;
	}

	/**
	 * 最优解，O(NlgN)
	 * [10,9,2,5,3,7,101,18]
	 * 其实可以这样理解，如果后面的数比前面的数大，那么直接加入到备选数组
	 * 比如刚开始备选数组是[10]，那么下一次就变成了[9]，一直到[2,3,7,18]
	 * 此时其实我们已经找到了最大的升序长度，但是如果18后面是[1,2,3,4,5]
	 * 此时我们就需要一次一次的进行替换最终将备选数组变成[1,2,3,4,5]
	 * 第一次会变成[1,3,7,18]，注意这个备选数组并不表示是最大升序列，只是
	 * 说后面可能还会出现更长的升序列，于是我们要将最小的一个大于1的数替换掉
	 * 最后除非后面的升序列比前面的更长，备选数组才能被全部替换,最大长度也会
	 * 改变，如果后面的升序列不能将前面的完全替换，那么最长长度则不变
	 */
	public int lengthOfLIS1(int[] nums) {
		int len = nums.length;
		if (len <= 1) {
			return len;
		}
		int maxL = 0;
		//dp[i]: 所有长度为i+1的递增子序列中, 最小的那个序列尾数
		int[] dp = new int[nums.length];
		dp[0] = nums[0];
		for (int i = 1; i < len; i++) {
			if (nums[i] > dp[maxL]) {
				dp[++maxL] = nums[i];
			} else {
				int left = 0, right = maxL;
				while (left < right) {
					int mid = left + (right - left) / 2;
					if (dp[mid] == nums[i]) {
						left = mid;
						break;
					} else if (dp[mid] < nums[i]) {
						left = left + 1;
					} else {
						right = mid;
					}
				}
				dp[left] = nums[i];
			}
		}
		return ++maxL;
	}
}
