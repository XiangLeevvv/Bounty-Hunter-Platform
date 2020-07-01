<template>
  <div>
    <el-card shadow="hover" class="workcard" v-for="(work,index) in works" style="position:relative;" :key="work.title">
      <div style="float: right">任务名：{{work.title}}</div>
      <button style="width:100%;height:100%;position:absolute;left:0;top:0;background: 0;border: 0;cursor: pointer"
              v-if="flag" @click="getDetails(work.id, type)"></button>
      <button style="width:90%;height:100%;position:absolute;left:0;top:0;background: 0;border: 0;cursor: pointer"
              v-if="!flag" @click="getDraftDetails(work.id,'get')"></button>
      <div style="float:left;">创建人：{{flag?work.publisher:work.creator}}</div>
      <div style="color:#FF5000">酬金：{{work.bonus}}</div>
      <div class="datetime" style="padding-top: 3%;">
        <i class="el-icon-date" style="float: left"></i>
        <span style="float: left; font-size: 14px">{{work.beginTime | formatDate}}</span>
        <span style="float: left; font-size: 14px; padding-left: 2%; padding-right: 2%">-</span>
        <span style="float: left; font-size: 14px">{{work.endTime | formatDate}}</span>
        <span style="float: right; font-size: 14px" v-if="taskType=='published'">发布时间：{{work.publishtime | formatDate}}</span>
        <span style="float: right; font-size: 14px" v-if="taskType=='ing'">领取人：{{work.receiver}}</span>
        <span style="float: right; font-size: 14px" v-if="taskType=='ed'">完成人：{{work.receiver}}</span>
        <span style="float: right; font-size: 14px" v-if="taskType=='executing'">领取时间：{{work.receivetime | formatDate}}</span>
        <span style="float: right" v-if="taskType=='draft'"></span>
      </div>
      <el-button v-if="!flag" style="color:#FF5000;float:right" size="small" @click="deleteDraft(index,work.id)">删除</el-button>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'workboard',
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
  data () {
    return {
      works: [],
      src: '',
      flag: true, // 不是草稿箱
      taskType: ''
    }
  },
  computed: {
    type () {
      return this.$route.query.type
    }
  },
  watch: {
    type: function (thenew, theold) {
      if (thenew === 'caogao') {
        this.taskType = 'draft'
        this.$axios.post('/api/getUserDrafts',
          {
            'user_name': localStorage.getItem('UserName')
          }
        )
          .then((response) => {
            if (response.data.status === 'right') {
              this.works = response.data.List
              this.flag = false
            } else {
              this.works = []
              this.$message.error({
                message: '草稿箱为空！',
                showClose: true,
                type: 'error'
              })
            }
          },
          (response) => {
            this.$message.error({
              message: '网络连接失败',
              showClose: true,
              type: 'error'
            })
          }
          )
      } else if (thenew === 'exing') {
        this.taskType = 'executing'
        this.$axios.post('/api/task/executeTask',
          {
            'user_name': localStorage.getItem('UserName')
          }
        )
          .then((response) => {
            if (response.data.status === 'right') {
              this.works = response.data.tasking
            } else {
              this.$message.error({
                message: '没有任务',
                showClose: true,
                type: 'error'
              })
            }
          },
          (response) => {
            this.$message.error({
              message: '网络连接失败',
              showClose: true,
              type: 'error'
            })
          }
          )
      } else { // post可以直接发参数，get必须用params打包
        this.taskType = thenew
        this.$axios.post('/api/task/publishedTask',
          {
            'user_name': localStorage.getItem('UserName'),
            'type': thenew
          }
        )
          .then((response) => {
            if (response.data.status === 'right') {
              this.works = response.data.tasks
              this.flag = true
            } else {
              this.$message.error({
                message: '没有任务',
                showClose: true,
                type: 'error'
              })
            }
          },
          (response) => {
            this.$message.error({
              message: '网络连接失败',
              showClose: true,
              type: 'error'
            })
          }
          )
      }
    }
  },
  methods: {
    getDetails (id, type) {
      this.$router.push({
        path: `/taskdetails/${type}/${id}`
      })
    },
    getDraftDetails (id, type) {
      this.$router.push({
        path: `/usercenter/createorder/${type}/${id}`
      })
    },
    deleteDraft (index, id) {
      this.works.splice(index, 1)
      this.$axios.post('/api/deleteDrafts',
        {
          'user_name': localStorage.getItem('UserName'),
          'task_id': id
        }
      )
        .then((response) => {
          console.log(response.data.message)
        },
        (response) => {
          this.$message.error({
            message: '网络连接失败',
            showClose: true,
            type: 'error'
          })
        }
        )
    }
  },
  mounted () {
    this.$axios.post('/api/task/publishedTask',
      {
        'user_name': localStorage.getItem('UserName'),
        'type': this.type
      }
    )
      .then((response) => {
        console.log(response.data.message)
        this.works = response.data.tasks
      },
      (response) => {
        this.$message.error({
          message: '网络连接失败',
          showClose: true,
          type: 'error'
        })
      }
      )
  }
}
</script>

<style>
  .workcard {
    width: 100%;
  }
</style>
