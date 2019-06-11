import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        isLogin: false,
        userType: -1,
        urls: [],
        diagnose: [],
        finalDiagnose: [],
        inspections: [],
        inspectionPrescriptions: [],
        prescription: [],
        diagnoseType: true
    },
    mutations: {
        setInspections (state, inspections) {
            state.inspections = inspections
        },
        addInspections (state, inspection) {
            state.inspections.push(inspection)
        },
        removeInspections (state, index) {
            state.inspections.splice(index, 1)
        },
        setInspectionPrescriptions (state, prescription) {
            state.inspectionPrescriptions = prescription
        },
        addInspectionPrescriptions (state, prescription) {
            state.inspectionPrescriptions.push(prescription)
        },
        removeInspectionPrescriptions (state, index) {
            state.inspectionPrescriptions.splice(index, 1)
        },
        changeDiagnoseType (state, type) {
            state.diagnoseType = type
        },
        addDisease (state, data) {
            console.log(data)
            if (data.isFinial) {
                state.finalDiagnose.push(data.disease)
            } else {
                state.diagnose.push(data.disease)
            }
        },
        setDiagnose (state, data) {
            if (data.isFinial) {
                state.finalDiagnose = data.disease
            } else
                state.diagnose = data.disease
        },
        clearDiagnose (state) {
            state.diagnose = []
            state.diagnoseType = 0
        },
        setLogin (state, login) {
            state.isLogin = login
        },
        setUserType (state, userType) {
            state.userType = userType
        },
        setUrls (state, urls) {
            state.urls = urls
        }
    },
    actions: {}
})
