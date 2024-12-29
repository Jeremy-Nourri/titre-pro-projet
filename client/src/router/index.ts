import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import DashboardView from '@/views/DashboardView.vue'
import NewProjectView from '@/views/NewProjectView.vue'
import ProjectBoardView from '@/views/ProjectBoardView.vue'
import ListProjectsView from '@/views/ListProjectsView.vue'

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
            meta: {
                requiresAuth: true,
            },
        },
        {
            path: '/nouveau-projet',
            name: 'NewProjectView',
            component: NewProjectView,
            meta: {
                requiresAuth: true,
            },
        },
        { 
            path: '/project/:id', 
            name: 'ProjectBoardView', 
            component: ProjectBoardView,
            props: true,
            meta: {
                requiresAuth: true,
            },
        },
        { 
            path: '/projects', 
            name: 'ListProjectsView', 
            component: ListProjectsView,
            props: true,
            meta: {
                requiresAuth: true,
            },
        },
    ],
    
})

router.beforeEach(async (to) => {

    const { useAuthStore } = await import('@/stores/authStore');
    const authStore = useAuthStore();

    console.log('Checking route:', to.name);
    console.log('Token exists:', authStore.token);
  
    if (to.meta.requiresAuth && (!authStore.token || !authStore.user)) {

        console.warn('Unauthorized access to a protected route:', to.name);

        return {
            path: '/',
            query: { redirect: to.fullPath },
        }
    }

    if (to.path === '/' && to.query.redirect) {
        router.replace({ path: '/' });
    }

    return true;
  
});

export default router
