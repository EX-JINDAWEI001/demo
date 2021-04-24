import time
import datetime
import json
from WindPy import w
import smtplib
from email.mime.text import MIMEText
import requests
import hmac
from hashlib import sha256
from concurrent.futures import ThreadPoolExecutor

# 线程池
executor = ThreadPoolExecutor(max_workers=4)
# 是否存储数据到开发环境: False-不存储, True-存储
isToDev = True
# 是否存储数据到测试环境: False-不存储, True-存储
isToSit = True
# 是否存储数据到准生产环境: False-不存储, True-存储
isToSim = True
# 是否存储数据到生产环境: False-不存储, True-存储
isToPrd = True
# 开发环境URL
devUrl = "http://golden.chtwebapi.lingcb1.com/setting/setting_market/Sync"
# 测试环境URL
sitUrl = "http://golden.chtwebapi.lingcb.net/setting/setting_market/Sync"
# 准生产环境URL
simUrl = "http://golden.chtwebapi.lingcb.cn/setting/setting_market/Sync"
# 生产环境URL
prdUrl = "http://golden.chtwebapi.lingcb.com/setting/setting_market/Sync"
# 万德产品编号
windCode = "AU9999.SGE,AU(T+D).SGE,XAUCNY.IDC,XPDCNY.IDC,XPTCNY.IDC,XAGCNY.IDC"
# 万德产品字段
windField = "rt_last,rt_open,rt_high,rt_low,rt_pct_chg,rt_chg,rt_pre_close,rt_date,rt_time"
# 万德数据格式(字典)
data = {'SGE': '', 'TD': '', 'LD': '', 'NOW': ''}
# QQ邮箱服务器地址
mail_host = 'smtp.qq.com'
# QQ邮箱用户名
mail_user = '1218679097'
# 密码(部分邮箱为授权码)
mail_pass = 'guyvpurubptrjhah'
# 发送方邮箱地址
sender = '1218679097@qq.com'
# 接受方邮箱地址;
receivers = ['452649079@qq.com', '15971565197@163.com']
# 获取数据异常时触发邮件的时间阈值(单位秒)
timeThreshold = 10
# 邮件发送间隔(单位秒)
timeInterval = 60 * 60
# 是否打印控制台: False-不打印, True-打印
isConsole = False


# 发送邮件
def send_email(content):
    # 邮件内容设置
    message = MIMEText('万德数据获取异常, 请及时排查问题:%s' % content, 'plain', 'utf-8')
    # 邮件主题
    message['Subject'] = '万德数据获取异常'
    # 发送方信息
    message['From'] = sender
    # 接受方信息
    message['To'] = receivers[0]
    # 获取邮件发送管理器
    smtp_obj = smtplib.SMTP()
    # 连接到服务器
    smtp_obj.connect(mail_host, 25)
    # 登录到服务器
    smtp_obj.login(mail_user, mail_pass)
    # 发送
    smtp_obj.sendmail(sender, receivers, message.as_string())
    # 退出
    smtp_obj.quit()


def do_task(url, body, headers):
    return requests.post(url, data=body, headers=headers)


# http请求
def do_request(au_data):
    key = "613cb11b0fd7882e15c9f108ebfec17b0895c5cfd0f54299284a42d1e44b4707"
    time_stamp = au_data['NOW']
    au_data = json.dumps(au_data)
    body = ("AuData%sTime-Stamp%s" % (au_data, time_stamp))
    signature = hmac.new(key.encode('utf-8'), body.encode('utf-8'), digestmod=sha256).hexdigest()
    headers = {'Content-Type': 'application/json', 'Time-Stamp': time_stamp, 'Signature': signature}
    body = json.dumps({"AuData": au_data})
    # 同时发起请求
    if isToDev: rsp_dev = executor.submit(do_task, devUrl, body, headers)
    if isToSit: rsp_sit = executor.submit(do_task, sitUrl, body, headers)
    if isToSim: rsp_sim = executor.submit(do_task, simUrl, body, headers)
    if isToPrd: rsp_prd = executor.submit(do_task, prdUrl, body, headers)
    # 获取响应结果
    if isToPrd: rsp_prd = rsp_prd.result(1)
    if isToSim: rsp_sim = rsp_sim.result(1)
    if isToSit: rsp_sit = rsp_sit.result(1)
    if isToDev: rsp_dev = rsp_dev.result(1)
    # 处理响应结果
    rsp_dev = (rsp_dev.json() if rsp_dev.status_code == 200 else str(rsp_dev)) if isToDev else 'isNotToDev'
    rsp_sit = (rsp_sit.json() if rsp_sit.status_code == 200 else str(rsp_sit)) if isToSit else 'isNotToSit'
    rsp_sim = (rsp_sim.json() if rsp_sim.status_code == 200 else str(rsp_sim)) if isToSim else 'isNotToSim'
    rsp_prd = (rsp_prd.json() if rsp_prd.status_code == 200 else str(rsp_prd)) if isToPrd else 'isNotToPrd'
    return rsp_dev, rsp_sit, rsp_sim, rsp_prd


# 处理万德数据
def handle(ret):
    category_sge = []; category_td = []; category_ld = []
    for j in range(0, len(ret.Codes)):
        item = {
            'last': str(ret.Data[0][j]),
            'open': str(ret.Data[1][j]),
            'high': str(ret.Data[2][j]),
            'low': str(ret.Data[3][j]),
            'pctChg': str(ret.Data[4][j]),
            'chg': str(ret.Data[5][j]),
            'preClose': str(ret.Data[6][j]),
            'dateTime': str(ret.Data[7][j]).split(".")[0] + str(ret.Data[8][j]).split(".")[0].zfill(6)
        }
        if (ret.Codes[j] == "AU9999.SGE"):
            item_small = item
            item_small.update({'materialType': 'AU'})
            category_sge.append(item_small)
        elif (ret.Codes[j] == "AU(T+D).SGE"):
            item_small = item
            item_small.update({'materialType': 'AU'})
            category_td.append(item_small)
        elif (ret.Codes[j] == "XAUCNY.IDC"):
            item_small = item
            item_small.update({'materialType': 'AU'})
            category_ld.append(item_small)
        elif (ret.Codes[j] == "XPDCNY.IDC"):
            item_small = item
            item_small.update({'materialType': 'PD'})
            category_ld.append(item_small)
        elif (ret.Codes[j] == "XPTCNY.IDC"):
            item_small = item
            item_small.update({'materialType': 'PT'})
            category_ld.append(item_small)
        elif (ret.Codes[j] == "XAGCNY.IDC"):
            item_small = item
            item_small.update({'materialType': 'AG'})
            category_ld.append(item_small)
    data['SGE'] = category_sge; data['TD'] = category_td; data['LD'] = category_ld


# 入口
if __name__ == '__main__':
    sucTime = time.time()
    today = ''
    while True:
        try:
            time.sleep(1)
            now = datetime.datetime.now()
            nowStr = now.strftime("%Y-%m-%d %H:%M:%S")
            newDay = now.strftime('%Y%m%d')
            pf = open('wsq_http_%s.log' % newDay, 'a')
            # 每天重启一次wind, 避免内存泄漏
            if today != newDay:
                today = newDay
                w.stop()
                w.close()
                w.start()
            wsq_ret = w.wsq(windCode, windField)
            if isConsole: print("wind wsq_ret:", wsq_ret)
            pf.writelines("wind wsq_ret:%s\n" % str(wsq_ret))
            if wsq_ret.ErrorCode != 0:
                if (time.time() - sucTime) > timeThreshold:
                    send_email(wsq_ret)
                    sucTime = time.time() + timeInterval
                start_ret = w.start()
                if isConsole: print("wind start_ret:", start_ret)
                pf.writelines("wind start_ret:%s\n" % str(start_ret))
                if start_ret.ErrorCode != 0:
                    # 启动失败, 等下个循环继续重试
                    raise Exception("Restart Failed, ErrorCode is %s, ErrorMessage is %s" % (start_ret.ErrorCode, start_ret.Data[0]))
                elif start_ret.Data[0] != "OK!":
                    # 启动成功, 但启动顺序异常, 需先停掉wind, 等下个循环继续重试
                    w.stop()
                else:
                    # 启动成功
                    pass
                raise Exception("Invalid ErrorCode, Bad Wind WSQ Response!")
            else:
                sucTime = time.time()
                handle(wsq_ret)
                data['NOW'] = nowStr
                try:
                    rsp_dev, rsp_sit, rsp_sim, rsp_prd = do_request(data)
                    if isConsole:
                        if isToDev: print("http_dev_result_is:", rsp_dev)
                        if isToSit: print("http_sit_result_is:", rsp_sit)
                        if isToSim: print("http_sim_result_is:", rsp_sim)
                        if isToPrd: print("http_prd_result_is:", rsp_prd)
                    if isToDev: pf.writelines("http_dev_result_is:%s\n" % rsp_dev)
                    if isToSit: pf.writelines("http_sit_result_is:%s\n" % rsp_sit)
                    if isToSim: pf.writelines("http_sim_result_is:%s\n" % rsp_sim)
                    if isToPrd: pf.writelines("http_prd_result_is:%s\n" % rsp_prd)
                except Exception as re:
                    if isConsole: print("an exception http error occur:", re)
                    pf.writelines("an exception http error occur:%s\n" % str(re))
        except Exception as e:
            if isConsole: print("an exception occur:", e)
            pf.writelines("an exception occur:%s\n" % str(e))
        finally:
            if isConsole: print(nowStr, "\n")
            pf.writelines(nowStr + "\n\n")
            pf.flush()
            pf.close()
