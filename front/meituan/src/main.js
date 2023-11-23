import { createApp } from 'vue'
import App from './App.vue'
import axios from 'axios'
import router from "./router";
import video from 'video.js'
import 'video.js/dist/video-js.css'
import 'vant/lib/index.css';
const app=createApp(App)
app.config.globalProperties.$axios=axios
app.config.globalProperties.$video = video
app.use(router).mount('#app')
