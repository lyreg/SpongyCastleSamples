package com.uestc.lyreg.spongycastle.utils;

public class HexStringConvert
{
	private final static String HEX = "0123456789ABCDEF";// 用于生成可读写的16进制字符串


	/**
	 * 用于将生成的加密后的原始数据（byte的形式表示）转换成可读的16进制表示的字符串
	 *
	 * @param buf
	 *            byte型表示的加密后的原始数据
	 * @return 以16进制表示的字符串形式的加密后数据
	 */
	public static String hexToString(byte[] buf)
	{
		return hexToString(buf, 0, buf.length);
	}

	public static String hexToString(byte[] buf, int from, int to)
	{
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2 * buf.length);
		for (int i = from; i < to; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	public static String hexToString(byte b)
	{
		StringBuffer hex = new StringBuffer();
		int byteL = 0x0F & (b >> 4);
		int byteR = 0x0F & b;
		hex.append(HEX.charAt(byteL));
		hex.append(HEX.charAt(byteR));
		return hex.toString();
	}

	/**
	 * 用于将16进制表示的加密后的字符串转换成byte数据
	 *
	 * @param hex
	 *            String型的16进制字符串
	 * @return 生成byte表示的加密后的数据(可作为参数传输decryptData函数)
	 */
	public static byte[] stringToHex(String hexString)
	{
		if (hexString == null || "".equals(hexString))
			return null;
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		return result;
	}

	private static void appendHex(StringBuffer sb, byte b)
	{
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}

	public static byte[] appendCRC(byte rawBytes[])
	{
		char uint16_crc = 0x6363;
		char unit16_b;

		for (int i = 0; i < rawBytes.length; i++) {
			unit16_b = (char) (rawBytes[i] ^ (uint16_crc & 0xFF));
			unit16_b ^= (unit16_b << 4) & 0xFF;
			uint16_crc = (char) ((uint16_crc >> 8) ^ (unit16_b << 8) ^ (unit16_b << 3) ^ (unit16_b >> 4));
		}

		byte result[] = new byte[rawBytes.length + 2];
		System.arraycopy(rawBytes, 0, result, 0, rawBytes.length);
		result[rawBytes.length] = (byte) uint16_crc;
		result[rawBytes.length + 1] = (byte) (uint16_crc >> 8);
		return result;
	}
}
