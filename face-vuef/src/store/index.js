import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        nomask: 0,
        mask: 0,
    },
    getters: {},
    mutations: {
        //总未戴口罩人数
        noMask(state, nomask) {
            state.nomask = nomask;
        },
        //没戴口罩中的新冠患者人数
        Mask(state, mask) {
            state.mask = mask
        }
    },
    actions: {},
    modules: {}
})