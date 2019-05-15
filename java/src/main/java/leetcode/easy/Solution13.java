package leetcode.easy;

import java.util.HashMap;
import java.util.Map;

/**
 * 13.Roman to Integer
 * Roman numerals are represented by seven different symbols: I, V, X, L, C, D and M.
 * <p>
 * Symbol       Value
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * For example, two is written as II in Roman numeral, just two one's added together.
 * Twelve is written as, XII, which is simply X + II. The number twenty seven is
 * written as XXVII, which is XX + V + II.
 * <p>
 * Roman numerals are usually written largest to smallest from left to right.
 * However, the numeral for four is not IIII. Instead, the number four is written as IV.
 * Because the one is before the five we subtract it making four. The same principle applies
 * to the number nine, which is written as IX. There are six instances where subtraction is used:
 * <p>
 * I can be placed before V (5) and X (10) to make 4 and 9.
 * X can be placed before L (50) and C (100) to make 40 and 90.
 * C can be placed before D (500) and M (1000) to make 400 and 900.
 * Given a roman numeral, convert it to an integer. Input is guaranteed to be within the range from 1 to 3999.
 * <p>
 * Example 1:
 * Input: "III"
 * Output: 3
 * <p>
 * Example 2:
 * Input: "IV"
 * Output: 4
 * <p>
 * Example 3:
 * Input: "IX"
 * Output: 9
 * <p>
 * Example 4:
 * Input: "LVIII"
 * Output: 58
 * Explanation: L = 50, V= 5, III = 3.
 * <p>
 * Example 5:
 * Input: "MCMXCIV"
 * Output: 1994
 * Explanation: M = 1000, CM = 900, XC = 90 and IV = 4.
 */
public class Solution13 {
	public static void main(String[] args) {
		Solution13 solution13 = new Solution13();
		System.out.println(solution13.romanToInt("MCMXCIV"));
	}

	public int romanToInt(String s) {
		Map<Character, Integer> valueMap = new HashMap<Character, Integer>() {{
			put('I', 1);
			put('V', 5);
			put('X', 10);
			put('L', 50);
			put('C', 100);
			put('D', 500);
			put('M', 1000);
		}};
		Map<Character, Integer> indexMap = new HashMap<Character, Integer>() {{
			put('I', 0);
			put('V', 1);
			put('X', 2);
			put('L', 3);
			put('C', 4);
			put('D', 5);
			put('M', 6);
		}};
		int result = 0;
		int len = s.length();
		int i = len - 1;
		while (i > 0) {
			int rightIndex = indexMap.get(s.charAt(i));
			int leftIndex = indexMap.get(s.charAt(i - 1));
			int rightValue = valueMap.get(s.charAt(i));
			int leftValue = valueMap.get(s.charAt(i - 1));
			if (rightIndex > leftIndex) {
				result = result + rightValue - leftValue;
				i -= 2;
			} else {
				result = result + rightValue;
				i--;
			}
		}
		if (i == 0) {
			result += valueMap.get(s.charAt(i));
		}
		return result;
	}

	//最佳解答
	public int romanToInt1(String s) {
		int[] nums = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
				case 'M':
					nums[i] = 1000;
					break;
				case 'D':
					nums[i] = 500;
					break;
				case 'C':
					nums[i] = 100;
					break;
				case 'L':
					nums[i] = 50;
					break;
				case 'X':
					nums[i] = 10;
					break;
				case 'V':
					nums[i] = 5;
					break;
				case 'I':
					nums[i] = 1;
					break;
			}
		}
		int sum = 0;
		for (int i = 0; i < nums.length - 1; i++) {
			if (nums[i] < nums[i + 1])
				sum -= nums[i];
			else
				sum += nums[i];
		}
		return sum + nums[nums.length - 1];
	}
}
