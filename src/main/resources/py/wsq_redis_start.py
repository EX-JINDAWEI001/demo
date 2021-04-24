import time
import datetime
import json
from WindPy import w
from rediscluster import StrictRedisCluster
import smtplib
from email.mime.text import MIMEText

# 万德产品编号
windCode = "AU9999.SGE,AU(T+D).SGE,XAUCNY.IDC,XPDCNY.IDC,XPTCNY.IDC,XAGCNY.IDC"
# 万德产品字段
windField = "rt_last,rt_open,rt_high,rt_low,rt_pct_chg,rt_chg,rt_pre_close,rt_date,rt_time"
# Redis万德数据key名
redisKey = "GOLDENCLOUD:KEYS:WIND_DATA"
# Redis万德数据格式(字典)
data = {'SGE': '', 'TD': '', 'LD': '', 'NOW': ''}
# 开发环境Redis集群信息
redis_nodes_dev = [{'host': '192.168.110.131', 'port': 6379}, {'host': '192.168.110.131', 'port': 6380},
                   {'host': '192.168.110.131', 'port': 6381}, {'host': '192.168.110.131', 'port': 6382},
                   {'host': '192.168.110.131', 'port': 6383}, {'host': '192.168.110.131', 'port': 6384}]
# 测试环境Redis集群信息
redis_nodes_sit = [{'host': '192.168.110.132', 'port': 6379}, {'host': '192.168.110.132', 'port': 6380},
                   {'host': '192.168.110.132', 'port': 6381}, {'host': '192.168.110.132', 'port': 6382},
                   {'host': '192.168.110.132', 'port': 6383}, {'host': '192.168.110.132', 'port': 6384}]
# 准生产环境Redis集群信息 TODO(修改以下配置)
redis_nodes_sim = [{'host': '192.168.110.132', 'port': 6379}, {'host': '192.168.110.132', 'port': 6380},
                   {'host': '192.168.110.132', 'port': 6381}, {'host': '192.168.110.132', 'port': 6382},
                   {'host': '192.168.110.132', 'port': 6383}, {'host': '192.168.110.132', 'port': 6384}]
# 生产环境Redis集群信息 TODO(修改以下配置)
redis_nodes_prd = [{'host': '192.168.110.132', 'port': 6379}, {'host': '192.168.110.132', 'port': 6380},
                   {'host': '192.168.110.132', 'port': 6381}, {'host': '192.168.110.132', 'port': 6382},
                   {'host': '192.168.110.132', 'port': 6383}, {'host': '192.168.110.132', 'port': 6384}]
# 是否存储数据到开发环境Redis: False-不存储, True-存储
isToDev = True
# 是否存储数据到测试环境Redis: False-不存储, True-存储
isToSit = True
# 是否存储数据到准生产环境Redis: False-不存储, True-存储
isToSim = False
# 是否存储数据到生产环境Redis: False-不存储, True-存储
isToPrd = False
# 163邮箱服务器地址
mail_host = 'smtp.qq.com'
# 163用户名
mail_user = '1218679097'
# 密码(部分邮箱为授权码)
mail_pass = 'guyvpurubptrjhah'
# 发送方邮箱地址
sender = '1218679097@qq.com'
# 接受方邮箱地址;
receivers = ['452649079@qq.com', '15971565197@163.com']
# 获取数据异常时触发邮件的时间阈值(单位秒)
timeThreshold = 60
# 邮件发送间隔(单位秒)
timeInterval = 60 * 60
# 是否打印控制台: False-不打印, True-打印
isConsole = False


# Redis集群连接
class RedisCluster(object):
    def __init__(self, node_list):
        self.conn_list = node_list

    def connect(self):
        try:
            return StrictRedisCluster(startup_nodes=self.conn_list, password='')
        except Exception as ex:
            if isConsole: print("connect to redis cluster failed:", ex)
            return False


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
    if isToDev: redis_conn_dev = RedisCluster(redis_nodes_dev).connect()
    if isToSit: redis_conn_sit = RedisCluster(redis_nodes_sit).connect()
    if isToSim: redis_conn_sim = RedisCluster(redis_nodes_sim).connect()
    if isToPrd: redis_conn_prd = RedisCluster(redis_nodes_prd).connect()
    sucTime = time.time()
    today = ''
    while True:
        try:
            time.sleep(1)
            now = datetime.datetime.now()
            nowStr = now.strftime("%Y-%m-%d %H:%M:%S")
            newDay = now.strftime('%Y%m%d')
            pf = open('wsq_redis_%s.log' % newDay, 'a')
            # 每天重启一次wind, 避免内存泄漏
            if today != newDay:
                today = newDay
                w.stop()
                time.sleep(10)
                w.start()
                pf.writelines("重启成功！")
                time.sleep(5)
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
                    if isToDev: redis_conn_dev.set(redisKey, json.dumps(data))
                    if isToSit: redis_conn_sit.set(redisKey, json.dumps(data))
                    if isToSim: redis_conn_sim.set(redisKey, json.dumps(data))
                    if isToPrd: redis_conn_prd.set(redisKey, json.dumps(data))
                    redisData = redis_conn_sit.get(redisKey)
                    if isConsole: print("redis data is:", redisData)
                    pf.writelines("redis data is:%s\n" % redisData)
                except Exception as re:
                    if isConsole: print("an exception redis error occur:", re)
                    pf.writelines("an exception redis error occur:%s\n" % str(re))
                    if isToDev: redis_conn_dev = RedisCluster(redis_nodes_dev).connect()
                    if isToSit: redis_conn_sit = RedisCluster(redis_nodes_sit).connect()
                    if isToSim: redis_conn_sim = RedisCluster(redis_nodes_sim).connect()
                    if isToPrd: redis_conn_prd = RedisCluster(redis_nodes_prd).connect()
        except Exception as e:
            if isConsole: print("an exception occur:", e)
            pf.writelines("an exception occur:%s\n" % str(e))
        finally:
            if isConsole: print(nowStr, "\n")
            pf.writelines(nowStr + "\n\n")
            pf.flush()
            pf.close()
