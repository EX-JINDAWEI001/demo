import time
import datetime
import os

# 清理日志文件(保留7天)
if __name__ == '__main__':
    # 日志文件目录
    rootDir = r'C:\Windows\System32\GroupPolicy\Machine\Scripts\Startup'
    while True:
        try:
            time.sleep(60 * 60 * 12)
            pf = open('wsq_log_clean.txt', 'a')
            now = datetime.datetime.now()
            nowStr = now.strftime("%Y-%m-%d %H:%M:%S")
            notDelete = ['wsq_http_%s.log' % now.strftime('%Y%m%d')]
            i = 0
            while i < 7:
                i += 1
                newDate = now + datetime.timedelta(days=-i)
                notDelete.append('wsq_http_%s.log' % newDate.strftime('%Y%m%d'))
            pf.write("notDelete is:%s\n" % str(notDelete))
            for filename in os.listdir(rootDir):
                pf.write("filename is:%s\n" % filename)
                if filename.split(".")[1] == "log" and filename not in notDelete:
                    rfn = os.path.join(rootDir, filename)
                    os.remove(rfn)
                    pf.write("delete file:%s\n" % rfn)
        except Exception as e:
            pf.write("an exception occur:%s\n" % str(e))
        finally:
            pf.write(nowStr + "\n\n")
            pf.flush()
            pf.close()
