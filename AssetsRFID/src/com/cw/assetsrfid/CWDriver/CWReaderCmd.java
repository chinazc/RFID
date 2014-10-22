/**
 * 
 */
package com.cw.assetsrfid.CWDriver;


import com.cw.assetsrfid.common.HexUtil;

/**
 * @author koogle
 *
 */

public class CWReaderCmd {

	
    //
    // ժҪ:
    //     ��ȡ�������汾��ѯ
    //
    //
    // @return String
    //     
    public static String CPU_SetManualModeFrame()
    {
    	//Command:��V��
    	byte arrCommand[] = {'V'};
    	//String strCommand = HexUtil.StringToHexString("NLS0302000");//ɨ��ģʽ ���ֶ����� 
    	//String strCommand = HexUtil.StringToHexString("NLS0302010");//ɨ��ģʽ ���Զ�����
    	String strCommand = HexUtil.StringToHexString("NLS0302020");//ɨ��ģʽ ����������
    	return Build2BarCodesCommand(strCommand);
    }  
    
    
    
	
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////CPU MAINBOARD//////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    
	
    
    
    //
    // ժҪ:
    //     ��ȡ�������汾��ѯ
    //
    //
    // @return String
    //     
    public static String CPU_GetModuleInfoFrame()
    {
    	//Command:��V��
    	byte arrCommand[] = {'V'};
    	String strCommand = HexUtil.bytesToHexString(arrCommand, 1);
    	return BuildCPUCommand(strCommand);
    }           
    
    //
    // ժҪ:
    //     ���ʣ�����
    //
    //
    // @return String
    //     
    public static String CPU_GetBatteryFrame()
    {
    	//Command:��B��
    	byte arrCommand[] = {'B'};
    	String strCommand = HexUtil.bytesToHexString(arrCommand, 1);
    	return BuildCPUCommand(strCommand);
    }    
    
    //
    // ժҪ:
    //     ����ģʽ�趨
    //
    // @param  
    //   strWorkMode:
    //
    // @return String
    //     
    public static String CPU_SetWordModeFrame(String strWorkMode )
    {
    	//Command:��W�� 'R'/'Q'/'0'
    	byte arrCommand[] = {'W', 0x00};
    	if (strWorkMode.equals("R")){
    		arrCommand[1] = 'R';
    	}else if (strWorkMode.equals("Q")){
    		arrCommand[1] = 'Q';
    	}else{
    		arrCommand[1] = 'R';
    	}
    	String strCommand = HexUtil.bytesToHexString(arrCommand, 2);
    	return BuildCPUCommand( strCommand );
    }
    
    //
    // ժҪ:
    //     ����RFID������������,ÿ�ΰ���ѯ�����������65535�Σ�Ĭ��0�Σ�Ϊ0ʱ�����¼������������ſ�ֹͣ
    //
    // @param     nNum:
    //
    // @return    String
    //     
    public static String CPU_SetRFIDParaReadNumFrame(int nNum )
    {
    	//Command:��S�� 'R'   2len
    	byte arrCommand[] = {'S', 'R'};
    	String strCommand = HexUtil.bytesToHexString(arrCommand, 2) + String.format("%04X", nNum);
    	
    	return BuildCPUCommand(strCommand);
    }
    
    //
    // ժҪ:
    //     ����RFID������������,�޲�������������Ȼ��ģ���������״̬
    //
    // @param     nNum:
    //
    // @return    String
    //     
    public static String CPU_SetRFIDParaSleppFrame(int nNum )
    {
    	//Command:��S�� 'A'   2len
    	byte arrCommand[] = {'S', 'A'};
    	String strCommand = HexUtil.bytesToHexString(arrCommand, 2) + String.format("%04X", nNum);
    	
    	return BuildCPUCommand(strCommand);
    }
    
    //
    // ժҪ:
    //     ����RFID��������������
    //
    // @param  
    //   cBeepPara:��1��=�̣���һ��/��2��=�У���һ��/ ��3��=������һ��
    //
    // @return      String
    //    
    public static String CPU_SetBeepParaFrame(char cBeepPara )
    {
    	//Command:��S�� 'B'   '1'/'2'/'3'

    	byte arrCommand[] = {'S', 'B', (byte)cBeepPara};
 
    	String strCommand = HexUtil.bytesToHexString(arrCommand, 3) ;
    	
    	return BuildCPUCommand(strCommand);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////BULE TOOTH////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////
    
    
    //
    // ժҪ:
    //     ��������ͨѶ
    //
    //
    // @return String
    //     
    public static String BLUE_TestCommFrame()
    {
    	String strCommand = "AT";
    	return BuildBlueToothCommand(strCommand);
    } 
    
    
    //
    // ժҪ:
    //     �޸�����������
    // @param   btBaud : '1'/2/3/4/5/6/7/8/9/A/B/C
    //
    // @return String
    //     
    public static String BLUE_SetBaudFrame( String sBaud )
    {
    	String strCommand = "AT+" + sBaud.trim();
    	return BuildBlueToothCommand(strCommand);
    } 
    
    
    //
    // ժҪ:
    //     �޸���������
    // @param   strName : 
    //
    // @return String
    //     
    public static String BLUE_SetNameFrame( String  strName )
    {
    	
    	String strCommand = "AT+"  + "NAME" + strName.trim();
    	return BuildBlueToothCommand(strCommand);
    }     
    

    //
    // ժҪ:
    //     �޸������������
    // @param   strPass : 
    //
    // @return String
    //     
    public static String BLUE_SetPassFrame( String  strPass )
    {
    	
    	String strCommand = "AT+"  + "PIN" + strPass.trim();
    	return BuildBlueToothCommand(strCommand);
    }  
 
    //
    // ժҪ:
    //     ��ȡATָ��汾
    // @param   strPass : 
    //
    // @return String
    //     
    public static String BLUE_GetATVersionFrame(  )
    {
    	
    	String strCommand = "AT+"  + "VERSION";
    	return BuildBlueToothCommand(strCommand);
    } 
    

    
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    
    
    
    
    
    //
    // ժҪ:
    //     Build Get Module Information Command Packet
    //
    // @param  
    //   infoType:
    //     Information Type(Hardware version, Software version or Manufacture Information
    //
    // @return 
    //     Get Module Information Command Packet
    public static String RFID_GetModuleInfoFrame(String infoType)
    {
    	return BuildRfidCommand(RFIDCommands.BuildGetModuleInfoFrame(infoType));
    }
    
    //
    // ժҪ:
    //     Build Get PA Power Packet
    //
    // @return 
    //     Get PA Power Packet
    public static String RFID_GetPaPowerFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildGetPaPowerFrame());
    }
    
    //
    // ժҪ:
    //     Build Get Query Parameter Packet
    //
    // @return 
    //     Get Query Parameter Packet 
    public static String RFID_GetQueryFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildGetQueryFrame());
    }   

    //
    // ժҪ:
    //     Build Get RF Channel Packet
    //
    // @return 
    //     Get RF Channel Packet
    public static String RFID_GetRfChannelFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildGetRfChannelFrame());
    }     

    //
    // ժҪ:
    //     Build Get Select Parameter Packet
    //
    // @return 
    //     Get Select Parameter Packet
    public static String RFID_GetSelectFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildGetSelectFrame());
    }    

    //
    // ժҪ:
    //     Build Insert RF Channel Command Packet
    //
    // @param  
    //   channelNum:
    //     the Count of Channels Will Be Inserted
    //
    //   channelList:
    //     List of All the Channel Index
    //
    // @return 
    //     Insert RF Channel Command Packet
    public static String RFID_InsertRfChFrame(int channelNum, byte[] channelList)
    {
        return BuildRfidCommand(RFIDCommands.BuildInsertRfChFrame(channelNum, channelList));
    }    
    
    
    //
    // ժҪ:
    //     Build IO Control Command Packet
    //
    // @param  
    //   optType:
    //     operation type. 0x00��set IO dirction; 0x01: set IO Level; 0x02: Read IO Level.
    //
    //   ioPort:
    //     the IO which will be handled. IO1 - IO4
    //
    //   modeOrLevel:
    //     if optType=0x00, set IO mode(0x00 for Input mode, 0x01 for Output mode) if
    //     optType=0x01, set IO Level(0x00 for output Low level, 0x01 for output High
    //     level
    //
    // @return 
    //     IO Control Command Packet   
    public static String RFID_IoControlFrame(byte optType, byte ioPort, byte modeOrLevel)
    {
        return BuildRfidCommand(RFIDCommands.BuildIoControlFrame(optType, ioPort, modeOrLevel));
    }    
    
  
    //
    // ժҪ:
    //     Build Kill Tag Packet
    //
    // @param  
    //   killPwd:
    //     Kill Password. If Kill Password is "00 00 00 00", the Kill Operation Will
    //     Be ignored By Tag
    //
    //   rfu:
    //     RFU(000)/Recomm. If you want to kill a tag, just pass 0 by default.
    //
    // @return 
    //     Kill Tag Packet
    public static String RFID_KillFrame(String killPwd, int rfu)
    {
        return BuildRfidCommand(RFIDCommands.BuildKillFrame(killPwd, rfu));
    }    
    
    
    //
    // ժҪ:
    //     Build Load Configuration From NV Memory Command Packet
    //
    // @return 
    //     Load Configuration From NV Memory Command Packet
    public static String RFID_LoadConfigFromNvFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildLoadConfigFromNvFrame());
    }     
    
    
    //
    // ժҪ:
    //     Build Lock Tag Packet
    //
    // @param  
    //   accessPwd:
    //     Access Password. If a tag NOT needs Access, it should be "00 00 00 00".
    //
    //   ld:
    //     Lock Payload.It should be 3 bytes, the first 4 bits are not used
    //
    // @return 
    //     Lock Tag Packet
    public static String RFID_LockFrame(String accessPwd, int ld)
    {
        return BuildRfidCommand(RFIDCommands.BuildLockFrame(accessPwd, ld));
    }   
    
    
    //
    // ժҪ:
    //     Build Change Config Command Packet for NXP G2X Tags
    //
    // @param  
    //   accessPwd:
    //     Access Password.
    //
    //   ConfigData:
    //     16 bits Config data. The bits to be toggled in the configuration register
    //     need to be set to '1'.
    //
    // @return 
    //     Change Config Command Packet
    public static String RFID_NXPChangeConfigFrame(String accessPwd, int ConfigData)
    {
        return BuildRfidCommand(RFIDCommands.BuildNXPChangeConfigFrame(accessPwd, ConfigData));
    }    
    
    
    //
    // ժҪ:
    //     Build Change EAS Command Packet for NXP G2X Tags
    //
    // @param  
    //   accessPwd:
    //     Access Password.
    //
    //   isSet:
    //     If uset want to set PSF bit, fill true.
    //
    // @return 
    //     Change EAS Command Packet
    public static String RFID_NXPChangeEasFrame(String accessPwd,  boolean isSet)
    {
        return BuildRfidCommand(RFIDCommands.BuildNXPChangeEasFrame(accessPwd, isSet));
    }   
    
    //
    // ժҪ:
    //     Build EAS Alarm Command Packet for NXP G2X Tags
    //
    // @return 
    //     EAS Alarm Command Packet
    public static String RFID_NXPEasAlarmFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildNXPEasAlarmFrame());
    }     
    
  
    //
    // ժҪ:
    //     Build ReadProtect/ResetReadProtect Command Packet for NXP G2X Tags
    //
    // @param  
    //   accessPwd:
    //     Access Password.
    //
    //   isReset:
    //     If it is a Reset ReadProtect command, fill true.
    //
    // @return 
    //     ReadProtect/ResetReadProtect Command Packet
    public static String RFID_NXPReadProtectFrame(String accessPwd, boolean isReset)
    {
        return BuildRfidCommand(RFIDCommands.BuildNXPReadProtectFrame(accessPwd, isReset));
    }     
    
 
    //
    // ժҪ:
    //     Build Read Tag Data Packet.
    //
    // @param  
    //   accessPwd:
    //     Access Password. If a tag NOT needs Access, it should be "00 00 00 00".
    //
    //   memBank:
    //     Memory Bank, 0x00-RFU,0x01-EPC,0x02-TID,0x03-User
    //
    //   sa:
    //     Start Address
    //
    //   dl:
    //     Data Length in Words
    //
    // @return 
    //     Read Tag Data Packet.
    public static String RFID_ReadDataFrame(String accessPwd, int memBank, int sa, int dl)
    {
        return BuildRfidCommand(RFIDCommands.BuildReadDataFrame(accessPwd, memBank, sa, dl));
    }     
    
    //
    // ժҪ:
    //     Build Read Modem Parameter Packet
    //
    // @return 
    //     Read Modem Parameter Packet
    public static String RFID_ReadModemParaFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildReadModemParaFrame());
    }    
    

    //
    // ժҪ:
    //     Build Read Multi Tag ID Packet
    //
    // @param  
    //   loopNum:
    //     Loop Number
    //
    // @return 
    //     Read Multi Tag ID Packet
    public static String RFID_ReadMultiFrame(int loopNum)
    {
        return BuildRfidCommand(RFIDCommands.BuildReadMultiFrame(loopNum));
    }   
    
 
    //
    // ժҪ:
    //     Build Read Single Tag ID Packet
    //
    // @return 
    //     Read Single Tag ID Packet
    public static String RFID_ReadSingleFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildReadSingleFrame());
    }  
    
    //
    // ժҪ:
    //     Build Save Configuration to NV Memory Command Packet
    //
    // @param  
    //   NVenable:
    //     0x01 for Enable NV Configuration. The Reader Will Load the Configuration
    //     when Next Power-up.  0x00 for Disable NV Configuration. This Setting Will
    //     Erase the Exist Configuration!
    //
    // @return 
    //     Save Configuration to NV Memory Command Packet
    public static String RFID_SaveConfigToNvFrame(byte NVenable)
    {
        return BuildRfidCommand(RFIDCommands.BuildSaveConfigToNvFrame(NVenable));
    }     
    
    
    //
    // ժҪ:
    //     Build Scan Jammer Command Packet
    //
    // @return 
    //     Scan Jammer Command Packet 
    public static String RFID_ScanJammerFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildScanJammerFrame());
    } 
    
    //
    // ժҪ:
    //     Build Scan RSSI Command Packet
    //
    // @return 
    //     Scan RSSI Command Packet
    public static String RFID_ScanRssiFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildScanRssiFrame());
    }     
    
    //
    // ժҪ:
    //     Build Set CW On/Off Packet
    //
    // @param  
    //   OnOff:
    //     On("FF")/Off("00")
    //
    // @return 
    //     Set CW On/Off Packet
    public static String RFID_SetCWFrame(String OnOff)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetCWFrame(OnOff));
    }     
    
    //
    // ժҪ:
    //     Build Set HFSS On/Off Packet
    //
    // @param  
    //   OnOff:
    //     SET_ON/SET_OFF
    //
    // @return 
    //     Set HFSS On/Off Packet
    public static String RFID_SetFhssFrame(String OnOff)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetFhssFrame(OnOff));
    }     
    
    
    //
    // ժҪ:
    //     Build Set Inventory Mode Frame.  When Set To Mode0, RFID Reader will first
    //     Send Select Command, Then Begion an Inventory Round. If Set to Mode1, It
    //     Will Not.
    //
    // @param  
    //   mode:
    //     Mode0("00")/Mode1("01")
    //
    // @return 
    //     Set Inventory Mode Frame
    public static String RFID_SetInventoryModeFrame(String mode)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetInventoryModeFrame(mode));
    }   
    
    
    //
    // ժҪ:
    //     Build Set Modem Parameter Packet
    //
    // @param  
    //   mixerGain:
    //     mixer gain(0-7)
    //
    //   IFAmpGain:
    //     IF gain(0-7)
    //
    //   signalThreshold:
    //     decode threshold
    //
    // @return 
    //     Set Modem Parameter Packet    
    public static String RFID_SetModemParaFrame(int mixerGain, int IFAmpGain, int signalThreshold)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetModemParaFrame(mixerGain, IFAmpGain, signalThreshold));
    }   
    
 
    //
    // ժҪ:
    //     Build Set Module to Sleep Mode Command Packet
    //
    // @return 
    //     Set Module to Sleep Mode Command Packet
    public static String RFID_SetModuleSleepFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildSetModuleSleepFrame());
    }     
    
    
    //
    // ժҪ:
    //     Build Set PA Power Packet
    //
    // @param  
    //   powerdBm:
    //     dBm * 100, eg. 7.5dBm = 750
    //
    // @return 
    //     Set PA Power Packet
    public static String RFID_SetPaPowerFrame(short powerdBm)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetPaPowerFrame(powerdBm));
    }   
    
    //
    // ժҪ:
    //     Build Set Query Parameter Packet
    //
    // @param  
    //   dr:
    //     DR(1 bit) DR=8(0)��DR=64/3(1)
    //
    //   m:
    //     M(2 bit) M=1(00)��M=2(01)��M=4(10)��M=8(11)
    //
    //   TRext:
    //     TRext(1 bit) no pilot tone(0)��use pilot tone(1)
    //
    //   sel:
    //     Sel(2 bit) ALL(00 01)��~SL(10)��SL(11)
    //
    //   session:
    //     Session(2 bit) S0(00)��S1(01)��S2(10)��S3(11)
    //
    //   target:
    //     Target(1 bit) A(0)��B(1)
    //
    //   q:
    //     Q(4 bit) 0-15
    //
    // @return 
    //     Set Query Parameter Packet
    public static String RFID_SetQueryFrame(int dr, int m, int TRext, int sel, int session, int target, int q)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetQueryFrame( dr,  m,  TRext,  sel,  session,  target,  q));
    }    
    
   
    //
    // ժҪ:
    //     Build Set Reader Environment Mode Command Packet
    //
    // @param  
    //   mode:
    //     Reader environment mode. 0x01 for Dense Reader mode, 0x00 for High Sensitivity
    //     mode
    //
    // @return 
    //     Set Reader Environment Mode Command Packet
    public static String RFID_SetReaderEnvModeFrame(byte mode)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetReaderEnvModeFrame( mode));
    }   
    
    
    //
    // ժҪ:
    //     Build set region Packet
    //
    // @param  
    //   region:
    //     Region Code
    //
    // @return 
    //     Set Region Packet   
    public static String RFID_SetRegionFrame(String region)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetRegionFrame(  region));
    }     
    
    
    //
    // ժҪ:
    //     Build Set RF Channel Packet
    //
    // @param  
    //   ch:
    //     Channel Number
    //
    // @return 
    //     Set RF Channel Packet   
    public static String RFID_SetRfChannelFrame(String ch)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetRfChannelFrame(  ch));
    }  
    
    
    //
    // ժҪ:
    //     Build Set Select Parameter Packet
    //
    // @param  
    //   target:
    //     Target(3 bit) S0(000)��S1(001)��S2(010)��S3(011)��SL(100)
    //
    //   action:
    //     Action(3 bit) Reference to ISO18000-6C
    //
    //   memBank:
    //     Memory bank(2 bit) RFU(00)��EPC(01)��TID(10)��USR(11)
    //
    //   pointer:
    //     Pointer(32 bit) Start Address
    //
    //   len:
    //     Length(8 bit)
    //
    //   mask:
    //     Mask(0-255bit) Mask Data according to Length
    //
    //   truncated:
    //     Truncate(1 bit) Disable(0)��Enable(1)
    //
    // @return 
    //     Set Select Parameter Packet
    public static String RFID_SetSelectFrame(int target, int action, int memBank, int pointer, int len, int truncated, String mask)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetSelectFrame(  target, action, memBank, pointer, len, truncated, mask));
    }  
    
    //
    // ժҪ:
    //     Build Set Module Sleep Time Command Packet
    //
    // @param  
    //   time:
    //     Idle Time In Minutes.  If the module has NO operation after the time, it
    //     will go to sleep mode.
    //
    // @return 
    //     Set Module Sleep Time Command Packet
    public static String RFID_SetSleepTimeFrame(byte time)
    {
        return BuildRfidCommand(RFIDCommands.BuildSetSleepTimeFrame( time));
    }  
    
    
    //
    // ժҪ:
    //     Build Stop Read Multi TAG ID Packet
    //
    // @return 
    //     Stop Read Multi TAG ID Packet
    public static String RFID_StopReadFrame()
    {
        return BuildRfidCommand(RFIDCommands.BuildStopReadFrame( ));
    }   
    
    //
    // ժҪ:
    //     Build Write Tag Data Packet
    //
    // @param  
    //   accessPwd:
    //     Access Password. If a tag NOT needs Access, it should be "00 00 00 00".
    //
    //   memBank:
    //     Memory Bank, 0x00-RFU,0x01-EPC,0x02-TID,0x03-User
    //
    //   sa:
    //     Start Address
    //
    //   dl:
    //     Data Length in word
    //
    //   dt:
    //     Data to Write. It Should Be "dl" Words
    //
    // @return 
    //     Write Tag Data Packet
    
    public static String RFID_WriteDataFrame(String accessPwd, int memBank, int sa, int dl, String dt)
    {
        return BuildRfidCommand(RFIDCommands.BuildWriteDataFrame(accessPwd, memBank, sa, dl, dt ));
    }  
    
    
    
    
	/**
	 * desc : ����rfidָ���ַ���
	 * 
	 * @param
	 * 
	 * @return 
	 */
	public  static String BuildRfidCommand(String rawframe)
	{
        String strCWDataLen, strCWCRC;
        strCWDataLen = String.format("%04X", rawframe.length()/2);
        strCWCRC = GetCWCRC(CWReaderConstCode.CW_FRAME_CMD_TYPE_R_HEX, strCWDataLen, rawframe);
       
        String frame = CWReaderConstCode.CW_FRAME_BEGIN_HEX 
				       		 + CWReaderConstCode.CW_FRAME_CMD_TYPE_R_HEX 
				       		 + strCWDataLen 
				       		 + rawframe 
				       		 + strCWCRC 
				       		 + CWReaderConstCode.CW_FRAME_END_HEX; 
        
        return frame;
	}
	
	
	/**
	 * desc : ����rfidָ���ַ���
	 * 
	 * @param
	 * 
	 * @return 
	 */
	private  static String Build2BarCodesCommand(String rawframe)
	{
        String strCWDataLen, strCWCRC;
        strCWDataLen = String.format("%04X", rawframe.length()/2);
        strCWCRC = GetCWCRC(CWReaderConstCode.CW_FRAME_CMD_TYPE_Q_HEX, strCWDataLen, rawframe);
       
        String frame = CWReaderConstCode.CW_FRAME_BEGIN_HEX 
				       		 + CWReaderConstCode.CW_FRAME_CMD_TYPE_Q_HEX 
				       		 + strCWDataLen 
				       		 + rawframe 
				       		 + strCWCRC 
				       		 + CWReaderConstCode.CW_FRAME_END_HEX; 
        
        return frame;
	}

	/**
	 * desc : ����BuleToothָ���ַ���
	 * 
	 * @param
	 * 
	 * @return 
	 */
	private  static String BuildBlueToothCommand(String rawframe)
	{
        String strCWDataLen, strCWCRC;
        strCWDataLen = String.format("%04X", rawframe.length()/2);
        strCWCRC = GetCWCRC(CWReaderConstCode.CW_FRAME_CMD_TYPE_B_HEX, strCWDataLen, rawframe);
       
        String frame = CWReaderConstCode.CW_FRAME_BEGIN_HEX 
				       		 + CWReaderConstCode.CW_FRAME_CMD_TYPE_B_HEX 
				       		 + strCWDataLen 
				       		 + rawframe 
				       		 + strCWCRC 
				       		 + CWReaderConstCode.CW_FRAME_END_HEX; 
        
        return frame;
	}
	
	/**
	 * desc : ����CPUָ���ַ���
	 * 
	 * @param
	 * 
	 * @return 
	 */
	private  static String BuildCPUCommand(String rawframe)
	{
        String strCWDataLen, strCWCRC;
        strCWDataLen = String.format("%04X", rawframe.length()/2);
        strCWCRC = GetCWCRC(CWReaderConstCode.CW_FRAME_CMD_TYPE_M_HEX, strCWDataLen, rawframe);
       
        String frame = CWReaderConstCode.CW_FRAME_BEGIN_HEX 
				       		 + CWReaderConstCode.CW_FRAME_CMD_TYPE_M_HEX 
				       		 + strCWDataLen 
				       		 + rawframe 
				       		 + strCWCRC 
				       		 + CWReaderConstCode.CW_FRAME_END_HEX; 
        
        return frame;
	}
	
	

	/**
	 * desc : 
	 * 
	 * @param   String strCMDType
	 * 
	 * @param   String strCMDLen
	 * 
	 * @param   String strCMDData
	 * 
	 * @return 
	 */	
	public static String GetCWCRC(String strCMDType, String strCMDLen, String strCMDData)
    {
        if (strCMDType == null 
		   		 || strCMDType.trim().length() == 0
		   		 || strCMDLen == null 
		   		 || strCMDLen.trim().length() == 0 
		   		 || strCMDData == null 
		   		 || strCMDData.trim().length() == 0 )
        {
            return "00";
        }

	   String strData = strCMDType + strCMDLen + strCMDData;
	   int nLoopNum = strData.length()/2;
	
	   
	   byte[] bos = new byte[nLoopNum];
	   bos = HexUtil.hexStringToBytes(strData);
	   
	   byte xor_data=0x00;
		for(int xori=0;xori<nLoopNum; xori++)
		{
			xor_data ^= bos[xori] ;
		}
		

		return String.format("%02X", xor_data);
    }	
    
    

	
}/* end Class */
