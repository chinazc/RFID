package com.cw.assetsrfid.CWDriver;

public class CWReaderConstCode {
		public final static String  CW_FRAME_BEGIN_HEX = "55";
		public final static String  CW_FRAME_CMD_TYPE_R_HEX = "52";//R=RFID，电子标贴52
		public final static String  CW_FRAME_CMD_TYPE_Q_HEX = "51";//Q=二维码  51
		public final static String  CW_FRAME_CMD_TYPE_M_HEX = "4D";//M=CPU，主板4D
		public final static String  CW_FRAME_CMD_TYPE_B_HEX = "42";//B=蓝牙设置命令42
		public final static String  CW_FRAME_END_HEX = "AA";
		
		

		public final static int   SUCCESS                     = 0;
		
		public final static int  ERR_CHECK_CRC 				= -10001;
		public final static int  ERR_CHECK_RFID_CRC 			= -10002;
		public final static int  ERR_PARSE_ERROR				= -10003;
		
	    public final static int FAIL_INVALID_PARA 				= -20001;
	    public final static int FAIL_INVENTORY_TAG_TIMEOUT 		= -20002;
	    public final static int FAIL_INVALID_CMD 					= -20003;
	    public final static int FAIL_FHSS_FAIL 					= -20004;
	    public final static int FAIL_ACCESS_PWD_ERROR 			= -20005;
	    public final static int FAIL_READ_MEMORY_NO_TAG 			= -20006;
	    public final static int FAIL_READ_ERROR_CODE_BASE 		= -20007;
	    public final static int FAIL_WRITE_MEMORY_NO_TAG 			= -20008;
	    public final static int FAIL_WRITE_ERROR_CODE_BASE 		= -20009;
	    public final static int FAIL_LOCK_NO_TAG					= -20010;
	    public final static int FAIL_LOCK_ERROR_CODE_BASE 		= -20011;
	    public final static int FAIL_KILL_NO_TAG 					= -20012;
	    public final static int FAIL_KILL_ERROR_CODE_BASE 		= -20013;
	    public final static int FAIL_NXP_CHANGE_CONFIG_NO_TAG 	= -20014;
	    public final static int FAIL_NXP_READPROTECT_NO_TAG 		= -20015;
	    public final static int FAIL_NXP_RESET_READPROTECT_NO_TAG = -20016;
	    public final static int FAIL_NXP_CHANGE_EAS_NO_TAG 		= -20017;
	    public final static int FAIL_NXP_CHANGE_EAS_NOT_SECURE 	= -20018;
	    public final static int FAIL_NXP_EAS_ALARM_NO_TAG 		= -20019;
	    public final static int FAIL_CUSTOM_CMD_BASE 				= -20020;
	    public final static int FAIl_MEM_OVERUN 					= -20021;
	    public final static int ERROR_CODE_OTHER_ERROR 			= -20022;
	    public final static int ERROR_CODE_MEM_OVERRUN 			= -20023;
	    public final static int ERROR_CODE_MEM_LOCKED 			= -20024;
	    public final static int ERROR_CODE_INSUFFICIENT_POWER 	= -20025;
	    public final static int ERROR_CODE_NON_SPEC_ERROR 		= -20026;
	    public final static int ERROR_CODE_UNKOWN_COMMND 			= -20091;
	    public final static int ERROR_CODE_UNKOWN_FRAME_TYPE		= -20092;
	    
	    public final static int FAIL_CPU_CHECK_CRC				= -30001;
	    public final static int FAIL_CPU_INVALID_COMMAND			= -30002;
	    public final static int FAIL_CPU_DATA_LENGTH				= -30003;
	    public final static int FAIL_CPU_INVALID_PARA				= -30004;
	    public final static int FAIL_CPU_UNKOWN					= -30091;
	    
		
}
