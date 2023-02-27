package com.capstoneproject.basnasejahtera.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstoneproject.basnasejahtera.model.DataKonsumen
import com.capstoneproject.basnasejahtera.model.UserModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getDataUserKonsumen(): LiveData<DataKonsumen> {
        return pref.getDataUserKonsumen().asLiveData()
    }

    fun saveData(role: String?) {
        viewModelScope.launch {
            if (role != null) {
                pref.saveData(role)
            }
        }
    }

    fun saveDataKonsumen(id: Int?) {
        viewModelScope.launch {
            if (id != null) {
                pref.saveDataKonsumen(id)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}