import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router/index'
import ElementPlus from 'element-plus';
import 'element-plus/theme-chalk/index.css';
import store from "./store"

const app = createApp(App)

app.use(createPinia())
app.use(ElementPlus)
app.use(router)
app.use(store)
app.mount('#app')
