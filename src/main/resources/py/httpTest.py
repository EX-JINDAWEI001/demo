import requests
import json
import hmac
from hashlib import sha256
import datetime
import asyncio
import time
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
devUrl = "http://192.168.1.215:46510/setting/setting_market/Sync"
# 测试环境URL
sitUrl = "http://192.168.1.215:46510/setting/setting_market/Sync"
# 准生产环境URL
simUrl = "http://192.168.1.215:46510/setting/setting_market/Sync"
# 生产环境URL
prdUrl = "http://192.168.1.215:46510/setting/setting_market/Sync"


async def task(url, body, headers):
    return requests.post(url, data=body, headers=headers)


def do_task(url, body, headers):
    return requests.post(url, data=body, headers=headers)


def do_request_task(au_data):
    key = "613cb11b0fd7882e15c9f108ebfec17b0895c5cfd0f54299284a42d1e44b4707"
    time_stamp = au_data['NOW']
    au_data = json.dumps(au_data)
    body = ("AuData%sTime-Stamp%s" % (au_data, time_stamp))
    signature = hmac.new(key.encode('utf-8'), body.encode('utf-8'), digestmod=sha256).hexdigest()
    headers = {'Content-Type': 'application/json', 'Time-Stamp': time_stamp, 'Signature': signature}
    body = json.dumps({"AuData": au_data})
    tasks = []
    if isToDev: tasks.append(task(devUrl, body, headers))
    if isToSit: tasks.append(task(sitUrl, body, headers))
    if isToSim: tasks.append(task(simUrl, body, headers))
    if isToPrd: tasks.append(task(prdUrl, body, headers))
    start = time.time()
    print("do_request_task开始：", start)
    rsp_dev, rsp_sit, rsp_sim, rsp_prd = asyncio.get_event_loop().run_until_complete(asyncio.gather(*tasks))
    print("do_request_task耗时：", time.time() - start)
    # 处理响应结果
    rsp_dev = (rsp_dev.json() if rsp_dev.status_code == 200 else str(rsp_dev)) if isToDev else 'isNotToDev'
    rsp_sit = (rsp_sit.json() if rsp_sit.status_code == 200 else str(rsp_sit)) if isToSit else 'isNotToSit'
    rsp_sim = (rsp_sim.json() if rsp_sim.status_code == 200 else str(rsp_sim)) if isToSim else 'isNotToSim'
    rsp_prd = (rsp_prd.json() if rsp_prd.status_code == 200 else str(rsp_prd)) if isToPrd else 'isNotToPrd'
    return rsp_dev, rsp_sit, rsp_sim, rsp_prd


def do_request_async(au_data):
    key = "613cb11b0fd7882e15c9f108ebfec17b0895c5cfd0f54299284a42d1e44b4707"
    time_stamp = au_data['NOW']
    au_data = json.dumps(au_data)
    body = ("AuData%sTime-Stamp%s" % (au_data, time_stamp))
    signature = hmac.new(key.encode('utf-8'), body.encode('utf-8'), digestmod=sha256).hexdigest()
    headers = {'Content-Type': 'application/json', 'Time-Stamp': time_stamp, 'Signature': signature}
    body = json.dumps({"AuData": au_data})
    start = time.time()
    print("do_request_async开始：", start)
    # 同时发起请求
    if isToDev: rsp_dev = executor.submit(do_task, devUrl, body, headers)
    if isToSit: rsp_sit = executor.submit(do_task, sitUrl, body, headers)
    if isToSim: rsp_sim = executor.submit(do_task, simUrl, body, headers)
    if isToPrd: rsp_prd = executor.submit(do_task, prdUrl, body, headers)
    # 获取响应结果
    if isToPrd: rsp_prd = rsp_prd.result(2)
    if isToSim: rsp_sim = rsp_sim.result(1)
    if isToSit: rsp_sit = rsp_sit.result(1)
    if isToDev: rsp_dev = rsp_dev.result(1)
    print("do_request_async耗时：", time.time() - start)
    # 处理响应结果
    rsp_dev = (rsp_dev.json() if rsp_dev.status_code == 200 else str(rsp_dev)) if isToDev else 'isNotToDev'
    rsp_sit = (rsp_sit.json() if rsp_sit.status_code == 200 else str(rsp_sit)) if isToSit else 'isNotToSit'
    rsp_sim = (rsp_sim.json() if rsp_sim.status_code == 200 else str(rsp_sim)) if isToSim else 'isNotToSim'
    rsp_prd = (rsp_prd.json() if rsp_prd.status_code == 200 else str(rsp_prd)) if isToPrd else 'isNotToPrd'
    return rsp_dev, rsp_sit, rsp_sim, rsp_prd


def do_request_sync(au_data):
    key = "613cb11b0fd7882e15c9f108ebfec17b0895c5cfd0f54299284a42d1e44b4707"
    time_stamp = au_data['NOW']
    au_data = json.dumps(au_data)
    body = ("AuData%sTime-Stamp%s" % (au_data, time_stamp))
    signature = hmac.new(key.encode('utf-8'), body.encode('utf-8'), digestmod=sha256).hexdigest()
    headers = {'Content-Type': 'application/json', 'Time-Stamp': time_stamp, 'Signature': signature}
    body = json.dumps({"AuData": au_data})
    rsp_dev = ''; rsp_sit = ''; rsp_sim = ''; rsp_prd = ''
    start = time.time()
    print("do_request_sync开始：", start)
    # 串行发起请求
    if isToDev: rsp_dev = requests.post(devUrl, data=body, headers=headers)
    if isToSit: rsp_sit = requests.post(sitUrl, data=body, headers=headers)
    if isToSim: rsp_sim = requests.post(simUrl, data=body, headers=headers)
    if isToPrd: rsp_prd = requests.post(prdUrl, data=body, headers=headers)
    print("do_request_sync耗时：", time.time() - start)
    # 处理响应结果
    rsp_dev = (rsp_dev.json() if rsp_dev.status_code == 200 else str(rsp_dev)) if isToDev else 'isNotToDev'
    rsp_sit = (rsp_sit.json() if rsp_sit.status_code == 200 else str(rsp_sit)) if isToSit else 'isNotToSit'
    rsp_sim = (rsp_sim.json() if rsp_sim.status_code == 200 else str(rsp_sim)) if isToSim else 'isNotToSim'
    rsp_prd = (rsp_prd.json() if rsp_prd.status_code == 200 else str(rsp_prd)) if isToPrd else 'isNotToPrd'
    return rsp_dev, rsp_sit, rsp_sim, rsp_prd


if __name__ == '__main__':
    rsp_dev, rsp_sit, rsp_sim, rsp_prd = do_request_sync({'SGE': '', 'TD': '', 'LD': '', 'NOW': datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")})
    print(rsp_dev, rsp_sit, rsp_sim, rsp_prd)
    rsp_dev, rsp_sit, rsp_sim, rsp_prd = do_request_task({'SGE': '', 'TD': '', 'LD': '', 'NOW': datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")})
    print(rsp_dev, rsp_sit, rsp_sim, rsp_prd)
    rsp_dev, rsp_sit, rsp_sim, rsp_prd = do_request_async({'SGE': '', 'TD': '', 'LD': '', 'NOW': datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")})
    print(rsp_dev, rsp_sit, rsp_sim, rsp_prd)