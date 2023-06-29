<template>
    <div class="h_page">
      <div class="h_top">
        <div class="h_logo">
          <span style="margin-left: 2rem;font-size: 1.5rem;"> QD MUSIC</span>
        </div>
        <div class="h_title">
          <span>My Personal System</span>
        </div>
        <div class="h_search">
          <div class="state">
            <div class="global_item special">
            </div>
            <div class="global_item">
              <img :src="state_src" alt="">
            </div>
          </div>
        </div>
      </div>
      <div class="h_body">
        <div class="h_card">
          <div class="bo_title">Message</div>
          <div class="bo_img">
            <div @click="to_person"></div>
          </div>
          <div class="body_menu">
            <div class="menu_top">
              <div class="menu_item first">
                <div @click="go_f"></div>
              </div>
              <div class="menu_item">
                <div @click="go_s"></div>
              </div>
            </div>
            <div class="menu_top">
              <div class="menu_item first">
                <div @click="go_t"></div>
              </div>
              <div class="menu_item">
                <div @click="go_fo"></div>
              </div>
            </div>
          </div>
        </div>
  
        <div class="recommend">
          <div class="explain">
            MBaPe
          </div>
          <div class="explain second">
            2023, at Manchester United Club
          </div>
        </div>
        <router-view></router-view>
        <div class="my_photo" @click="to_library">
          <div class="photo_title">
            Music Library
          </div>
          <div class="photo_title third">
            Everything you want to hear is here!
          </div>
        </div>
        <div class="my_wait">
  
        </div>
      </div>
  
  
    </div>
  </template>
    
  <script>
  import { computed } from 'vue';
  import { useStore } from 'vuex';
  
  export default {
    name: 'HelloWorld',
    props: {
      // msg: String
    },
    data() {
      return {
        state: false,
        is_first: false,
        state_src: "/music/play.png"
      }
    },
    mounted() {
      this.state = this.cardInfo.playState
  
      if(this.state == "true"){
        this.rotateBox(true)
      }
    },
    components: {
  
    },
    methods: {
      play_music: function () {
        if (this.state == "false") {
          this.rotateBox(false)
          this.state = false
          this.state_src = "/music/play.png"
        }
        else {
          if (this.is_first) {
            this.$message({
              message: '当前没有播放任务噢~',
              type: 'warning'
            });
          }
          else {
            this.rotateBox(true)
            this.state = true
            this.state_src = "/music/pause.png"
          }
        }
      },
      go_f: function () {
        this.$router.push("/SongSheet")
      },
      go_s: function () {
        this.$router.push("/Search")
      },
      go_t: function () {
        this.$router.push("/Mv")
      },
      go_fo: function () {
        this.$router.push("/Recommend")
      },
      to_person: function () {
        this.$router.push("/Person")
      },
      to_library:function(){
        this.$router.push("/Music_Library")
      },
      // 传入一个布尔值参数，表示是否旋转
      rotateBox: function (shouldRotate) {
        const box = document.querySelector('#box');
        if (shouldRotate) {
          let angle = 0; // 当前角度
          const speed = 10; // 旋转速度，单位：度/帧
  
          // 定时器中每帧调用的函数
          const rotate = function () {
            angle += speed; // 更新角度
            if (angle >= 360) angle -= 360; // 限制角度在0到359之间
            box.style.transform = 'rotate(' + angle + 'deg)'; // 更新CSS属性transform
          }
  
          // 启动定时器
          box.timer = setInterval(rotate, 1000 / 20); // 每秒60帧更新一次，即每帧间隔约16.67ms
        }
        else {
          // 如果已经启动了定时器，则停止它
          if (box.timer) clearInterval(box.timer);
        }
      }
    },
    setup() {
      const store = useStore(); // 通过调用 useStore() 方法获得 Store 实例
  
      const cardInfo = computed(() => store.state.cardInfo); // 通过 computed 属性将 store.state 对象映射到本地计算属性
      const setplayState = (newstate) => store.commit('setplayState', newstate); // 在本地方法中声明 setAge，该方法可将新年龄绑定到 store.commit 的 mutation 中
  
      const updateState = () => {
        var x = "true"
        setplayState(x);
      };
      const getState = () => {
        return this.cardInfo.playState.value
      }
      // alert(HelloWorld.state)
      return {
        cardInfo, // return 本地计算变量，可以在模板中使用
        updateState, // return 本地方法，可以在模板中绑定
        getState
      };
    },
  }
  
  </script>
    
    <!-- Add "scoped" attribute to limit CSS to this component only -->
  <style scoped>
  .photo_title{
    position: absolute;
    color: white;
    bottom: 3rem;
    left: 1rem;
  }
  .explain{
    color: white;
    position: absolute;
    bottom: 5rem;
    left: 1rem;
  }
  .second{
    bottom: 3rem;
  }
  .third{
    bottom: 1rem;
  }
  .my_wait,
  .my_photo {
    width: 25rem;
    height: 15rem;
    background-image: url("../img/giphy.gif");
    position: absolute;
    right: 30rem;
    bottom: 1.5rem;
    border-radius: 20%;
  }
  
  .my_wait {
    right: 10rem;
    width: 15rem;
  }
  
  .h_search {
    height: 100%;
  }
  
  .recommend {
    height: 20rem;
    /* background-color: palevioletred; */
    /* border: 1px solid #443282; */
    border-radius: 10%;
    width: 30%;
    position: absolute;
    right: 10rem;
    top: 6rem;
    background-image: url("../img/p_gif/giphy.gif");
  
  }
  
  .state {
    /* background-color: #443282; */
    height: 100%;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .global_item {
    width: 50%;
    height: 100%;
    /* background-color: pink; */
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .global_item img {
    width: 2rem;
  }
  
  .special {
    justify-content: right;
  }
  
  .menu_top {
    width: 100%;
    height: 50%;
    /* background-color: antiquewhite; */
    display: flex;
  }
  
  .bo_title {
    /* width: 1rem; */
    /* height: 1rem; */
    /* background-color: #443282; */
    width: 100%;
    text-align: center;
    font-size: 1.3rem;
    height: 2rem;
    color: whitesmoke;
  }
  
  .menu_item {
    width: 50%;
    height: 100%;
    /* background-color: #443282; */
    display: flex;
    align-items: center;
    justify-content: center;
  }
  
  .menu_item img {
    width: 3rem;
  }
  
  .bo_img {
    width: 100%;
    text-align: center;
    margin: 1rem 0 0;
    /* border-radius: 50%; */
  }
  
  .body_menu {
    margin-top: 2rem;
    width: 100%;
    height: 15rem;
    /* background-color: white; */
  }
  
  .bo_img img {
    border-radius: 50%;
    width: 30%;
  }
  
  .h_page {
    height: 100%;
  }
  
  .h_top {
    width: 100%;
    height: 10%;
    /* background-color: aquamarine; */
    box-shadow: 0 0 2rem rgba(0, 0, 255, 0.1);
    display: flex;
    align-items: center;
    justify-content: center;
  
  }
  
  .h_body {
    width: 100%;
    height: 90%;
    display: flex;
    background-image: url("../img/p_home/doly.jpg");
    /* background-color: blanchedalmond; */
  
  }
  
  .h_logo {
    flex: 1.1;
    /* background-color: aquamarine; */
    display: flex;
    align-items: center;
    /* font-family:'Courier New', Courier, monospace ; */
  }
  
  .h_logo span {
    font-family: Georgia, 'Times New Roman', Times, serif;
  }
  
  .h_title span {
    font-size: 1.5rem;
    font-family: Verdana, Geneva, Tahoma, sans-serif;
    color: rgba(20, 20, 10, 0.8);
  }
  
  .h_logo img {
    width: 2.5rem;
    border-radius: 50%;
    margin-left: 2.5rem;
    vertical-align: middle;
  }
  
  .h_search {
    flex: 0.5;
  }
  
  .h_title {
    flex: 1;
  }
  
  .h_card {
    background-color: #ffffff;
    background-image: url("../img/p_home/peason_back.jpg");
    -webkit-box-shadow: 0 0 2rem rgba(0, 0, 255, 0.1);
    box-shadow: 0 0 2rem rgba(0, 0, 255, 0.1);
    padding: 2rem;
    /* margin: 2rem; */
    /* margin-top: 1rem; */
    width: 19rem;
    height: 30rem;
    justify-items: center;
    cursor: pointer;
    border-radius: 1.75rem;
    -webkit-animation: fadein 1s ease-in-out 0ms 1;
    animation: fadein 1s ease-in-out 0ms 1;
    color: #443282;
    position: relative;
    top: 2rem;
    left: 2rem;
  }
  
  * {
    margin: 0;
    padding: 0;
    font-family: 'Lucida Sans', 'Lucida Sans Regular', 'Lucida Grande', 'Lucida Sans Unicode', Geneva, Verdana, sans-serif;
  }
  </style>
    