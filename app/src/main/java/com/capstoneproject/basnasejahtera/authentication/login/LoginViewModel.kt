package com.capstoneproject.basnasejahtera.authentication.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.capstoneproject.basnasejahtera.model.UserModel
import com.capstoneproject.basnasejahtera.model.UserPreference
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun saveData(role: String?) {
        viewModelScope.launch {
            if (role != null) {
                pref.saveData(role)
            }
        }
    }

    fun login() {
        viewModelScope.launch {
            pref.login()
        }
    }
}