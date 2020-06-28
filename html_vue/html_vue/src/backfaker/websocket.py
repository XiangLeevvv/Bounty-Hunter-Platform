import datetime
import json
from flask_socketio import Namespace, emit, SocketIO, disconnect
from flask import Flask, render_template,request
import pymysql

app = Flask(__name__)
socketio = SocketIO(app,cors_allowed_origins="*")

class MyCustomNamespace(Namespace):
    def __init__(self,namespace):
        super(MyCustomNamespace, self).__init__(namespace)
        self.pool = {}
        self.nametable = {}

    def on_connect(self):           
        sid = request.sid
        print('建立连接成功！-{}'.format(sid))

    def on_disconnect(self):
        sid = request.sid
        print('客户端断开连接！-{}'.format(sid))
        emit('getdown', self.nametable[sid], broadcast=True)
        # del self.pool[self.user_id]

    def close_room(self, room):
        socketio.close_room(room)
        print('{}-断开连接'.format(room))
        # del self.pool[self.user_id]

    def on_my_id(self, data):       # 登录成功 
        sid = request.sid
        print('myidddddd:', data)
        user = User(sid, data)
        self.pool[user.id] = user
        self.nametable[sid] = data
        emit('getup', data, broadcast=True)
        # self.pool[self.user_id] = (self.sid, self)

    def on_checkup(self, data):
        sid = request.sid
        if data in self.pool:
            emit('getup', '1', room = sid)
        else:
            emit('getdown', '1', room = sid)

    def on_my_avatar(self, data):
        sid = request.sid
        print(data)
        setavatar(self.nametable[sid], data)

    def on_my_aim(self, data):      # 打开了对某人的对话框

        ids = data.split()          # 此处修改一下前端，不需要发送者的id了
        self.pool[ids[0]].aim = ids[1]
        his = findhistory(ids[0], ids[1])
        res = []
        for i in his:
            res.append(list(map(str, i)))
        emit('server_history', res, room=self.pool[ids[0]].sid)
        emit('server_avatar', getavatar(ids[0]), room=self.pool[ids[0]].sid)

    def on_message(self, data):     # 发送消息
        sid = request.sid
        sendid = self.nametable[sid]
        receiveid = self.pool[sendid].aim
        if receiveid in self.pool:
            if self.pool[receiveid].aim == sendid:
                emit('server_response', data, room=self.pool[receiveid].sid)
            else:
                self.sendnotice(sendid, receiveid, '发来了一条消息')
        else:
            pass            # 不在线的情况 存入notice表
        addmessage(sendid, data, sendid, receiveid)

    def on_gettask(self, data):     # 发送消息
        sid = request.sid
        sendid = self.nametable[sid]
        datadetails = data.split()
        receiveid = datadetails[1]
        self.sendnotice('系统', receiveid, sendid + '接取了您的需求' + datadetails[0])
    
    def on_finishtask(self, data):     # 发送消息
        sid = request.sid
        sendid = self.nametable[sid]
        datadetails = data.split()
        receiveid = datadetails[1]
        self.sendnotice('系统', receiveid, '您接取的来自' + sendid +'的任务' + datadetails[0] + '已被确认完成')
        
    def sendnotice(self, sendid, receiveid, data):
        t = datetime.datetime.now().strftime('%H:%M:%S')
        params = {'time': t, 'name': sendid, 'content': data}
        emit('server_notice', params, room = self.pool[receiveid].sid)

class User:
    def __init__(self, sid='', idd='', aim=''):
        self.sid = sid
        self.id = idd
        self.aim = aim

socketio.on_namespace(MyCustomNamespace('/test'))

def connect_mysql():#链接mysql
    conn = pymysql.connect(host="cdb-mw8hntaa.bj.tencentcdb.com", port=10027, user="root", password="lx123456", database="SE-Platform")
    cursor = conn.cursor()
    return conn, cursor

def addmessage(idd, content, sender, receiver):
    try:
        conn,cursor = connect_mysql()           # 连接到mysql
        t = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        sql = 'INSERT INTO ChatMessage(content, `time`, sender, receiver) VALUES (\"%s\", \"%s\", \"%s\", \"%s\")'%(content, t, sender, receiver)
        print(sql)
        cursor.execute(sql) 
        conn.commit()
        cursor.close()
        conn.close()
    except Exception as e:
        print(e)
        return 'error'

def findhistory(user_id, aim):
    try:
        conn,cursor = connect_mysql()           # 连接到mysql
        sql = 'SELECT content, `time`, sender FROM ChatMessage WHERE (sender = \"%s\" AND receiver = \"%s\") OR (sender = \"%s\" AND receiver = \"%s\") '%(user_id, aim, aim, user_id)
        cursor.execute(sql) 
        result = cursor.fetchall()
        #print(result)
        conn.commit()
        cursor.close()
        conn.close()
        return list(result)
    except Exception as e:
        print(e)
        return 'error'

def setavatar(user_id, data):
    try:
        conn,cursor = connect_mysql()           # 连接到mysql
        sql = 'UPDATE User SET user_avatar=\"%s\" WHERE `user_name`=\"%s\"'%(data, user_id)
        cursor.execute(sql) 
        #print(result)
        conn.commit()
        cursor.close()
        conn.close()
        return 'ok'
    except Exception as e:
        print(e)
        return 'error'

def getavatar(user_id):
    try:
        conn,cursor = connect_mysql()           # 连接到mysql
        sql = 'SELECT user_avatar FROM User WHERE `user_name`=\"%s\"'%(user_id)
        cursor.execute(sql) 
        result = cursor.fetchone()
        #print(result)
        conn.commit()
        cursor.close()
        conn.close()
        return result
    except Exception as e:
        print(e)
        return 'error'

if __name__ == '__main__':
    socketio.run(app)