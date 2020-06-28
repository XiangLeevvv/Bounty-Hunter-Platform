<template>
  <div style="width:100%;height:100%;">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
          <span>聊天框</span>
          <span>{{aim_user}}</span>
          <el-divider><i class="el-icon-mobile-phone"></i></el-divider>
          <div style="height:200px;">
            <el-scrollbar id="showplace" ref="myScrollbar" style="height:100%">
              <div v-for="item in items"
                :key="item.label" style="width:95%;padding:2px;">
              <el-avatar :style="item.avastyle">{{item.user}}</el-avatar>
              <el-tag
                :style="item.style"
                effect="dark"
                color="#39bbb0">
                {{ item.label }}
              </el-tag>
              <div style="clear:both;"></div>
              </div>
            </el-scrollbar>
          </div>
        </div>
        <div class="text item" style="height:100px;">
          <el-input type="textarea"
            :rows="3"
            v-model="message"
            resize=none
            autocomplete="off"
            maxlength="200"
            show-word-limit></el-input>
          <el-button @click="send" style="float:right;">发送</el-button>
        </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'chat',
  props: {
    aim_user: {
      type: String
    }
  },
  data () {
    return {
      params: '',
      message: '',
      id: '',
      items: [],
      user: '',
      board: []
    }
  },
  sockets: {
    // 通信的name
    // 这里是监听connect事件
    connect: function () {
      this.id = this.$socket.id
      // alert('建立连接')
      this.$socket.emit('my_id', this.user)
    },
    disconnect: function () {
      console.log('断开连接')
    },
    reconnect: function () {
      console.log('重新连接')
      this.$socket.emit('conect')
    },
    server_response: function (data) {
      console.log('接收数据', data)
      this.items.push({label: data, user: this.aim_user, avastyle: {'line-height': '30px', float: 'left', margin: '5px', 'background-color': '#00BCD4', width: '30px', height: '30px'}, style: {float: 'left', border: '0', margin: '5px'}})
    },
    send_message: function (data) {
      this.$socket.emit('message', data)
    },
    getup: function (data) {
      this.$parent.getup()
    },
    getdown: function (data) {
      this.$parent.getdown()
    },
    server_history: function (data) {
      console.log('接收历史', data)
      let i = 0
      for (; i < data.length; i++) {
        if (data[i][2] === this.user) {
          this.items.push({label: data[i][0], user: this.user, avastyle: {'line-height': '30px', float: 'right', margin: '5px', 'background-color': '#ffc107', width: '30px', height: '30px'}, style: {float: 'right', border: '0', margin: '5px'}})
        } else {
          this.items.push({label: data[i][0], user: this.aim_user, avastyle: {'line-height': '30px', float: 'left', margin: '5px', 'background-color': '#00BCD4', width: '30px', height: '30px'}, style: {float: 'left', border: '0', margin: '5px'}})
        }
      }
    }
  },
  created () {
    this.user = localStorage.getItem('UserName')
  },
  methods: {
    send () {
      let data = this.message
      this.$socket.emit('message', data)
      this.items.push({label: data, user: this.user, avastyle: {'line-height': '30px', float: 'right', margin: '5px', 'background-color': '#ffc107', width: '30px', height: '30px'}, style: {float: 'right', border: '0', margin: '5px'}})
    },
    scrollDown () {
      this.$refs['myScrollbar'].wrap.scrollTop = this.$refs['myScrollbar'].wrap.scrollHeight
    }
  },
  watch: {
    board: function (thenew, theold) {
      if (theold === false && thenew === true) {
        let User = localStorage.getItem('User')
        this.hellouseer = '欢迎 [' + User + '] 个人中心'
        this.loginlink = '/usercenter'
        this.$socket.connect()
      } else {
        this.hellouseer = '登录/注册'
        this.loginlink = '/login'
      }
    }
  },
  mounted () {
    this.$socket.emit('my_aim', this.user + ' ' + this.aim_user)
  },
  updated: function () {
    this.scrollDown()
  },
  destroyed () {
    this.$socket.emit('my_aim', this.user + ' ' + 'Null')
  }
}
</script>

<style>
.el-scrollbar__wrap{overflow-x:hidden;}
</style>
