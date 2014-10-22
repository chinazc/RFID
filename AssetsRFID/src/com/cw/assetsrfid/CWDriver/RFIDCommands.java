/*
*  @author 标明开发该类模块的作者 
*  @version 标明该类模块的版本 
*/

package com.cw.assetsrfid.CWDriver;







//namespace RFID_Reader_Cmds

 // 摘要:
 //     This Class defines some utility to build packet according to RFID Reader
 //     Serial Port Communication Protocol.
 public class RFIDCommands
 {
     public RFIDCommands()
     {
     }
 

     

     
     
     /*
     * 摘要:
     *     Build a whole frame
     *
     * @param  
     *   data:
     *     this should not include checksum
     *
     * @return String
     *     Whole frame. Frame header, ender and checksum will be added automatically.
     *     It will insert a space between every two chars.
     */
     public static String BuildFrame(String data)
     {
         if (data == null)
         {
             return "";
         }
         String frame = data.trim();

         String checkSum = CalcCheckSum(frame);
         frame = RFIDConstCode.FRAME_BEGIN_HEX  + frame + checkSum + RFIDConstCode.FRAME_END_HEX;
         
         return frame;
     }
     
     
     
     
     /*
     * 摘要:
     *     Build a Whole Frame that Doesn't Have Data Field
     *
     *  @param   msgType:  Packet Type
     *
     *  @param   cmdCode:   Command Code
     *
     *  @return    String
     *     Whole frame. Frame header, ender and checksum will be added automatically.
     *     It will insert a space between every two chars.
     */
     public static String BuildFrame(String msgType, String cmdCode)
     {
         if (msgType == null || cmdCode == null)
         {
             return "";
         }
         msgType = msgType.trim();
         if (msgType.length() == 1)
         {
             msgType = "0" + msgType;
         }
         cmdCode = cmdCode.trim();
         if (cmdCode.length() == 1)
         {
             cmdCode = "0" + cmdCode;
         }
         String frame = msgType + cmdCode + "00" + "00";
         frame = RFIDConstCode.FRAME_BEGIN_HEX + frame + cmdCode + RFIDConstCode.FRAME_END_HEX;
         
 //        return AutoAddSpace(frame);
         return frame;
     }
     
     
     
     
     //
     // 摘要:
     //     Build a whole frame
     //
     // @param  
     //   msgType:
     //     Packet Type
     //
     //   cmdCode:
     //     Command Code
     //
     //   data:
     //     Data field
     //
     // @return 
     //     Whole frame. Frame header, ender and checksum will be added automatically.
     //     It will insert a space between every two chars.
     public static String BuildFrame(String msgType, String cmdCode, String data)
     {
         if (msgType == null || cmdCode == null)
         {
             return "";
         }
         msgType = msgType.trim();
         if (msgType.length() == 1)
         {
             msgType = "0" + msgType;
         }
         cmdCode = cmdCode.trim();
         if (cmdCode.length() == 1)
         {
             cmdCode = "0" + cmdCode;
         }
         int dataHexLen = 0;
         if (data != null)
         {
             data = data.trim();
             if (data.length() == 1)
             {
                 data = "0" + data;
             }
             dataHexLen = data.length() / 2;
             // if data.Length is odd, the last char of data will be discard
             
             data = data.substring(0, dataHexLen * 2);//data = data.Substring(0, dataHexLen * 2);
         }
         
         String frame = msgType + cmdCode + String.format("%04X", dataHexLen) + data;//String frame = msgType + cmdCode + dataHexLen.ToString("X4") + data;
         String checkSum = CalcCheckSum(frame);

         frame = RFIDConstCode.FRAME_BEGIN_HEX + frame + checkSum + RFIDConstCode.FRAME_END_HEX;
         
 //        frame = AutoAddSpace(frame);
         return frame;
     }
     
     
     
     //
     // 摘要:
     //     Build a whole frame
     //
     // @param  
     //   msgType:
     //     Packet Type
     //
     //   cmdCode:
     //     Command Code
     //
     //   dataArr:
     //     Data field
     //
     // @return 
     //     Whole frame. Frame header, ender and checksum will be added automatically.
     //     It will insert a space between every two chars.
     public static String BuildFrame(String msgType, String cmdCode, String[] dataArr)
     {
         if (msgType == null || cmdCode == null)
         {
             return "";
         }
         msgType = msgType.trim();
         if (msgType.length() == 1)
         {
             msgType = "0" + msgType;
         }
         cmdCode = cmdCode.trim();
         if (cmdCode.length() == 1)
         {
             cmdCode = "0" + cmdCode;
         }

         int dataHexLen = 0;
         if (dataArr != null)
         {
             dataHexLen = dataArr.length ;
         }

         String frame = RFIDConstCode.FRAME_BEGIN_HEX + msgType + cmdCode + String.format("%04X",dataHexLen);

         int checksum = 0;
         checksum += RFIDConstCode.FRAME_BEGIN_BYTE + RFIDConstCode.FRAME_END_BYTE;
         try
         {
             for (int i = 0; i < dataHexLen; i++)
             {
                 dataArr[i] = dataArr[i].trim();
                 if (dataArr[i].length() == 1)
                 {
                     frame += "0" + dataArr[i];
                 }
                 else
                 {
                     frame += dataArr[i];
                 }
                 checksum += Integer.valueOf(dataArr[i], 16);
             }
         }
         catch (Exception e)
         {
        	 System.out.printf("convert error" + e.toString());
         }       
         
         frame = frame + String.format("%02X", checksum % 256) + RFIDConstCode.FRAME_END_HEX;
         
         return frame;
     }
     
     
     //
     // 摘要:
     //     Build Get Module Information Command Packet
     //
     // @param  
     //   infoType:
     //     Information Type(Hardware version, Software version or Manufacture Information
     //
     // @return 
     //     Get Module Information Command Packet
     public static String BuildGetModuleInfoFrame(String infoType)
     {
    	 return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_GET_MODULE_INFO, infoType);
     }
     
     
     
     //
     // 摘要:
     //     Build Get PA Power Packet
     //
     // @return 
     //     Get PA Power Packet
     public static String BuildGetPaPowerFrame()
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_GET_POWER);
     }
     
     
     
     //
     // 摘要:
     //     Build Get Query Parameter Packet
     //
     // @return 
     //     Get Query Parameter Packet
     public static String BuildGetQueryFrame()
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_GET_QUERY);
     }
     
     
     //
     // 摘要:
     //     Build Get RF Channel Packet
     //
     // @return 
     //     Get RF Channel Packet
     public static String BuildGetRfChannelFrame()
     {
    	 return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_GET_RF_CHANNEL);
     }
     
     
     //
     // 摘要:
     //     Build Get Select Parameter Packet
     //
     // @return 
     //     Get Select Parameter Packet
     public static String BuildGetSelectFrame()
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_GET_SELECT_PARA);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildInsertRfChFrame(int channelNum, byte[] channelList)
     {
    	 
         String param = String.format("%02X",channelNum);
         if (channelList == null || channelList.length  == 0)
         {
             return "";
         }
         for (int i = 0; i < channelNum; i++)
         {
        	 param += String.format("%02X", channelList[i]);//param += channelList[i].ToString("X2");
         }
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_INSERT_FHSS_CHANNEL, param);   	
          

     }
     
     
     
     //
     // 摘要:
     //     Build IO Control Command Packet
     //
     // @param  
     //   optType:
     //     operation type. 0x00：set IO dirction; 0x01: set IO Level; 0x02: Read IO Level.
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
     public static String BuildIoControlFrame(byte optType, byte ioPort, byte modeOrLevel)
     {
    	 
    	 String strParam0 = String.format("%02X", optType);
    	 String strParam1 = String.format("%02X",ioPort);
    	 String strParam2 = String.format("%02X",modeOrLevel);
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_IO_CONTROL, strParam0 + strParam1 + strParam2);


     }
     
     
     //
     // 摘要:
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
     public static String BuildKillFrame(String killPwd, int rfu )//默认0
     {
         killPwd = killPwd.trim();
         String dataField = killPwd;
      
         if (rfu != 0)
         {
             try
             {
                 dataField += String.format("%02X", rfu);
             }
             catch (Exception ex)
             {
            	 System.out.println("Convert RFU Error! " + ex);
             }

         }
      
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_KILL, dataField);
     }
     
     
     //
     // 摘要:
     //     Build Load Configuration From NV Memory Command Packet
     //
     // @return 
     //     Load Configuration From NV Memory Command Packet
     public static String BuildLoadConfigFromNvFrame()
     {
    	 return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_LOAD_NV_CONFIG);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildLockFrame(String accessPwd, int ld)
     {
         accessPwd = accessPwd.trim();
         if (accessPwd.length() != 8)
         {
             return "";
         }
         String dataField = accessPwd.trim();
         dataField += String.format("%06X", ld);//dataField += ld.ToString("X6");
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_LOCK_UNLOCK, dataField);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildNXPChangeConfigFrame(String accessPwd, int ConfigData)
     {
         accessPwd = accessPwd.trim();
         if (accessPwd.length() != 8)
         {
             return "";
         }
         String dataField = accessPwd;
         dataField += String.format("%04X", ConfigData);
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_NXP_CHANGE_CONFIG, dataField);

     }
     
     
     
     //
     // 摘要:
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
     public static String BuildNXPChangeEasFrame(String accessPwd, boolean isSet)
     {
         accessPwd = accessPwd.trim();
         if (accessPwd.length() != 8)
         {
             return "";
         }
         String dataField = accessPwd;
         dataField += isSet ? "01" : "00";
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_NXP_CHANGE_EAS, dataField);
     }
     
     
     //
     // 摘要:
     //     Build EAS Alarm Command Packet for NXP G2X Tags
     //
     // @return 
     //     EAS Alarm Command Packet
     public static String BuildNXPEasAlarmFrame()
     {
    	 return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_NXP_EAS_ALARM);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildNXPReadProtectFrame(String accessPwd, boolean isReset)
     {
         accessPwd = accessPwd.trim();
         if (accessPwd.length() != 8)
         {
             return "";
         }
         String dataField = accessPwd;
         dataField += isReset ? "01" : "00";
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_NXP_READPROTECT, dataField);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildReadDataFrame(String accessPwd, int memBank, int sa, int dl)
     {
         if (accessPwd.trim().length() != 8)
         {
             return "";
         }
         String dataField = accessPwd.trim();
         dataField += String.format("%02X", memBank) + String.format("%04X", sa) + String.format("%04X", dl);
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_READ_DATA, dataField);
     }
     
     
     
     //
     // 摘要:
     //     Build Read Modem Parameter Packet
     //
     // @return 
     //     Read Modem Parameter Packet
     public static String BuildReadModemParaFrame()
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_READ_MODEM_PARA);
     }
     
     
     
     //
     // 摘要:
     //     Build Read Multi Tag ID Packet
     //
     // @param  
     //   loopNum:
     //     Loop Number
     //
     // @return 
     //     Read Multi Tag ID Packet
     public static String BuildReadMultiFrame(int loopNum)
     {
         if (loopNum <= 0 || loopNum > 65536)
         {
             return "";
         }
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_READ_MULTI, "22" + String.format("%04X", loopNum));
         
     }
     
     
     
     //
     // 摘要:
     //     Build Read Single Tag ID Packet
     //
     // @return 
     //     Read Single Tag ID Packet
     public static String BuildReadSingleFrame()
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_INVENTORY);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildSaveConfigToNvFrame(byte NVenable)
     {
    	 return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SAVE_NV_CONFIG, String.format("%02X", NVenable));
     }
     
     
     
     //
     // 摘要:
     //     Build Scan Jammer Command Packet
     //
     // @return 
     //     Scan Jammer Command Packet
     public static String BuildScanJammerFrame()
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SCAN_JAMMER);
     }
     
     
     
     //
     // 摘要:
     //     Build Scan RSSI Command Packet
     //
     // @return 
     //     Scan RSSI Command Packet
     public static String BuildScanRssiFrame()
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SCAN_RSSI);
     }
     
     
     //
     // 摘要:
     //     Build Set CW On/Off Packet
     //
     // @param  
     //   OnOff:
     //     On("FF")/Off("00")
     //
     // @return 
     //     Set CW On/Off Packet
     public static String BuildSetCWFrame(String OnOff)
     {
         if (OnOff == null || OnOff.trim().length() == 0)
         {
             return "";
         }
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_CW, OnOff);
     }
     
     
     
     //
     // 摘要:
     //     Build Set HFSS On/Off Packet
     //
     // @param  
     //   OnOff:
     //     SET_ON/SET_OFF
     //
     // @return 
     //     Set HFSS On/Off Packet
     public static String BuildSetFhssFrame(String OnOff)
     {
         if (OnOff == null || OnOff.trim().length() == 0)
         {
             return "";
         }
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_FHSS, OnOff);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildSetInventoryModeFrame(String mode)
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_INVENTORY_MODE, mode);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildSetModemParaFrame(int mixerGain, int IFAmpGain, int signalThreshold)
     {
         String dataField = String.format("%02X", mixerGain) + String.format("%02X", IFAmpGain) + String.format("%04X", signalThreshold);
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_MODEM_PARA, dataField);
    	
     }
     
     
     
     //
     // 摘要:
     //     Build Set Module to Sleep Mode Command Packet
     //
     // @return 
     //     Set Module to Sleep Mode Command Packet
     public static String BuildSetModuleSleepFrame()
     {
    	 return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SLEEP_MODE);
     }
     
     
     
     //
     // 摘要:
     //     Build Set PA Power Packet
     //
     // @param  
     //   powerdBm:
     //     dBm * 100, eg. 7.5dBm = 750
     //
     // @return 
     //     Set PA Power Packet
     public static String BuildSetPaPowerFrame(short powerdBm)
     {
    	 String strPower =String.format("%04X", powerdBm); //String strPower = powerdBm.ToString("X4");
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_POWER, strPower);
     }
     
     
     
     //
     // 摘要:
     //     Build Set Query Parameter Packet
     //
     // @param  
     //   dr:
     //     DR(1 bit) DR=8(0)、DR=64/3(1)
     //
     //   m:
     //     M(2 bit) M=1(00)、M=2(01)、M=4(10)、M=8(11)
     //
     //   TRext:
     //     TRext(1 bit) no pilot tone(0)、use pilot tone(1)
     //
     //   sel:
     //     Sel(2 bit) ALL(00 01)、~SL(10)、SL(11)
     //
     //   session:
     //     Session(2 bit) S0(00)、S1(01)、S2(10)、S3(11)
     //
     //   target:
     //     Target(1 bit) A(0)、B(1)
     //
     //   q:
     //     Q(4 bit) 0-15
     //
     // @return 
     //     Set Query Parameter Packet
     public static String BuildSetQueryFrame(int dr, int m, int TRext, int sel, int session, int target, int q)
     {
         // msb contains DR, M, TRext, Sel, Session
         int msb = (dr << 7) | (m << 5) | (TRext << 4) | (sel << 2) | (session);
         // lsb contains Target, Q
         int lsb = (target << 7) | (q << 3);
         String dataField = String.format("%02X", msb) + String.format("%02X", lsb);
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_QUERY, dataField);
     }
     
     
     
     //
     // 摘要:
     //     Build Set Reader Environment Mode Command Packet
     //
     // @param  
     //   mode:
     //     Reader environment mode. 0x01 for Dense Reader mode, 0x00 for High Sensitivity
     //     mode
     //
     // @return 
     //     Set Reader Environment Mode Command Packet
     public static String BuildSetReaderEnvModeFrame(byte mode)
     {
        return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_READER_ENV_MODE, String.format("%02X", mode));

     }
     
     
     
     //
     // 摘要:
     //     Build set region Packet
     //
     // @param  
     //   region:
     //     Region Code
     //
     // @return 
     //     Set Region Packet
     public static String BuildSetRegionFrame(String region)
     {
         if (region == null || region.length() == 0)
         {
             return "";
         }
         String frame = BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_REGION, region);
         return frame;
     }
     
     
     
     //
     // 摘要:
     //     Build Set RF Channel Packet
     //
     // @param  
     //   ch:
     //     Channel Number
     //
     // @return 
     //     Set RF Channel Packet
     public static String BuildSetRfChannelFrame(String ch)
     {
         if (ch == null || ch.length() == 0)
         {
             return "";
         }
         String frame = BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_RF_CHANNEL, ch);
         return frame;
     }
     
     
     
     //
     // 摘要:
     //     Build Set Select Parameter Packet
     //
     // @param  
     //   target:
     //     Target(3 bit) S0(000)、S1(001)、S2(010)、S3(011)、SL(100)
     //
     //   action:
     //     Action(3 bit) Reference to ISO18000-6C
     //
     //   memBank:
     //     Memory bank(2 bit) RFU(00)、EPC(01)、TID(10)、USR(11)
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
     //     Truncate(1 bit) Disable(0)、Enable(1)
     //
     // @return 
     //     Set Select Parameter Packet
     public static String BuildSetSelectFrame(int target, int action, int memBank, int pointer, int len, int truncated, String mask)
     {
         /// <summary>
         /// Build Set Select Parameter Packet
         /// </summary>
         /// <param name="target">Target(3 bit)     S0(000)、S1(001)、S2(010)、S3(011)、SL(100)</param>
         /// <param name="action">Action(3 bit)    Reference to ISO18000-6C</param>
         /// <param name="memBank">Memory bank(2 bit)    RFU(00)、EPC(01)、TID(10)、USR(11)</param>
         /// <param name="pointer">Pointer(32 bit)     Start Address</param>
         /// <param name="len">Length(8 bit)  </param>
         /// <param name="mask">Mask(0-255bit)   Mask Data according to Length</param>
         /// <param name="truncated">Truncate(1 bit)   Disable(0)、Enable(1)</param>
         /// <returns>Set Select Parameter Packet</returns>
    	 
  	 
         String dataField = "";//String.Empty;
         // this contains target, action, memBank
         int combByte = (target << 5) | (action << 2) | memBank;
         dataField = String.format("%02X", combByte);
         dataField += String.format("%08X", pointer) + String.format("%02X", len);
         if (truncated == 0x80 || truncated == 0x01)
         {
             dataField += "80";  
         }
         else
         {
             dataField += "00";
         }
         //dataField += mask.Substring(0, (int)Math.Ceiling(len / 8.0));
         dataField += mask.trim();
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_SELECT_PARA, dataField);

     }/* end BuildSetSelectFrame */
     
     
     
     //
     // 摘要:
     //     Build Set Module Sleep Time Command Packet
     //
     // @param  
     //   time:
     //     Idle Time In Minutes.  If the module has NO operation after the time, it
     //     will go to sleep mode.
     //
     // @return 
     //     Set Module Sleep Time Command Packet
     public static String BuildSetSleepTimeFrame(byte time)
     {
    	 return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_SET_SLEEP_TIME, String.format("%02X", time));

     }
     
     
     
     //
     // 摘要:
     //     Build Stop Read Multi TAG ID Packet
     //
     // @return 
     //     Stop Read Multi TAG ID Packet
     public static String BuildStopReadFrame()
     {
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_STOP_MULTI);
     }
     
     
     
     //
     // 摘要:
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
     public static String BuildWriteDataFrame(String accessPwd, int memBank, int sa, int dl, String dt)
     {
         if (accessPwd.trim().length() != 8)
         {
             return "";
         }
         String dataField = accessPwd.trim();
         dataField += String.format("%02X", memBank) + String.format("%04X", sa) + String.format("%04X", dl) + dt.trim();
         return BuildFrame(RFIDConstCode.FRAME_TYPE_CMD, RFIDConstCode.CMD_WRITE_DATA, dataField);
         
     }/* end BuildWriteDataFrame */
     
    

     public class lock_payload_type
     {
         /// <summary>
         /// byte0 of lock payload, high 4 bits are reserved.
         /// </summary>
         public byte byte0;  // high 4 bits are reserved
         /// <summary>
         /// byte1 of lock payload. It is middle 8 bits.
         /// </summary>
         public byte byte1;
         /// <summary>
         /// byte2 of lock payload. It is the lowest 8 bits .
         /// </summary>
         public byte byte2;
         
         public lock_payload_type()
         {
         
         }
     }         
     
     /// <summary>
     /// Generate the lock payload.
     /// </summary>
     /// <param name="lockOpt">Value from 0 to 3 means 0:unlock, 1:lock, 2:param unlock, 3:perma lock</param>
     /// <param name="memSpace">Value from 0 to 4 means 0:Kill password, 1:Access password, 2:EPC memBank, 3:TID memBank, 4:User memBank</param>
     /// <returns>lock payload</returns>
     public  lock_payload_type genLockPayload(byte lockOpt, byte memSpace) //C# return static
     {
         lock_payload_type payload= new lock_payload_type();

         payload.byte0 = 0;
         payload.byte1 = 0;
         payload.byte2 = 0;

         /********
          * 			 Kill passwd , access passwd , ecp mem , TID mem , User mem
          * mask		|   0     1		 0	   1	   0   1	 0   1     0    1
          *     		| <--         byte0        --> <----             byte1
          * action 	    |	0     1		 0	   1	   0   1	 0   1     0    1
          *        		| --------->  <-----              byte2            ----->
          */

         switch (memSpace)
         {
         case 0 : //kill password
             if (lockOpt == 0) // unlock
             {
                 payload.byte0 |= 0x08;
                 payload.byte1 |= 0x00;
             }
             else if (lockOpt == 1) // lock
             {
                 payload.byte0 |= 0x08;
                 payload.byte1 |= 0x02;
             }
             else if (lockOpt == 2) //perma unlock
             {
                 payload.byte0 |= 0x0C;
                 payload.byte1 |= 0x01;
             }
             else if (lockOpt == 3) //perma lock
             {
                 payload.byte0 |= 0x0C;
                 payload.byte1 |= 0x03;
             }
             break;
         case 1 : //access password
             if (lockOpt == 0) // unlock
             {
                 payload.byte0 |= (0x08 >> 2);
                 payload.byte2 |= 0x00;
             }
             else if (lockOpt == 1) // lock
             {
                 payload.byte0 |= (0x08 >> 2);
                 payload.byte2 |= 0x80;
             }
             else if (lockOpt == 2) //perma unlock
             {
                 payload.byte0 |= (0x0C >> 2);
                 payload.byte2 |= 0x40;
             }
             else if (lockOpt == 3) //perma lock
             {
                 payload.byte0 |= (0x0C >> 2);
                 payload.byte2 |= 0xC0;
             }
             break;
         case 2 : // epc mem
             if (lockOpt == 0) // unlock
             {
                 payload.byte1 |= 0x80;
                 payload.byte2 |= 0x00;
             }
             else if (lockOpt == 1) // lock
             {
                 payload.byte1 |= 0x80;
                 payload.byte2 |= 0x20;
             }
             else if (lockOpt == 2) //perma unlock
             {
                 payload.byte1 |= 0xC0;
                 payload.byte2 |= 0x10;
             }
             else if (lockOpt == 3) //perma lock
             {
                 payload.byte1 |= 0xC0;
                 payload.byte2 |= 0x30;
             }
             break;
         case 3 : // TID mem
             if (lockOpt == 0) // unlock
             {
                 payload.byte1 |= (0x80 >> 2);
                payload.byte2 |= 0x00;
             }
            else if (lockOpt == 1) // lock
             {
                 payload.byte1 |= (0x80 >> 2);
                 payload.byte2 |= 0x08;
             }
             else if (lockOpt == 2) //perma unlock
             {
                 payload.byte1 |= (0xC0 >> 2);
                 payload.byte2 |= 0x04;
             }
             else if (lockOpt == 3) //perma lock
             {
                 payload.byte1 |= (0xC0 >> 2);
                 payload.byte2 |= 0x0C;
             }
             break;
         case 4 : // User mem
             if (lockOpt == 0) // unlock
             {
                 payload.byte1 |= 0x08;
                 payload.byte2 |= 0x00;
             }
             else if (lockOpt == 1) // lock
             {
                 payload.byte1 |= 0x08;
                 payload.byte2 |= 0x02;
             }
             else if (lockOpt == 2) //perma unlock
             {
                 payload.byte1 |= 0x0C;
                 payload.byte2 |= 0x01;
             }
             else if (lockOpt == 3) //perma lock
             {
                 payload.byte1 |= 0x0C;
                 payload.byte2 |= 0x03;
             }
             break;

         default :
             break;
         }

         return payload;
     }    
     
     
     
     
     
     
     
     
     
     
     
     
     
     //
     // 摘要:
     //     Calculate the checksum of data. It will add all hexadecimal number of data,
     //     and only uses the LSB.
     //
     // @param  
     //   data:
     //     data should be hexadecimal format
     //
     // @return 
     //     checksum of data, described in hexadecimal string
     public static String CalcCheckSum(String data)
     {
         if (data == null)
         {
             return "";
         }
         int checksum = 0;
         String dataNoSpace = data.trim();	//data.Replace(" ", ""); // remove all spaces
         String temp;
         try
         {
             for (int j = 0; j < dataNoSpace.length(); j += 2)
             {
            	temp = dataNoSpace.substring(j, j+2); 
             	checksum += Integer.valueOf(temp, 16);   //           checksum += Convert.ToInt32(dataNoSpace.Substring(j, 2), 16);
             }
         }
         catch (Exception e)
         {
        	 System.out.printf("do checksum error" + e.toString());
         }

         checksum = checksum % 256;
         return String.format("%02X", checksum);//checksum.ToString("X2");
     
     }/* end CalcCheckSum */
     
   /*  
     public static final String bytesToHexString(byte[] bArray, int num) {
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
     private static byte charToByte(char c) {  
         return (byte) "0123456789ABCDEF".indexOf(c);  
     }     
     */
 }/* end  class Commands */

