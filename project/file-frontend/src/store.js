import { createStore } from 'vuex'

const store = createStore({
  state() {
    return {
      MyPath:"D:/"
    }
  },
  mutations: {
    increment(state,path) {
      state.MyPath = path
    }
  }
})
export default store;