package com.other.utils.md5;
/*////////////////////////////////////////////////////////////////////////////////
 名称：
 版本：1
 功能：

 *///////////////////////////////////////////////////////////////////////////////

public class MD5Encrypt {
  public MD5Encrypt() {
  }

  private final static String[] hexDigits = {
      "0", "1", "2", "3", "4", "5", "6", "7",
      "8", "9", "a", "b", "c", "d", "e", "f"};

  /**
   * 转换字节数组为16进制字串
   */
  public static String byteArrayToString(byte[] b) {
    StringBuffer resultSb = new StringBuffer();
    for (int i = 0; i < b.length; i++) {
       resultSb.append(byteToHexString(b[i]));//使用本函数则返回结果的16进制字串
    }
    return resultSb.toString();
  }

  private static String byteToHexString(byte b) {
    int n = b;
    if (n < 0) {
      n = 256 + n;
    }
    int d1 = n / 16;
    int d2 = n % 16;
    return hexDigits[d1] + hexDigits[d2];
  }
  private static BaseMD5 basemd5 ;
  public static String md5Encode(String origin) {
    String resultString = null;

    try {
    	if(basemd5==null)
    	 basemd5 = new BaseMD5();
    	resultString = basemd5.getMD5ofStr(origin);
    	if(resultString!=null)resultString = resultString.toLowerCase();

    }
    catch (Exception ex) {

    }
    return resultString;
  }
 
 
}


