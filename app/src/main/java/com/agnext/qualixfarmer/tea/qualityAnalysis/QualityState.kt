package com.agnext.qualixfarmer.tea.qualityAnalysis

enum class QualityState{

    commoditySuccess,
    commodityFailure,
    scansListSuccess,
    noScanListSuccess,
    scansListFailure,

    avgScanDataSuccess,
    avgScanDataFaliure,

    monthFlcDataSuccess,
    monthFlcDataFailure,

    tokenExpired
}