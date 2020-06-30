<template>
  <div class="container">
    <div class="leftmenu">
        <div class="block">
            <el-upload
              class="avatar-uploader"
              action="https://jsonplaceholder.typicode.com/posts/"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload"
              >
              <img v-if="src" :src="src" class="avatar">
              <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <el-button size="small" type="primary" @click="Upload">上传</el-button>
            <p><span class="demonstration" style="font-size: 20px">{{user_name}}</span></p>
            <el-button :plain="true" @click="logout">登出</el-button>
        </div>
        <el-menu router :default-active="$route.path" class="el-menu-vertical-demo" @open="handleOpen" @close="handleClose">
          <el-submenu index="1">
            <template slot="title">
              <i class="el-icon-message-solid"></i>
              <span>我的需求</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/usercenter/workboard?type=published">已发布</el-menu-item>
              <el-menu-item index="/usercenter/workboard?type=caogao">草稿箱</el-menu-item>
              <el-menu-item index="/usercenter/workboard?type=ing">进行中</el-menu-item>
              <el-menu-item index="/usercenter/workboard?type=ed">已完成</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-submenu index="2">
            <template slot="title">
              <i class="el-icon-s-order"></i>
              <span>我的任务</span>
            </template>
            <el-menu-item-group>
              <el-menu-item index="/usercenter/workboard?type=exing">进行中</el-menu-item>
            </el-menu-item-group>
          </el-submenu>
          <el-menu-item index="/usercenter/createorder/0/0">
            <i class="el-icon-menu"></i>
            <span slot="title">发布任务</span>
          </el-menu-item>
        </el-menu>
    </div>
    <el-card class="rightcontent" shadow="never">
      <router-view></router-view>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'usercenter',
  methods: {
    handleOpen (key, keyPath) {
      console.log(key, keyPath)
    },
    handleClose (key, keyPath) {
      console.log(key, keyPath)
    },
    logout () {
      this.$store.dispatch('userLogout')
      this.$message({
        showClose: true,
        message: '登出成功',
        type: 'success'
      })
      localStorage.removeItem('User')
      localStorage.removeItem('UserName')
      localStorage.removeItem('userType')
      localStorage.removeItem('userId')
      localStorage.removeItem('User_avatar')
      this.$router.push('/index')
      this.$socket.close()
    },
    Upload () {
      this.$axios.post('/api/avatar',
        {
          userName: localStorage.getItem('UserName'),
          userAvatar: localStorage.getItem('User_avatar')
        }
      ).then(response => {
        if (response.data.status === 'right') {
          this.$message.success({
            message: response.data.details,
            showClose: true,
            type: 'success'
          })
        } else {
          this.$message.error(response.data.details)
        }
      })
    },
    handleAvatarSuccess (res, file) {
      this.src = 'data:image/jpeg;base64,' + localStorage.getItem('User_avatar')
      this.$socket.emit('my_avatar', localStorage.getItem('User_avatar'))
    },
    beforeAvatarUpload (file) {
      const isJPG = file.type === 'image/jpeg'
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
      }
      var reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = function (e) {
        let imgFile = e.target.result
        let arr = imgFile.split(',')
        this.n64 = arr[1]
        localStorage.setItem('User_avatar', arr[1])
      }
      return isJPG && isLt2M
    }
  },
  data () {
    return {
      src: '',
      user_name: '',
      n64: ''
    }
  },
  mounted () {
    this.user_name = localStorage.getItem('UserName')
    this.src = 'data:image/jpeg;base64,' + localStorage.getItem('User_avatar')
    this.$router.push('/usercenter/workboard?type=published')
  }
}
</script>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 110px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
.container {
  width: 80%;
  min-width:1005px;
  margin: 0 auto;
}
.leftmenu {
  float: left;
  width: 200px;
}
.rightcontent {
  float: left;
  width: 750px;
  margin-left:50px;
}
.block {
  border-right: solid 1px #e6e6e6;
  list-style: none;
  position: relative;
  margin: 0;
  padding: 10px;
  background-color: #FFF;
}
.userimg {
  width: 160px;
  margin: 0 auto;
  border-radius: 5px;
}
</style>
