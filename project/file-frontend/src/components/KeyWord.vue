<template>
    <div>
        <h2>关键词</h2>
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
            <li v-for="(c_item, c_index) in item.matches">
                <span>行号{{ c_item.lineNumber }}</span>
                <span style="margin-left: 5rem;" v-html="highlightKeyword(c_item.lineContent)"></span>
            </li>
            </li>
        </ul>
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
            searchResult: []
        }
    },
    methods: {
        handleSearch() {
            // const url = `http://127.0.0.1:8080/search?keyword=${encodeURIComponent(this.searchText)}&folderPath=${encodeURIComponent(this.MyPath)}`;
            // fetch(url)
            // .then((response) => {
            //     console.log(response.data)
            // })
            axios.get(`http://127.0.0.1:8080/search?keyword=${encodeURIComponent(this.searchText)}&folderPath=${encodeURIComponent(this.MyPath)}`).then((response) => {
                this.searchResult = response.data
            })
        }
    },
    setup() {
        const store = useStore()
        const MyPath = computed(() => store.state.MyPath)

        return {
            MyPath
        }
    },
    computed: {
        highlightKeyword() {
            return function (lineContent) {
                // 将特定词语标红的处理逻辑
                const keyword = this.searchText;
                const regex = new RegExp(keyword, 'gi');
                return lineContent.replace(regex, '<span class="highlighted">$&</span>');
            }
        }
    }
}
</script>
<style>
#keyword div div {
    padding: 0;
}

#result li {
    list-style: none;
}

.highlighted {
    /* margin-left: 5rem; */
    color: red;
}
</style>