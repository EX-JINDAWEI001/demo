from WindPy import w
import time


def callback(indata: w.WindData):
    au = ""
    xau = ""
    for j in range(0,len(indata.Codes)):
        if(indata.Codes[j] == "AU9999.SGE"):
            au = str(indata.Data[0][j])
            print("au", au)
        if(indata.Codes[j] == "XAUCNY.IDC"):
            xau = str(indata.Data[0][j])
            print("xau", xau)
    data = "AU9999.SGE_" + au + ",XAUCNY.IDC_" + xau + "\n"
    pf.writelines(data)
    print("写入文件数据：", data)
    pf.flush()


start_ret = w.start()
if start_ret.ErrorCode != 0:
    print("Start failed")
    print("Error Code:", start_ret.ErrorCode)
    print("Error Message:", start_ret.Data[0])
else:
    # Open a file to write.
    pf = open('wsq_sub_file_data.log', 'w')
    # Subscribe market quotation data
    wsq_ret = w.wsq("AU9999.SGE,XAUCNY.IDC", "rt_last", func=callback)
    print("wind wsq_ret:", wsq_ret)
    if wsq_ret.ErrorCode != 0:
        print("Error Code:", wsq_ret.ErrorCode)
    while True:
        time.sleep(1)
