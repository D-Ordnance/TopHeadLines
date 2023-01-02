package com.deeosoft.pasteltest.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deeosoft.pasteltest.db.model.UIHeadLinesCollection
import com.deeosoft.pasteltest.repository.HeadLinesRepository
import com.deeosoft.pasteltest.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeadLineViewModel
    @Inject constructor(
        private val repository: HeadLinesRepository,
    ): ViewModel() {

    private var _success: MutableLiveData<UIHeadLinesCollection> = MutableLiveData()
    val success: LiveData<UIHeadLinesCollection> = _success

    private var _failure: MutableLiveData<String> = MutableLiveData()
    val failure: LiveData<String> = _failure

    private var _loading: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading

    fun getTopHeadLine(forceServer: Boolean = false){
        viewModelScope.launch(Dispatchers.IO) {
            if(forceServer){
                _loading.postValue(true)
            }
            repository.getTopHeadLines(forceServer).collect{
                _loading.postValue(false)
                if(it is Resource.Error){
                    _failure.postValue(it.message)
                }
                if(it is Resource.Success){
                    _success.postValue(it.data)
                }
            }
        }
    }
}