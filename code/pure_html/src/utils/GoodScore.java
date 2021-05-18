package utils;

import java.util.Arrays;


public class GoodScore {
	
	private static String[] GoodScore = {"18","19","20","21","22","23","24","25","26","27","28", "29","30","30L"};
	
	private static String[] ValidScore = {"", "ABSENT", "RIMANDATO", "RIPROVATO", "18","19","20","21","22","23","24","25","26","27","28", "29","30","30L"};

	
	
	public static boolean CheckGoodScore(String score) {
		return Arrays.asList(GoodScore).contains(score);

	}

	public static boolean CheckValidScore(String score) {
		return Arrays.asList(ValidScore).contains(score);
	}
}

