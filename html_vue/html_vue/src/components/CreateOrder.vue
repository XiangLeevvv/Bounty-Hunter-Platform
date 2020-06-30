<template>
  <div>

   <el-popover
      placement="top"
      width="250"
      style="float:right"
      v-model="visible">
      <img src="../assets/pay.jpg" height="390px" width="250px">
      <div style="text-align: right; margin: 0">
        <el-button size="mini" type="text" @click="visible = false">取消</el-button>
        <el-button type="primary" size="mini" @click="submitForm('ruleForm')">完成付款</el-button>
      </div>
    </el-popover>
    <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
      <el-form-item label="任务名" prop="name">
        <el-input type="text" v-model="ruleForm.name" autocomplete="off" maxlength="15" show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="任务描述" prop="introduce">
        <el-input type="textarea"
          v-model="ruleForm.introduce"
          placeholder="请输入对任务的具体描述"
          :rows="2"
          resize=none
          autocomplete="off"
          maxlength="200"
          show-word-limit></el-input>
      </el-form-item>
      <el-form-item label="出价" prop="price">
        <el-input v-model.number="ruleForm.price"></el-input>
      </el-form-item>
      <el-form-item label="标签" prop="tags">
        <el-cascader
          ref="cascaderAddr"
          v-model="ruleForm.tags"
          :options="options"
          :props="props"
          :show-all-levels="false"
          clearable>
        </el-cascader>
      </el-form-item>
      <el-form-item label="时间范围" prop="time">
        <el-date-picker
          v-model="ruleForm.time"
          type="datetimerange"
          :picker-options="pickerOptions"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          align="center">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="频率" prop="frequency">
          <el-radio-group v-model="ruleForm.frequency">
            <el-radio label="仅一次"></el-radio>
            <el-radio label="两次"></el-radio>
            <el-radio label="多次"></el-radio>
          </el-radio-group>
        </el-form-item>
      <el-form-item>

        <el-button type="primary" @click="beforeSubmit('ruleForm')">提交</el-button>
        <el-button type="primary" @click="storeForm('ruleForm')">保存</el-button>
        <el-button @click="resetForm('ruleForm')">重置</el-button>
          <el-button @click="ret">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'createorder',
  data () {
    var validateName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入任务名'))
      } else {
        callback()
      }
    }
    var checkPrice = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('出价不能为空'))
      }
      setTimeout(() => {
        if (!Number.isInteger(value)) {
          callback(new Error('请输入数字值'))
        } else {
          if (value > 500) {
            callback(new Error('限额500元'))
          } else if (value < 0) {
            callback(new Error('不能小于0'))
          } else {
            callback()
          }
        }
      }, 1000)
    }
    var validateIntroduce = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请描述一下任务细节'))
      } else {
        callback()
      }
    }
    return {
      visible: false,
      isref: 'reference',
      ruleForm: {
        name: '',
        introduce: '',
        price: '',
        tags: '',
        time: '',
        frequency: ''
      },
      rules: {
        name: [
          { validator: validateName, trigger: 'blur' }
        ],
        introduce: [
          { validator: validateIntroduce, trigger: 'blur' }
        ],
        price: [
          { validator: checkPrice, trigger: 'blur' }
        ]
      },
      pickerOptions: {
        shortcuts: [{
          text: '三天',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            end.setTime(end.getTime() + 3600 * 1000 * 24 * 3)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '一周',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            end.setTime(end.getTime() + 3600 * 1000 * 24 * 7)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '半个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            end.setTime(end.getTime() + 3600 * 1000 * 24 * 15)
            picker.$emit('pick', [start, end])
          }
        }, {
          text: '一个月',
          onClick (picker) {
            const end = new Date()
            const start = new Date()
            end.setTime(end.getTime() + 3600 * 1000 * 24 * 30)
            picker.$emit('pick', [start, end])
          }
        }]
      },
      value1: [new Date(2000, 10, 10, 10, 10), new Date(2000, 10, 11, 10, 10)],
      value2: '',
      props: { multiple: true },
      options: [{
        value: 1,
        label: '生活日常',
        children: [{
          value: 2,
          label: '跑腿'
        }, {
          value: 3,
          label: '代购'
        }, {
          value: 4,
          label: '兼职'
        }]
      }, {
        value: 5,
        label: '学习问答'
      }, {
        value: 6,
        label: '物品租赁',
        children: [{
          value: 7,
          label: '体育用品'
        }, {
          value: 8,
          label: '学习用品'
        }, {
          value: 9,
          label: '生活用品'
        }]
      }, {
        value: 10,
        label: '娱乐游戏'
      }, {
        value: 11,
        label: '其他'
      }]
    }
  },
  mounted () {
    if (this.$route.params.type === 'get') {
      this.$axios.post('/api/getDraftinfo',
        {
          'task_id': this.$route.params.id
        }
        // {headers: {'Content-Type': 'multipart/form-data'}}
      )
        .then((response) => {
          if (response.data.status === 'right') {
            let task = response.data.List
            this.ruleForm.name = task.title
            this.ruleForm.introduce = task.info
            this.ruleForm.price = task.bonus
            this.ruleForm.tags = task.tags
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
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.visible = false
          let nodes = this.$refs['cascaderAddr'].getCheckedNodes()
          let i = 0
          let tags = []
          for (i = 0; i < nodes.length; i++) {
            tags.push(nodes[i].pathLabels.pop())
          }
          let userName = localStorage.getItem('UserName')
          if (userName != null) {
            this.$axios.post('/api/task/publishTask',
              {
                'user_name': localStorage.getItem('UserName'),
                'task_title': this.ruleForm.name,
                'task_info': this.ruleForm.introduce,
                'task_bonus': this.ruleForm.price,
                'begin_time': window.JSON.stringify(this.ruleForm.time[0]),
                'end_time': window.JSON.stringify(this.ruleForm.time[1]),
                'task_type': window.JSON.stringify(tags)
              }
            )
              .then((response) => {
                let currentTime = response.data.currentTimes
                this.$axios.post('/api/task/publishTask1',
                  {
                    'user_name': localStorage.getItem('UserName'),
                    'current_time': currentTime
                  }).then(response => {
                  console.log(response.data)
                })
                this.$message.success({
                  message: '创建成功',
                  showClose: true,
                  type: 'success'
                })
                this.$router.go(-1)
              },
              (response) => {
                this.$message.error({
                  message: '创建失败',
                  showClose: true,
                  type: 'error'
                })
              })
          } else {
            this.$message.error({
              message: '请先登录',
              showClose: true,
              type: 'error'
            })
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    storeForm (formName) {
      let nodes = this.$refs['cascaderAddr'].getCheckedNodes()
      let i = 0
      var tags = []
      for (i = 0; i < nodes.length; i++) {
        tags.push(nodes[i].pathLabels.pop())
      }
      const end = this.formatDate()
      const start = this.formatDate()
      // eslint-disable-next-line camelcase
      var begin_time = window.JSON.stringify(this.ruleForm.time[0]) === undefined ? start : window.JSON.stringify(this.ruleForm.time[0])
      var end_time = window.JSON.stringify(this.ruleForm.time[1]) === undefined ? start : window.JSON.stringify(this.ruleForm.time[1])

      var data = {
        'user_name': localStorage.getItem('UserName'),
        'task_title': this.ruleForm.name,
        'task_info': this.ruleForm.introduce,
        'task_bonus': this.ruleForm.price,
        'begin_time': begin_time,
        'end_time': end_time,
        'task_type': window.JSON.stringify(tags)
      }
      if (this.$route.params.type === 'get') {
        data['task_id'] = this.$route.params.id
        this.$axios.post('/api/modifyDrafts', data)
          .then((response) => {
            this.$message.success({
              message: '保存成功',
              showClose: true,
              type: 'success'
            })
            this.$router.go(-1)
          },
          (response) => {
            this.$message.error({
              message: '保存失败',
              showClose: true,
              type: 'error'
            })
          })
      } else {
        this.$axios.post('/api/task/publishTask', data)
          .then((response) => {
            let currentTime = response.data.currentTimes
            this.$axios.post('/api/createDrafts',
              {
                'user_name': localStorage.getItem('UserName'),
                'current_time': currentTime
              }).then(response => {
              console.log(response.data)
            })
            this.$message.success({
              message: '创建成功',
              showClose: true,
              type: 'success'
            })
            this.$router.go(-1)
          },
          (response) => {
            this.$message.error({
              message: '创建失败',
              showClose: true,
              type: 'error'
            })
          })
      }
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    },
    ret () {
      this.$router.go(-1)
    },
    formatDate: function () {
      let date = new Date()
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
      return '"' + y + '-' + MM + '-' + d + ' ' + h + ':' + m + ':' + s + '"'
    },

    beforeSubmit: function (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var st = this.ruleForm.time[1].replace(/-/g, '/')
          var date = new Date(st)
          var now = new Date()
          if (now < date) {
            this.visible = true
          } else {
            this.visible = false
            this.$message({
              type: 'error',
              message: '时间范围错误'
            })
          }
        } else {
          this.visible = false
        }
      })
    }
  }
}
</script>

<style>
  .workcard {
    width:100%;
  }
  .el-picker-panel el-date-range-picker el-popper has-sidebar has-time {
    border:0;
  }
</style>
