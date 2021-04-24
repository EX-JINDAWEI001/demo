from WindPy import w
import time
import mysql.connector

myDb = mysql.connector.connect(
    host="192.168.110.236",
    user="DemoCloudUser",
    passwd="123456@lcb",
    database="ClearingCloud"
)
myCursor = myDb.cursor()


def do_update(ret):
    print("wind请求结果：", ret)
    au = ""
    xau = ""
    for j in range(0, len(ret.Codes)):
        if (ret.Codes[j] == "AU9999.SGE"):
            au = str(ret.Data[0][j])
            print("au", au)
        if (ret.Codes[j] == "XAUCNY.IDC"):
            xau = str(ret.Data[0][j])
            print("xau", xau)
    data = "AU9999.SGE_" + au + "," + "XAUCNY.IDC_" + xau + "\n"
    pf.writelines(data)
    print("写入文件数据：", data)
    pf.flush()

    #    sql = "UPDATE ClearingCloud.sites SET AU9999 = %s, XAUCNY = %s, Stamp = current_timestamp() WHERE ID = 1"
    sql = "INSERT INTO ClearingCloud.sites(AU9999,XAUCNY,Stamp) VALUES(%s, %s, current_timestamp())"
    val = (au, xau)
    myCursor.execute(sql, val)
    myDb.commit()
    print(myCursor.rowcount, "条金价已更新.\n")


if __name__ == "__main__":
    start_ret = w.start()
    if start_ret.ErrorCode != 0:
        print("Start failed")
        print("Error Code:", start_ret.ErrorCode)
        print("Error Message:", start_ret.Data[0])
    else:
        pf = open('wsq_db_data.log', 'w')
        while True:
            try:
                time.sleep(5)
                wsq_ret = w.wsq("AU9999.SGE,XAUCNY.IDC", "rt_last")
                print("wind wsq_ret:", wsq_ret)
                if wsq_ret.ErrorCode == 0:
                    do_update(wsq_ret)
            except Exception as e:
                print("an error occur:", e)
