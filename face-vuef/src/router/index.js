import Vue from 'vue'
import VueRouter from 'vue-router'
// import ScreenPage from '@/views/ScreenPage'
// import Map3DPage from '@/views/Map3DPage'
// import PersonLocusPage from '@/views/PersonLocusPage'
// import MapPage from '@/views/MapPage'
// import AllPeoplePage from '@/views/AllPeoplePage'
// import PeopleRankPage from '@/views/PeopleRankPage'
// import CameraRankPage from '@/views/CameraRankPage'
// import DetailPeoplePage from '@/views/DetailPeoplePage'


//1、安装插件
Vue.use(VueRouter)

//2、创建router实例对象
//懒加载
const Login = () =>
    import ('@/views/Login')
const ScreenPage = () =>
    import ('@/views/ScreenPage')
const Map3DPage = () =>
    import ('@/views/Map3DPage')
const PersonLocusPage = () =>
    import ('@/views/PersonLocusPage')
const Pingmiantu = () =>
    import ('@/views/Pingmiantu')
const TestT = () =>
    import ('@/views/TestT')
const FaceMask = () =>
    import ('@/views/FaceMask')
const PermissionPage = () =>
    import ('@/views/PermissionPage')
const UserInfoPage = () =>
    import ('@/views/UserInfoPage')
const CircleMapPage = () =>
    import ('@/views/CircleMapPage')
const XunJianPage = () =>
    import ('@/views/XunJianPage')


const routes = [{
        path: '/',
        redirect: '/login'
    },
    {
        path: '/login',
        component: Login,
        meta: {
            title: '登录界面'
        }
    },
    {
        path: '/screenpage',
        component: ScreenPage,
        meta: {
            title: '数据可视化面板-大屏展示'
        }
    },
    {
        path: '/map3dpage',
        component: Map3DPage,
        meta: {
            title: '人脸检测-视频播放'
        }
    },
    {
        path: '/personlocuspage',
        component: PersonLocusPage,
        meta: {
            title: '个人信息轨迹'
        }
    },
    {
        // 平面图测试
        path: '/pingmiantu',
        component: Pingmiantu,
        meta: {
            title: '平面图'
        }
    },
    {
        // 平面图测试
        path: '/faceMask',
        component: FaceMask,
        meta: {
            title: '人脸测试'
        }
    },
    {
        // 平面图测试
        path: '/permission',
        component: PermissionPage,
        meta: {
            title: '人脸测试'
        }
    },
    {
        // 平面图测试
        path: '/userInfo',
        component: UserInfoPage,
        meta: {
            title: '用户信息界面'
        }
    },
    {
        // 平面图测试
        path: '/drawMapPage',
        component: CircleMapPage,
        meta: {
            title: '绘制地图界面'
        }
    },
    {
        // 平面图测试
        path: '/testT',
        component: TestT,
        meta: {
            title: '流接入'
        }
    },
    {
        // 巡检页面模块
        path: '/XunJianPage',
        component: XunJianPage,
        meta: {
            title: '巡检信息模块'
        }
    }
    // {
    //   path: '/mappage',
    //   component: MapPage
    // },
    // {
    //   path: '/allpeoplepage',
    //   component: AllPeoplePage
    // },
    // {
    //   path: '/peoplerankpage',
    //   component: PeopleRankPage
    // },
    // {
    //   path: '/camerarankpage',
    //   component: CameraRankPage
    // },
    // {
    //   path: '/detailpeoplepage',
    //   component: DetailPeoplePage
    // },

]

const router = new VueRouter({
    routes
})

router.beforeEach((to, from, next) => {
    //全局-路由发生变化修改页面title
    if (to.meta.title) {
        //使用matched[0]每次使用第一个，避免有路由嵌套的时候出现错误
        document.title = to.matched[0].meta.title
    }
    next()
})

export default router