<template>
  <div class="Module">
    <div class="sideModule">
      <el-radio-group v-model="isCollapse" style="margin-bottom: 20px">
        <el-radio-button :label="false">展开</el-radio-button>
        <el-radio-button :label="true" >收起</el-radio-button>
      </el-radio-group>
      <el-menu default-active="0" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose" :collapse="isCollapse">
        <el-menu-item index="0" @click="Init">
          <i class="el-icon-time"></i>
          <label style="cursor: pointer">最新发布</label>
        </el-menu-item>
        <el-submenu index="1">
          <template slot="title">
            <i class="el-icon-s-help"></i>
            <label style="cursor: pointer;color: black">生活日常</label>
          </template>
          <el-menu-item-group>
            <el-menu-item index="1-1" @click="chooseModule('跑腿')">跑腿</el-menu-item>
            <el-menu-item index="1-2" @click="chooseModule('代购')">代购</el-menu-item>
          </el-menu-item-group>
            <el-menu-item index="1-3" @click="chooseModule('兼职')">兼职</el-menu-item>
        </el-submenu>
        <el-menu-item index="2" @click="chooseModule('学习问答')">
          <i class="el-icon-edit"></i>
          <label style="cursor: pointer">学习问答</label>
        </el-menu-item>
        <el-submenu index="3">
          <template slot="title">
            <i class="el-icon-takeaway-box"></i>
            <label style="cursor: pointer;color: black">物品租赁</label>
          </template>
          <el-menu-item-group>
            <el-menu-item index="3-1" @click="chooseModule('体育用品')">体育用品</el-menu-item>
            <el-menu-item index="3-2" @click="chooseModule('学习用品')">学习用品</el-menu-item>
            <el-menu-item index="3-3" @click="chooseModule('生活用品')">生活用品</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-menu-item index="4" @click="chooseModule('娱乐游戏')">
          <i class="el-icon-headset"></i>
          <label style="cursor: pointer">娱乐游戏</label>
        </el-menu-item>
        <el-menu-item index="5" @click="chooseModule('其他')">
          <i class="el-icon-guide"></i>
          <label style="cursor: pointer">其他</label>
        </el-menu-item>
      </el-menu>
    </div>
    <div class="taskModule">
      <router-view></router-view>
      <div class="task">
        <div class="guideBar">
          <div class="plus">
            <router-link to="/index/createorder">
              <el-tooltip class="item" effect="light" content="创建任务" placement="right">
                <i class="el-icon-circle-plus-outline" style="font-size: 30px"></i>
              </el-tooltip>
            </router-link>
          </div>
        </div>
        <div class="taskCard" v-if="taskList.length > 0">
          <el-col v-for="task in taskList" :key="task">
            <el-card class="cards" shadow="hover" style="cursor: pointer">
              <div class="taskinfo" @click="gotoDetails(task.id)">
                <div class="introWord">
                  <p id="intro">
                    <span class="taskTitle" style="font-size: large">{{task.title}}</span><br>
                    <i class="el-icon-coin" style="color: #ffd84d;padding-top: 3%">{{task.bonus}}</i><br>
                  </p>
                  <div class="datetime" style="padding-top: 3%;padding-left: 3%">
                    <i class="el-icon-date" style="float: left"></i>
                    <span style="float: left">{{task.beginTime | formatDate}}</span>
                    <span style="float: left;padding-left: 2%;padding-right: 2%">-</span>
                    <span style="float: left">{{task.endTime | formatDate}}</span>
                  </div>
                </div>
                <div class="publisher">
                  <p>
                    发布者: {{task.publisher}}<br>
                    发布时间: {{task.publishtime | formatDate}}
                  </p>
                </div>
              </div>
            </el-card>
          </el-col>
        </div>
        <div class="taskCard" v-if="taskList.length === 0">
          <span style="color: orangered;font-size: large">该模块还没有已发布的任务</span>
        </div>
      </div>
    </div>
    <div class="rightModule">
      <div class="avatar" style="margin-top: 10px;text-align: center">
        <el-avatar :size="95" shape="square" :src="userInfo.avatar"></el-avatar><br>
      </div>
      <div class="name" v-if="userInfo.userName!=null" style="margin-top: 20px">
        <span style="font-size: x-large">hi! {{userInfo.userName}}</span>
      </div>
      <div class="name" v-else style="margin-top: 20px">
        <span style="font-size: x-large">logout</span>
      </div>
      <div class="calendar" style="margin-top: 10px">
        <el-calendar v-model="value">
        </el-calendar>
      </div>
    </div>
  </div>
</template>

<script type="text/javascript">
export default {
  name: 'Index',
  filters: {
    formatDate: function (value) {
      let date = new Date(value)
      let y = date.getFullYear()
      let MM = date.getMonth() + 1
      MM = MM < 10 ? ('0' + MM) : MM
      let d = date.getDate()
      d = d < 10 ? ('0' + d) : d
      let h = date.getHours()
      h = h < 10 ? ('0' + h) : h
      let m = date.getMinutes()
      m = m < 10 ? ('0' + m) : m
      let s = date.getSeconds()
      s = s < 10 ? ('0' + s) : s
      return y + '/' + MM + '/' + d + ' ' + h + ':' + m + ':' + s
    }
  },
  mounted () {
    // 事件监听滚动条
    window.addEventListener('scroll', this.watchScroll)
    this.$axios.post('/api/task/latest').then((response) => {
      this.taskList = response.data.latest
    })
      .catch((error) => {
        console.log(error)
      })
  },
  methods: {
    handleOpen (key, keyPath) {
      // console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      // console.log(key, keyPath)
    },
    chooseModule (moduleName) {
      this.$axios.post('/api/task/findTaskByTags',
        {
          tags: moduleName
        }).then((response) => {
        if (response.data.Status === 'right') {
          this.taskList = response.data.task_omitinfo
        } else {
          alert(response.data.Details)
        }
      })
        .catch((error) => {
          console.log(error)
        })
    },
    gotoDetails (id) {
      let type = 'published'
      this.$axios.post('/addHistory', {
        'task_id': id,
        'user_name': localStorage.getItem('UserName')
      }).then((response) => {
        console.log(response.data)
      })
        .catch((error) => {
          console.log(error)
        })
      this.$router.push({
        path: `/taskdetails/${type}/${id}`
      })
    },
    Init () {
      this.$axios.post('/api/task/latest').then((response) => {
        this.taskList = response.data.latest
      })
        .catch((error) => {
          console.log(error)
        })
    }
  },
  data () {
    return {
      value: new Date(),
      isCollapse: true,
      activeName: 'first',
      taskList: [],
      userInfo: {'userName': localStorage.getItem('UserName'), 'avatar': 'data:image/jpeg;base64,' + localStorage.getItem('User_avatar'), 'userId': localStorage.getItem('User')}
    }
  }
}
</script>

<style scoped>
  .Module{
    height: max-content;
  }
  .sideModule{
    width: 14%;
    min-width: 150px;
    margin-top: 8px;
    float: left;
    background-color: white;
    height: auto;
  }
  .taskModule{
    margin-top: 8px;
    margin-left: 0.8%;
    width: 60%;
    min-width: 600px;
    float: left;
    background-color: white;
    height: auto;
  }
  .task{
    height: 100%;
  }
  .guideBar{
    padding-left: 15px;
    padding-top: 10px;
  }
  .taskCard{
    margin-top: 20px;
    width: 100%;
  }
  .el-col{
    width: 98%;
    padding-left: 1.5%;
  }
  .el-card{
    height: 110px;
  }
  .introWord{
    float: left;
    width: 70%;
    margin: 0;
    text-align: left;
  }
  #intro{
    margin: 0;
    text-align: left;
    padding-left: 20px;
  }
  .publisher{
    width: 25%;
    float: left;
    padding-top: 5px;
  }
  .el-card__body {
    padding: 10px;
  }
  .plus{
    width: 40px;
    height: 40px;
    padding-left: 85%;
  }
  .el-icon-circle-plus-outline{
    margin-top: 5px;
  }
  .cards{
    margin-bottom: 4px;
  }
  .rightModule{
    width: 24.4%;
    min-width: 200px;
    margin-left: 0.8%;
    margin-top: 8px;
    float: left;
    background-color: white;
    height: auto;
  }
  .el-menu-vertical-demo:not(.el-menu--collapse) {
    width: 99%;
    min-height: 400px;
  }
  .el-menu--collapse {
    width: 50%;
  }
  .el-tooltip{
    disabled: false;
  }
  .el-menu{
    border-right: none;
  }
</style>
