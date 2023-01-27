package com.oneseries.starter.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ControlsViewModel: ViewModel() {
    private var _targetPackageName: MutableLiveData<String> = MutableLiveData("")
    private var _targetPackageVersion: MutableLiveData<Long> = MutableLiveData(0)
    private var _latestVersion: MutableLiveData<Long> = MutableLiveData(0)
    private var _status: MutableLiveData<Int> = MutableLiveData(-1)
    private var _hasSpecial: MutableLiveData<Boolean> = MutableLiveData(false)

    val targetPackageName: LiveData<String>
        get() = _targetPackageName

    val targetPackageVersion: LiveData<Long>
        get() = _targetPackageVersion

    val latestVersion: LiveData<Long>
        get() = _latestVersion

    val status: LiveData<Int>
        get() = _status
    val hasSpecial: LiveData<Boolean>
        get() = _hasSpecial

    fun setTargetPackageName(pn: String){
        _targetPackageName.value = pn
    }
    fun setTargetPackageVersion(pv: Long){
        _targetPackageVersion.value = pv
    }
    fun setLatestVersion(lv: Long){
        _latestVersion.value = lv
    }
    fun setStatus(int: Int){
        _status.value = int
    }
    fun setHasSpecial(boolean: Boolean){
        _hasSpecial.value = boolean
    }
}