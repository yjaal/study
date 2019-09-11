package ali;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 随机10000次，将1~10000全部随机出来，不能重复
 *
 * @author yj
 * @version 1.0
 * @date 2019/4/28 21:02
 */
public class MyRandom {

	private List<Integer> res = new ArrayList<>();

	public List<Integer> solution() {
		randInt(1, 10000);
		res.sort(Integer::compareTo);
		res.forEach(System.out::println);
		return res;
	}

	private void randInt(int min, int max) {
		if (min <= max) {
			int randomNum = new Random().nextInt((max - min) + 1) + min;
			res.add(randomNum);
			randInt(min, randomNum - 1);
			randInt(randomNum + 1, max);
		}
	}
}
