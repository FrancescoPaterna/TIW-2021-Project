package utils;


public class GoodScore {
	
	private static String[] GoodScore = {"18","19","20","21","22","23","24","25","26","27","28", "29","30","30L"};

	
	
	public static boolean CheckGoodScore(String score) {
		for(int i = 0; i< 14; i++) {
			if (score.equals(GoodScore[i]))
				return true;
		}
		return false;	
	}

}
