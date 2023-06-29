<template>
  <div id="help_e">
    <h2 id="key_word">关键词</h2>
    <div id="keyword">
      <el-input v-model="searchText" placeholder="请输入搜索关键词">
        <template v-slot:suffix>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
        </template>
      </el-input>
    </div>
    <ul id="result">
      <li v-for="(item, index) in searchResult" style="margin-top: 5rem;">
        <span>结果{{ index + 1 }}：</span>
        <span>{{ item.filePath }}</span>
        <div>-------------------------------------------------------------</div>
        <ul style="padding: 0;">
          <li v-for="(c_item, c_index) in item.matches">
            <input type="checkbox" :id="'checkbox-' + index + '-' + c_index" v-model="c_item.selected">
            <label :for="'checkbox-' + index + '-' + c_index">
              <span>行号{{ c_item.lineNumber }}</span>
              <span style="margin-left: 5rem;" v-html="highlightKeyword(c_item.lineContent)"></span>
            </label>
            <!-- Add hidden input field to store parent index -->
            <input type="hidden" :name="'parentIndex-' + index + '-' + c_index" :value="index">
          </li>
        </ul>
      </li>
    </ul>
    <div>
      <el-button @click="downloadSelectedItems" class="download-button" :disabled="isDownloadDisabled">
        <i class="el-icon-download"></i> 下载选中项
      </el-button>

    </div>
  </div>
  <div id="loading" v-if="isloading">
    <img src="../img/loading.gif" alt="">
    <div>loading...</div>
  </div>
</template>

<script>
import axios from 'axios'
import { useStore } from 'vuex'
import { computed } from 'vue'

export default {
  name: "KeyWord",
  data() {
    return {
      searchText: '',
      searchResult: [],
      isloading:false,
    }
  },
  methods: {
    handleSearch() {
      this.isloading = true
      this.searchResult = []
      axios.get(`http://localhost:8080/api/search?keyword=${encodeURIComponent(this.searchText)}`).then((response) => {
        this.searchResult = response.data
        this.isloading = false
        if (response.data.length === 0) {
          alert("无结果")
        }
      })
      // console.log(this.searchResult)
    },
    downloadSelectedItems() {
      const selectedItems = [];

      // Collect selected items with parent information
      this.searchResult.forEach((item, index) => {
        item.matches.forEach((c_item, c_index) => {
          if (c_item.selected) {
            const selectedItem = {
              lineNumber: c_item.lineNumber,
              lineContent: c_item.lineContent,
              parentIndex: index, // Store parent index
            };
            selectedItems.push(selectedItem);
          }
        });
      });

      // Generate text content with parent information
      const textContent = selectedItems.map((item) => {
        const parentIndex = item.parentIndex;
        const filePath = this.searchResult[parentIndex].filePath;
        return `文件：${filePath}\n行号${item.lineNumber}: ${item.lineContent}`;
      }).join('\n\n');

      // Create a download link
      const downloadLink = document.createElement('a');
      downloadLink.href = 'data:text/plain;charset=utf-8,' + encodeURIComponent(textContent);
      downloadLink.download = 'selected_items.txt';

      // Trigger the download
      downloadLink.click();
    },
    highlightKeyword(content) {
      // Implement your highlightKeyword method logic here
      // This is just a placeholder
      return content;
    }
  },
  setup() {
    const store = useStore();
    const MyPath = computed(() => store.state.MyPath);

    return {
      MyPath
    };
  },
  computed: {
    isDownloadDisabled() {
      return this.searchResult.length === 0;
    },
    highlightKeyword() {
      return function (lineContent) {
        // 将特定词语标红的处理逻辑
        const keyword = this.searchText;
        const regex = new RegExp(keyword, 'gi');
        return lineContent.replace(regex, '<span class="highlighted">$&</span>');
      };
    }
  }
};
</script>

<style>
#key_word{
  font-size: 1.2rem;
  text-align: left;
  padding: 1rem 1rem 1rem;
}
#help_e {
  width: 100%;
  position: relative;

}

#keyword {
  width: 80%;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
}

#loading {
  height: 80%;
  background-color: rgb(222, 220, 220);
  /* display: flex;
  align-items: center;
  justify-content: center; */

}

#loading img {
  border-radius: 50%;
  width: 10rem;
  height: 10rem;
  margin-top: 10rem;
}

#keyword div div {
  padding: 0;

}

#result {
  padding: 1rem;
}

#result li {
  list-style: none;
  text-align: left;
}

.highlighted {
  color: red;
}

.download-button {
  margin-top: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 20px;
  font-size: 16px;
  font-weight: bold;
  text-align: center;
  text-decoration: none;
  color: #fff;
  background-color: #3498db;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.download-button i {
  margin-right: 5px;
}
</style>