import { createRouter, createWebHistory } from 'vue-router'
import Home from "../components/Disk.vue"
import FileSelect from "../components/MyFile.vue"
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path:"/Home",
      component:Home
    },
    {
      path:"/FileSelect/:variable",
      name:"FileSelect",
      component:FileSelect
    }

  ]
})
export default router

