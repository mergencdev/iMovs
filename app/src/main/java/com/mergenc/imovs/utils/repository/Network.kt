package com.mergenc.imovs.utils.repository

enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

class Network(val status: Status, val msg: String) {
    companion object {
        val LOADED: Network
        val LOADING: Network
        val ERROR: Network
        val END: Network

        init {
            LOADED = Network(Status.SUCCESS, "Success")
            LOADING = Network(Status.RUNNING, "Running")
            ERROR = Network(Status.FAILED, "Something went wrong")
            END = Network(Status.FAILED, "THE END")
        }
    }
}