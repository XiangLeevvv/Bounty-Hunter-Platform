from flask import Flask
from flask_cors import CORS, cross_origin
from flask_restful import Api, Resource, reqparse
import datetime
import time

app = Flask(__name__)
api = Api(app)
cors = CORS(app)

class Register(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('name', type=str)
        self.parser.add_argument('pass', type=str)
        self.parser.add_argument('checkPass', type=str)
        self.parser.add_argument('age', type=str)
        self.parser.add_argument('sex', type=str)

    def post(self):
        data = self.parser.parse_args()
        print(data)
        # rst = self.create(params)
        return {'rst': 'ok'}

class Login(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('user_name', type=str)
        self.parser.add_argument('user_password', type=str)

    def post(self):
        data = self.parser.parse_args()
        
        print(data)
        # rst = self.create(params)
        return {'rst': data['user_name']}

class Order(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('order_id', type=str)
        self.parser.add_argument('user_id', type=str)
        self.parser.add_argument('order_type', type=str)

    def post(self):
        data = self.parser.parse_args()
        print(data)
        n = 0
        price = '1'
        if data['order_type'] == 'publiced':
            n = 4
            price = 'publiced'
            a = 'ming'
            b = 'hong'
        elif data['order_type'] == 'caogao':
            n = 2
            price = 'caogaoxiang'
            a = 'ming'
            b = 'hong'
        elif data['order_type'] == 'ing':
            n = 5
            price = 'ing'
            a = 'hong'
            b = 'ming'
        elif data['order_type'] == 'achieved':
            n = 6
            price = 'achieved'
            a = 'hong'
            b = 'ming'
        params = {
            'code': 200,
            'message': 'success',
            'works': [
                {
                    'id':str(i),
                    'title': 'work' + str(i),
                    'beinger': a,
                    'ender': b,
                    'price': price
                }
                for i in range(n)
            ]
        }    
        # rst = self.create(params)
        return params

class Task(Resource):
    def __init__(self):
        self.parser = reqparse.RequestParser()
        self.parser.add_argument('task_id', type=str)
        self.parser.add_argument('user_name', type=str)
        self.parser.add_argument('task_title', type=str)
        self.parser.add_argument('task_info', type=str)
        self.parser.add_argument('task_bonus', type=str)
        self.parser.add_argument('task_tags', type=str)
        self.parser.add_argument('end_time', type=str)

    def post(self):
        data = self.parser.parse_args()
        print(data)
        if data['task_id'] == '0':
            # rst = self.create(params)
            return {
                'publisher':'1652514',
                'title': '任务名',
                'info':'任务详细介绍，详细详细xxxxxxxxxxxxxxxxxxxxxxxxxxxxx',
                'bonus':'150',
                'tags':['英雄联盟', '守望先锋', '网游', '娱乐', '休闲'],
                'createtime':'2019-12-28 21:06:03'
                }
        else:
            return {
                'publisher':'1752514',
                'title': '任务名',
                'info':'任务详细介绍，详细详细xxxxxxxxxxxxxxxxxxxxxxxxxxxxx',
                'bonus':'150',
                'tags':['英雄联盟', '守望先锋', '网游', '娱乐', '休闲'],
                'createtime':'2019-12-28 21:06:03'
                }

api.add_resource(Register, '/register')
api.add_resource(Login, '/login')
api.add_resource(Order, '/order')
api.add_resource(Task, '/task')

if __name__ == '__main__':
    app.run(debug=True, port= 9000)
