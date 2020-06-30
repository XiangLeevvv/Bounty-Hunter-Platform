<template>
    <div class="Main">
      <div class="Task">
        <el-card class="box-card">
          <div>
            <el-col :span="8"><span style="font-size: 20px;font-weight: 600;float: right;">{{task[0].title}}</span></el-col>
            <el-col :span="8">
              <span style="font-size: 20px;font-weight: 600;float: right;">元</span>
              <span style="font-size: 26px;font-weight: 600;color: red;float: right;">{{task[0].bonus}}</span>
              <span style="font-size: 20px;font-weight: 600;float: right;">悬赏金：</span>
            </el-col>
            <el-col :span="8" style="margin-bottom:10px;">
              <el-button style="float: right; padding: 3px 0;font-size: 20px;font-weight: 600"  v-if="flag === 1" @click="receiveTask" type="success" plain>认领</el-button>
              <el-button style="float: right; padding: 3px 0;font-size: 20px;font-weight: 600"  v-if="flag === 3" @click="finshTask" type="success" plain>确认完成</el-button>
              </el-col>
            <hr style="width:100%;">
            <el-col :span="8"><span style="font-size: 18px;font-weight: 400;float: right;line-height:30px;">标签：</span></el-col>
            <el-col :span="10">
              <el-tag v-for="tag in task[0].tags.split(' ')" :key="tag"  style="float: left;margin-right:5px;">{{tag}}</el-tag>
            </el-col>
            <el-col :span="6">
            </el-col>
            <el-col :span="24" style="text-indent: 2em;width:100%;margin-top:20px;margin-bottom:20px;">
              {{task[0].info}}
            </el-col>
            <div >
              <div class="allmap" id="allmap" style="height:300px;">
              </div>
            </div>
          </div>
        </el-card>
      </div>
      <div class="Chat">
        <div class="userInfo" style="padding: 20px;width: 357px;">
          <div class="avatar" style="width: auto;float: left">
            <div class="picture">
              <el-avatar fit="fill" :size="90" :src="userAvatar"></el-avatar>
              <div class="status" :style="status"></div>
            </div>
          </div>
          <div class="name">
            <span style="font-size: x-large;font-weight: 500">{{task[0].publisher}}</span><br>
            <span style="font-size: small;color: #72767b;font-style:oblique">当前状态:</span>
            <span style="font-size: small;color: #72767b;font-style:oblique" v-if="up">在线</span>
            <span style="font-size: small;color: #72767b;font-style:oblique" v-else>离线</span>
          </div>
        </div>
        <div class="chatView" style="height: 300px;width: 400px;">
          <chat v-if="chated" :aim_user="task[0].publisher"></chat>
        </div>
      </div>
    </div>
</template>

<script>
import chat from './chat.vue'
export default {
  name: 'taskdetail',
  data () {
    return {
      task: {},
      src: '',
      chated: false,
      userId: '',
      userAvatar: '',
      flag: 0,
      userflag: true,
      up: false,
      status: {
        'background-color': '#CE3C34',
        'border-radius': '100%',
        'width': '16px',
        'height': '16px',
        'float': 'right',
        'margin-top': '-15px'
      }
    }
  },
  components: {
    chat: chat
  },
  computed: {
    task_id () {
      return this.$route.params.id
    },
    task_type () {
      if (this.$route.params.type === 'exing') {
        this.userflag = false
        return 'ing'
      } else {
        this.userflag = true
        return this.$route.params.type
      }
    }
  },
  beforeMount () {
    // 获取任务信息
    this.$axios.post('/api/task/findTaskondetail',
      {
        'task_id': this.task_id
      }).then((response) => {
      this.task = response.data[this.task_type]
      if (this.task_type === 'ing') {
        if (this.userflag) {
          this.flag = 3
        } else {
          this.flag = 2
          this.chated = true
        }
      } else {
        if (localStorage.getItem('UserName') === this.task[0].publisher) {
          this.flag = 2
        } else {
          this.flag = 1
          this.chated = true
          this.$store.dispatch('setaimuser', this.task[0].publisher)
        }
      }
      console.log(this.task)
      // 获取发布者信息
      this.$axios.post('/api/getUserInfo',
        {
          'user_name': this.task[0].publisher
        }).then((response) => {
        this.userId = response.data.user_Id
        this.$socket.emit('checkup', this.task[0].publisher)
        this.userAvatar = 'data:image/jpeg;base64,' + response.data.user_avatar
        if (this.task[0].publisher === localStorage.getItem('UserName')) {
          this.getup()
        }
      })
        .catch((error) => {
          this.$message.error({
            message: '网络连接失败',
            showClose: true,
            type: 'error'
          })
          console.log(error)
        })
    })
      .catch((error) => {
        this.$message.error({
          message: '网络连接失败',
          showClose: true,
          type: 'error'
        })
        console.log(error)
      })
  },
  methods: {
    receiveTask () {
      let userName = localStorage.getItem('UserName')
      if (userName != null) {
        this.$axios.post('/api/task/receiveTask', {
          'task_id': this.task_id,
          'user_name': userName
        }).then((response) => {
          console.log(response)
          this.$message.success('领取成功')
          this.$socket.emit('gettask', this.task_id + ' ' + this.task[0].publisher)
          this.$router.push('/index')
        })
          .catch((error) => {
            console.log(error)
          })
      } else {
        this.$message.error('您还没有登录!请登录')
      }
    },
    finshTask () {
      let userName = localStorage.getItem('UserName')
      if (userName != null) {
        this.$axios.post('/api/task/completeTask', {
          'task_id': this.task_id
        }).then((response) => {
          console.log(response)
          this.$message.success('任务完成')
          this.$socket.emit('finishtask', this.task_id + ' ' + this.task[0].receiver)
          this.$router.push('/usercenter')
        })
          .catch((error) => {
            console.log(error)
          })
      } else {
        this.$message.error('您还没有登录!请登录')
      }
    },
    getup () {
      this.up = true
      this.status['background-color'] = '#13ce66'
    },
    getdown () {
      this.up = false
      this.status['background-color'] = '#CE3C34'
    }
  }
}
</script>

<style scoped>
  @import '../element/index.css';
  .Task{
    width: 68%;
    float: left;
    margin-top: 5px;
  }
  .Chat{
    width: 29%;
    float: left;
    margin-top: 5px;
    border-radius: 5px;
    background-color: white;
    margin-left: 1%;
  }
  .userInfo{
    border: 1px solid rgb(230,230,230);
    border-radius: 5px;
    width: 90%;
    height: 110px;
  }
  .chatView{
    border-radius: 5px;
    border-right: 1px solid rgb(230,230,230);
    border-left: 1px solid rgb(230,230,230);
  }
  .picture{
    text-align: left;
    width: 100px;
  }
  .name{
    float: left;
    width: 50%;
    margin-top: 16px;
    margin-left: 20px;
    text-align: left;
  }
</style>
