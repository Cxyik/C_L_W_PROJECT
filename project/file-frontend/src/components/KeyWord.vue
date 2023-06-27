<template>
    <div id="help_e">
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
        <button @click="downloadSelectedItems">下载选中项</button>

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
            searchResult: [
            ],
        }
    },
    methods: {
        handleSearch() {
            axios.get(`http://127.0.0.1:8080/search?keyword=${encodeURIComponent(this.searchText)}`).then((response) => {
                this.searchResult = response.data
                console.log(response.data.length)
                if(response.data.length === 0){
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
#help_e{
    width: 100%;
    position: relative;
    
}
#keyword{
    width: 80%;
    position: absolute;
    left: 50%;
    transform: translateX(-50%);
}
#keyword div div {
    padding: 0;
    
}
#result{
    padding: 1rem;
}
#result li {
    list-style: none;
    text-align: left;
}

.highlighted {
    /* margin-left: 5rem; */
    color: red;
}
</style>