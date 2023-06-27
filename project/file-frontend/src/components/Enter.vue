<template>
    <div>
        <div v-if="!isLoggedIn" class="login-container">
            <div class="login-box">
                <h1 class="login-title">Welcome to Our App</h1>
                <form class="login-form">
                    <button class="login-button" @click="login">Login</button>
                </form>
            </div>
        </div>
        <div v-else>
            <!-- 在这里放置登录后的内容 -->
            <RouterView></RouterView>
        </div>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            isLoggedIn: false, // 登录状态，默认为未登录
        };
    },
    methods: {
        login() {
            // Handle login logic here
            // Example: Redirect to the main page
            this.$router.push('/Home');
            this.isLoggedIn = true;
        },
    },
};
</script>
  
<style scoped>
*{
    margin: 0;
    padding: 0;
}
.login-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    overflow: hidden;
    background-image: url("../img/global_back.jpg");
}

.login-box {
    width: 320px;
    padding: 2rem;
    background-color: #f2f2f2;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    overflow: hidden;
    opacity: 0.5;
}

.login-title {
    text-align: center;
    margin-bottom: 2rem;
    font-size: 24px;
    color: #333;
}

.login-form {
    display: flex;
    flex-direction: column;
}

.login-input {
    margin-bottom: 1rem;
    padding: 0.5rem;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 16px;
}

.login-button {
    padding: 0.5rem;
    border: none;
    border-radius: 4px;
    background-color: #007bff;
    color: #fff;
    font-size: 16px;
    cursor: pointer;
}

/* Hide scrollbar for the whole page */
body {
    overflow: hidden;
}
</style>
  