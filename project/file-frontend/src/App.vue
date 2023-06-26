<template>
  <div class="w-start">

    <div class="w-box">
      <div class="w-title">
        <h1>文件内容检索APP</h1>
      </div>
      <div class="w-btn-start" @click="getdisks"><button>Start</button></div>
      <h2>可用磁盘</h2>
      <ul id="disk">
        <li @click="select(index)" v-for="(disk, index) in disks" :key="index" :ref="`myLi_${index}`">{{ disk }}</li>
      </ul>
      <div id="certain-disk">
        <button @click="selectFile">Next</button>
      </div>
      <h2>文件目录</h2>
      <MyFile v-bind:message="this.mypath"></MyFile>
      <KeyWord></KeyWord>
    </div>
  </div>
</template>

<script>
import MyFile from "./components/MyFile.vue"
import KeyWord from "./components/KeyWord.vue";
export default {
  data() {
    return {
      disks: [],
      activeIndex: null, // 当前活动的索引
      mypath:""
    };
  },
  components: {
    MyFile,
    KeyWord
  },
  methods: {
    getdisks: function () {
      fetch("http://localhost:8080/disks", {
        mode: 'cors',
        headers: {
          "Content-Type": "application/json"
        }
      })
        .then(response => response.json())
        .then(data => {
          this.disks = data
        })
        .catch(error => console.error(error));
    },
    select: function (index) {
      // 取消之前选中的元素
      if (this.activeIndex !== null) {
        this.$refs[`myLi_${this.activeIndex}`][0].classList.remove('active')
      }

      // 设置当前选中的元素
      this.$refs[`myLi_${index}`][0].classList.add('active')
      this.activeIndex = index
      // alert(this.disks[this.activeIndex])
    },

    selectFile: function () {
      if (this.activeIndex != null) {
        const str = this.disks[this.activeIndex]
        const regex = /\(([^)]+)\)/; // 匹配括号中的内容
        const match = str.match(regex);
        if (match) {
          const content = match[1]; // 取匹配到的第一个分组，即括号中的内容
          console.log(content); // 输出：X:
          this.mypath = content
        }
      }
      else {
        alert("请先选择磁盘!!!")
      }
    },

  },
  mounted() {
  }
}
</script>

<style scoped>
.w-start {
  width: 100%;
  height: 180rem;
  display: flex;
  justify-content: center;
}

.w-box {
  width: 60%;
  position: relative;
  /* background-color: pink; */
}

.w-title {
  text-align: center;
}

.w-btn-start {
  text-align: center;
}

.w-btn-start button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 3px;
  font-size: 16px;
  cursor: pointer;
  width: 30%;
}

.w-btn-start button:hover {
  opacity: 0.8;
}

li {
  list-style: none;
}

#disk {
  display: flex;
  height: 13rem;
  border-bottom: 1px solid black;
}

#disk li {
  width: 10rem;
  height: 10rem;
  border: 1px solid #ccc;
  text-align: center;
  line-height: 10rem;
  margin: 2rem;
}

#disk li:hover {
  border: 1px solid purple;
}

#disk li.active {
  border: 2px solid #007bff;
}

#certain-disk {
  position: absolute;
  right: 0;
  bottom: 155.5rem;
}

#certain-disk button {
  padding: 10px 20px;
  background-color: #007bff;
  color: #fff;
  border: none;
  border-radius: 3px;
  font-size: 16px;
  cursor: pointer;
  
}
</style>
