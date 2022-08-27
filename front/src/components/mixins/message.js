// 定义一个混入对象
var messageMixin = {
  methods: {
    errorAlert(msg) {
      this.$message.error(msg)
    }
  }
}

export default messageMixin
