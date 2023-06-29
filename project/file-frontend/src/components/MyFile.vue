<template>
    <div style="float: left;border-right: 1px solid bisque;
    height: 100vh; /* 设置固定的高度，使用视口高度作为示例 */
    overflow-y: auto; /* 添加垂直滚动条 */
    position: relative;">
        <div id="myOperate">
            <button @click="back">返回上一级</button>
            <button @click="shwow">选定</button>
        </div>
        <div class="w-titleq">
            <span id="t-content">目录</span>
            <span id="t-operate">操作</span>
        </div>
        <ul id="w-content">
            <li v-for="(item, index) in fileList" :key="index">
                <span id="label" v-on:click="getItem(item)">{{ item }}</span>
                <span class="operate">
                    <input type="radio" :id="`radio-${index}`" :value="item" v-model="selectedItems">
                </span>
            </li>
        </ul>



    </div>
    <div style="float: left;width: 30%;">
        <ul id="taregtfile">
            <div id="target-title">选定文件夹下，具有的可查询文件(txt\word\pdf)如下</div>
            <li v-for="(item, index) in targetFile" :key="index">
                <span><img :src="get_src(item)" alt=""></span>
                <span>{{ item }}</span>
            </li>
        </ul>
    </div>
    <div style="float: left;width: 35%;margin-left: 5rem;
    text-align: center;border-left: 1px solid bisque;
    height: 100vh; /* 设置固定的高度，使用视口高度作为示例 */
    overflow-y: auto; /* 添加垂直滚动条 */">
        <KeyWord></KeyWord>
    </div>
</template>
   
<script>
import axios from 'axios'
import { useStore } from 'vuex'
import { computed } from 'vue'
import KeyWord from './KeyWord.vue'

export default {
    data() {
        return {
            fileList: [],
            myPath: "C:",
            selectedItems: '',
            selectedItemsList: [],

            targetFile: [],
            pdf_src: '../icon/pdf.png',
            word_src: '../icon/word.png',
            txt_src: '../icon/txt.png'
        }
    },
    components:{
        KeyWord
    },
    created() {
        this.myPath = this.$route.params.variable;
        console.log("已选硬盘" + this.$route.params.variable)
        this.getItem('')
    },
    methods: {
        getFileList(path) {
            console.log(path)
            axios.get(`http://localhost:8080/api/file-list?path=${path}`).then((response) => {
                this.fileList = response.data.fileList
            })
        },
        getItem(item) {
            this.myPath = this.myPath + "/" + item
            this.getFileList(this.myPath)
        },
        back: function () {
            if (this.myPath.length > 4) {
                const slashIndex = this.myPath.lastIndexOf('/');
                this.myPath = this.myPath.substring(0, slashIndex);
                this.getFileList(this.myPath)
            }
            else {
                alert("已经回到了根目录")
            }

        },
        shwow: function () {
            this.selectedItemsList = []
            const selectedItem = this.selectedItems;
            if (selectedItem === '') {
                alert('请选择要查找的文件夹');
                return;
            }
            this.selectedItemsList.push(selectedItem);

            // 管理变量状态
            this.modify(this.myPath + "/" + this.selectedItemsList)

            // 获取目标文件夹下的各种txt\word\pdf文件
            axios.get(`http://localhost:8080/api/files?path=${this.MyPath}`).then((response) => {
                this.targetFile = response.data.fileList
                if (this.targetFile.length === 0) {
                    this.$message({
                        message: '文件夹中无可检索文件',
                        type: 'warning'
                    });
                }
                else{
                    this.$message({
                        message: '文件夹选定成功',
                        type: 'success'
                    });
                }
            })
        },
        // 依据文件结尾，获取图标
        get_src(item) {
            if (item.endsWith("txt")) {
                return this.txt_src
            }
            else if (item.endsWith("pdf")) {
                return this.pdf_src
            }
            else if (item.endsWith("doc") || item.endsWith("docx")) {
                return this.word_src
            }
        }
    },
    watch: {
        message(newVal) {
            newVal = newVal + "/"
            this.getFileList(newVal)
            this.myPath = newVal
        }
    },
    setup() {
        const store = useStore()
        const MyPath = computed(() => store.state.MyPath)
        const modify = (value) => store.commit('increment', value)

        return {
            MyPath,
            modify
        }
    }
}
</script>

<style>
#w-content {
    margin: 1rem 2rem 0;
    padding: 0;
}

#w-content li {
    list-style: none;
    width: 20rem;
    height: 1.3rem;
    margin: 0.2rem 0 0;
    border: 1px solid rgb(133, 133, 133);
    cursor: pointer;
    padding-left: 0.1rem;
}

#myOperate {
    height: 4rem;
    display: flex;
    align-items: center;
    justify-content: center;
    position: fixed;
    width: 27%;
    background-color: antiquewhite;
    z-index: 10;
}

#myOperate button {
    margin: 1.2rem;
    width: 7rem;
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 3px;
    cursor: pointer;
    text-align: center;
}

#taregtfile li {
    list-style: none;
    padding-left: 2rem;
    margin: 1rem 0 0;
}

#taregtfile li span img {
    width: 1.2rem;
    vertical-align: middle;
    margin-right: 1rem;
}

#target-title {
    font-weight: 600;
    margin-bottom: 2rem;
    margin-top: 1rem;
}

.w-titleq {
    width: 20rem;
    height: 2.2rem;
    margin: 4rem 2rem 0;
    border: 1px solid;
}

#back {
    display: inline-block;
    width: 8rem;
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 3px;
    cursor: pointer;
    text-align: center;
}

#label {
    float: left;
    width: 14.9rem;
    border-right: 1px solid;
    white-space: nowrap;
    /* 禁用文本换行 */
    overflow: hidden;
    /* 隐藏超出容器宽度的文本 */
    text-overflow: ellipsis;
    /* 使用省略号表示被截断的文本 */
}

.operate {
    float: right;
    display: inline-block;
    width: 5rem;
    text-align: center;
}

#t-content {
    display: inline-block;
    width: 10rem;
    text-align: center;
    height: 100%;
    line-height: 2rem;
    border-right: 1px solid black;
}

#t-operate {
    display: inline-block;
    width: 5rem;
    text-align: center;
}

#select-disk {}

#select-disk button {
    padding: 10px 20px;
    background-color: #007bff;
    color: #fff;
    border: none;
    border-radius: 3px;
    cursor: pointer;
    width: 8rem;
    text-align: center;
}
</style>