const state = {
  dbUrl: localStorage.getItem('dbUrl'),
  dbUsername: localStorage.getItem('dbUsername'),
  dbPassword: localStorage.getItem('dbPassword')
}

const mutations = {
  SET_DB_INFO: (state, data) => {
    // eslint-disable-next-line no-prototype-builtins
    for (var key in data) {
      state[key] = data[key]
      localStorage.setItem(key, data[key])
    }
  }
}

const actions = {
  setDbInfo({commit}, data) {
    commit('SET_DB_INFO', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

