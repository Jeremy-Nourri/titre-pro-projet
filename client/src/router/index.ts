import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import DashboardView from '@/views/DashboardView.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'HomeView',
            component: HomeView,
        },
        {
            path: '/dashboard',
            name: 'DashboardView',
            component: DashboardView,
        }
    ],
})

export default router
