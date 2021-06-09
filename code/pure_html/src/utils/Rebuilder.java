package utils;

public final class Rebuilder {

	public static String resetMask(String mask) {

		String recovered_mask;
		/* Recover URI To Go Back */
		
			if (mask.charAt(0) == '0') {
				recovered_mask = '1' + mask.substring(1, 7);
				return recovered_mask;

			}
			if (mask.charAt(0) == '1') {
				recovered_mask = '0' + mask.substring(1, 7);
				return recovered_mask;

			}
		

			if (mask.charAt(1) == '0') {
				recovered_mask = mask.substring(0, 1) + '1' + mask.substring(2, 7);
				return recovered_mask;

			}
		

			if (mask.charAt(1) == '1') {
				recovered_mask = mask.substring(0, 1) + '0' + mask.substring(2, 7);
				return recovered_mask;

			}
		

			if (mask.charAt(2) == '0') {
				recovered_mask = mask.substring(0, 2) + '1' + mask.substring(3, 7);
				return recovered_mask;

			}

		

			if (mask.charAt(2) == '1') {
				recovered_mask = mask.substring(0, 2) + '0' + mask.substring(3, 7);
				return recovered_mask;

			}

			if (mask.charAt(3) == '0') {
				recovered_mask = mask.substring(0, 3) + '1' + mask.substring(4, 7);
				return recovered_mask;

			}
		

			if (mask.charAt(3) == '1') {
				recovered_mask = mask.substring(0, 3) + '0' + mask.substring(4, 7);
				return recovered_mask;

			}
			if (mask.charAt(4) == '0') {
				recovered_mask = mask.substring(0, 4) + '1' + mask.substring(5, 7);
				return recovered_mask;

			}
		

			if (mask.charAt(4) == '1') {
				recovered_mask = mask.substring(0, 4) + '0' + mask.substring(5, 7);
				return recovered_mask;

			}
			if (mask.charAt(5) == '0') {
				recovered_mask = mask.substring(0, 5) + '1' + mask.substring(6, 7);
				return recovered_mask;

			}
		

			if (mask.charAt(5) == '1') {
				recovered_mask = mask.substring(0, 5) + '0' + mask.substring(6, 7);
				return recovered_mask;

			}
			if (mask.charAt(6) == '0') {
				recovered_mask = mask.substring(0, 6) + '1';
				return recovered_mask;

			}
		

			if (mask.charAt(6) == '1') {
				recovered_mask = mask.substring(0, 6) + '0';
				return recovered_mask;


			}
		
		return mask;
	}
	
}