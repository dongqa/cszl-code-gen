<template>
  <div class="app-container">
    <el-row class="row-search">
      <el-col :span="6">
        <el-input v-model="dbInfos.dbUrl" placeholder="请输入数据库url"/>
      </el-col>
      <el-col :span="3">
        <el-input v-model="dbInfos.dbUsername" placeholder="请输入账号"/>
      </el-col>
      <el-col :span="3">
        <el-input v-model="dbInfos.dbPassword" placeholder="请输入密码"/>
      </el-col>
      <el-col :span="3">
        <el-button type="primary" @click="fetchData">检索</el-button>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="3">
        <el-checkbox v-model="params.enableCache">是否开启二级缓存</el-checkbox>
      </el-col>
    </el-row>
    <el-row class="row-search">
      <el-col :span="3">
        <el-input v-model="params.module" placeholder="请输入模块名，默认为表名"/>
      </el-col>
    </el-row>
    <el-row class="row-search">
      <el-col :span="3">
        <el-input v-model="params.author" placeholder="请输入作者"/>
      </el-col>
      <el-col :span="3">
        <el-input v-model="params.email" placeholder="请输入邮箱"/>
      </el-col>
    </el-row>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="表名">
        <template slot-scope="scope">
          {{ scope.row.tableName }}
        </template>
      </el-table-column>
      <el-table-column label="注释" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.tableComment }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="200">
        <template slot-scope="scope">
          <el-button type="primary" @click="genCodeByTableName(scope.row.tableName,scope.row.tableComment)">生成
          </el-button>
          <el-button type="primary" @click="genCodeByCondition(scope.row.tableName,scope.row.tableComment)">生成方法
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      title="生成方法"
      :fullscreen="true"
      :visible="showConditionGen"
      width="100%"
      @close="showConditionGen = false"
    >
      <el-row class="row-search">
        {{ this.params.tableName }} {{ this.params.tableComment }}
      </el-row>
      <el-row class="row-search">
        <el-col :span="3">
          <el-input v-model="params.module" placeholder="请输入模块名，默认为表名"/>
        </el-col>
        <el-col :span="3">
          <el-input v-model="params.author" placeholder="请输入作者"/>
        </el-col>
        <el-col :span="3">
          <el-input v-model="params.email" placeholder="请输入邮箱"/>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="genCode">创建</el-button>
        </el-col>
      </el-row>
      <el-row class="row-search">
        <el-col>
          创建方法：
          <span v-for="(d,_) in allGenMethodList" :key="d">
            <el-button size="mini" @click="createMethod(d)">{{ d }}</el-button>
          </span>
        </el-col>
      </el-row>
      <el-row>
        <span v-for="(d,i) in params.genMethodList" :key="i">
          <el-card class="method-card">
            <div class="method-card-row">
              <el-button size="mini" type="danger" @click="removeMethod(i)">删除</el-button>
            </div>
            <div class="method-card-row">方法名：{{ d.genMethodName }}</div>
            <div v-if="showQueryXX(d.genMethodName)" class="method-card-row">
              <el-button
                size="mini"
                type="warning"
                @click="chooseQueryXX(i)"
              >条件字段</el-button>：
              <el-tag v-for="(k,v) in d.queryXX" :key="v">{{ k }}</el-tag>
            </div>
            <div v-if="showUpdateXX(d.genMethodName)" class="method-card-row">
              <el-button
                size="mini"
                type="warning"
                @click="chooseUpdateXX(i)"
              >更新字段</el-button>：
            <el-tag v-for="(k,v) in d.updateXX" :key="v">{{ k }}</el-tag>
            </div>
          </el-card>
        </span>
      </el-row>
    </el-dialog>
    <el-dialog
      :visible="showColumnList"
      width="50%"
      @close="showColumnList = false"
    >
      <el-row>
        <el-table ref="columnList" :data="columnList">
          <el-table-column
            type="selection"
            width="55"
          />
          <el-table-column label="字段名" prop="columnName"/>
          <el-table-column label="数据类型" prop="dataType"/>
          <el-table-column label="是否主键">
            <template slot-scope="s">
              {{ s.row.columnKey == 'PRI' }}
            </template>
          </el-table-column>
          <el-table-column label="可否为空" prop="isNullable"/>
          <el-table-column label="注释" prop="columnComment"/>
          <el-table-column label="查询条件" v-if="changeType==1">
            <template slot-scope="s">
              <el-select v-model="s.row.queryRel">
                <el-option
                  v-for="item in queryRels"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="条件关联" v-if="changeType==1">
            <template slot-scope="s">
              <el-select v-model="s.row.queryConditionRel">
                <el-option
                  v-for="item in queryConditionRels"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
      <el-row class="row-search">
        <el-button type="primary" @click="setFields">确定</el-button>
        <el-button type="danger" @click="showColumnList = false">取消</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import {
  getTableList,
  genCode,
  listColumnByTableName,
  listGenMethodName,
  listQueryRels,
  listQueryConditionRels
} from '@/api/gen'

export default {
  data() {
    return {
      list: null,
      listLoading: false,
      queryRels: [],
      queryConditionRels: [],
      allGenMethodList: [],
      params: {
        module: localStorage.getItem('module') || '',
        author: localStorage.getItem('author') || '',
        email: localStorage.getItem('email') || '',
        genMethodList: []
      },
      dbInfos: {
        dbUrl: localStorage.getItem('dbUrl') || '',
        dbUsername: localStorage.getItem('dbUsername') || '',
        dbPassword: localStorage.getItem('dbPassword') || ''
      },
      showConditionGen: false,
      columnList: [],
      showColumnList: false,
      changeMethod: {}, // 改变的方法
      changeType: null // 改变的类型
    }
  },
  watch: {
    'params.module': (_new) => {
      localStorage.setItem('module', _new)
    },
    'params.author': (_new) => {
      localStorage.setItem('author', _new)
    },
    'params.email': (_new) => {
      localStorage.setItem('email', _new)
    }
  },
  created() {
  },
  mounted() {
    listQueryRels().then(res => {
      this.queryRels = res.data.map(m => {
        return {
          label: m,
          value: m
        }
      })
    })
    listQueryConditionRels().then(res => {
      this.queryConditionRels = res.data.map(m => {
        return {
          label: m,
          value: m
        }
      })
    })
    listGenMethodName().then(res => {
      this.allGenMethodList = res.data
    })
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.listLoading = true
      this.$store.dispatch('db/setDbInfo', this.dbInfos)
      await getTableList(this.dbInfos).then(response => {
        this.list = response.data
        this.listLoading = false
      }, () => {
        this.listLoading = false
      })
    },
    genCodeByTableName(tableName, tableComment) {
      this.params.tableName = tableName
      this.params.tableComment = tableComment
      this.genCode()
    },
    async genCode() {
      await genCode(this.params)
      // this.params.genMethodList = []
    },
    genCodeByCondition(tableName, tableComment) {
      this.params.tableName = tableName
      this.params.tableComment = tableComment
      this.showConditionGen = true
      this.listColumnByTableName(tableName)
      this.params.genMethodList = []
    },
    listColumnByTableName(tableName) {
      listColumnByTableName(tableName).then(res => {
        this.columnList = res.data.map(m => {
          m.queryRel = this.queryRels[0].value
          m.queryConditionRel = this.queryConditionRels[0].value
          return m
        })
        console.log(this.columnList)
      })
    },
    createMethod(methodName) {
      const method = {
        genMethodName: methodName,
        queryXX: [],
        updateXX: []
      }
      this.params.genMethodList.push(method)
    },
    chooseQueryXX(i) {
      this.showColumnList = true
      this.changeMethod = this.params.genMethodList[i]
      this.changeType = 1
    },
    chooseUpdateXX(i) {
      this.showColumnList = true
      this.changeMethod = this.params.genMethodList[i]
      this.changeType = 2
    },
    setFields() {
      const selection = this.$refs.columnList.selection
      if (selection.length === 0) {
        this.errorAlert('请选择至少一个字段')
        return
      }
      const columnNames = selection.map(item => {
        return item.columnName
      })
      if (this.changeType === 1) {
        this.changeMethod.queryXX = selection.map(item => {
          return {
            name: item.columnName,
            queryRel: item.queryRel,
            queryConditionRel: item.queryConditionRel
          }
        })
      }
      if (this.changeType === 2) {
        this.changeMethod.updateXX = columnNames
      }
      this.showColumnList = false
      this.$refs.columnList.clearSelection()
    },
    removeMethod(idx) {
      this.params.genMethodList.splice(idx, 1)
    },
    showQueryXX(methodName) {
      if (methodName !== 'insertBatch') {
        return true
      }
      return false
    },
    showUpdateXX(methodName) {
      if (methodName.indexOf('updateXX') > -1) {
        return true
      }
      return false
    }
  }
}
</script>
