import smtplib
import datetime
import time
from email.mime.text import MIMEText
#设置服务器所需信息
#QQ邮箱服务器地址
mail_host = 'smtp.qq.com'
#用户名
mail_user = '1218679097'
#密码(部分邮箱为授权码)
mail_pass = 'guyvpurubptrjhah'
#发送方邮箱地址
sender = '1218679097@qq.com'
#接受方邮箱地址;
receivers = ['15971565197@163.com','1159705653@qq.com']

#设置email信息
#邮件内容设置
message = MIMEText('万德数据获取异常, 请傻屌及时排查问题!','plain','utf-8')
#邮件主题
message['Subject'] = '傻屌, 数据异常啦！'
#发送方信息
message['From'] = sender
#接受方信息
message['To'] = receivers[0]

#登录并发送邮件
if __name__ == '__main__':
    try:
        smtpObj = smtplib.SMTP()
        #连接到服务器
        smtpObj.connect(mail_host, 25)
        #登录到服务器
        smtpObj.login(mail_user, mail_pass)
        #发送
        smtpObj.sendmail(sender, receivers, message.as_string())
        #退出
        smtpObj.quit()
        print('success')
    except smtplib.SMTPException as e:
        print('error',e)