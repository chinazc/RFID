package com.cw.assetsrfid.common;

public class HexUtil {
	/**
	 * desc : 
	 * 
	 * @param   byte[] bArray
	 * 
	 * @param    int num
	 * 
	 * @return    String
	 */   
    public static final String bytesToHexString(byte[] bArray, int num) {
        StringBuffer sb = new StringBuffer(num);
        String sTemp;
        for (int i = 0; i < num; i++) {
         sTemp = Integer.toHexString(0xFF & bArray[i]);
         if (sTemp.length() < 2)
          sb.append(0);
         sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
	/**
	 * desc : 
	 * 
	 * @param   byte[] bArray
	 * 
	 * @param    int num
	 * 
	 * @return    String
	 */   
    public static final String bytesToHexStringWithSpace(byte[] bArray, int num) {
        StringBuffer sb = new StringBuffer(num);
        String sTemp;
        for (int i = 0; i < num; i++) {
         sTemp = Integer.toHexString(0xFF & bArray[i]);
         if (sTemp.length() < 2)
          sb.append(0);
         sb.append(sTemp.toUpperCase()+ " ");
        }
        return sb.toString();
    }
	/**
	 * desc : 
	 * 
	 * @param    String hexString
	 * 
	 * @return   byte[]
	 */  
    public static byte[] hexStringToBytes(String hexString) {  
        if (hexString == null || hexString.equals("")) {  
            return null;  
        }  
        hexString = hexString.toUpperCase();  
        int length = hexString.length() / 2;  
        char[] hexChars = hexString.toCharArray();  
        byte[] d = new byte[length];  
        for (int i = 0; i < length; i++) {  
            int pos = i * 2;  
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
              
        }  
        return d;  
    }  
    
	/**
	 * desc : 
	 * 
	 * @param    char c
	 * 
	 * @return   byte
	 */   
    private static byte charToByte(char c) {  
        return (byte) "0123456789ABCDEF".indexOf(c);  
    }     
    
    
    
    /* 摘要:
    *     Insert a Space Between Every Two Chars
    *
    * @param  Str String
    *     String that has no spaces
    *
    * @return String
    *     String inserted spaces
    */
    public static String AutoAddSpace(String Str)
    {
        String StrDone = "";//string.Empty;
        if (Str == null || Str.length() == 0)
        {
            return StrDone;
        }
        int i;
       
        for (i = 0; i <= (Str.length() - 2); i = i + 2)
        {
            StrDone = StrDone + Str.charAt(i) + Str.charAt(i+1) + " ";
        }
        if (Str.length() % 2 != 0)
        {
            StrDone = StrDone + "0" + Str.charAt(i);
        }
   
    
        return StrDone;
        
    }/* end AutoAddSpace */
    
    
    
    
    /* 摘要:
    *     16进制表达的字符窜转成int
    *	  举列："123"=>291
    *
    * @param  String strHex
    *    
    *
    * @return int
    *     
    */
    public static int Convert16StrToInt(String strHex)
    {
       
    	return Integer.valueOf(strHex, 16);
    }
    
    
    /* 摘要:
    *     10进制表达的字符窜转成int
    *	  举列："123"=>123
    *
    * @param  String strHex
    *     
    *
    * @return int
    *     
    */
    public static int Convert10StrToInt(String strHex)
    {
       
    	return Integer.valueOf(strHex, 16);
    }
    
    /* 摘要:
    *     byte变量转成16进制表达的字符窜
    *	  举列：0x55=>"55"
    *
    * @param  String strHex
    *     
    *
    * @return String
    *     
    */
    public static String ConvertByteTo16Str(byte bt)
    {
       
    	return String.format("%02X", bt);
    }
    
    
    /* 摘要:
    *     16进制表达的字符窜转成单字符窜
    *	  举列："51"=>"Q"
    *
    * @param  String strHex
    *     
    *
    * @return String
    *     
    */
    public static String Convert16StrToStr(String strHex)
    {
    	int nHex = Integer.valueOf(strHex, 16);
    	return String.format("%c", nHex);
    }
    
    
    /* 摘要:
    *     16进制表达的字符窜转成单字符窜
    *	  举列："51"=>"Q"
    *
    * @param  String strHex
    *     
    *
    * @return String
    *     
    */
    public static String Convert16StrToStr(String[] strHex, int nStartIndex, int nLength)
    {
    	StringBuffer sb=new StringBuffer();
    	
    	for(int i=nStartIndex; i< nStartIndex+nLength; i++)
    	{
    		sb.append(Convert16StrToStr(strHex[i]));
    	}
    	
    	return sb.toString();
    }
    
    /* 摘要:
    *     字符串转化成16进制表达的字符窜
    *	  举列："ABCDEFG"=>"41424344454647"
    *
    * @param  String s
    *     
    *
    * @return HexString
    *     
    */
	public static String StringToHexString(String s)   
	{   
		if (s==null)return "";
		
		String str="";   
		for (int i=0;i<s.length();i++)   
		{   
		int ch = (int)s.charAt(i);   
		String s4 = Integer.toHexString(ch);   
		str = str + s4; 
		}   
		return str;   
	} 
	
	
	
	
    /* 摘要:
    *     16进制表达的字符窜转成字符窜
    *	  举列："41424344454647"=>ABCDEFGQ"
    *
    * @param  String strHex
    *     
    *
    * @return String
    *     
    */
    public static String HexStringToString(String strHex)
    {
    	if (strHex==null)return "";
    	
    	StringBuffer sb=new StringBuffer();
    	String strTemp="";
    	for(int i=0; i< strHex.length(); i+=2)
    	{
    		strTemp = strHex.substring(i, i+2);
    		sb.append(Convert16StrToStr(strTemp));
    	}
    	
    	return sb.toString().trim();
    }
}
