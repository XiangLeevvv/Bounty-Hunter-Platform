<template>
  <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="用户名" prop="name">
      <el-input type="text" v-model="ruleForm.name" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="pass">
      <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
      <el-button @click="resetForm('ruleForm')">重置</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
export default {
  name: 'loginform',
  data () {
    var validateName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入用户名'))
      } else {
        callback()
      }
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        name: '',
        pass: ''
      },
      rules: {
        name: [
          { validator: validateName, trigger: 'blur' }
        ],
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    submitForm (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.$axios.post('/login',
            {
              'user_name': this.ruleForm.name,
              'user_password': this.ruleForm.pass
            })
            .then((response) => {
              if (response.data.status === 'right') {
                // 设置Vuex登录标志为true，默认userLogin为false
                this.$store.dispatch('userLogin', true)
                // Vuex在用户刷新的时候userLogin会回到默认值false，所以我们需要用到HTML5储存
                // 我们设置一个名为Flag，值为isLogin的字段，作用是如果Flag有值且为isLogin的时候，证明用户已经登录了。
                localStorage.setItem('Flag', 'isLogin')
                localStorage.setItem('User', response.data.user_id)
                localStorage.setItem('User_avatar', response.data.user_avatar)
                localStorage.setItem('UserName', this.ruleForm.name)
                this.$message.success({
                  message: '登录成功',
                  showClose: true,
                  type: 'success'
                })
                // 登录成功后跳转到指定页面
                this.$router.push('/index')
              } else {
                this.$message.error({
                  message: '用户名或密码错误',
                  showClose: true,
                  type: 'error'
                })
              }
            },
            (response) => {
              this.$message.error({
                message: '登录失败',
                showClose: true,
                type: 'error'
              })
            }
            )
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
