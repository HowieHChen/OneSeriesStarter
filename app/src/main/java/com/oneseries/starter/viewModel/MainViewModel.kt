package com.oneseries.starter.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private var _isSamsung: MutableLiveData<Boolean> = MutableLiveData(false)
    private var _targetPackageName: MutableLiveData<String> = MutableLiveData("")
    private var _targetPackageVersion: MutableLiveData<Long> = MutableLiveData(0)
    private var _status: MutableLiveData<Int> = MutableLiveData(-1)

    val isSamsung: LiveData<Boolean>
        get() = _isSamsung
    val targetPackageName: LiveData<String>
        get() = _targetPackageName

    val targetPackageVersion: LiveData<Long>
        get() = _targetPackageVersion

    val status: LiveData<Int>
        get() = _status

    fun setIsSamsung(boolean: Boolean){
        _isSamsung.value = boolean
    }
    fun setTargetPackageName(pn: String){
        _targetPackageName.value = pn
    }

    fun setTargetPackageVersion(pv: Long){
        _targetPackageVersion.value = pv
    }

    fun setStatus(int: Int){
        _status.value = int
    }
}