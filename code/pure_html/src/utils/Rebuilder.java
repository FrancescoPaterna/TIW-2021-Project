package utils;

public final class Rebuilder {

	public static String resetMask(String mask, int secretsortcode) {

		String recovered_mask;
		/* Recover URI To Go Back */
		if (secretsortcode == 00) {
			if (mask.charAt(0) == '0') {
				recovered_mask = '0' + mask.substring(1, 7);
				return recovered_mask;


			}
		} else if (secretsortcode == 11) {
			if (mask.charAt(0) == '0') {
				recovered_mask = '1' + mask.substring(1, 7);
				return recovered_mask;

			}
		} else if (secretsortcode == 10) {
			if (mask.charAt(0) == '1') {
				recovered_mask = '0' + mask.substring(1, 7);
				return recovered_mask;

			}
		}

		else if (secretsortcode == 21) {
			if (mask.charAt(1) == '0') {
				recovered_mask = mask.substring(0, 1) + '1' + mask.substring(2, 7);
				return recovered_mask;

			}
		}

		else if (secretsortcode == 20) {
			if (mask.charAt(1) == '1') {
				recovered_mask = mask.substring(0, 1) + '0' + mask.substring(2, 7);
				return recovered_mask;

			}
		}

		else if (secretsortcode == 31) {
			if (mask.charAt(2) == '0') {
				recovered_mask = mask.substring(0, 2) + '1' + mask.substring(3, 7);
				return recovered_mask;

			}

		}

		else if (secretsortcode == 30) {
			if (mask.charAt(2) == '1') {
				recovered_mask = mask.substring(0, 2) + '0' + mask.substring(3, 7);
				return recovered_mask;

			}

		} else if (secretsortcode == 41) {
			if (mask.charAt(3) == '0') {
				recovered_mask = mask.substring(0, 3) + '1' + mask.substring(4, 7);
				return recovered_mask;

			}
		}

		else if (secretsortcode == 40) {
			if (mask.charAt(3) == '1') {
				recovered_mask = mask.substring(0, 3) + '0' + mask.substring(4, 7);
				return recovered_mask;

			}
		} else if (secretsortcode == 51) {
			if (mask.charAt(4) == '0') {
				recovered_mask = mask.substring(0, 4) + '1' + mask.substring(5, 7);
				return recovered_mask;

			}
		}

		else if (secretsortcode == 50) {
			if (mask.charAt(4) == '1') {
				recovered_mask = mask.substring(0, 4) + '0' + mask.substring(5, 7);
				return recovered_mask;

			}
		} else if (secretsortcode == 61) {
			if (mask.charAt(5) == '0') {
				recovered_mask = mask.substring(0, 5) + '1' + mask.substring(6, 7);
				return recovered_mask;

			}
		}

		else if (secretsortcode == 60) {
			if (mask.charAt(5) == '1') {
				recovered_mask = mask.substring(0, 5) + '0' + mask.substring(6, 7);
				return recovered_mask;

			}
		} else if (secretsortcode == 71) {
			if (mask.charAt(6) == '0') {
				recovered_mask = mask.substring(0, 6) + '1';
				return recovered_mask;

			}
		}

		else if (secretsortcode == 70) {
			if (mask.charAt(6) == '1') {
				recovered_mask = mask.substring(0, 6) + '0';
				return recovered_mask;


			}
		}
		return mask;
	}
	
	public static int resetSecretSortCode(int secretsortcode) {
		if (secretsortcode <= 71 ) {
			if(secretsortcode == 00) {
				return 1;
			}
			return (secretsortcode/10);
		}
		else {
			return secretsortcode;
		}
	}

}
