/**
 * 
 */
package com.cw.assetsrfid.CWDriver;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.util.Log;

import com.cw.assetsrfid.common.HexUtil;
//import com.test.BTClient.CommonUtil;



/**
 * @author koogle
 *
 */
@SuppressLint("DefaultLocale")
public class CWReaderParser {
	
    private static  String TAG = "BTCLientActivity";
    private static  boolean D = true;
    
    
    
	private static final String Convert = null;
	private byte[] 				m_arrByteRcvData;
	private byte[]  				m_arrByteCommandData;
	private String  				m_strRcvData;
	private String   				m_strCommandData;
	private String[]  				m_arrStrHexRcvData;
	private String[]  				m_arrStrHexCommandData;
	private int     				m_nRcvLen;
	private int 					m_nCommandLen;	
	
    private String 					strCWHeader,  strCWCommandType, strCWDataLen,  strCWCommand, strCWCRC ,strCWTailer;
    private int 					nCWDataLen;

	
	
			

    private int LoopNum_cnt = 0;
    private boolean change_q_1st = true;
    private boolean change_q_message = true;
    String pc = "";
    String epc = "";
    String crc = "";
    String rssi = "";

    int FailEPCNum = 0;
    int SucessEPCNum = 0;
    double errnum = 0;
    double db_errEPCNum = 0;
    double db_LoopNum_cnt = 0;
    String per = "0.000";
    private static int[] mixerGainTable = {0, 3, 6, 9, 12, 15, 16};
    private static int[] IFAmpGainTable = { 12, 18, 21, 24, 27, 30, 36, 40 };   

    

	
	public int ERR_CODE; //�ɹ�����Ϊ0������������strReturnData, ʧ�ܲ���<0, ʧ��������strErrDesc
	public String RFIDCOMMAND="";  //���ڷ������֣�RFID����
    public String strErrDesc = "";
    public String strReturnData = "";
    public String strOKDesc = "";
    public static HashMap<String , String> mapData= new HashMap<String , String>();

	
	
	/**
	 * desc : ���캯��
	 * 
	 * @param   byte[] arrRcvRawData, 
	 * @param   int nLen
	 * 
	 * @return    String
	 */ 
	
	
	
	public void CWReaderParser(){
		

		return;
	}
	
	
	public void PreparePackage(byte[] arrRcvRawData, int nLen){
		
		ERR_CODE 		= 0;
		RFIDCOMMAND         = "";
		strErrDesc 		= "";
		strReturnData 	= "";
		strOKDesc 		= "";
		
        mapData.put("PC", "");
        mapData.put("EPC", "");
        mapData.put("CRC", "");
        mapData.put("RSSI", "");
        mapData.put("PER", "");
        mapData.put("TIME", "");
        mapData.put("BARCODE", "");
		
		m_arrByteRcvData 		= arrRcvRawData;
		m_nRcvLen 				= nLen;
		m_strRcvData 			= HexUtil.bytesToHexString(m_arrByteRcvData, m_nRcvLen);
		String strTemp 			= HexUtil.bytesToHexStringWithSpace(m_arrByteRcvData, m_nRcvLen);
		m_arrStrHexRcvData 		= strTemp.split(" ");
		
		return;
	}
	
	/**
	 * desc : ������byte���鷽ʽ�ı�������
	 * 
	 * 
	 * @return    byte[]
	 */ 
	
	public byte[] getArrayByteRcvData()
	{
		return m_arrByteRcvData;
	}
	
	
	/**
	 * desc : �������ַ�����ʽ�ı�������
	 * 
	 * 
	 * @return    byte[]
	 */ 
	
	public String getStringRcvData()
	{
		return m_strRcvData;
	}
	
	/**
	 * desc : ������16�����ַ������鷽ʽ�ı�������
	 * 
	 * 
	 * @return    byte[]
	 */ 
	public String[] getArrayStrHexRcvData()
	{
		return m_arrStrHexRcvData;
	}
	
	
    private void setErrDesc(int nErrCode, String msg)
    {
    	ERR_CODE 		= nErrCode;
    	strErrDesc 		= msg;
    	strOKDesc		= "";
    	strReturnData	= "";
    	return;
    }
    
    private void setOKDesc(String msg, String data)
    {
    	ERR_CODE 		= CWReaderConstCode.SUCCESS;
    	strErrDesc		= "";
    	strOKDesc 		= msg;
    	strReturnData 	= data;
    	return;
    }
    
    public int getErrCode()
    {
    	return  ERR_CODE;
    } 
    public String getOKDesc()
    {
    	return  strOKDesc;
    }
    public String getErrDesc()
    {
    	return  strErrDesc;
    }
    public String getReturnData()
    {
    	return  strReturnData;
    }
	
	
	/**
	 * desc : ��������
	 * 
	 * 
	 * @return   ������
	 */ 
	public int parsePacket()
	{
		//����Ӧ����ͷ
		
		try{
        
        strCWHeader     = m_arrStrHexRcvData[0];
        strCWCommandType= m_arrStrHexRcvData[1];
        strCWDataLen    = m_arrStrHexRcvData[2] + m_arrStrHexRcvData[3];
        nCWDataLen      = Integer.valueOf(strCWDataLen, 16);
        strCWCommand    = m_strRcvData.substring(8, 8+nCWDataLen*2);
        strCWCRC        = m_arrStrHexRcvData[m_nRcvLen-2];
        strCWTailer     = m_arrStrHexRcvData[m_nRcvLen-1];
        m_strCommandData=strCWCommand;
        m_nCommandLen   = nCWDataLen;
        
        if(D)Log.d(TAG, "rcv strCWHeader:"+ strCWHeader);
        if(D)Log.d(TAG, "rcv strCWCommandType:"+ strCWCommandType);
        if(D)Log.d(TAG, "rcv strCWDataLen:"+ strCWDataLen);
        if(D)Log.d(TAG, "rcv nCWDataLen:"+ String.format("%d", nCWDataLen));
        if(D)Log.d(TAG, "rcv strCWCommand:"+ strCWCommand);
        if(D)Log.d(TAG, "rcv strCWCRC:"+ strCWCRC);
        if(D)Log.d(TAG, "rcv strCWTailer:"+ strCWTailer);	
        
     
        m_arrByteCommandData = HexUtil.hexStringToBytes(strCWCommand);
        m_arrStrHexCommandData = new String[nCWDataLen];
        System.arraycopy(m_arrStrHexRcvData, 4,m_arrStrHexCommandData,0, nCWDataLen);
        											
		}catch(Exception e){
			setErrDesc(CWReaderConstCode.ERR_PARSE_ERROR,"Parse Error!");
			return ERR_CODE;
        }
        
        if(D)Log.d(TAG, "rcv strCWHeader:"+ strCWHeader);
        if(D)Log.d(TAG, "rcv strCWCommandType:"+ strCWCommandType);
        if(D)Log.d(TAG, "rcv strCWDataLen:"+ strCWDataLen);
        if(D)Log.d(TAG, "rcv strCWCommand:"+ strCWCommand);
        if(D)Log.d(TAG, "rcv strCWCRC:"+ strCWCRC);
        if(D)Log.d(TAG, "rcv strCWTailer:"+ strCWTailer);		
        
        

        
        
        //���CWC
        String strCWC = CWReaderCmd.GetCWCRC(strCWCommandType, strCWDataLen, strCWCommand);
        if (strCWC.equals(strCWCRC) == false){
        	setErrDesc(CWReaderConstCode.ERR_CHECK_CRC,"Check CRC Error!");
        	return ERR_CODE;
        }
        
        if(D)Log.d(TAG, "rcv check crc ok");
        
      
        //
        if (m_arrByteRcvData[1] == 'R'){
        	parseRFIDCommand(strCWCommand);
        }else if (m_arrByteRcvData[1] == 'Q'){
        	parse2BarCodeCommand(strCWCommand);
        }else if (m_arrByteRcvData[1] == 'B'){
        	parseBlueToothCommand(strCWCommand);
        }else if (m_arrByteRcvData[1] == 'M'){
        	parseCPUCommand(strCWCommand);
        }
        
       
        
		return 0;
		
	}// end Parse 
	
	/**
	 * desc : ������ά�������
	 * 
	 * @param  String strCommand
	 * 
	 * @return   ������
	 */ 
	private String  parse2BarCodeCommand(String strCommand)
	{
		
		String strCode = HexUtil.Convert16StrToStr(m_arrStrHexCommandData, 0, m_arrStrHexCommandData.length);
		setOKDesc("��ȡ����ɹ�", strCode);
		mapData.put("BARCODE", strCode); 
		
		return "";
	}
	
	
	/**
	 * desc : �������������
	 * 
	 * @param  String strCommand
	 * 
	 * @return   ������
	 */ 
	private int  parseBlueToothCommand(String strCommand)
	{
		String strAscCommand = m_arrByteCommandData.toString();
		
		return 0;
	}
	

	
	
	/**
	 * desc : ����CPU���������
	 * 
	 * @param  String strCommand
	 * 
	 * @return   ������
	 */ 
	private int  parseCPUCommand(String strHexCommand)
	{
		
		byte btCommand = m_arrByteCommandData[0];
		
		if ( btCommand == (byte)'V' ){
			String strHardwareVersion = HexUtil.Convert16StrToStr(m_arrStrHexCommandData, 1,12);//Ӳ���汾��ASCii�� 12byte
			String strSoftwareVersion = HexUtil.Convert16StrToStr(m_arrStrHexCommandData, 1+12,12);//����汾��ASCii�� 12 byte
			setOKDesc("��ȡ�������汾��Ϣ�ɹ�", strHardwareVersion + "," + strSoftwareVersion);
			mapData.put("CPU_SOFTWAREVERSION", strSoftwareVersion);
			mapData.put("CPU_HARDWAREVERSION", strHardwareVersion);
			
		}else if ( btCommand == (byte)'B' ){
			int Data1 = (int)m_arrByteCommandData[1]; //��ѹ=byte*0.1V 1byte 
			int Data2 = (int)m_arrByteCommandData[2]; //ʣ������ٷֱ�=byte% 1byte
			setOKDesc("��ѯ���ʣ������ɹ�", String.format("V%f,%d%%", Data1*0.1,Data2));
			mapData.put("CPU_VOLTAGE", String.format("%.2f", Data1*0.1));
			mapData.put("CPU_ELECTRIC", String.format("%d", Data2));
			
		}else if ( btCommand == (byte)'W' ){
			byte Data = m_arrByteCommandData[1];//��R��=rfid��Ĭ�ϣ�,��Q��=��ά��,��0��=����ģʽ����������
			setOKDesc("���ù���ģʽ�ɹ�", HexUtil.Convert16StrToStr(m_arrStrHexCommandData[1]));
			mapData.put("CPU_WORKMODE", HexUtil.Convert16StrToStr(m_arrStrHexCommandData[1]));
		}else if ( btCommand == (byte)'S' ){
			byte subCommand = m_arrByteCommandData[1];
			if (subCommand == (byte)'R'){
				int nReadNum = Integer.valueOf(m_arrStrHexCommandData[2] + m_arrStrHexCommandData[3], 16); // Hbye,lbyte;ÿ�ΰ���ѯ�����������65535�Σ�Ĭ��0�Σ�Ϊ0ʱ�����¼������������ſ�ֹͣ��
				setOKDesc("����RFID-�����������������ɹ�", String.format("%d", nReadNum));
				mapData.put("CPU_READNUM", String.format("%d", nReadNum));
			}else if (subCommand == (byte)'A'){
				int nSleepPara = Integer.valueOf(m_arrStrHexCommandData[2] + m_arrStrHexCommandData[3],16); //Hbye,lbyte;�޲�������������Ȼ��ģ���������״̬
				setOKDesc("����RFID-���߲����ɹ�", String.format("%d", nSleepPara));
				mapData.put("CPU_SLEEPNUM",  String.format("%d", nSleepPara));
			}else if (subCommand == (byte)'B'){
				String strBeepPara = HexUtil.Convert16StrToStr(m_arrStrHexCommandData[2]); //��1��=�̣���һ�� ,��2��=�У���һ��, ��3��=������һ��
				setOKDesc("��RFID-�����������ɹ�", strBeepPara);
				mapData.put("CPU_READNUM", strBeepPara);
			}
		}else if ( btCommand == (byte)'E' ){
			String strCPUErrCode = HexUtil.Convert16StrToStr(m_arrStrHexCommandData[1]);
			if (strCPUErrCode.equals("1")){
				setErrDesc(CWReaderConstCode.FAIL_CPU_CHECK_CRC, 
							"У���벻��");
			}else if (strCPUErrCode.equals("2")){
				setErrDesc(CWReaderConstCode.FAIL_CPU_INVALID_COMMAND, 
							"�޴�����");
			}else if (strCPUErrCode.equals("3")){
				setErrDesc(CWReaderConstCode.FAIL_CPU_DATA_LENGTH, 
							"���ݳ��Ȳ���");
			}else if (strCPUErrCode.equals("4")){
				setErrDesc(CWReaderConstCode.FAIL_CPU_INVALID_PARA, 
							"����������Χ");
			}else{
				setErrDesc(CWReaderConstCode.FAIL_CPU_UNKOWN, 
							"δ֪������"+strCPUErrCode);
			}
		}else{
			setErrDesc(CWReaderConstCode.FAIL_CPU_UNKOWN, 
					"δ֪CPUָ��");
		}
		
		
		return 0;
	}
	
	
	
	/**
	 * desc : ����RFID�����
	 * 
	 * @param  String strCommand
	 * 
	 * @return   ������
	 */ 
	private void  parseRFIDCommand(String strHexCommand)
	{
		
	    String strRFIDHeader,  strRFIDCommandType,  strRFIDCommand,strRFIDCmdContent, strRFIDCRC ,strRFIDTailer;
	    String strErrParameter; //ֻ���ڷ��ش���ʱ����
		
	    strRFIDHeader 		= m_arrStrHexCommandData[0];
	    strRFIDCommandType 	= m_arrStrHexCommandData[1];
	    strRFIDCommand 		= m_arrStrHexCommandData[2];
	    strRFIDCmdContent 	= strHexCommand.substring(3*2, 3*2+(m_nCommandLen -5)*2);
	    strRFIDCRC 			= m_arrStrHexCommandData[m_nCommandLen-2];
	    strRFIDTailer 		= m_arrStrHexCommandData[m_nCommandLen-1];
	    strErrParameter 	= m_arrStrHexCommandData[5];
        if(D)Log.d(TAG, "rcv strRFIDHeader:"+ strRFIDHeader);
        if(D)Log.d(TAG, "rcv strRFIDCommandType:"+ strRFIDCommandType);
        if(D)Log.d(TAG, "rcv strRFIDCmdContent:"+ strRFIDCmdContent);
        if(D)Log.d(TAG, "rcv strRFIDCRC:"+ strRFIDCRC);
        if(D)Log.d(TAG, "rcv strRFIDTailer:"+ strRFIDTailer);
        if(D)Log.d(TAG, "rcv strErrParameter:"+ strErrParameter);	 
        
        RFIDCOMMAND = strRFIDCommand;
        
        //Check RFID crc
        //ERR_CODE= CWReaderConstCode.ERR_CHECK_RFID_CRC;
        
	    /*
	    if (strRFIDCommandType.equals("01"))//��Ӧ֡
	    {
	    	if (m_arrStrHexCommandData[2].equals("FF"))//��������ִ֡��ʧ��
	    	{
	    		String PL = m_arrStrHexCommandData[3] + m_arrStrHexCommandData[4]; //ָ���������
	    		String Parameter = m_arrStrHexCommandData[5];
	    		//Command Error  0x17 				  	    ����֡��ָ��������
	    		//FHSS Fail			 0x20 				    ��Ƶ�����ŵ���ʱ�������ŵ������ʱ���ڶ���ռ�á�
	    		//Inventory Fail 0x15 						��ѯ����ʧ�ܡ�û�б�ǩ���ػ��߷������� CRC У�����
	    		//Access Fail 	 0x16 						���ʱ�ǩʧ�ܣ��п����Ƿ������� password ���ԡ�
	    		//Read Fail 	   0x09 						����ǩ���ݴ�����ʧ�ܡ� ��ǩû�з��ػ��߷������� CRC У�����
	    		//Read Error 	   0xA0 | Error code          ����ǩ���ݴ洢�����󡣷��صĴ����� 0xA0 λ�� Error Code �õ�Error code ��Ϣ����±�
	    		//Write Fail	   0x10 							д��ǩ���ݴ�����ʧ�ܡ���ǩû�з��ػ��߷������� CRC У�����
	    		//Write Error    0xB0 | Error code  д��ǩ���ݴ洢�����󡣷��صĴ����� 0xB0 λ�� Error Code �õ�Error code ��Ϣ����±�
	    		//Lock Fail      0x13 						������ǩ���ݴ�����ʧ�ܡ� ��ǩû�з��ػ��߷������� CRC У�����
	    		//Lock Error     0xC0 | Error code ������ǩ���ݴ洢�����󡣷��صĴ����� 0xC0 λ�� Error Code�õ�Error code ��Ϣ����±�
	    		//Kill Fail      0x12 						����ǩʧ�ܡ���ǩû�з��ػ��߷������� CRC У�����
	    		//Kill Error 	   0xD0 | Error code����ǩ���󡣷��صĴ����� 0xC0 λ�� Error Code �õ���Errorcode��Ϣ����±�
	    		//ReadProtect Fail 							 0x2A    ReadProtect ָ��ʧ�ܣ���ǩû�з������ݻ��߷�������CRC У�����
	    		//Reset ReadProtect Fail 				 0x2B    Reset ReadProtect ָ��ʧ�ܣ���ǩû�з������ݻ��߷������� CRC У�����
	    		//Change EAS Fail								 0x1B    Change EAS ָ��ʧ�ܣ� ��ǩû�з������ݻ��߷�������CRC У�����
	    		//NXP ����ָ���ǩ���صĴ������ 0xE0 | Error code   NXP ����ָ���ǩ���صĴ�����룬��������� 0xE0���ϱ�ǩ���ص� Error Code �õ���
	    	}
	    }*/
		

		
        if (strRFIDCommandType.equals( RFIDConstCode.FRAME_TYPE_INFO )  //֪ͨ֡
        		&& strRFIDCommand.equals(RFIDConstCode.CMD_INVENTORY)  )        //Succeed to Read EPC
        {
            //Console.Beep();
            SucessEPCNum = SucessEPCNum + 1;
            db_errEPCNum = FailEPCNum;
            db_LoopNum_cnt = db_LoopNum_cnt + 1;
            errnum = (db_errEPCNum / db_LoopNum_cnt) * 100;
            per = String.format("{0:0.000}", errnum);

            int rssidBm = Integer.valueOf(m_arrStrHexCommandData[5], 16); // rssidBm is negative && in bytes
            if (rssidBm > 127)
            {
                rssidBm = -((-rssidBm)&0xFF);
            }
            String tbxCoupling="-10";
            String tbxAntennaGain="3";
            rssidBm -= Integer.valueOf(tbxCoupling, 10);
            rssidBm -= Integer.valueOf(tbxAntennaGain, 10);
            String rssi = String.format("%d", rssidBm);   

            int PCEPCLength = ((Integer.valueOf((m_arrStrHexCommandData[6]), 16)) / 8 + 1) * 2;
            
            pc = m_arrStrHexCommandData[6] + " " + m_arrStrHexCommandData[7];
            epc = "";
            for (int i = 0; i < PCEPCLength - 2; i++)
            {
                epc = epc + m_arrStrHexCommandData[8 + i];
            }
            //epc = HexUtil.AutoAddSpace(epc);
            crc = m_arrStrHexCommandData[6 + PCEPCLength] + " " + m_arrStrHexCommandData[7 + PCEPCLength];
            per="";
            
            
            if(D)Log.d(TAG, "rssi=" + rssi);
            if(D)Log.d(TAG, "PCEPCLength=" + PCEPCLength);
            if(D)Log.d(TAG, "pc=" + pc);
            if(D)Log.d(TAG, "epc=" + epc);
            if(D)Log.d(TAG, "crc=" + crc);
            setOKDesc("��ѯ�����ɹ�", GetEPC(pc, epc, crc, rssi, per));
            mapData.put("PC", pc);
            mapData.put("EPC", epc);
            mapData.put("CRC", crc);
            mapData.put("RSSI", rssi);
            mapData.put("PER", per);
            
            Date currentTime = new Date();
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        String dateString = formatter.format(currentTime);
            mapData.put("TIME",dateString);
            
            
            
        }
        else if (strRFIDCommandType.equals( RFIDConstCode.FRAME_TYPE_ANS))  //��Ӧ֡
        {
            if (strRFIDCommand.equals(RFIDConstCode.CMD_EXE_FAILED))
            {
            	String strRFIDErrCode = m_arrStrHexCommandData[5];
            	
            	int failType = Integer.valueOf(m_arrStrHexCommandData[5], 16);
                if (m_arrStrHexCommandData.length > 9) // has PC+EPC field
                {
                    String txtOperateEpc = "";
                    int pcEpcLen = Integer.valueOf(m_arrStrHexCommandData[6], 16);

                    for (int i = 0; i < pcEpcLen; i++)
                    {
                        txtOperateEpc += m_arrStrHexCommandData[i + 7] + " ";
                    }
                }
                else
                {
                    String txtOperateEpc = "";
                }
                if (strRFIDErrCode.equals( RFIDConstCode.FAIL_INVENTORY_TAG_TIMEOUT))//���Թ�
                {
                    FailEPCNum = FailEPCNum + 1;
                    db_errEPCNum = FailEPCNum;
                    db_LoopNum_cnt = db_LoopNum_cnt + 1;
                    errnum = (db_errEPCNum / db_LoopNum_cnt) * 100;
                    String per = String.format("{0:0.000}", errnum);
//TODO                    GetEPC(pc, epc, crc, rssi_i, rssi_q, per);
                    //pbx_Inv_Indicator.Visible = false;
                    setErrDesc(CWReaderConstCode.FAIL_INVENTORY_TAG_TIMEOUT, 
                    			"��ѯ����ʧ�ܡ�û�б�ǩ���ػ��߷������� CRC У�����");
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_FHSS_FAIL))
                {
                	setErrDesc(CWReaderConstCode.FAIL_FHSS_FAIL,
                				"��Ƶ�����ŵ���ʱ�������ŵ������ʱ���ڶ���ռ�á�");
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_ACCESS_PWD_ERROR))
                {
                	setErrDesc(CWReaderConstCode.FAIL_ACCESS_PWD_ERROR,
                				"���ʱ�ǩʧ�ܣ��п����Ƿ������� password ���ԡ�");
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_READ_MEMORY_NO_TAG))
                {
                	setErrDesc(CWReaderConstCode.FAIL_READ_MEMORY_NO_TAG,
                				"����ǩ���ݴ洢��ʧ�ܡ� ��ǩû�з��ػ��߷������� CRC У�����");
                }
                else if (strErrParameter.charAt(0) == RFIDConstCode.FAIL_READ_ERROR_CODE_BASE.charAt(0))
                {
                	setErrDesc(CWReaderConstCode.FAIL_READ_ERROR_CODE_BASE,
                				"����ǩ���ݴ洢�����󡣴�����: " + ParseErrCode(failType));
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_WRITE_MEMORY_NO_TAG))
                {
                	setErrDesc(CWReaderConstCode.FAIL_WRITE_MEMORY_NO_TAG,
                				"д��ǩ���ݴ洢��ʧ�ܡ���ǩû�з��ػ��߷������� CRC У�����");
                }
                else if (strErrParameter.charAt(0) == RFIDConstCode.FAIL_WRITE_ERROR_CODE_BASE.charAt(0))
                {
                	setErrDesc(CWReaderConstCode.FAIL_WRITE_ERROR_CODE_BASE,
                				"д��ǩ���ݴ洢������ ������: " + ParseErrCode(failType));
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_LOCK_NO_TAG))
                {
                	setErrDesc(CWReaderConstCode.FAIL_LOCK_NO_TAG,
                				"������ǩ���ݴ�����ʧ�ܡ� ��ǩû�з��ػ��߷������� CRC У�����");
                }
                else if (strErrParameter.charAt(0) == RFIDConstCode.FAIL_LOCK_ERROR_CODE_BASE.charAt(0))
                {
                	setErrDesc(CWReaderConstCode.FAIL_LOCK_ERROR_CODE_BASE,
                				"������ǩ���ݴ洢�����󡣴�����: " + ParseErrCode(failType));
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_KILL_NO_TAG))
                {
                	setErrDesc(CWReaderConstCode.FAIL_KILL_NO_TAG,
                				"����ǩʧ�ܡ���ǩû�з��ػ��߷������� CRC У�����");
                }
                else if (strErrParameter.charAt(0) == RFIDConstCode.FAIL_KILL_ERROR_CODE_BASE.charAt(0))
                {
                	setErrDesc(CWReaderConstCode.FAIL_KILL_ERROR_CODE_BASE,
                				"����ǩʧ�ܡ�������: " + ParseErrCode(failType));
                }
                else if (strRFIDErrCode.equals(RFIDConstCode.FAIL_NXP_CHANGE_CONFIG_NO_TAG))
                {
                	setErrDesc(CWReaderConstCode.FAIL_NXP_CHANGE_CONFIG_NO_TAG,
                				"û�б�ǩ��Ӧ, NXP Change Config����ʧ��");
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_NXP_CHANGE_EAS_NO_TAG))
                {
                	setErrDesc(CWReaderConstCode.FAIL_NXP_CHANGE_EAS_NO_TAG,
                				"û�б�ǩ��Ӧ, NXP Change EAS����ʧ��");
                }
                else if (strRFIDErrCode.equals(RFIDConstCode.FAIL_NXP_CHANGE_EAS_NOT_SECURE))
                {
                	setErrDesc(CWReaderConstCode.FAIL_NXP_CHANGE_EAS_NOT_SECURE,
                				"��ǩ���ڰ�ȫ״̬�� NXP Change EAS����ʧ��");
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_NXP_EAS_ALARM_NO_TAG))
                {
                    String txtOperateEpc = "";
                    setErrDesc(CWReaderConstCode.FAIL_NXP_EAS_ALARM_NO_TAG,
                    			"û�б�ǩ��Ӧ, NXP EAS Alarm����ʧ��");
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_NXP_READPROTECT_NO_TAG))
                {
                	setErrDesc(CWReaderConstCode.FAIL_NXP_READPROTECT_NO_TAG,
                				"û�б�ǩ��Ӧ, NXP ReadProtect ����ʧ��");
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_NXP_RESET_READPROTECT_NO_TAG))
                {
                	setErrDesc(CWReaderConstCode.FAIL_NXP_RESET_READPROTECT_NO_TAG,
                				"û�б�ǩ��Ӧ, NXP Reset ReadProtect ����ʧ��");
                }
                else if (strErrParameter.charAt(0) == RFIDConstCode.FAIL_CUSTOM_CMD_BASE.charAt(0))
                {
                	setErrDesc(CWReaderConstCode.FAIL_CUSTOM_CMD_BASE,
                				"ָ��ִ��ʧ�ܣ�������: " + ParseErrCode(failType));
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_INVALID_PARA))
                {
                	setErrDesc(CWReaderConstCode.FAIL_INVALID_PARA,
                				"��Ч������");
                }
                else if (strRFIDErrCode.equals( RFIDConstCode.FAIL_INVALID_CMD))
                {
                	setErrDesc(CWReaderConstCode.FAIL_INVALID_CMD,
                				"����֡��ָ��������");
                }
                
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_STOP_MULTI) )           //Stop Multi Invertory
            {
            	setOKDesc("ֹͣ�����ѯ�����ɹ�", "");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_SET_QUERY) )           //SetQuery
            {
            	setOKDesc("����Query�����ɹ�", "");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_GET_QUERY) )           //GetQuery
            {
                String infoGetQuery = "";
                String[] strMSB = String16toString2(m_arrStrHexCommandData[5]);
                String[] strLSB = String16toString2(m_arrStrHexCommandData[6]);
                int intQ = Integer.valueOf(strLSB[6]) * 8 
                				+ Integer.valueOf(strLSB[5]) * 4
                				+ Integer.valueOf(strLSB[4]) * 2 
                				+ Integer.valueOf(strLSB[3]);
                String strM = "";
                if ((strMSB[6] + strMSB[5]) == "00")
                {
                    strM = "1";
                }
                else if ((strMSB[6] + strMSB[5]) == "01")
                {
                    strM = "2";
                }
                else if ((strMSB[6] + strMSB[5]) == "10")
                {
                    strM = "4";
                }
                else if ((strMSB[6] + strMSB[5]) == "11")
                {
                    strM = "8";
                }
                String strTRext = "";
                if (strMSB[4] == "0")
                {
                    strTRext = "NoPilot";
                }
                else
                {
                    strTRext = "UsePilot";
                }
                String strTarget = "";
                if (strLSB[7] == "0")
                {
                    strTarget = "A";
                }
                else
                {
                    strTarget = "B";
                }
                infoGetQuery = "DR=" + strMSB[7] + ", ";
                infoGetQuery = infoGetQuery + "M=" + strM + ", ";
                infoGetQuery = infoGetQuery + "TRext=" + strTRext + ", ";
                infoGetQuery = infoGetQuery + "Sel=" + strMSB[3] + strMSB[2] + ", ";
                infoGetQuery = infoGetQuery + "Session=" + strMSB[1] + strMSB[0] + ", ";
                infoGetQuery = infoGetQuery + "Target=" + strTarget + ", ";
                infoGetQuery = infoGetQuery + "Q=" + intQ;

                setOKDesc("��ȡQuery�����ɹ�", infoGetQuery);
            }
            else if (strRFIDCommand.equals(RFIDConstCode.CMD_READ_DATA) )        //Read Tag Memory
            {
                String strInvtReadData = "";
                String txtInvtRWData = "";
                String txtOperateEpc = "";
                int dataLen = Integer.valueOf(m_arrStrHexCommandData[3], 16) * 256 + Integer.valueOf(m_arrStrHexCommandData[4], 16);
                int pcEpcLen = Integer.valueOf(m_arrStrHexCommandData[5], 16);

                for (int i = 0; i < pcEpcLen; i++)
                {
                    txtOperateEpc += m_arrStrHexCommandData[i + 6] + "";
                }

                for (int i = 0; i < dataLen - pcEpcLen - 1; i++)
                {
                    strInvtReadData = strInvtReadData + m_arrStrHexCommandData[i + pcEpcLen + 6];
                }
                txtInvtRWData = HexUtil.AutoAddSpace(strInvtReadData);

                setOKDesc("����ǩ���ݴ洢���ɹ�",txtOperateEpc + "=====" + strInvtReadData);
                mapData.put("READ_EPC", txtOperateEpc);
                mapData.put("READ_DATA", strInvtReadData);
            }
            else if ( strRFIDCommand.equals( RFIDConstCode.CMD_WRITE_DATA))
            {
                getSuccessTagEpc(m_arrStrHexCommandData);
                setOKDesc("д��ǩ���ݴ洢���ɹ�" , "");
            }
            else if (strRFIDCommand.equals(  RFIDConstCode.CMD_LOCK_UNLOCK))
            {
                getSuccessTagEpc(m_arrStrHexCommandData);
                setOKDesc("������ǩ���ݴ洢���ɹ�", "");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_KILL) )
            {
                getSuccessTagEpc(m_arrStrHexCommandData);
                setOKDesc("����ǩ�ɹ�", "");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_NXP_CHANGE_CONFIG))
            {
                int pcEpcLen = getSuccessTagEpc(m_arrStrHexCommandData);
                String configWord = m_arrStrHexCommandData[pcEpcLen + 6] + m_arrStrHexCommandData[pcEpcLen + 7];
                setOKDesc("NXP��ǩ Change Config �ɹ�, Config Word: 0x" + configWord ,"");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_NXP_CHANGE_EAS))
            {
                getSuccessTagEpc(m_arrStrHexCommandData);
                setOKDesc("NXP��ǩ Change EAS �ɹ�","");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_NXP_READPROTECT))
            {
                getSuccessTagEpc(m_arrStrHexCommandData);
                setOKDesc("NXP��ǩ ReadProtect �ɹ�","");
            }
            else if (strRFIDCommand.equals(RFIDConstCode.CMD_NXP_RESET_READPROTECT))
            {
                getSuccessTagEpc(m_arrStrHexCommandData);
                setOKDesc("NXP��ǩ Reset ReadProtect �ɹ�","");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_NXP_EAS_ALARM))
            {
            	setOKDesc("NXP��ǩ EAS Alarm �ɹ�","");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_GET_SELECT_PARA) )           //GetQuery
            {
                String infoGetSelParam = "";
                String[] strSelCombParam = String16toString2(m_arrStrHexCommandData[5]);
                String strSelTarget = strSelCombParam[7] + strSelCombParam[6] + strSelCombParam[5];
                String strSelAction = strSelCombParam[4] + strSelCombParam[3] + strSelCombParam[2];
                String strSelMemBank = strSelCombParam[1] + strSelCombParam[0];

                String strSelTargetInfo = null;
                if (strSelTarget == "000")
                {
                    strSelTargetInfo = "S0";
                }
                else if (strSelTarget == "001")
                {
                    strSelTargetInfo = "S1";
                }
                else if (strSelTarget == "010")
                {
                    strSelTargetInfo = "S2";
                }
                else if (strSelTarget == "011")
                {
                    strSelTargetInfo = "S3";
                }
                else if (strSelTarget == "100")
                {
                    strSelTargetInfo = "SL";
                }
                else
                {
                    strSelTargetInfo = "RFU";
                }

                String strSelMemBankInfo = null;
                if (strSelMemBank == "00")
                {
                    strSelMemBankInfo = "RFU";
                }
                else if (strSelMemBank == "01")
                {
                    strSelMemBankInfo = "EPC";
                }
                else if (strSelMemBank == "10")
                {
                    strSelMemBankInfo = "TID";
                }
                else
                {
                    strSelMemBankInfo = "User";
                }
                infoGetSelParam = "Target=" + strSelTargetInfo + ", Action=" + strSelAction + ", Memory Bank=" + strSelMemBankInfo;
                infoGetSelParam = infoGetSelParam + ", Pointer=0x" + m_arrStrHexCommandData[6] + m_arrStrHexCommandData[7] + m_arrStrHexCommandData[8] + m_arrStrHexCommandData[9];
                infoGetSelParam = infoGetSelParam + ", Length=0x" + m_arrStrHexCommandData[10];
                String strTruncate = null;
                if (m_arrStrHexCommandData[11] == "00")
                {
                    strTruncate = "Disable Truncation";
                }
                else
                {
                    strTruncate = "Enable Truncation";
                }
                infoGetSelParam = infoGetSelParam + ", " + strTruncate;

                String txtGetSelLength = m_arrStrHexCommandData[10];

                String strGetSelMask = null;
                int intGetSelMaskByte = Integer.valueOf(m_arrStrHexCommandData[10], 16) / 8;
                int intGetSelMaskBit = Integer.valueOf(m_arrStrHexCommandData[10], 16) - intGetSelMaskByte * 8;
                if (intGetSelMaskBit == 0)
                {
                    for (int i = 0; i < intGetSelMaskByte; i++)
                    {
                        strGetSelMask = strGetSelMask + m_arrStrHexCommandData[12 + i];
                    }
                }
                else
                {
                    for (int i = 0; i < intGetSelMaskByte + 1; i++)
                    {
                        strGetSelMask = strGetSelMask + m_arrStrHexCommandData[12 + i];
                    }
                }

                String txtGetSelMask = HexUtil.AutoAddSpace(strGetSelMask);
                //MessageBox.Show(infoGetSelParam, "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
                setOKDesc("��ȡselect ����ָ��ɹ�", "");
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_GET_RF_CHANNEL))
            {
                double curRfCh = Integer.valueOf(m_arrStrHexCommandData[5],16);
                String curRegion = "02";
                if (curRegion.equals(RFIDConstCode.REGION_CODE_CHN2)){// China 2
                        curRfCh = 920.125 + curRfCh * 0.25;
                }else if (curRegion.equals(RFIDConstCode.REGION_CODE_CHN1)){// China 1
                        curRfCh = 840.125 + curRfCh * 0.25;
                }else if (curRegion.equals(RFIDConstCode.REGION_CODE_US)){// US
                        curRfCh = 902.25 + curRfCh * 0.5;
                }else if (curRegion.equals(RFIDConstCode.REGION_CODE_EUR)){// Europe
                        curRfCh = 865.1 + curRfCh * 0.2;
                }else if (curRegion.equals(RFIDConstCode.REGION_CODE_KOREA)){// Korea
                        curRfCh = 917.1 + curRfCh * 0.2;
                }
                //MessageBox.Show("Current RF Channel is " + curRfCh + " MHz", "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
                setOKDesc("��ȡ�����ŵ��ɹ�",String.format("%f", curRfCh));
                mapData.put("RFCHANNEL", String.format("%f", curRfCh));
            }
            else if (strRFIDCommand.equals(RFIDConstCode.CMD_SET_RF_CHANNEL))
            {
                if (	m_arrStrHexCommandData[3].equals("00")
                   	&& 	m_arrStrHexCommandData[4].equals("01")
                   	&&  m_arrStrHexCommandData[5].equals("00"))
                    	setOKDesc("���ù����ŵ��ɹ�","");
                    else
                    	setOKDesc("���ù����ŵ��ɹ�","����");
                
            }
            else if (strRFIDCommand.equals(RFIDConstCode.CMD_GET_POWER))
            {
                String curPower = m_arrStrHexCommandData[5] + m_arrStrHexCommandData[6];
                //MessageBox.Show("Current Power is " + (Convert.ToInt16(curPower, 16) / 100.0) + "dBm", "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
                float fPower = Integer.valueOf(curPower, 16)/100;
                setOKDesc("��ȡ���书�ʳɹ�",String.format("%.2f", fPower));
                mapData.put("POWER", String.format("%.2f", fPower));
            }
            else if (strRFIDCommand.equals(RFIDConstCode.CMD_SET_POWER))
            {
                if (	m_arrStrHexCommandData[3].equals("00")
                	&& 	m_arrStrHexCommandData[4].equals("01")
                	&&  m_arrStrHexCommandData[5].equals("00"))
                	setOKDesc("���÷��书�ʳɹ�","");
                else
                	setOKDesc("���÷��书�ʳɹ�","����");
                
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_READ_MODEM_PARA))
            {
                int mixerGain = mixerGainTable[Integer.valueOf(m_arrStrHexCommandData[5], 16)];
                int IFAmpGain = IFAmpGainTable[Integer.valueOf(m_arrStrHexCommandData[6], 16)];
                String signalTh = m_arrStrHexCommandData[7] + m_arrStrHexCommandData[8];
                //MessageBox.Show("Mixer Gain is " + mixerGain + "dB, IF AMP Gain is " + IFAmpGain + "dB, Decode Threshold is 0x" + signalTh + ".", 
                String strTemp = String.format("%d,%d, %s", mixerGain, IFAmpGain, signalTh);
                setOKDesc("��ȡ���ս���������ɹ�", strTemp);
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_SCAN_JAMMER))
            {
                int startChannel = Integer.valueOf(m_arrStrHexCommandData[5], 16);
                int stopChannel = Integer.valueOf(m_arrStrHexCommandData[6], 16);
//TODO   
                /*            
                hBarChartJammer.Items.Maximum = 40;
                hBarChartJammer.Items.Minimum = 0;

                hBarChartJammer.Items.Clear();

                int[] allJammer = new int[(stopChannel - startChannel + 1)];
                int maxJammer = -100;
                int minJammer = 20;
                for (int i = 0; i < (stopChannel - startChannel + 1); i++)
                {
                    int jammer = Integer.valueOf(m_arrStrHexCommandData[7 + i], 16);
                    if (jammer > 127)
                    {
                        jammer = -((-jammer) & 0xFF);
                    }
                    allJammer[i] = jammer;
                    if (jammer >= maxJammer)
                    {
                        maxJammer = jammer;
                    }
                    if (jammer <= minJammer)
                    {
                        minJammer = jammer;
                    }
                }
                int offset = -minJammer + 3;
                for (int i = 0; i < (stopChannel - startChannel + 1); i++)
                {
                    allJammer[i] = allJammer[i] + offset;
                    hBarChartJammer.Items.Add(new HBarItem((double)(allJammer[i]),(double)offset, (i + startChannel).ToString(), Color.FromArgb(255, 190, 200, 255)));
                }
                hBarChartJammer.RedrawChart();
                */
            }
            else if (strRFIDCommand.equals( RFIDConstCode.CMD_SCAN_RSSI))
            {
                int startChannel = Integer.valueOf(m_arrStrHexCommandData[5], 16);
                int stopChannel = Integer.valueOf(m_arrStrHexCommandData[6], 16);
//TODO                
/*
                hBarChartRssi.Items.Maximum = 73;
                hBarChartRssi.Items.Minimum = 0;

                hBarChartRssi.Items.Clear();

                int[] allRssi = new int[(stopChannel - startChannel + 1)];
                int maxRssi = -100;
                int minRssi = 20;
                for (int i = 0; i < (stopChannel - startChannel + 1); i++)
                {
                    int rssi = Integer.valueOf(m_arrStrHexCommandData[7 + i], 16);
                    if (rssi > 127)
                    {
                        rssi = -((-rssi) & 0xFF);
                    }
                    allRssi[i] = rssi;
                    if (rssi >= maxRssi)
                    {
                        maxRssi = rssi;
                    }
                    if (rssi <= minRssi)
                    {
                        minRssi = rssi;
                    }
                }
                int offset = -minRssi + 3;
                for (int i = 0; i < (stopChannel - startChannel + 1); i++)
                {
                    allRssi[i] = allRssi[i] + offset;
                    hBarChartRssi.Items.Add(new HBarItem((double)(allRssi[i]), (double)offset, (i + startChannel).ToString(), Color.FromArgb(255, 190, 200, 255)));
                }
                hBarChartRssi.RedrawChart();
                */
            }
            else if (strRFIDCommand.equals(RFIDConstCode.CMD_GET_MODULE_INFO))//���Թ�
            {
            	//boolean checkingReaderAvailable=true;
                //if (checkingReaderAvailable)
                //{
                    String hardwareVersion = "";
                    String softwareVersion="";
                    String manufactory="";
                    if (m_arrStrHexCommandData[5].equals(RFIDConstCode.MODULE_HARDWARE_VERSION_FIELD))
                    {
                        try
                        {
                            for (int i = 0; i < Integer.valueOf(m_arrStrHexCommandData[4], 16) - 1; i++)
                            {
                                hardwareVersion += HexUtil.Convert16StrToStr(m_arrStrHexCommandData[6 + i]);
                            }
                            mapData.put("RFID_HARDWAREVERSION", hardwareVersion); 
                        }
                        catch (Exception ex)
                        {
                            hardwareVersion = m_arrStrHexCommandData[6].charAt( 1) + "." + m_arrStrHexCommandData[7];
                            mapData.put("RFID_HARDWAREVERSION", hardwareVersion); 
                        }
                        setOKDesc("��ȡ��д��ģ��Ӳ���汾��Ϣ�ɹ�", hardwareVersion);
                    }else if (m_arrStrHexCommandData[5].equals(RFIDConstCode.MODULE_SOFTWARE_VERSION_FIELD))
                    {
                        try
                        {
                            for (int i = 0; i < Integer.valueOf(m_arrStrHexCommandData[4], 16) - 1; i++)
                            {
                                softwareVersion += HexUtil.Convert16StrToStr(m_arrStrHexCommandData[6 + i]);
                            }
                            mapData.put("RFID_SOFTWAREVERSION", softwareVersion); 
                        }
                        catch (Exception ex)
                        {
                        	softwareVersion = m_arrStrHexCommandData[6].charAt( 1) + "." + m_arrStrHexCommandData[7];
                            mapData.put("RFID_SOFTWAREVERSION", softwareVersion);        
                        }
                        setOKDesc("��ȡ��д��ģ������汾��Ϣ�ɹ�", softwareVersion);
                    }else if (m_arrStrHexCommandData[5].equals(RFIDConstCode.MODULE_MANUFACTURE_INFO_FIELD))
                    {
                        try
                        {
                            for (int i = 0; i < Integer.valueOf(m_arrStrHexCommandData[4], 16) - 1; i++)
                            {
                            	manufactory += HexUtil.Convert16StrToStr(m_arrStrHexCommandData[6 + i]);
                            }
                            mapData.put("RFID_MANUFACTORY", manufactory);
                        }
                        catch (Exception ex)
                        {
                        	manufactory = m_arrStrHexCommandData[6].charAt( 1) + "." + m_arrStrHexCommandData[7];
                        	mapData.put("RFID_MANUFACTORY", manufactory);
                        }
                        setOKDesc("��ȡ��д��ģ����������Ϣ�ɹ�", manufactory);
                    }else{
                    	setOKDesc("��ȡ��д��ģ����Ϣ�ɹ�", "unknown info");
                    }
                    
                //}
            }
            else if (strRFIDCommand.equals( "1A"))
            {
                if (m_arrStrHexCommandData[5] == "02")
                {
                    //MessageBox.Show("IO" + m_arrStrHexCommandData[6].charAt(1) + " is " + (m_arrStrHexCommandData[7] == "00" ? "Low" : "High"), "Information", MessageBoxButtons.OK, MessageBoxIcon.Information);
                }
            }else{
            	setErrDesc(CWReaderConstCode.ERROR_CODE_UNKOWN_COMMND, 
            				"δָ֪�����" + strRFIDCommand);
            }
        }// end ��Ӧ֡����
        else{
        	setErrDesc(CWReaderConstCode.ERROR_CODE_UNKOWN_FRAME_TYPE, 
        				"δ֪֡����"+strRFIDCommandType);
        }

    
	
			
		
		
		
	}
	
    private String[] String16toString2(String S)
    {
        String[] S_array = new String[8];
        int intS = Integer.valueOf(S, 16);
        for (int i = 7; i >= 0; i--)
        {
            S_array[i] = "0";
            if (intS >= Math.pow(2, i)) S_array[i] = "1";
 //TODO           //intS = intS - Integer.valueOf(S_array[i]) * Convert.ToInt32(System.Math.Pow(2, i));
        }
        return S_array;
    }
	
	
    private String ParseErrCode(int errorCode)
    {
        switch (errorCode & 0x0F)
        {
            case RFIDConstCode.ERROR_CODE_OTHER_ERROR :
                return "��������";
            case RFIDConstCode.ERROR_CODE_MEM_OVERRUN:
                return "�洢�����޻򲻱�֧�ֵ� PC ֵ ";
            case RFIDConstCode.ERROR_CODE_MEM_LOCKED:
                return "�洢������ ";
            case RFIDConstCode.ERROR_CODE_INSUFFICIENT_POWER:
                return "��Դ����";
            case RFIDConstCode.ERROR_CODE_NON_SPEC_ERROR:
                return "���ض�����";
            default :
                return "���ض�����";
        }
    }
	
	
	
    private String GetEPC(String pc, String epc, String crc, String rssi , String per)
    {
    	String strTemp;
    	strTemp = "PC="	+ pc + ";" 
    			+ "EPC="+ epc + ";"
    			+ "CRC=" + crc + ";"
    			+ "RSSI=" + rssi + ";"
    			+ "PER=" + per;
    	return strTemp;
    }
	
    private int getSuccessTagEpc(String[] packetRx)
    {
    	/*
        txtOperateEpc.Text = "";
        if (packetRx.Length < 9)
        {
            return 0;
        }
        int pcEpcLen = Convert.ToInt32(packetRx[5], 16);
        for (int i = 0; i < pcEpcLen; i++)
        {
            txtOperateEpc.Text += packetRx[i + 6] + " ";
        }*/
    	int pcEpcLen=0;
        return pcEpcLen;
    }
	

    
    
    /*
    private void setStatus(String msg)
    {
        //rtbxStatus.Text = msg;
        //rtbxStatus.ForeColor = color;
    	//strErrDesc = msg;
    }*/
    

	

        


	

}// end class CWReaderParser
