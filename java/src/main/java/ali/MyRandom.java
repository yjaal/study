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
		//生成一个[1,10000]的数
		int num = new Random().nextInt(10000) + 1;
		res.add(num);
		
		return res;
	}

	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
