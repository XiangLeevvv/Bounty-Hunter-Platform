<template>
  <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">

    <el-form-item label="用户名" prop="name">
      <el-input type="text" v-model="ruleForm.name" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="密码" prop="pass">
      <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="确认密码" prop="checkPass">
      <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
    </el-form-item>
    <el-form-item label="手机号" prop="phone">
      <el-input v-model.number="ruleForm.phone"></el-input>
    </el-form-item>
    <el-form-item label="验证码" prop="sms_code">
      <el-input v-model.number="ruleForm.vcode" style="width:60%;float:left"></el-input>
      <el-button type="primary" @click="send_smscode" style="width:40%;float:left">获取验证码</el-button>
    </el-form-item>

    <el-form-item label="性别" prop="sex">
      <el-radio-group v-model="ruleForm.sex">
        <el-radio label="男"></el-radio>
        <el-radio label="女"></el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
      <el-button @click="resetForm('ruleForm')">重置</el-button>
    </el-form-item>
  </el-form>
</template>
<script>
export default {
  name: 'registerform',
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
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    var checksex = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请选择性别'))
      } else {
        callback()
      }
    }
    var checkphone = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入手机号'))
      } else {
        callback()
      }
    }
    var checksms = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入验证码'))
      } else {
        callback()
      }
    }
    return {
      ruleForm: {
        name: '',
        pass: '',
        checkPass: '',
        phone: '',
        vcode: '',
        sex: ''
      },
      rules: {
        name: [
          { validator: validateName, trigger: 'blur' }
        ],
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        sex: [
          { validator: checksex, trigger: 'change' }
        ],
        phone: [
          { validator: checkphone, trigger: 'blur' }
        ],
        vcode: [
          { validator: checksms, trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    send_smscode () {
      this.$axios.post('/getCode',
        {
          'user_phone': this.ruleForm.phone
        }
      )
        .then((response) => {
          this.$message.success({
            message: '获取成功',
            showClose: true,
            type: 'success'
          })
        },
        (response) => {
          this.$message.error({
            message: '获取失败',
            showClose: true,
            type: 'error'
          })
        }
        )
    },
    submitForm (formName) {
      // this.$refs[formName].validate((valid) => {
      // if (valid) {
      this.$axios.post('api/register',
        {
          'user_name': this.ruleForm.name,
          'user_password': this.ruleForm.pass,
          'user_phone': this.ruleForm.phone,
          'user_code': this.ruleForm.vcode,
          'user_gender': this.ruleForm.sex
        }
      )
        .then((response) => {
          if (response.data.status === 'wrong') { this.$message.error(response.data.details) } else {
            this.$message.success({
              message: response.data.details,
              showClose: true,
              type: 'success'
            })
          }
        },
        (response) => {
          this.$message.error({
            message: '注册失败',
            showClose: true,
            type: 'error'
          })
        })
        // } else {
        //   console.log('error submit!!')
        //   return false
        // }
      // }
      // )
    },
    resetForm (formName) {
      this.$refs[formName].resetFields()
    }
  }
}
</script>
