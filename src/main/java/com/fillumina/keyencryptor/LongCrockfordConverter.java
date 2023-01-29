package com.fillumina.keyencryptor;

/**
 * Most code copied from Tsid
 *
 * @see https://github.com/f4b6a3/tsid-creator
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LongCrockfordConverter {

    // note that lacks O, I and L because they could be mismatched for 0 and 1 representations
	private static final char[] ALPHABET_UPPERCASE = //
			{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', //
					'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', //
					'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X', 'Y', 'Z' };

    public static String toString(long number) {
		final char[] chars = new char[13];

		chars[0x00] = ALPHABET_UPPERCASE[(int) ((number >>> 60) & 0b11111)];
		chars[0x01] = ALPHABET_UPPERCASE[(int) ((number >>> 55) & 0b11111)];
		chars[0x02] = ALPHABET_UPPERCASE[(int) ((number >>> 50) & 0b11111)];
		chars[0x03] = ALPHABET_UPPERCASE[(int) ((number >>> 45) & 0b11111)];
		chars[0x04] = ALPHABET_UPPERCASE[(int) ((number >>> 40) & 0b11111)];
		chars[0x05] = ALPHABET_UPPERCASE[(int) ((number >>> 35) & 0b11111)];
		chars[0x06] = ALPHABET_UPPERCASE[(int) ((number >>> 30) & 0b11111)];
		chars[0x07] = ALPHABET_UPPERCASE[(int) ((number >>> 25) & 0b11111)];
		chars[0x08] = ALPHABET_UPPERCASE[(int) ((number >>> 20) & 0b11111)];
		chars[0x09] = ALPHABET_UPPERCASE[(int) ((number >>> 15) & 0b11111)];
		chars[0x0a] = ALPHABET_UPPERCASE[(int) ((number >>> 10) & 0b11111)];
		chars[0x0b] = ALPHABET_UPPERCASE[(int) ((number >>> 5) & 0b11111)];
		chars[0x0c] = ALPHABET_UPPERCASE[(int) (number & 0b11111)];

		return new String(chars);
    }

	private static final long[] ALPHABET_VALUES = new long[128];
	static {
		for (int i = 0; i < ALPHABET_VALUES.length; i++) {
			ALPHABET_VALUES[i] = -1;
		}
		// Numbers
		ALPHABET_VALUES['0'] = 0x00;
		ALPHABET_VALUES['1'] = 0x01;
		ALPHABET_VALUES['2'] = 0x02;
		ALPHABET_VALUES['3'] = 0x03;
		ALPHABET_VALUES['4'] = 0x04;
		ALPHABET_VALUES['5'] = 0x05;
		ALPHABET_VALUES['6'] = 0x06;
		ALPHABET_VALUES['7'] = 0x07;
		ALPHABET_VALUES['8'] = 0x08;
		ALPHABET_VALUES['9'] = 0x09;
		ALPHABET_VALUES['A'] = 0x0a;
		ALPHABET_VALUES['B'] = 0x0b;
		ALPHABET_VALUES['C'] = 0x0c;
		ALPHABET_VALUES['D'] = 0x0d;
		ALPHABET_VALUES['E'] = 0x0e;
		ALPHABET_VALUES['F'] = 0x0f;
		ALPHABET_VALUES['G'] = 0x10;
		ALPHABET_VALUES['H'] = 0x11;
		ALPHABET_VALUES['J'] = 0x12;
		ALPHABET_VALUES['K'] = 0x13;
		ALPHABET_VALUES['M'] = 0x14;
		ALPHABET_VALUES['N'] = 0x15;
		ALPHABET_VALUES['P'] = 0x16;
		ALPHABET_VALUES['Q'] = 0x17;
		ALPHABET_VALUES['R'] = 0x18;
		ALPHABET_VALUES['S'] = 0x19;
		ALPHABET_VALUES['T'] = 0x1a;
		ALPHABET_VALUES['V'] = 0x1b;
		ALPHABET_VALUES['W'] = 0x1c;
		ALPHABET_VALUES['X'] = 0x1d;
		ALPHABET_VALUES['Y'] = 0x1e;
		ALPHABET_VALUES['Z'] = 0x1f;
		// Upper case OIL (maps to number 0 and 1)
		ALPHABET_VALUES['O'] = 0x00;
		ALPHABET_VALUES['I'] = 0x01;
		ALPHABET_VALUES['L'] = 0x01;
	}

	public static long fromString(final String string) {

		final char[] chars = toCharArray(string);

		long number = 0;

		number |= ALPHABET_VALUES[chars[0x00]] << 60;
		number |= ALPHABET_VALUES[chars[0x01]] << 55;
		number |= ALPHABET_VALUES[chars[0x02]] << 50;
		number |= ALPHABET_VALUES[chars[0x03]] << 45;
		number |= ALPHABET_VALUES[chars[0x04]] << 40;
		number |= ALPHABET_VALUES[chars[0x05]] << 35;
		number |= ALPHABET_VALUES[chars[0x06]] << 30;
		number |= ALPHABET_VALUES[chars[0x07]] << 25;
		number |= ALPHABET_VALUES[chars[0x08]] << 20;
		number |= ALPHABET_VALUES[chars[0x09]] << 15;
		number |= ALPHABET_VALUES[chars[0x0a]] << 10;
		number |= ALPHABET_VALUES[chars[0x0b]] << 5;
		number |= ALPHABET_VALUES[chars[0x0c]];

		return number;
	}

	static char[] toCharArray(final String string) {
		char[] chars = string == null ? null : string.toCharArray();
		if (!isValidCharArray(chars)) {
			throw new IllegalArgumentException(
                    String.format("Invalid crockford code: \"%s\"", string));
		}
		return chars;
	}

	/**
	 * Checks if the string is a valid TSID.
	 *
	 * A valid TSID string is a sequence of 13 characters from Crockford's base 32
	 * alphabet.
	 *
	 * The first character of the input string must be between 0 and F.
	 *
	 * @param chars a char array
	 * @return boolean true if valid
	 */
	public static boolean isValidCharArray(final char[] chars) {

		if (chars == null || chars.length != 13) {
			return false; // null or wrong size!
		}

		// The extra bit added by base-32 encoding must be zero
		// As a consequence, the 1st char of the input string must be between 0 and F.
		if ((ALPHABET_VALUES[chars[0]] & 0b10000) != 0) {
			return false; // overflow!
		}

		for (int i = 0; i < chars.length; i++) {
			if (ALPHABET_VALUES[chars[i]] == -1) {
				return false; // invalid character!
			}
		}
		return true; // It seems to be OK.
	}

}
