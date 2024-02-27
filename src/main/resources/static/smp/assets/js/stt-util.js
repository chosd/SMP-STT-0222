(function () {
    window.sttobj = {
        getSttTrainSearchCondition: function () {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                updatedBy: "",
                status: "",
                from: "",
                to: "",
                pageSize: 10
            }));
        },
        getSttTrainDetail: function() {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                description: "",
                updatedBy: "",
                requestedAt: "",
                completedAt: "",
                duration: "",
                status: "",
                resultMsg: "",
                dataNum: "",
                resultModelId: ""
            }));
        },
        getSttTrainInsertRequest: function () {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                description: ""
            }));
        },
        getSttTrainStatuses: function () {
            return [
                {key: "READY", value: "미학습 상태"},
                {key: "TRAINING", value: "학습 중"},
                {key: "COMPLETE", value: "학습 완료"},
                {key: "FAIL", value: "학습 실패"}
            ]
        },
        getSttDeployModelSearchCondition: function() {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                uploadedBy: "",
                from: "",
                to: "",
                pageSize: 10
            }));
        },
        getSttDeployModelDetail: function() {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                description: "",
                updatedBy: "",
                dataNum: "",
                resultModelId: ""
            }));
        },
        getSttDeployModelInsertRequest: function () {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                dataNum: "",
                resultModelId: "",
                description: "",
                modelAuthKey: "",
                modelFileName: ""
            }));
        },
        getSttDeploySearchCondition: function() {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                createdBy: "",
                status: "",
                from: "",
                to: "",
                pageSize: 10
            }));
        },
        getSttDeployDetail: function() {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                resultModelId: "",
                resultModelDescription: "",
                description: "",
                createdBy: "",
                requestedAt: "",
                completedAt: "",
                duration: "",
                status: "",
                resultMsg: ""
            }));
        },
        getSttDeployInsertRequest: function () {
            return JSON.parse(JSON.stringify({
                serviceModelId: "",
                resultModelId: "",
                resultModelDescription: "",
                description: "",
            }));
        },
        getSttDeployStatuses: function () {
            return [
                {key: "READY", value: "미배포 상태"},
                {key: "DEPLOYING", value: "배포 중"},
                {key: "COMPLETE", value: "배포 완료"},
                {key: "FAIL", value: "배포 실패"}
            ]
        },
        getSttTestSearchCondition: function () {
            return JSON.parse(JSON.stringify({
                serviceModelId: ""
            }));
        },
        getRequestStatSearchObj: function () {
            return JSON.parse(JSON.stringify({
                "pageSize": 10,
                serviceModelId: "",
                serviceCode: "",
                searchUnit: "MINUTE",
                from: moment(new Date()).format("YYYY-MM-DD"),
                startSearchTime: "00:00",
                to: moment(new Date()).format("YYYY-MM-DD"),
                endSearchTime: "23:59",
                pageSize: 10
            }))
        }
    };
})();

var ObjectUtils = {
    deleteNullOrDefaultFields: function (obj) {
        for (var key of Object.keys(obj)) {
            if (obj[key] == null || obj[key] == undefined) {
                delete obj[key];
            }
            if (typeof obj[key] == "number" && obj[key] == 0) {
                delete obj[key];
            }
            if (typeof obj[key] == "string" && obj[key] == '') {
                delete obj[key];
            }
        }
    },
    copy: function (obj) {
        return JSON.parse(JSON.stringify(obj));
    },
    getDefaultString: function (val, defaultString = "") {
        if (val == null || val == undefined || val.trim().length == 0) {
            return defaultString;
        } else {
            return val.trim();
        }
    }
}