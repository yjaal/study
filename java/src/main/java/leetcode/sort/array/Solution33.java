package leetcode.sort.array;


/**
 * 33. 搜索旋转排序数组
 * <p>
 * 假设按照升序排序的数组在预先未知的某个点上进行了旋转。
 * ( 例如，数组 [0,1,2,4,5,6,7] 可能变为 [4,5,6,7,0,1,2] )。
 * 搜索一个给定的目标值，如果数组中存在这个目标值，则返回它的索引，否则返回 -1 。
 * 你可以假设数组中不存在重复的元素。
 * 你的算法时间复杂度必须是 O(log n) 级别。
 * <p>
 * 示例 1:
 * 输入: nums = [4,5,6,7,0,1,2], target = 0
 * 输出: 4
 * <p>
 * 示例 2:
 * 输入: nums = [4,5,6,7,0,1,2], target = 3
 * 输出: -1
 */
public class Solution33 {
	public static void main(String[] args) {
		Solution33 s = new Solution33();
		int[] nums = new int[]{4, 5, 6, 7, 0, 1, 2};
		int res = s.search(nums, 3);
		System.out.println(res);
	}

	public int search(int[] nums, int target) {
		if (nums == null || nums.length < 1) {
			return -1;
		}
		if (nums.length == 1) {
			if (target == nums[0]) {
				return 0;
			} else {
				return -1;
			}
		}
		return search(nums, 0, nums.length - 1, target);
	}

	private int search(int[] nums, int start, int end, int target) {
		if (end - start == 1) {
			if (target == nums[start]) {
				return start;
			}
			if (target == nums[end]) {
				return end;
			}
			return -1;
		}
		int middle = (end + start) / 2;
		if (nums[middle] == target) {
			return middle;
		}
		if (nums[start] < nums[middle]) {
			//表明左边是有序的
			if (target < nums[middle] && target >= nums[start]) {
				if (target == nums[start]) {
					return start;
				}
				return searchInSortedArray(nums, start, middle - 1, target);
			} else {
				return search(nums, middle, end, target);
			}
		} else {
			//表明右边是有序的
			if (target > nums[middle] && target <= nums[end]) {
				if (target == nums[end]) {
					return end;
				}
				return searchInSortedArray(nums, middle + 1, end, target);
			} else {
				return search(nums, start, middle, target);
			}
		}
	}

	//在正序数组中查找
	private int searchInSortedArray(int[] nums, int start, int end, int target) {
		if (start > end) {
			return -1;
		} else if (start == end && nums[start] == target) {
			return start;
		} else if (start == end && nums[start] != target) {
			return -1;
		}
		int middle = (end + start) / 2;
		if (nums[middle] == target) {
			return middle;
		} else if (nums[middle] > target) {
			return searchInSortedArray(nums, start, middle - 1, target);
		} else {
			return searchInSortedArray(nums, middle + 1, end, target);
		}
	}


	//最优解
	public int search1(int[] nums, int target) {
		if (nums.length <= 0) return -1;
		return searchBinary(0, nums.length - 1, nums, target);
	}

	private int searchBinary(int left, int right, int[] nums, int target) {
		if (left > right) return -1;
		if (target == nums[left]) {
			return left;
		}
		if (target == nums[right]) {
			return right;
		}
		if (right - left <= 1) {
			return -1;
		}
		if (nums[right] == nums[left] && target != nums[right]) {
			return -1;
		}
		if ((target < nums[left] && target < nums[right]) || (target > nums[left] && target > nums[right])) {
			return searchBinary(left + 1, right - 1, nums, target);
		} else if (target < nums[left] && target > nums[right]) {
			return -1;
		} else if (target > nums[left] && target < nums[right]) {
			int middle = (left + right) / 2;
			if (target > nums[middle]) {
				return searchBinary(middle, right, nums, target);
			} else if (target < nums[middle]) {
				return searchBinary(left, middle, nums, target);
			} else {
				return middle;
			}
		} else {
			return -1;
		}
	}
}
