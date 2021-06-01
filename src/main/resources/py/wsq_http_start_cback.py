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
isToSim = False
# 是否存储数据到生产环境: False-不存储, True-存储
isToPrd = False
# 开发环境URL
devUrl = "http://goldendev.chtwebapi.lingcb.net/setting/setting_market/Sync"
# 测试环境URL
sitUrl = "http://golden.chtwebapi.lingcb.net/setting/setting_market/Sync"
# 准生产环境URL
simUrl = "http://golden.chtwebapi.lingcb.cn/setting/setting_market/Sync"
# 生产环境URL
prdUrl = "http://golden.chtwebapi.lingcb.com/setting/setting_market/Sync"
# 万德产品编号
windCode = "AU9999.SGE,AU(T+D).SGE,XAUCNY.IDC,XPDCNY.IDC,XPTCNY.IDC,XAGCNY.IDC"
# 万德产品字段
windField = "rt_last,rt_open,rt_high,rt_low,rt_pct_chg,rt_chg,rt_pre_close"
# 万德数据格式(字典)
data = {'SGE': '', 'TD': '', 'LD': '', 'NOW': ''}
# QQ邮箱服务器地址
mail_host = 'smtp.qq.com'
# QQ邮箱用户名
mail_user = 'amywei103'
# 密码(部分邮箱为授权码)
mail_pass = 'nsaybunklzsmbhad'
# 发送方邮箱地址
sender = 'amywei103@qq.com'
# 接受方邮箱地址
receivers = ['amy@lingcb.com']
# 抄送方邮箱地址
ccreceivers = ['1218679097@qq.com', 'cbb@lingcb.com', 'tony@lingcb.com']
# 获取数据异常时触发邮件的时间阈值(单位秒)
timeThreshold = 60 * 5
# 邮件发送间隔(单位秒)
timeInterval = 60 * 120
# 是否打印控制台: False-不打印, True-打印
isConsole = False


# 发送邮件通知
def send_email(msg, subject):
    # 邮件内容设置
    message = MIMEText(msg, 'plain', 'utf-8')
    # 邮件主题
    message['Subject'] = subject
    # 发送方信息
    message['From'] = sender
    # 接受方信息
    message['To'] = receivers[0]
    # 抄送方信息
    message['Cc'] = ccreceivers[0]
    # 获取邮件发送管理器
    smtp_obj = smtplib.SMTP()
    # 连接到服务器
    smtp_obj.connect(mail_host, 25)
    # 登录到服务器
    smtp_obj.login(mail_user, mail_pass)
    receivers.extend(ccreceivers)
    # 发送
    smtp_obj.sendmail(sender, receivers, message.as_string())
    # 退出
    smtp_obj.quit()


# 发送HTTP请求
def do_task(url, body, headers):
    return requests.post(url, data=body, headers=headers)


# 处理http请求
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


# 处理回调数据
def do_match(fields, codes, datas):
    # 定义相关
    category_sge = []; category_td = []; category_ld = []
    # 遍历数据
    for j in range(0, len(codes)):
        item = {}
        for i in range(0, len(fields)):
            # 处理回调Fields
            if (fields[i] == "RT_LAST"): item['last'] = datas[i][j]
            elif (fields[i] == "RT_OPEN"): item['open'] = datas[i][j]
            elif (fields[i] == "RT_HIGH"): item['high'] = datas[i][j]
            elif (fields[i] == "RT_LOW"): item['low'] = datas[i][j]
            elif (fields[i] == "RT_PCT_CHG"): item['pctChg'] = datas[i][j]
            elif (fields[i] == "RT_CHG"): item['chg'] = datas[i][j]
            elif (fields[i] == "RT_PRE_CLOSE"): item['preClose'] = datas[i][j]
        # 处理回调Codes
        if (codes[j] == "AU9999.SGE"):
            item.update({'materialType': 'AU'})
            category_sge.append(item)
        elif (codes[j] == "AU(T+D).SGE"):
            item.update({'materialType': 'AU'})
            category_td.append(item)
        elif (codes[j] == "XAUCNY.IDC"):
            item.update({'materialType': 'AU'})
            category_ld.append(item)
        elif (codes[j] == "XPDCNY.IDC"):
            item.update({'materialType': 'PD'})
            category_ld.append(item)
        elif (codes[j] == "XPTCNY.IDC"):
            item.update({'materialType': 'PT'})
            category_ld.append(item)
        elif (codes[j] == "XAGCNY.IDC"):
            item.update({'materialType': 'AG'})
            category_ld.append(item)
    # 映射响应
    data['SGE'] = category_sge; data['TD'] = category_td; data['LD'] = category_ld


# 处理万得回调
def do_cback(wsq_ret):
    # 定义相关
    global lastCbackTime
    # 间隔不足1s直接拒绝
    if(time.time() - lastCbackTime < 1): return
    try:
        lastCbackTime = time.time()
        now = datetime.datetime.now()
        nowStr = now.strftime("%Y-%m-%d %H:%M:%S")
        pf = open('wsq_http_%s.log' % now.strftime('%Y%m%d'), 'a')
        if isConsole: print("wind wsq_ret:", wsq_ret)
        pf.writelines("wind wsq_ret:%s\n" % str(wsq_ret))
        # 成功回调
        if wsq_ret.ErrorCode == 0:
            # 处理回调数据
            do_match(wsq_ret.Fields, wsq_ret.Codes, wsq_ret.Data)
            data['NOW'] = nowStr
            if isConsole: print("wind redis data:", data)
            pf.writelines("wind redis data:%s\n" % str(data))
            # 发送HTTP请求
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
        if isConsole: print("an exception do_cback:", e)
        pf.writelines("an exception do_cback:%s\n" % str(e))
    finally:
        if isConsole: print(nowStr, "\n")
        pf.writelines(nowStr + "\n\n")
        pf.flush()
        pf.close()


# 启动客户端并触发wsq
def do_wsq():
    # 定义相关
    global sucTime, isNeedSucEmail, isNeedRetry
    try:
        now = datetime.datetime.now()
        nowStr = now.strftime("%Y-%m-%d %H:%M:%S")
        pf = open('wsq_http_%s.log' % now.strftime('%Y%m%d'), 'a')
        if w.isconnected():
            w.stop()
            time.sleep(3)
        start_ret = w.start()
        if isConsole: print("wind start_ret:", start_ret)
        pf.writelines("wind start_ret:%s\n" % str(start_ret))
        # 启动成功
        if start_ret.ErrorCode == 0:
            # 远程调用(万得实时金价)
            w.cancelRequest(0)
            wsq_ret = w.wsq(windCode, windField, func=do_cback)
            if isConsole: print("wind wsq_ret:", wsq_ret)
            pf.writelines("wind wsq_ret:%s\n" % str(wsq_ret))
            # 触发wsq成功
            if wsq_ret.ErrorCode == 0:
                isNeedRetry = False
                sucTime = time.time()
                # 校验是否需要发送成功邮件
                if isNeedSucEmail:
                    send_email("异常已恢复, 请勿紧张!", "万德数据获取成功")
                    isNeedSucEmail = False
            # 触发wsq失败
            else:
                isNeedRetry = True
                # 校验是否需要发送失败邮件
                if (time.time() - sucTime) > timeThreshold:
                    send_email('万德数据获取异常, 请及时排查问题: %s' % wsq_ret, "万德数据获取异常")
                    sucTime = time.time() + timeInterval
                    isNeedSucEmail = True
        # 启动失败
        else:
            isNeedRetry = True
    except Exception as re:
        isNeedRetry = True
        if isConsole: print("an exception wind do_wsq:", re)
        pf.writelines("an exception wind do_wsq:%s\n" % str(re))
    finally:
        if isConsole: print(nowStr, "\n")
        pf.writelines(nowStr + "\n\n")
        pf.flush()
        pf.close()


# 入口
if __name__ == '__main__':
    # 最新成功触发wsq的时间
    sucTime = time.time()
    # 最新回调时间
    lastCbackTime = time.time()
    # 是否需要发送成功邮件
    isNeedSucEmail = False
    # 是否需要重新触发wsq
    isNeedRetry = True
    while True:
        time.sleep(1)
        if isNeedRetry: do_wsq()
