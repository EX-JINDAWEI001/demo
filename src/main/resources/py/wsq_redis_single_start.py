import time
import datetime
import redis
from WindPy import w


def handle(ret):
    tauLast="";tauOpen="";tauHigh="";tauLow="";tauPctChg="";tauChg="";tauPreClose="";tauDateTimeStr=""
    auLast="";auOpen="";auHigh="";auLow="";auPctChg="";auChg="";auPreClose="";auDateTimeStr=""
    xauLast="";xauOpen="";xauHigh="";xauLow="";xauPctChg="";xauChg="";xauPreClose="";xauDateTimeStr=""
    for j in range(0, len(ret.Codes)):
        if(ret.Codes[j] == "AU(T+D).SGE"):
            tauLast = str(ret.Data[0][j]) + "|"
            tauOpen = str(ret.Data[1][j]) + "|"
            tauHigh = str(ret.Data[2][j]) + "|"
            tauLow = str(ret.Data[3][j]) + "|"
            tauPctChg = str(ret.Data[4][j]) + "|"
            tauChg = str(ret.Data[5][j]) + "|"
            tauPreClose = str(ret.Data[6][j]) + "|"
            tauDate = str(ret.Data[7][j]).split(".")[0]
            tauTime = str(ret.Data[8][j]).split(".")[0].zfill(6)
            tauDateTime = datetime.datetime.strptime(tauDate+tauTime,'%Y%m%d%H%M%S')
            tauDateTimeStr = datetime.datetime.strftime(tauDateTime,'%Y-%m-%d %H:%M:%S')
        if(ret.Codes[j] == "AU9999.SGE"):
            auLast = str(ret.Data[0][j]) + "|"
            auOpen = str(ret.Data[1][j]) + "|"
            auHigh = str(ret.Data[2][j]) + "|"
            auLow = str(ret.Data[3][j]) + "|"
            auPctChg = str(ret.Data[4][j]) + "|"
            auChg = str(ret.Data[5][j]) + "|"
            auPreClose = str(ret.Data[6][j]) + "|"
            auDate = str(ret.Data[7][j]).split(".")[0]
            auTime = str(ret.Data[8][j]).split(".")[0].zfill(6)
            auDateTime = datetime.datetime.strptime(auDate+auTime,'%Y%m%d%H%M%S')
            auDateTimeStr = datetime.datetime.strftime(auDateTime,'%Y-%m-%d %H:%M:%S')
        if(ret.Codes[j] == "XAUCNY.IDC"):
            xauLast = str(ret.Data[0][j]) + "|"
            xauOpen = str(ret.Data[1][j]) + "|"
            xauHigh = str(ret.Data[2][j]) + "|"
            xauLow = str(ret.Data[3][j]) + "|"
            xauPctChg = str(ret.Data[4][j]) + "|"
            xauChg = str(ret.Data[5][j]) + "|"
            xauPreClose = str(ret.Data[6][j]) + "|"
            xauDate = str(ret.Data[7][j]).split(".")[0]
            xauTime = str(ret.Data[8][j]).split(".")[0].zfill(6)
            xauDateTime = datetime.datetime.strptime(xauDate+xauTime,'%Y%m%d%H%M%S')
            xauDateTimeStr = datetime.datetime.strftime(xauDateTime,'%Y-%m-%d %H:%M:%S')
    return "AU(T+D).SGE_" + tauLast + tauOpen + tauHigh + tauLow + tauPctChg + tauChg + tauPreClose + tauDateTimeStr \
           + ",AU9999.SGE_" + auLast + auOpen + auHigh + auLow + auPctChg + auChg + auPreClose + auDateTimeStr \
           + ",XAUCNY.IDC_" + xauLast + xauOpen + xauHigh + xauLow + xauPctChg + xauChg + xauPreClose + xauDateTimeStr


if __name__ == '__main__':
    w.start()
    redis_conn = redis.StrictRedis(host='114.67.201.227', port=6379, db=0, password="Socbb9988")
    while True:
        try:
            time.sleep(1)
            now = datetime.datetime.now()
            nowStr = now.strftime("%Y-%m-%d %H:%M:%S")
            pf = open('wsq_redis_single_data_%s.log' % now.strftime('%Y%m%d'), 'a')
            wsq_ret = w.wsq("AU(T+D).SGE,AU9999.SGE,XAUCNY.IDC", "rt_last,rt_open,rt_high,rt_low,rt_pct_chg,rt_chg,rt_pre_close,rt_date,rt_time")
            print("wind wsq_ret:", wsq_ret)
            pf.writelines("wind wsq_ret:%s\n" % str(wsq_ret))
            if wsq_ret.ErrorCode != 0:
                start_ret = w.start()
                print("wind start_ret:", start_ret)
                pf.writelines("wind start_ret:%s\n" % str(start_ret))
                if start_ret.ErrorCode != 0:
                    raise Exception("Restart Failed, ErrorCode is %s, ErrorMessage is %s" % (start_ret.ErrorCode, start_ret.Data[0]))
                elif start_ret.Data[0] != "OK!":
                    w.stop()
                else:
                    pass
                raise Exception("Invalid ErrorCode, Bad Wind WSQ Response!")
            else:
                data = handle(wsq_ret)
                if len(data) > 0:
                    try:
                        redis_conn.set("CLEARINGCLOUD:AU_PRICE", data)
                        redisData = redis_conn.get("CLEARINGCLOUD:AU_PRICE");
                        print("redis data is:", redisData)
                        pf.writelines("redis data is:%s\n" % redisData)
                    except Exception as re:
                        print("an exception redis error occur:", re)
                        pf.writelines("an exception redis error occur:%s\n" % str(re))
                        redis_conn = redis.StrictRedis(host='114.67.201.227', port=6379, db=0, password="Socbb9988")
        except Exception as e:
            print("an exception occur:", e)
            pf.writelines("an exception occur:%s\n" % str(e))
        finally:
            print(nowStr, "\n")
            pf.writelines(nowStr + "\n\n")
            pf.flush()
            pf.close()
